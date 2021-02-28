package com.moisegui.artia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.moisegui.artia.data.model.Motif;
import com.moisegui.artia.filters.ar.ImageDetectionFilter;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.File;
import java.io.IOException;

public class MotifDbHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "ARTIA.DB";
    static final int DB_VERSION = 1;

    private static final String CREATE_TABLE = "create table " + MotifContrat.MotifTable.TABLE_NAME + "("
            + MotifContrat.MotifTable._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MotifContrat.MotifTable.t + " INTEGER, "
            + MotifContrat.MotifTable.w + " INTEGER, "
            + MotifContrat.MotifTable.h + " INTEGER, "
            + MotifContrat.MotifTable.pix + " BLOB, "
            + MotifContrat.MotifTable.motifID + " TEXT UNIQUE, "
            + MotifContrat.MotifTable.motifName + " TEXT, "
            + MotifContrat.MotifTable.motifDescription + " TEXT, "
            + MotifContrat.MotifTable.motifImageSrc + " TEXT);";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + MotifContrat.MotifTable.TABLE_NAME;

    public MotifDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        db.execSQL(CREATE_TABLE);
    }


    public void dbput(Motif motif, Mat m) {
        long nbytes = m.total() * m.elemSize();
        byte[] bytes = new byte[(int) nbytes];
        m.get(0, 0, bytes);

        dbput(motif, m.type(), m.cols(), m.rows(), bytes);
    }

    public void dbput(Motif motif, int t, int w, int h, byte[] bytes) {
        Log.d("dbput", motif.getMotifName() + " " + t + " " + w + "x" + h);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("t", t);
        values.put("w", w);
        values.put("h", h);
        values.put("pix", bytes);
        values.put("motifID", motif.getMotifID());
        values.put("motifName", motif.getMotifName());
        values.put("motifDescription", motif.getMotifDescription());
        values.put("motifImageSrc", motif.getMotifImageSrc());
        try {
            db.insert(MotifContrat.MotifTable.TABLE_NAME, null, values);
        } catch (Exception e) {
            // do nothing
        }
        db.close();
    }

    public void deleteDb() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MotifContrat.MotifTable.TABLE_NAME, null, null);
    }

    public Cursor dbget() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"t", "w", "h", "pix", "motifID", "motifName", "motifDescription", "motifImageSrc"};
        Cursor cursor = db.query(MotifContrat.MotifTable.TABLE_NAME, columns, null,
                null,//new String[] { name }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        return cursor;
    }


    public static void downloadMotifAndSave(Context context, Motif motif) throws IOException {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // [START download_create_reference]
        // Create a storage reference from our app
        StorageReference httpsReference = storage.getReferenceFromUrl(motif.getMotifImageSrc());

        // [START download_to_local_file]
        File localFile = File.createTempFile("image", "jpg");

        httpsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                try {
                    ImageDetectionFilter.saveMotif(context, motif, localFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        // [END download_to_local_file]
    }
}
