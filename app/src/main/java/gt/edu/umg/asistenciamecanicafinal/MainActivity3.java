package gt.edu.umg.asistenciamecanicafinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import gt.edu.umg.asistenciamecanicafinal.basedatos.DatabaseHelper;

public class MainActivity3 extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private EditText descripcionProblema, tipoVehiculo, modeloVehiculo, numeroPlaca;
    private Button btnConfirmarServicio, btnRegresar, btnUbicacion;
    private GoogleMap map;
    private LatLng ubicacionVehiculo;

    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // Inicializar la base de datos
        myDb = new DatabaseHelper(this);

        // Inicializar el fragmento del mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa2);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Inicializar los EditTexts con los IDs correctos
        descripcionProblema = findViewById(R.id.editTextDescripcionProblema);
        tipoVehiculo = findViewById(R.id.editTextTipoVehiculo);
        modeloVehiculo = findViewById(R.id.editTextModeloVehiculo);
        numeroPlaca = findViewById(R.id.editTextNumeroPlaca);

        // Inicializar los botones
        btnConfirmarServicio = findViewById(R.id.btnConfirmarServicio);
        btnRegresar = findViewById(R.id.btnRegresar);
        btnUbicacion = findViewById(R.id.buttonubicacion);

        // Configurar el botón para confirmar el servicio
        btnConfirmarServicio.setOnClickListener(v -> {
            // Obtener los datos ingresados en los EditText
            String problema = descripcionProblema.getText().toString();
            String tipo = tipoVehiculo.getText().toString();
            String modelo = modeloVehiculo.getText().toString();
            String placa = numeroPlaca.getText().toString();

            if (problema.isEmpty() || tipo.isEmpty() || modelo.isEmpty() || placa.isEmpty() || ubicacionVehiculo == null) {
                Toast.makeText(MainActivity3.this, "Por favor, llena todos los campos y selecciona una ubicación", Toast.LENGTH_SHORT).show();
            } else {
                // Insertar los datos en la base de datos
                boolean isInserted = myDb.insertServiceData(problema, tipo, modelo, placa, ubicacionVehiculo.latitude, ubicacionVehiculo.longitude);

                if (isInserted) {
                    Toast.makeText(MainActivity3.this, "Datos guardados exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity3.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configurar el botón de regresar para cerrar el activity
        btnRegresar.setOnClickListener(v -> finish());

        // Configurar el botón de ubicación para mostrar la ubicación seleccionada en el mapa
        btnUbicacion.setOnClickListener(v -> {
            if (ubicacionVehiculo != null) {
                map.addMarker(new MarkerOptions().position(ubicacionVehiculo).title("Ubicación del vehículo"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionVehiculo, 15));
                Toast.makeText(MainActivity3.this, "Ubicación guardada: " +
                        ubicacionVehiculo.latitude + ", " + ubicacionVehiculo.longitude, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity3.this, "No hay ubicación seleccionada", Toast.LENGTH_SHORT).show();
            }
        });
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
        ubicacionVehiculo = latLng;

        Toast.makeText(this, "Ubicación seleccionada: " + latLng.latitude + ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
    }
    @Override
    @SuppressWarnings("MissingSuperCall")
    public void onBackPressed() {
        // No hacemos nada cuando se presiona el botón Atrás del dispositivo
    }
}
