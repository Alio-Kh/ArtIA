package com.moisegui.artia.service;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moisegui.artia.data.model.History;
import com.moisegui.artia.data.model.Motif;
import com.moisegui.artia.services.MotifCallback;
import com.moisegui.artia.services.MotifService;
import com.moisegui.artia.ui.history.AppAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryService {

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseUser user;
    DatabaseReference historyReference;
    private static final String TAG = "ReadAndWriteSnippets";

    List<History> historyList = new ArrayList<>();
    static List<Motif> motifList = new ArrayList<>();

    List<String> titles_list = new ArrayList<>();
    List<String> historyIDs_list = new ArrayList<>();
    List<String> images_list = new ArrayList<>();
    List<String> dates_list = new ArrayList<>();
    List<String> desc_list = new ArrayList<>();


    AppAdapter appAdapter;

    public HistoryService() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();
        historyReference = database.getReference().child("Histories").child("UserIds").child(user.getUid());
        MotifService.findAll(new MotifCallback() {
            @Override
            public void onCallback(List<Motif> motifs) {
                HistoryService.motifList = motifs;
            }
        });
    }

    /*insertNewHistory*/
    public String add(History history) {
        if (user != null) {
            String historyId = historyReference.push().getKey();
            Date date = new Date();
            history.setHistoryID(historyId);
            historyReference.child(historyId).setValue(history);
            System.out.println("data inserted" + historyId);
            return historyId;
        } else {
            return null;
        }
    }


    public void findAll(View root, ListView lv) {
        historyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                historyList.clear();
                titles_list.clear();
                images_list.clear();
                dates_list.clear();
                desc_list.clear();
                historyIDs_list.clear();
                for (DataSnapshot dataSnapshot_ : dataSnapshot.getChildren()) {
                    System.out.println(dataSnapshot_.getValue());
                    History history = dataSnapshot_.getValue(History.class);
                    historyList.add(history);

                    String motifIDHistory = history.getMotif();
                    for (Motif motif : motifList) {
                        String motifId = motif.getMotifID();
                        if (motifId.equals(motifIDHistory)) {
                            historyIDs_list.add(history.getHistoryID());
                            titles_list.add(motif.getMotifName());
                            images_list.add(motif.getMotifImageSrc());
                            dates_list.add(history.getHistorydate());
                            desc_list.add(motif.getMotifDescription());
                        }
                    }

                    String historyIDs_array[] = historyIDs_list.toArray(new String[0]);
                    String titles_array[] = titles_list.toArray(new String[0]);
                    String images_array[] = images_list.toArray(new String[0]);
                    String date_array[] = dates_list.toArray(new String[0]);
                    String desc_array[] = desc_list.toArray(new String[0]);
                    appAdapter = new AppAdapter(root.getContext(), images_array, titles_array, date_array, desc_array,historyIDs_array);
                    lv.setAdapter(appAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MotifService", "onCancelled: " + error.getMessage());
            }
        });
    }

    //**/
    public void deleteById(String historyId) {
        historyReference.child(historyId).removeValue();
    }


}
