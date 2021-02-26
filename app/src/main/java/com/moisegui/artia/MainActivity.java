package com.moisegui.artia;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.moisegui.artia.data.model.Motif;
import com.moisegui.artia.services.MotifCallback;
import com.moisegui.artia.services.MotifService;

import java.io.IOException;
import java.util.List;
import com.moisegui.artia.data.model.History;
import com.moisegui.artia.service.HistoryService;
import com.moisegui.artia.services.AdminService;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private int REQUEST_CODE_PERMISSIONS = 101;
    private String[] REQUIRED_PERMISSIONS = new String[]{"Manifest.permission.CAMERA",
            "Manifest.permission.READ_EXTERNAL_STORAGE", "Manifest.permission.WRITE_EXTERNAL_STORAGE"};

    // Get Started Button
    private Button getStartedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the Action bar
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        // To navigate from Main Acticity to Search Activity (for pattern detection)
        getStarted();

        //FirebaseDatabase database = FirebaseDatabase.getInstance();
       // DatabaseReference myRef = database.getReference("message");

       // myRef.setValue("Hello, World!");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user =auth.getCurrentUser();
        //AdminService.addAdmin("1",user.getUid());

//        History history = new History(new Date().toString(), user.getUid(), "motif_1614314768128");
//        HistoryService service = new HistoryService();
//        service.add(history);

    }

    private void enablePersistence() {
        // [START rtdb_enable_persistence]
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // [END rtdb_enable_persistence]
    }

    // To navigate from Main Acticity to Search Activity (for pattern detection)
    public void getStarted() {
        final Intent searchAct = new Intent(this, HomeActivity.class);
        getStartedBtn = findViewById(R.id.get_started);
        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(searchAct);
            }
        });


        MotifService.findAll(new MotifCallback() {
            @Override
            public void onCallback(List<Motif> motifs) {
                for (Motif motif : motifs) {
                    try {
                        MotifDbHelper.downloadMotifAndSave(getApplicationContext(), motif);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

}