package com.moisegui.artia.services;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.moisegui.artia.data.model.Admin;
import com.moisegui.artia.data.model.Motif;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MotifService {
    private static DatabaseReference mMotifReference = FirebaseDatabase.getInstance().getReference("motifs");

    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    private static  String folder_name ="motifs";
    private static  String file_name ;

    public static void addMotif(String libelle, String signification, String path,MyCallback callback) {
        file_name = libelle+".jpg";
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference to "file_name"
        StorageReference motifsRef = storageRef.child(file_name);

        // Create a reference to 'folder/file_name'
        StorageReference motifFolderRef = storageRef.child(folder_name+"/"+file_name);


        Uri file = Uri.fromFile(new File(path));
        UploadTask uploadTask = motifFolderRef.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return motifFolderRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    List<String > motifData = new ArrayList<>();
                    motifData.add(libelle);
                    motifData.add(signification);
                    motifData.add(downloadUri.toString());
                    callback.onCallback(motifData);
                } else {
                    Log.w("MotifService","Get downloadUri task failed");
                }
            }
        });
    }

    public static void saveMotif(List<String> data){

        Motif motif = new Motif(data.get(0),data.get(1),data.get(2));
        mMotifReference.child("motif_"+System.currentTimeMillis()).setValue(motif);

    }

    public static void findAll(MotifCallback callback) {
        List<Motif> motifs = new ArrayList<>();

        mMotifReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Motif motif = new Motif();
                for (DataSnapshot motifSnapshot : dataSnapshot.getChildren()) {
                    motif = motifSnapshot.getValue(Motif.class);
                    motifs.add(motif);
                }
                callback.onCallback(motifs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MotifService", "onCancelled: " + error.getMessage());
            }
        });
    }
}
