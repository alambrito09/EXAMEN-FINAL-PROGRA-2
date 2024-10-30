package gt.edu.umg.asistenciamecanicafinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;



public class SecondActivity extends AppCompatActivity {

    private Button buttonRegresar, buttonServicioGrua;
    private Button buttonServicioMecanico;
    private Button buttonServicioCombustible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        buttonServicioMecanico = findViewById(R.id.buttonServicioMecanico);
        buttonServicioCombustible = findViewById(R.id.buttonServicioCombustible);

        // Inicializar el botón "Regresar al Menú"
        buttonRegresar = findViewById(R.id.buttonRegresar);

        // Inicializar el botón "Servicio de Grúa"
        buttonServicioGrua = findViewById(R.id.buttonServicioGrua);

        // Configurar el botón para regresar al MainActivity
        buttonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para regresar al MainActivity
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Configurar el botón para abrir el ThirdActivity (Servicio de Grúa)
        buttonServicioGrua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir el ThirdActivity
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });
        // Configurar el botón para ir a Servicio Mecánico
        buttonServicioMecanico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, MainActivity3.class);
                startActivity(intent);
            }
        });
        // Configurar el botón para ir a Servicio de Combustible
        buttonServicioCombustible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, ServicioCombustibleActivity.class);
                startActivity(intent);
            }
        });
    }


    // Sobrescribir el método onBackPressed para deshabilitar el botón Atrás del dispositivo
    @Override
    @SuppressWarnings("MissingSuperCall")
    public void onBackPressed() {
        // No hacemos nada cuando se presiona el botón Atrás del dispositivo
    }
}

