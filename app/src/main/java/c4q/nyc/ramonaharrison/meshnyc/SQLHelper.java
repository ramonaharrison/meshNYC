package c4q.nyc.ramonaharrison.meshnyc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class SQLHelper extends SQLiteOpenHelper {

    public static final String MY_DB = "mydb";
    public static final int VERSION = 1;

    //create table shelters
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + Columns.TABLE_NAME_SHELTERS + " (" +
            Columns._ID + " INTEGER PRIMARY KEY," +
            Columns.COLUMN_CITY + " TEXT," +
            Columns.COLUMN_ADDRESS + " TEXT," +
            Columns.COLUMN_LATITUDE + " INTEGER," +
            Columns.COLUMN_LONGITUDE + " INTEGER," +
            Columns.COLUMN_POSTAL + " TEXT" +
            " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Columns.TABLE_NAME_SHELTERS;

    private static SQLHelper INSTANCE;

    public static synchronized SQLHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SQLHelper(context.getApplicationContext());
        }

        return INSTANCE;
    }

    private SQLHelper(Context context) {
        super(context, MY_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void deleteTableShelters() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    public static abstract class Columns implements BaseColumns {
        public static final String TABLE_NAME_SHELTERS = "shelters";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_LATITUDE = "longitude";
        public static final String COLUMN_LONGITUDE = "latitude";
        public static final String COLUMN_POSTAL = "postal";
    }

    public void insertRow(String city, String address, double latitude, double longitude, String postal)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Columns.COLUMN_CITY, city);
        values.put(Columns.COLUMN_ADDRESS, address);
        values.put(Columns.COLUMN_LONGITUDE, latitude);
        values.put(Columns.COLUMN_LATITUDE, longitude);
        values.put(Columns.COLUMN_POSTAL, postal);

        db.insert(
                Columns.TABLE_NAME_SHELTERS,
                null,
                values);
    }

    public ArrayList<Shelter> getAllShelters()
    {
        String[] projection = {
                Columns._ID,
                Columns.COLUMN_CITY,
                Columns.COLUMN_ADDRESS,
                Columns.COLUMN_LATITUDE,
                Columns.COLUMN_LONGITUDE,
                Columns.COLUMN_POSTAL
        };

        SQLiteDatabase db = getWritableDatabase();

        ArrayList<Shelter> shelters = new ArrayList<>();

        Cursor cursor = db.query(Columns.TABLE_NAME_SHELTERS, projection, null, null, null, null, null);
        while(cursor.moveToNext())
        {
            shelters.add(new Shelter(
                    cursor.getInt(cursor.getColumnIndex(Columns._ID)),
                    cursor.getString(cursor.getColumnIndex(Columns.COLUMN_CITY)),
                    cursor.getString(cursor.getColumnIndex(Columns.COLUMN_ADDRESS)),
                    cursor.getDouble(cursor.getColumnIndex(Columns.COLUMN_LATITUDE)),
                    cursor.getDouble(cursor.getColumnIndex(Columns.COLUMN_LONGITUDE)),
                    cursor.getString(cursor.getColumnIndex(Columns.COLUMN_POSTAL))));
        }
        cursor.close();
        return shelters;
    }
}


