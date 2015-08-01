package c4q.nyc.ramonaharrison.meshnyc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

public class SQLHelper extends SQLiteOpenHelper {

    public static final String MY_DB = "mydb";
    public static final int VERSION = 1;

    //create table shelters
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + Columns.TABLE_NAME_SHELTERS + " (" +
            Columns._ID + " INTEGER PRIMARY KEY," +
            Columns.COLUMN_CITY + " TEXT," +
            Columns.COLUMN_ADDRESS + " TEXT," +
            Columns.COLUMN_LONGITUDE + " INTEGER," +
            Columns.COLUMN_LATITUDE + " INTEGER," +
            Columns.COLUMN_POSTAL + " TEXT" +
            " )";


    //create table messages
    private static final String SQL_CREATE_MESSAGES = "CREATE TABLE " + MessageColumns.TABLE_NAME_MESSAGES + " (" +
            MessageColumns._ID + " INTEGER PRIMARY KEY," +
            MessageColumns.COLUMN_MESSAGE_INTENTION + " TEXT," +
            MessageColumns.COLUMN_MESSAGE_SEND_STATUS + " INTEGER," +
            MessageColumns.COLUMN_MESSAGE_TO_NAME + " TEXT," +
            MessageColumns.COLUMN_MESSAGE_TIMESTAMP + " TEXT," +
            MessageColumns.COLUMN_MESSAGE_CONTENT + " TEXT" +
            " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Columns.TABLE_NAME_SHELTERS;
    private static final String SQL_DELETE_MESSAGES = "DROP TABLE IF EXISTS " + MessageColumns.TABLE_NAME_MESSAGES;


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
        db.execSQL(SQL_CREATE_MESSAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_MESSAGES);
        onCreate(db);
    }

    public static abstract class Columns implements BaseColumns {
        public static final String TABLE_NAME_SHELTERS = "shelters";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_POSTAL = "postal";
    }


    public static abstract class MessageColumns implements BaseColumns {
        public static final String TABLE_NAME_MESSAGES = "messages";
        public static final String COLUMN_MESSAGE_INTENTION = "msgIntention";
        public static final String COLUMN_MESSAGE_SEND_STATUS = "isSent";
        public static final String COLUMN_MESSAGE_TO_NAME = "msgPhonenumber";
        public static final String COLUMN_MESSAGE_TIMESTAMP = "msgTimestamp";
        public static final String COLUMN_MESSAGE_CONTENT = "msgcontent";
    }


    // TODO: PUT THIS WHEN MESSAGE IS SENT OR RECEIVED
    public void insertMessageRow(String id, String intention, int isSent, String name, String timeStamp, String messageContent) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MessageColumns.COLUMN_MESSAGE_INTENTION, intention);
        values.put(MessageColumns.COLUMN_MESSAGE_SEND_STATUS, isSent);
        values.put(MessageColumns.COLUMN_MESSAGE_TO_NAME, name);
        values.put(MessageColumns.COLUMN_MESSAGE_TIMESTAMP, timeStamp);
        values.put(MessageColumns.COLUMN_MESSAGE_CONTENT, messageContent);

        db.insert(
                MessageColumns.TABLE_NAME_MESSAGES,
                null,
                values);
    }

    public int getAllMessages() {
        String[] projection = {
                MessageColumns._ID,
                MessageColumns.COLUMN_MESSAGE_INTENTION,
                MessageColumns.COLUMN_MESSAGE_SEND_STATUS,
                MessageColumns.COLUMN_MESSAGE_TO_NAME,
                MessageColumns.COLUMN_MESSAGE_TIMESTAMP,
                MessageColumns.COLUMN_MESSAGE_CONTENT,

        };

        SQLiteDatabase db = getWritableDatabase();

        ArrayList<Message> messages = new ArrayList<>();

        Cursor cursor = db.query(MessageColumns.TABLE_NAME_MESSAGES, projection, null, null, null, null, null);
        while (cursor.moveToNext()) {
            messages.add(new Message(
                    cursor.getString(cursor.getColumnIndex(MessageColumns._ID)),
                    cursor.getString(cursor.getColumnIndex(MessageColumns.COLUMN_MESSAGE_INTENTION)),
                    cursor.getInt(cursor.getColumnIndex(MessageColumns.COLUMN_MESSAGE_SEND_STATUS)),
                    cursor.getString(cursor.getColumnIndex(MessageColumns.COLUMN_MESSAGE_TO_NAME)),
                    cursor.getString(cursor.getColumnIndex(MessageColumns.COLUMN_MESSAGE_TIMESTAMP)),
                    cursor.getString(cursor.getColumnIndex(MessageColumns.COLUMN_MESSAGE_CONTENT))));
        }
        cursor.close();
        return messages.size();
    }
}
