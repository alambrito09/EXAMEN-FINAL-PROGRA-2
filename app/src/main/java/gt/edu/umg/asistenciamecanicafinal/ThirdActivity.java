package gt.edu.umg.asistenciamecanicafinal;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import gt.edu.umg.asistenciamecanicafinal.basedatos.DatabaseHelper;

public class ThirdActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button buttonRegresarMenu, buttonTomarFoto, buttonUbicacion, buttonEnviar;
    private EditText editTextMarca, editTextModelo, editTextPlaca, editTextColor;
    private GoogleMap map;
    private LatLng ubicacionCarro;
    private String currentPhotoPath;  // Ruta de la foto

    private DatabaseHelper myDb;  // Inicializar la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        // Inicializar la base de datos
        myDb = new DatabaseHelper(this);

        // Inicializar el fragmento del mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

        // Inicializar botones y EditTexts
        buttonUbicacion = findViewById(R.id.buttonUbicacion);
        buttonRegresarMenu = findViewById(R.id.buttonRegresarMenu);
        buttonTomarFoto = findViewById(R.id.buttonTomarFoto);
        buttonEnviar = findViewById(R.id.buttonEnviar);

        editTextMarca = findViewById(R.id.editTextMarca);
        editTextModelo = findViewById(R.id.editTextModelo);
        editTextPlaca = findViewById(R.id.editTextPlaca);
        editTextColor = findViewById(R.id.editTextColor);

        // Configurar el botón para regresar al SecondActivity
        buttonRegresarMenu.setOnClickListener(v -> {
            Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);
            startActivity(intent);
            finish();  // Cierra el ThirdActivity para evitar volver a él
        });

        // Configurar el botón para abrir la cámara
        buttonTomarFoto.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        // Configurar el botón "Enviar" para guardar los datos del vehículo
        buttonEnviar.setOnClickListener(v -> {
            String marca = editTextMarca.getText().toString();
            String modelo = editTextModelo.getText().toString();
            String placa = editTextPlaca.getText().toString();
            String color = editTextColor.getText().toString();

            if (marca.isEmpty() || modelo.isEmpty() || placa.isEmpty() || color.isEmpty() || ubicacionCarro == null || currentPhotoPath == null) {
                Toast.makeText(ThirdActivity.this, "Por favor, llena todos los campos, selecciona la ubicación y toma una foto", Toast.LENGTH_SHORT).show();
            } else {
                // Insertar los datos del vehículo en la base de datos
                boolean isInserted = myDb.insertVehicleData(marca, modelo, placa, color, ubicacionCarro.latitude, ubicacionCarro.longitude, currentPhotoPath);

                if (isInserted) {
                    Toast.makeText(ThirdActivity.this, "Datos guardados exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ThirdActivity.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configurar el botón de "Ubicación del carro" para mostrar la ubicación seleccionada
        buttonUbicacion.setOnClickListener(v -> {
            if (ubicacionCarro != null) {
                map.addMarker(new MarkerOptions().position(ubicacionCarro).title("Ubicación del carro"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionCarro, 15));
                Toast.makeText(ThirdActivity.this, "Ubicación guardada: " +
                        ubicacionCarro.latitude + ", " + ubicacionCarro.longitude, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ThirdActivity.this, "No hay ubicación seleccionada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Sobrescribir onActivityResult para manejar la foto capturada
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Obtener la foto como un Bitmap
            Bitmap fotoBitmap = (Bitmap) data.getExtras().get("data");

            // Guardar la imagen en el almacenamiento externo
            File photoFile = null;
            try {
                photoFile = createImageFile();
                if (photoFile != null) {
                    FileOutputStream fos = new FileOutputStream(photoFile);
                    fotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close();
                    Toast.makeText(this, "Foto guardada en: " + currentPhotoPath, Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para crear un archivo de imagen en el almacenamiento externo
    private File createImageFile() throws IOException {
        // Crear un nombre único para el archivo de la imagen
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // Nombre del archivo
                ".jpg",  // Extensión del archivo
                storageDir  // Directorio
        );

        // Guardar la ruta del archivo
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(this);

        // Marcador inicial en Guatemala
        LatLng GUATEMALA = new LatLng(14.62469819970514, -90.51995003632942);
        map.addMarker(new MarkerOptions().position(GUATEMALA).title("GUATEMALA"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(GUATEMALA, 10));
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        // Borrar los marcadores previos
        map.clear();

        // Agregar un nuevo marcador en la ubicación seleccionada
        map.addMarker(new MarkerOptions().position(latLng).title("Ubicación seleccionada"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        // Guardar la ubicación seleccionada
        ubicacionCarro = latLng;

        Toast.makeText(this, "Ubicación seleccionada: " + latLng.latitude + ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
    }
    @Override
    @SuppressWarnings("MissingSuperCall")
    public void onBackPressed() {
        // No hacemos nada cuando se presiona el botón Atrás del dispositivo
    }
}
