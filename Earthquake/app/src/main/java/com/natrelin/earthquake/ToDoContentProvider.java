package com.natrelin.earthquake;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.sql.RowId;

/**
 * Created by NATRELIN on 10/7/2015.
 */
public class ToDoContentProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://com.reline.todoprovider/todoitems");

    public static final String KEY_ID = "_id";
    public static final String KEY_TASK = "task";
    public static final String KEY_CREATION_DATE = "creation_date";
    private MySQLiteOpenHelper myOpenHelper;
    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW = 2;
    private static final UriMatcher uriMatcher;

    // populate UriMatcher object
    // 'todoitems' is request for all items
    // 'todoitems/[rowID]' requests a single row
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.reline.todoprovider", "todoitems", ALLROWS);
        uriMatcher.addURI("com.reline.todoprovider", "todoitems/#", SINGLE_ROW);
    }


    @Override
    public boolean onCreate() {
        myOpenHelper = new MySQLiteOpenHelper(getContext(),
        MySQLiteOpenHelper.DATABASE_NAME, null,
        MySQLiteOpenHelper.DATABASE_VERSION);
        return true;
    }

    @Override
    public String getType(Uri uri) {
        // return String that identifies the MIME type for a Content Provider URI
        switch (uriMatcher.match(uri)) {
            case ALLROWS: return "vn.android.cursor.dir/vnd.reline.todos";
            case SINGLE_ROW: return "vnd.android.cursor.item/vnd.reline.todos";
            default: throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // open read-only db
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        // replace with valid SQL statements if necessary
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MySQLiteOpenHelper.DATABASE_TABLE);

        // if a row query, limit result set to the row
        switch (uriMatcher.match(uri)) {
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(KEY_ID + "=" + rowID);
            default:
                break;
        }

        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // open/read db
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        // if row URI, limit deletion to row
        switch(uriMatcher.match(uri)) {
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                selection = KEY_ID + "=" + rowID + (!TextUtils.isEmpty(selection) ? " AND (" +
                selection + ')' : ""); // if selection is not empty, append selection to the SQL command
            default:
                break;
        }

        // specify where clause; delete all rows and values by passing in '1'
        if (selection == null) {
            selection = "1";
        }

        // execute the deletion
        int deleteCount = db.delete(MySQLiteOpenHelper.DATABASE_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] wArgs) {
        return 0;
    }

    private static class MySQLiteOpenHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "todoDatabase.db";
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_TABLE = "todoItemTable";

        // SQL statement to create a new database
        private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (" +
                KEY_ID + " integer primary key autoincrement, " + KEY_TASK + " text not null, " +
                KEY_CREATION_DATE + "long);";

        public MySQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // called when no database exists in disk and the helper class needs to create a new one
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_TABLE);
        }

        // called when there is a database version mismatch meaning that the version of the db on
        // disk needs to be upgraded to the current version
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // log version upgrade
            Log.w("TaskDBAdapter", "Upgrading from version " + oldVersion + " to " + newVersion +
                    ", which will destroy old data.");

            // upgrade the existing database to conform to the new version

            // drop old table
            db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);

            // create new table
            onCreate(db);
        }
    }
}
