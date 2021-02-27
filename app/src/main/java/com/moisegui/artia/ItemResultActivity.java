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
import com.moisegui.artia.data.model.Motif;
import com.moisegui.artia.services.MotifService;
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


    private Motif motif;

    View alertDialogView;
    MaterialAlertDialogBuilder materialAlertDialogBuilder;
    TextInputLayout libelle;
    TextInputLayout signification;
    ImageView new_motif;
    Button telecharger;

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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            motif = (Motif) bundle.getSerializable("motif");
            Picasso.get()
                    .load(motif.getMotifImageSrc())
                    .placeholder(R.drawable.mx_bg_gradient1)
                    .fit()
                    .centerCrop()
                    .into(image);
            title.setText(motif.getMotifName());
            pattern.setText(motif.getMotifName());
            desc.setText(motif.getMotifDescription());
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
                builder.setTitle(getApplication().getString(R.string.delete_the_item) + motif.getMotifName());
                builder.setMessage(R.string.verify_delete);

                // add the buttons
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MotifService.deleteById(motif.getMotifID());
                        finish();
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
        new_motif = alertDialogView.findViewById(R.id.new_motif);
        telecharger = alertDialogView.findViewById(R.id.telecharger);
        telecharger.setVisibility(View.GONE);

        libelle.getEditText().setText(motif.getMotifName());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(alertDialogView);
        alertDialogBuilder.setTitle("Update " + motif.getMotifName() +" pattern");
        alertDialogBuilder.setMessage("Updating" + motif.getMotifName());
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Motif motif_updated = new Motif();
                motif_updated.setMotifID(motif.getMotifID());

                if(libelle.getEditText().getText() == null){
                    motif_updated.setMotifName(motif.getMotifName());
                }else {
                    motif_updated.setMotifName(libelle.getEditText().getText().toString());
                }

                if(signification.getEditText().getText() == null){
                    motif_updated.setMotifDescription(motif.getMotifDescription());
                }else {
                    motif_updated.setMotifDescription(signification.getEditText().getText().toString());
                }
                motif_updated.setMotifImageSrc(motif.getMotifImageSrc());
                MotifService.updateMotif(motif_updated);
            }


        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogBuilder.show();
    }
}