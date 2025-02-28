package co.edu.unipiloto.petsafe;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PetSafe.db";
    private static final int DATABASE_VERSION = 1;

    // Sentencias SQL para crear las tablas
    private static final String CREATE_TABLE_DUENO = "CREATE TABLE Dueno (" +
            "IDDueno TEXT PRIMARY KEY, " +
            "Nombre TEXT NOT NULL, " +
            "Telefono TEXT NOT NULL, " +
            "Correo TEXT UNIQUE NOT NULL, " +
            "Contrasena TEXT NOT NULL);";

    private static final String CREATE_TABLE_MASCOTA = "CREATE TABLE Mascota (" +
            "IDMascota TEXT PRIMARY KEY, " +
            "IDDueno TEXT NOT NULL, " +
            "Foto TEXT, " +
            "Nombre TEXT NOT NULL, " +
            "FechaNacimiento DATE NOT NULL, " +
            "Especie TEXT NOT NULL, " +
            "Raza TEXT NOT NULL, " +
            "Sexo TEXT NOT NULL, " +
            "Color TEXT NOT NULL, " +
            "Microchip TEXT CHECK(Microchip IN ('si', 'no')) NOT NULL, " +
            "PesoMascota TEXT NOT NULL, " +
            "FOREIGN KEY (IDDueno) REFERENCES Dueno(IDDueno) ON DELETE CASCADE);";

    private static final String CREATE_TABLE_VACUNA = "CREATE TABLE Vacuna (" +
            "IDVacuna TEXT PRIMARY KEY, " +
            "NomVacuna TEXT NOT NULL);";

    private static final String CREATE_TABLE_VACUNAS_APLICADAS = "CREATE TABLE VacunasAplicadas (" +
            "IDMascota TEXT NOT NULL, " +
            "IDVacuna TEXT NOT NULL, " +
            "UltimaFechaVacunacion DATE NOT NULL, " +
            "PRIMARY KEY (IDMascota, IDVacuna), " +
            "FOREIGN KEY (IDMascota) REFERENCES Mascota(IDMascota) ON DELETE CASCADE, " +
            "FOREIGN KEY (IDVacuna) REFERENCES Vacuna(IDVacuna) ON DELETE CASCADE);";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_DUENO);
            db.execSQL(CREATE_TABLE_MASCOTA);
            db.execSQL(CREATE_TABLE_VACUNA);
            db.execSQL(CREATE_TABLE_VACUNAS_APLICADAS);
            Log.d("Database", "Base de datos creada exitosamente");
        } catch (SQLiteException e) {
            Log.e("Database", "Error al crear la base de datos: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS VacunasAplicadas");
            db.execSQL("DROP TABLE IF EXISTS Vacuna");
            db.execSQL("DROP TABLE IF EXISTS Mascota");
            db.execSQL("DROP TABLE IF EXISTS Dueno");
            onCreate(db);
            Log.d("Database", "Base de datos actualizada a la versión " + newVersion);
        } catch (SQLiteException e) {
            Log.e("Database", "Error al actualizar la base de datos: " + e.getMessage());
        }
    }

    /**
     * Método para autenticar un usuario en el login.
     *
     * @param correo    Correo del usuario.
     * @param password  Contraseña sin cifrar ingresada por el usuario.
     * @return True si la autenticación es correcta, false en caso contrario.
     */
    public boolean autenticarUsuario(String correo, String password) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        boolean autenticado = false;

        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT Contrasena FROM Dueno WHERE Correo = ?", new String[]{correo});

            if (cursor.moveToFirst()) {
                String passwordHash = cursor.getString(0);
                autenticado = passwordHash.equals(hashPassword(password));
            }
        } catch (Exception e) {
            Log.e("Database", "Error en la autenticación: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        return autenticado;
    }

    /**
     * Método para cifrar una contraseña con SHA-256.
     *
     * @param password Contraseña en texto plano.
     * @return Contraseña cifrada en formato hexadecimal.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al cifrar la contraseña", e);
        }
    }
}