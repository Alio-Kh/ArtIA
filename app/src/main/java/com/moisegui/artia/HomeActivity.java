package com.moisegui.artia;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.moisegui.artia.ui.result.ResultFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    static FragmentTransaction fragmentManager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search, R.id.navigation_history, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        fragmentManager = getSupportFragmentManager().beginTransaction();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if(getSupportActionBar().getTitle().toString().equals("Search")){
//
//        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_picture, menu);

        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.v("Click", "Click");
                return false;
            }
        });

        return true;
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
            moveTaskToBack(true); // close the app
        } else {
            Toast.makeText(this, R.string.press_back_again,
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                Toast.makeText(HomeActivity.this, "Submit", Toast.LENGTH_SHORT).show();break;
            case RESULT_CANCELED:
                Toast.makeText(HomeActivity.this, "Cancel", Toast.LENGTH_SHORT).show();break;
            default:
                Toast.makeText(HomeActivity.this, "No result!", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToResult(int image,String title,String date){

Intent bundle = new Intent(this, ResultActivity.class);
            bundle.putExtra("image",image);
            bundle.putExtra("title",title);
            bundle.putExtra("date",date);

            startActivity(bundle);
            /*fragment.setArguments(bundle);
            fragmentManager.replace(R.id.nav_host_fragment, fragment).commit();*/
    }
}