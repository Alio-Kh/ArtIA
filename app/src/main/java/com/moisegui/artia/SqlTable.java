package com.moisegui.artia;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.opencv.core.Mat;

public class SqlTable extends SQLiteOpenHelper {
    String table = "mydb";

    public SqlTable(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + table + " (name TEXT UNIQUE, t INTEGER, w INTEGER, h INTEGER, pix BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
    }

    public void dbput(String name, Mat m) {
        long nbytes = m.total() * m.elemSize();
        byte[] bytes = new byte[(int) nbytes];
        m.get(0, 0, bytes);

        dbput(name, m.type(), m.cols(), m.rows(), bytes);
    }

    public void dbput(String name, int t, int w, int h, byte[] bytes) {
        Log.d("dbput", name + " " + t + " " + w + "x" + h);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("t", t);
        values.put("w", w);
        values.put("h", h);
        values.put("pix", bytes);
        db.insert(table, null, values);
        db.close();
    }

    public void deleteDb() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, null, null);
    }

    public Cursor dbget() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"t", "w", "h", "pix"};
        Cursor cursor = db.query(table, columns, null,
                null,//new String[] { name }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit


        // int t = cursor.getInt(0);
        //int w = cursor.getInt(1);
        //int h = cursor.getInt(2);
        // byte[] p = cursor.getBlob(3);
        //  Mat m = new Mat(h,w,t);
        // m.put(0,0,p);
        //  Log.d("dbget("+name+")", m.toString());
        return cursor;
    }
};
