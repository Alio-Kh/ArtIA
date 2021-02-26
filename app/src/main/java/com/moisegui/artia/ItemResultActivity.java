package com.moisegui.artia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

public class ItemResultActivity extends AppCompatActivity {

    private ImageView image;
    private TextView title;
    private TextView date;
    private TextView origin;
    private TextView pattern;
    private TextView desc;
    View root;
    public Context context;

    private String title_data;
    private String image_data;
    private String desc_data;

    View alertDialogView;
    MaterialAlertDialogBuilder materialAlertDialogBuilder;
    TextInputLayout libelle;
    TextInputLayout signification;
    Button telecharger;
    ImageView new_motif;

    private final int FILE_CHOOSER_REQUEST = 112;
    String picturePath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().setTitle("List patterns");
        final Context contextThemeWrapper = new ContextThemeWrapper(this, R.style.AppTheme2);
        // clone the inflater using the ContextThemeWrapper
        LayoutInflater layoutInflater =  getLayoutInflater().cloneInContext(contextThemeWrapper);
        root = layoutInflater.inflate(R.layout.fragment_list_motifs, null, false);


        image = findViewById(R.id.image_result_fragment);
        title = findViewById(R.id.title_result_fragment);
        pattern = findViewById(R.id.pattern_result_fragment);
        desc = findViewById(R.id.desc_result_fragment);

        String origin_ = "origin";
        String pattern_ = "pattern";

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            image_data = bundle.getString("image");
            title_data = bundle.getString("title");
//            desc_data = bundle.getString("description");
            System.out.println(image_data);
            Picasso.get().load(image_data).placeholder(R.drawable.mx_bg_gradient1).into(image);
            title.setText(title_data);
            String desc_ = bundle.getString("desc");
            pattern.setText(title_data);
            desc.setText(desc_);
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete_update, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_button:
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getApplication().getString(R.string.delete_the_item) + title_data + getApplication().getString(R.string.from_your_history));
                builder.setMessage(R.string.verify_delete);
                /*builder.setIcon(image_data);*/

                // add the buttons
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.action_update_button:

                launchAlertDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchAlertDialog() {
        alertDialogView = LayoutInflater.from(root.getContext())
                .inflate(R.layout.add_motif_custom_dialog, null, false);
        libelle = alertDialogView.findViewById(R.id.libelle);
        signification = alertDialogView.findViewById(R.id.signification);
        telecharger = alertDialogView.findViewById(R.id.telecharger);
        new_motif = alertDialogView.findViewById(R.id.new_motif);

        libelle.getEditText().setText(title_data);
//        signification.getEditText().setText();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(alertDialogView);
        alertDialogBuilder.setTitle("Update " + title_data +" pattern");
        alertDialogBuilder.setMessage("Updating" + title_data);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (libelle.getEditText().getText() == null || signification.getEditText().getText() == null
                        || picturePath == null) {
                    Toast.makeText(getApplicationContext(), "You must fill in all the fields!", Toast.LENGTH_LONG).show();
                } else {
                    String libelle_ = libelle.getEditText().getText().toString();
                    String signification_ = signification.getEditText().getText().toString();

//                    MotifService.addMotif(libelle_, signification_, picturePath, new MyCallback() {
//                        @Override
//                        public void onCallback(List<String> values) {
//                            MotifService.saveMotif(values);
//                            Log.i("ListMotifFragment", "onCallback save motif");
//                        }
//                    });
                }

            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        telecharger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(intent, REQUEST_CODE);

                try {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, FILE_CHOOSER_REQUEST);
//                    startActivityForResult(
//                            Intent.createChooser(intent, "Select a File to Upload"),
//                            FILE_CHOOSER_REQUEST);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(getApplicationContext(), "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialogBuilder.show();
    }
}