package com.moisegui.artia.ui.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.moisegui.artia.ItemResultActivity;
import com.moisegui.artia.R;
import com.moisegui.artia.data.model.Motif;
import com.moisegui.artia.services.MotifService;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Motif> {

    private  int resource;
    private  List<Motif> objects;
    Context context;


    View alertDialogView;
    TextInputLayout libelle;
    TextInputLayout signification;
    Button telecharger;
    ImageView new_motif;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<Motif> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(super.getContext());
        convertView = layoutInflater.inflate(this.resource, parent, false);
        ImageView imageView = convertView.findViewById(R.id.img_motif);
        TextView textView = convertView.findViewById(R.id.lib_motif);
        Button delete_button = convertView.findViewById(R.id.delete_button_admin);
        Button update_button = convertView.findViewById(R.id.update_button_admin);;
        Button showMoreButton = convertView.findViewById(R.id.show_more_button_admin);

        // Charger l'image avec la librairie Picasso
        Picasso.get()
                .load(getItem(position).getMotifImageSrc())
                .fit()
                .centerCrop()
                .into(imageView);
        textView.setText(getItem(position).getMotifName());

        // Boutton pour supprimer un motif
        // Il lance une boite de dialogue
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                builder.setTitle(context.getString(R.string.delete_the_item) + objects.get(position).getMotifName() + context.getString(R.string.from_your_history));
                builder.setMessage(R.string.verify_delete);
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MotifService.deleteById(getItem(position).getMotifID());
                    }
                });
                builder.show();
            }
        });

        // Button pour modifier un motif
        // Il lance une boite de dialogue
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAlertDialog(position);
            }
        });

        // Button pour afficher les details d'un motif
        showMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ItemResultActivity.class);
                intent.putExtra("motif", (Serializable) objects.get(position));
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    // Pour creer et lancer la boite de dialogue
    private void launchAlertDialog(int position) {
        alertDialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.add_motif_custom_dialog, null, false);
        libelle = alertDialogView.findViewById(R.id.libelle);
        signification = alertDialogView.findViewById(R.id.signification);
        telecharger = alertDialogView.findViewById(R.id.telecharger);
        telecharger.setVisibility(View.GONE);
        new_motif = alertDialogView.findViewById(R.id.new_motif);

        libelle.getEditText().setText(objects.get(position).getMotifName());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(alertDialogView);
        alertDialogBuilder.setTitle("Update " + objects.get(position).getMotifName() +" pattern");
        alertDialogBuilder.setMessage("Updating" + objects.get(position).getMotifName());
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Motif motif_updated = new Motif();
                motif_updated.setMotifID(getItem(position).getMotifID());

                if(libelle.getEditText().getText() == null){
                    motif_updated.setMotifName(getItem(position).getMotifName());
                }else {
                    motif_updated.setMotifName(libelle.getEditText().getText().toString());
                }

                if(signification.getEditText().getText() == null){
                    motif_updated.setMotifDescription(getItem(position).getMotifDescription());
                }else {
                    motif_updated.setMotifDescription(signification.getEditText().getText().toString());
                }
                    motif_updated.setMotifImageSrc(getItem(position).getMotifImageSrc());
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
