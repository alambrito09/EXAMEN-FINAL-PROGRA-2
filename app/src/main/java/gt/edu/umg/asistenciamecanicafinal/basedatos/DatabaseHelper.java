

package gt.edu.umg.asistenciamecanicafinal.basedatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nombre de la base de datos y las tablas
    private static final String DATABASE_NAME = "userData.db";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_VEHICLE = "vehicle";
    private static final String TABLE_SERVICE = "service";
    private static final String TABLE_SERVICIO_COMBUSTIBLE = "servicio_combustible";  // Nueva tabla para el servicio de combustible

    // Versión de la base de datos
    private static final int DATABASE_VERSION = 4;

    // Columnas de la tabla users
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_NUMBER = "number";

    // Columnas de la tabla vehicle
    private static final String COLUMN_VEHICLE_ID = "id";
    private static final String COLUMN_MARCA = "marca";
    private static final String COLUMN_MODELO = "modelo";
    private static final String COLUMN_PLACA = "placa";
    private static final String COLUMN_COLOR = "color";
    private static final String COLUMN_LATITUD = "latitud";
    private static final String COLUMN_LONGITUD = "longitud";
    private static final String COLUMN_FOTO_RUTA = "foto_ruta";  // Columna para almacenar la ruta de la foto

    // Columnas de la tabla service (para el servicio mecánico)
    private static final String COLUMN_SERVICE_ID = "id";
    private static final String COLUMN_PROBLEMA = "problema";
    private static final String COLUMN_TIPO = "tipo";
    private static final String COLUMN_MODELO_SERVICE = "modelo";
    private static final String COLUMN_PLACA_SERVICE = "placa";
    private static final String COLUMN_LATITUD_SERVICE = "latitud";
    private static final String COLUMN_LONGITUD_SERVICE = "longitud";

    // Columnas de la tabla servicio_combustible (para el servicio de combustible)
    private static final String COLUMN_COMBUSTIBLE_ID = "id";
    private static final String COLUMN_TIPO_COMBUSTIBLE = "tipo_combustible";
    private static final String COLUMN_TIPO_DIESEL = "tipo_diesel";
    private static final String COLUMN_TOTAL_GALONES = "total_galones";
    private static final String COLUMN_LATITUD_COMBUSTIBLE = "latitud";
    private static final String COLUMN_LONGITUD_COMBUSTIBLE = "longitud";

    // Constructor de la clase
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creación de la tabla users
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_NUMBER + " TEXT)";
        db.execSQL(createUsersTable);

        // Creación de la tabla vehicle
        String createVehicleTable = "CREATE TABLE " + TABLE_VEHICLE + " ("
                + COLUMN_VEHICLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MARCA + " TEXT, "
                + COLUMN_MODELO + " TEXT, "
                + COLUMN_PLACA + " TEXT, "
                + COLUMN_COLOR + " TEXT, "
                + COLUMN_LATITUD + " REAL, "
                + COLUMN_LONGITUD + " REAL, "
                + COLUMN_FOTO_RUTA + " TEXT)";
        db.execSQL(createVehicleTable);

        // Creación de la tabla service (nueva tabla para MainActivity3)
        String createServiceTable = "CREATE TABLE " + TABLE_SERVICE + " ("
                + COLUMN_SERVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PROBLEMA + " TEXT, "
                + COLUMN_TIPO + " TEXT, "
                + COLUMN_MODELO_SERVICE + " TEXT, "
                + COLUMN_PLACA_SERVICE + " TEXT, "
                + COLUMN_LATITUD_SERVICE + " REAL, "
                + COLUMN_LONGITUD_SERVICE + " REAL)";
        db.execSQL(createServiceTable);

        // Creación de la tabla servicio_combustible
        String createServicioCombustibleTable = "CREATE TABLE " + TABLE_SERVICIO_COMBUSTIBLE + " ("
                + COLUMN_COMBUSTIBLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TIPO_COMBUSTIBLE + " TEXT, "
                + COLUMN_TIPO_DIESEL + " TEXT, "
                + COLUMN_TOTAL_GALONES + " REAL, "
                + COLUMN_LATITUD_COMBUSTIBLE + " REAL, "
                + COLUMN_LONGITUD_COMBUSTIBLE + " REAL)";
        db.execSQL(createServicioCombustibleTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Actualización de la base de datos si es necesario
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICIO_COMBUSTIBLE);
        onCreate(db);
    }

    // Método para insertar datos en la tabla users
    public boolean insertUserData(String name, String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_NUMBER, number);

        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;  // Devuelve true si la inserción fue exitosa
    }

    // Método para insertar datos en la tabla vehicle
    public boolean insertVehicleData(String marca, String modelo, String placa, String color, double latitud, double longitud, String fotoRuta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MARCA, marca);
        contentValues.put(COLUMN_MODELO, modelo);
        contentValues.put(COLUMN_PLACA, placa);
        contentValues.put(COLUMN_COLOR, color);
        contentValues.put(COLUMN_LATITUD, latitud);
        contentValues.put(COLUMN_LONGITUD, longitud);
        contentValues.put(COLUMN_FOTO_RUTA, fotoRuta);  // Guardar la ruta de la foto

        long result = db.insert(TABLE_VEHICLE, null, contentValues);
        return result != -1;  // Devuelve true si la inserción fue exitosa
    }

    // Método para insertar datos en la tabla service (para MainActivity3)
    public boolean insertServiceData(String problema, String tipo, String modelo, String placa, double latitud, double longitud) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PROBLEMA, problema);
        contentValues.put(COLUMN_TIPO, tipo);
        contentValues.put(COLUMN_MODELO_SERVICE, modelo);
        contentValues.put(COLUMN_PLACA_SERVICE, placa);
        contentValues.put(COLUMN_LATITUD_SERVICE, latitud);
        contentValues.put(COLUMN_LONGITUD_SERVICE, longitud);

        long result = db.insert(TABLE_SERVICE, null, contentValues);
        return result != -1;  // Devuelve true si la inserción fue exitosa
    }

    // Método para insertar datos en la tabla servicio_combustible
    public boolean insertCombustibleData(String tipoCombustible, String tipoDiesel, double totalGalones, double latitud, double longitud) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TIPO_COMBUSTIBLE, tipoCombustible);
        contentValues.put(COLUMN_TIPO_DIESEL, tipoDiesel);
        contentValues.put(COLUMN_TOTAL_GALONES, totalGalones);
        contentValues.put(COLUMN_LATITUD_COMBUSTIBLE, latitud);
        contentValues.put(COLUMN_LONGITUD_COMBUSTIBLE, longitud);

        long result = db.insert(TABLE_SERVICIO_COMBUSTIBLE, null, contentValues);
        return result != -1;  // Devuelve true si la inserción fue exitosa
    }
}
