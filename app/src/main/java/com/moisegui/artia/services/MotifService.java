package com.moisegui.artia.services;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

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
import com.moisegui.artia.data.model.Motif;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MotifService {
    private static DatabaseReference mMotifReference = FirebaseDatabase.getInstance().getReference("motifs");

    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    private static  String folder_name ="motifs";
    private static  String file_name ;
    private static StorageReference storageRef = storage.getReference();

    public static void addMotif(Motif motif, MyCallback callback) {
        String motif_id = mMotifReference.push().getKey();
        motif.setMotifID(motif_id);
        file_name = motif.getMotifID()+".jpg";

        // Create a reference to 'folder/file_name'
        StorageReference motifFolderRef = storageRef.child(folder_name+"/"+file_name);


        Uri file = Uri.fromFile(new File(motif.getMotifImageSrc()));
        UploadTask uploadTask = motifFolderRef.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return motifFolderRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                motif.setMotifImageSrc(downloadUri.toString());
                callback.onCallback(motif);
            } else {
                Log.w("MotifService","Get downloadUri task failed");
            }
        });
    }

    public static void saveMotif(Motif motif){

        mMotifReference.child(motif.getMotifID()).setValue(motif);

    }

    public static void findAll(MotifCallback callback) {
        List<Motif> motifs = new ArrayList<>();

        mMotifReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                motifs.clear();
                motif = new Motif();
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

    public static void deleteById(String id) {

        mMotifReference.child(id).removeValue();
        file_name = id+".jpg";
        // Create a reference to the file to delete
        StorageReference motifFolderRef = storageRef.child(folder_name+"/"+file_name);
        motifFolderRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("MotifService","Pattern delted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("MotifService","Error! Couldn't delete the pattern. "+exception.getMessage());
            }
        });
    }

    public static void updateMotif(Motif motif){
        mMotifReference.child(motif.getMotifID()).setValue(motif);
    }
}
