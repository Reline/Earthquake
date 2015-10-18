package com.natrelin.earthquake;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.net.URI;

/**
 * Created by mukondono on 10/16/15.
 */
public class EarthquakeProvider extends ContentProvider {

    public class EarthquakeDatabaseHelper extends SQLiteOpenHelper {

        public EarthquakeDatabaseHelper(Context context, String name,
                                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        private static final String TAG = "EarthquakeProvider";
        private static final String DATABASE_NAME = "earthquakes.db";
        private static final int DATABASE_VERSION = 1;
        private static final String EARTHQUAKE_TABLE = "earthquakes";
        private static final String DATABASE_CREATE =
                "create table " + EARTHQUAKE_TABLE + " (" + KEY_ID + " integer primary key autoincrement, "
                        + KEY_DATE + " INTEGER, "
                        + KEY_DETAILS + " TEXT, "
                        + KEY_SUMMARY + " TEXT, "
                        + KEY_LOCATION_LAT + " FLOAT, "
                        + KEY_LOCATION_LNG + " FLOAT, "
                        + KEY_MAGNITUDE + " FLOAT, "
                        + KEY_LINK + " TEXT);";

        private SQLiteDatabase db;

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + EARTHQUAKE_TABLE);
            onCreate(db);
        }
    }

    public static final Uri CONTENT_URI =
            Uri.parse("content://com.natrelin.earthquakeprovider/earthquakes");

    // Column Names
    public static final String KEY_ID = "id";
    public static final String KEY_DATE = "date";
    public static final String KEY_DETAILS = "details";
    public static final String KEY_SUMMARY = "summary";
    public static final String KEY_LOCATION_LAT = "latitude";
    public static final String KEY_LOCATION_LNG = "longitude";
    public static final String KEY_MAGNITUDE = "magnitude";
    public static final String KEY_LINK = "link";

    private static final int QUAKES = 1;
    private static final int QUAKE_ID = 2;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.natrelin.earthquakeprovider", "earthquakes", QUAKES);
        uriMatcher.addURI("com.natrelin.earthquakeprovider", "earthquakes/#", QUAKE_ID);
    }

    /*@Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case QUAKES:
                return "vnd.android.cursor.dir/vnd.natrelin.earthquake";
            case QUAKE_ID:
                return "vnd.android.cursor.item/vnd.natrelin.earthquake";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }*/

    @Override
    public boolean onCreate() {
        Context context = getContext();
        EarthquakeDatabaseHelper dbHelper = new EarthquakeDatabaseHelper(context,
                EarthquakeDatabaseHelper.DATABASE_NAME, null,
                EarthquakeDatabaseHelper.DATABASE_VERSION);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


}
