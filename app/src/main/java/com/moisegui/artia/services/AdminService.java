package com.moisegui.artia.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moisegui.artia.data.model.Admin;

import java.util.ArrayList;
import java.util.List;

public class AdminService {
    private static DatabaseReference mAdminReference = FirebaseDatabase.getInstance().getReference("admins");

    public static void addAdmin(String id,String userId) {
        Admin admin = new Admin(userId);

        mAdminReference.child(id).setValue(admin);
    }

    public static void findAll(AdminCallback myCallback) {
        List<String> adminsIds = new ArrayList<>();

        mAdminReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Admin admin = new Admin();
                for (DataSnapshot adminSnapshot : dataSnapshot.getChildren()) {
                    admin = adminSnapshot.getValue(Admin.class);
                    adminsIds.add(admin.getAdminId());
                }
                myCallback.onCallback(adminsIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("AdminService", "onCancelled: " + error.getMessage());
            }
        });

        /*mAdminReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    //myCallback.onCallback();
                }
            }
        });*/
    }
}
