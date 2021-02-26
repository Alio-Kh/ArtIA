package com.moisegui.artia.service;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.moisegui.artia.data.model.History;
import com.moisegui.artia.data.model.Motif;
import com.moisegui.artia.ui.history.AppAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryService {

    FirebaseAuth auth = FirebaseAuth.getInstance();;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = auth.getCurrentUser();/*
    DatabaseReference historyReference = database.getReference("histories");
    DatabaseReference motifReference = database.getReference("Motifs");*/
    private static final String TAG = "ReadAndWriteSnippets";

    List<History> historyList = new ArrayList<>();
     static List<Motif> motifList = new ArrayList<>();

    List<String> titles_list = new ArrayList<>();
    List<String> images_list = new ArrayList<>();
    List<String> dates_list = new ArrayList<>();
    List<String> desc_list = new ArrayList<>();


    AppAdapter appAdapter;

    public HistoryService() {
    }




    /*insertNewMotif*/
    public String insertNewMotif() {
        DatabaseReference ref = database.getReference().child("motifs");
        String motifID = ref.push().getKey();
        Motif motif = new Motif(motifID,  "motifName",  "motifDescription",  "motifImageSrc");
        ref.child(motifID).setValue(motif);
        System.out.println("data inserted"+motifID);

        return motifID;
    }
    /*insertNewHistory*/
    public String insertNewHistory() {
        if(user != null) {
            DatabaseReference ref = database.getReference().child("Histories").child("UserIds").child(user.getUid());
            String motifID = insertNewMotif();
            String historyId = ref.push().getKey();
            Date date = new Date();
            History history = new History(historyId, date.toString(), user.getUid(), motifID, "historySrcImage");
            ref.child(historyId).setValue(history);
            System.out.println("data inserted" + historyId);

        return historyId;
        }else{
            return null;
        }
    }


    /* addHistoryEventListener */
    public List<History> addHistoryEventListener(View root, ListView lv) {
        if(user != null) {
            DatabaseReference historyReference = database.getReference().child("Histories").child("UserIds").child(user.getUid());
            System.out.println("upppp123");
            ValueEventListener historyListener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    historyList.clear();
                    for (DataSnapshot dataSnapshot_ : dataSnapshot.getChildren()) {
                        System.out.println(dataSnapshot_.getValue());
                        History history = dataSnapshot_.getValue(History.class);
                        historyList.add(history);
                    }
                    titles_list.clear();
                    images_list.clear();
                    dates_list.clear();
                    desc_list.clear();
                    System.out.println(motifList.size());
                    if (!motifList.isEmpty() && !historyList.isEmpty()) {
                        for (History history : historyList) {
                            String motifIDHistory = history.getMotif();
                            for (Motif motif : motifList) {
                                String motifId = motif.getMotifID();
                                if (motifId.equalsIgnoreCase(motifIDHistory)) {
                                    System.out.println("ok2");
                                    titles_list.add(motif.getMotifName());
                                    images_list.add(motif.getMotifImageSrc());
                                    dates_list.add(history.getHistorydate());
                                    desc_list.add(motif.getMotifDescription());
                                }
                            }
                        }
                    }

                    String titles_array[] = titles_list.toArray(new String[0]);
                    String images_array[] = images_list.toArray(new String[0]);
                    String date_array[] = dates_list.toArray(new String[0]);
                    String desc_array[] = dates_list.toArray(new String[0]);
                    appAdapter = new AppAdapter(root.getContext(), titles_array, images_array, date_array, desc_array);
                    lv.setAdapter(appAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadHistory cancelled", databaseError.toException());
                }
            };
            historyReference.addValueEventListener(historyListener);
            return historyList;
        }else {
            return null;
        }
    }

    /* addMotifEventListener */
    public List<Motif> addMotifEventListener() {
        DatabaseReference ref = database.getReference().child("motifs");
        ValueEventListener motifListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    motifList.clear();
                    for(DataSnapshot dataSnapshot_ : dataSnapshot.getChildren()) {
                        System.out.println(dataSnapshot_.getValue());

                        Motif motif = dataSnapshot_.getValue(Motif.class);
                        motifList.add(motif);

                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadHistory cancelled", databaseError.toException());
            }
        };
        ref.addValueEventListener(motifListener);

        return motifList;
    }


}
