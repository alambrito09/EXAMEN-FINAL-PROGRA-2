package gt.edu.umg.asistenciamecanicafinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import gt.edu.umg.asistenciamecanicafinal.basedatos.DatabaseHelper;


public class MainActivity extends AppCompatActivity {

    private EditText editTextNumber, editTextName;
    private Button buttonSubmit, buttonSalir;
    private DatabaseHelper myDb; // Declarar el helper de la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar el helper de la base de datos
        myDb = new DatabaseHelper(this);

        // Referencias a los campos de texto y botones
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextName = findViewById(R.id.editTextName);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSalir = findViewById(R.id.buttonsalir);  // Referencia al botón "Salir"

        // Manejo del clic en el botón "Enviar"
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editTextNumber.getText().toString();
                String name = editTextName.getText().toString();

                // Validar que los campos no estén vacíos
                if (name.isEmpty() || number.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, llena todos los campos.", Toast.LENGTH_SHORT).show();
                } else {
                    // Insertar los datos en la base de datos
                    boolean isInserted = myDb.insertUserData(name, number);

                    if (isInserted) {
                        Toast.makeText(MainActivity.this, "Datos guardados exitosamente", Toast.LENGTH_SHORT).show();

                        // Crear Intent para abrir el segundo Activity
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        startActivity(intent);
                        // Finalizar el primer activity
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Manejo del clic en el botón "Salir"
        buttonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Cierra la aplicación
            }
        });
    }
    @Override
    @SuppressWarnings("MissingSuperCall")
    public void onBackPressed() {
        // No hacemos nada cuando se presiona el botón Atrás del dispositivo
    }
}
