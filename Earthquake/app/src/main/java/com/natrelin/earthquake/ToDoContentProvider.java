package com.natrelin.earthquake;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Created by NATRELIN on 10/7/2015.
 */
public class ToDoContentProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://com.reline.todoprovider/todoitems");

    public static final String KEY_ID = "_id";
    public static final String KEY_TASK = "task";
    public static final String KEY_CREATION_DATE = "creation_date";

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public String getType(Uri url) {
        return null;
    }

    @Override
    public Cursor query(Uri url, String[] projection, String selection, String[] selectionArgs, String sort) {
        return null;
    }

    @Override
    public Uri insert(Uri url, ContentValues initialValues) {
        return null;
    }

    @Override
    public int delete(Uri url, String where, String[] whereArgs) {
        return 0;
    }

    @Override
    public int update(Uri url, ContentValues values, String where, String[] wArgs) {
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
            // TODO create database tables
        }

        // called when there is a database version mismatch meaning that the version of the db on
        // disk needs to be upgraded to the current version
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO upgrade database
        }
    }
}
