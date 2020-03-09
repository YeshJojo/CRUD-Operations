package com.jojo.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class dbHandler extends SQLiteOpenHelper {

    public static final String DB_NAME = "studentDb";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "data";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_SEM = "sem";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +
                    "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_NAME + " TEXT,"
                    + COL_SEM + " INTEGER)";

    public dbHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        this.onCreate(db);
    }
    public long insertData(String name, String sem){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_SEM, sem);
        long id = database.insert(TABLE_NAME, null, values);
        database.close();
        return id;
    }
    public int updateData(String id, String name, String sem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_SEM, sem);
        return db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{id});
    }
    public void deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " = ?", new String[] {id});
        db.close();
    }
    public List<stdDetails> getAllData(){
        List<stdDetails> details = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                stdDetails stdDetails = new stdDetails();
                stdDetails.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
                stdDetails.setName(cursor.getString(cursor.getColumnIndex(COL_NAME)));
                stdDetails.setSem(cursor.getInt(cursor.getColumnIndex(COL_SEM)));
                details.add(stdDetails);
            } while (cursor.moveToNext());
        }
        database.close();
        return details;
    }
}
