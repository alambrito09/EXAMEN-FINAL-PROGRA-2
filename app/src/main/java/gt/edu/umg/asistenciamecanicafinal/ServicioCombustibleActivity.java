package gt.edu.umg.asistenciamecanicafinal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class ServicioCombustibleActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private CheckBox checkBoxMagna;
    private CheckBox checkBoxPremium;
    private CheckBox checkBoxDieselA;
    private CheckBox checkBoxBiodiesel;
    private EditText editTextGalones;
    private Button btnSolicitarCombustible;
    private Button btnRegresar, btnUbicacion;

    private String selectedCombustible = ""; // Para almacenar el tipo de combustible seleccionado
    private String selectedDiesel = "";     // Para almacenar el tipo de diésel seleccionado
    private LatLng ubicacionCombustible;    // Para almacenar la ubicación seleccionada

    private GoogleMap map;
    private DatabaseHelper myDb; // Base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio_combustible);

        // Inicializar la base de datos
        myDb = new DatabaseHelper(this);

        // Referencias a los elementos de la interfaz
        checkBoxMagna = findViewById(R.id.checkBoxMagna);
        checkBoxPremium = findViewById(R.id.checkBoxPremium);
        checkBoxDieselA = findViewById(R.id.checkBoxDieselA);
        checkBoxBiodiesel = findViewById(R.id.checkBoxBiodiesel);
        editTextGalones = findViewById(R.id.editTextGalones);
        btnSolicitarCombustible = findViewById(R.id.btnSolicitarCombustible);
        btnRegresar = findViewById(R.id.btnRegresar);
        btnUbicacion = findViewById(R.id.ubicacion);

        // Inicializar el fragmento del mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa3);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Configurar el botón "Solicitar Servicio de Combustible"
        btnSolicitarCombustible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar qué checkboxes están seleccionados
                if (checkBoxMagna.isChecked()) {
                    selectedCombustible = checkBoxMagna.getText().toString();
                } else if (checkBoxPremium.isChecked()) {
                    selectedCombustible = checkBoxPremium.getText().toString();
                }

                if (checkBoxDieselA.isChecked()) {
                    selectedDiesel = checkBoxDieselA.getText().toString();
                } else if (checkBoxBiodiesel.isChecked()) {
                    selectedDiesel = checkBoxBiodiesel.getText().toString();
                }

                String totalGalones = editTextGalones.getText().toString();

                if (selectedCombustible.isEmpty() && selectedDiesel.isEmpty()) {
                    Toast.makeText(ServicioCombustibleActivity.this, "Selecciona al menos un tipo de combustible o diésel.", Toast.LENGTH_SHORT).show();
                } else if (totalGalones.isEmpty()) {
                    Toast.makeText(ServicioCombustibleActivity.this, "Ingresa el total de galones.", Toast.LENGTH_SHORT).show();
                } else if (ubicacionCombustible == null) {
                    Toast.makeText(ServicioCombustibleActivity.this, "Selecciona una ubicación en el mapa.", Toast.LENGTH_SHORT).show();
                } else {
                    // Insertar los datos en la base de datos
                    boolean isInserted = myDb.insertCombustibleData(selectedCombustible, selectedDiesel, Double.parseDouble(totalGalones), ubicacionCombustible.latitude, ubicacionCombustible.longitude);

                    if (isInserted) {
                        Toast.makeText(ServicioCombustibleActivity.this, "Solicitud enviada exitosamente.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ServicioCombustibleActivity.this, "Error al enviar la solicitud.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Configurar el botón para volver atrás
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Cerrar el Activity actual y volver al anterior
            }
        });

        // Configurar el botón de ubicación para mostrar y guardar la ubicación seleccionada
        btnUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ubicacionCombustible != null) {
                    map.addMarker(new MarkerOptions().position(ubicacionCombustible).title("Ubicación del combustible"));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionCombustible, 15));
                    Toast.makeText(ServicioCombustibleActivity.this, "Ubicación guardada: " +
                            ubicacionCombustible.latitude + ", " + ubicacionCombustible.longitude, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ServicioCombustibleActivity.this, "No hay ubicación seleccionada", Toast.LENGTH_SHORT).show();
                }
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
        ubicacionCombustible = latLng;

        Toast.makeText(this, "Ubicación seleccionada: " + latLng.latitude + ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
    }
    @Override
    @SuppressWarnings("MissingSuperCall")
    public void onBackPressed() {
        // No hacemos nada cuando se presiona el botón Atrás del dispositivo
    }
}
