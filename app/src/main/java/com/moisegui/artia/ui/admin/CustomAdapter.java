package com.moisegui.artia.ui.admin;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.moisegui.artia.ItemResultActivity;
import com.moisegui.artia.R;

import com.moisegui.artia.ResultActivity;
import com.moisegui.artia.data.model.Motif;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Motif> {

    private  int resource;
    private  List<Motif> objects;
    Context context;


    View alertDialogView;
    MaterialAlertDialogBuilder materialAlertDialogBuilder;
    TextInputLayout libelle;
    TextInputLayout signification;
    Button telecharger;
    ImageView new_motif;

    private final int FILE_CHOOSER_REQUEST = 112;
    String picturePath;

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
        Button showMoreButton = convertView.findViewById(R.id.show_more_button_admin);;

        Picasso.get()
                .load(getItem(position).getMotifImageSrc())
                .resize(163,141)
                .into(imageView);
        textView.setText(getItem(position).getMotifName());

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                builder.setTitle(context.getString(R.string.delete_the_item) + objects.get(position).getMotifName() + context.getString(R.string.from_your_history));
                builder.setMessage(R.string.verify_delete);
//                builder.setIcon();
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
                builder.show();
            }
        });

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAlertDialog(position);
            }
        });

        showMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ItemResultActivity.class);
//                intent.putExtra("image", images[position]);
                intent.putExtra("title", objects.get(position).getMotifName());
//                intent.putExtra("description", objects.get(position).getMotifDescription());

                context.startActivity(intent);

            }
        });

        return convertView;
    }

    private void launchAlertDialog(int position) {
        alertDialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.add_motif_custom_dialog, null, false);
        libelle = alertDialogView.findViewById(R.id.libelle);
        signification = alertDialogView.findViewById(R.id.signification);
        telecharger = alertDialogView.findViewById(R.id.telecharger);
        new_motif = alertDialogView.findViewById(R.id.new_motif);

        libelle.getEditText().setText(objects.get(position).getMotifName());
//        signification.getEditText().setText();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(alertDialogView);
        alertDialogBuilder.setTitle("Update " + objects.get(position).getMotifName() +" pattern");
        alertDialogBuilder.setMessage("Updating" + objects.get(position).getMotifName());
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (libelle.getEditText().getText() == null || signification.getEditText().getText() == null
                        || picturePath == null) {
                    Toast.makeText(context, "You must fill in all the fields!", Toast.LENGTH_LONG).show();
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

                try {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    ((Activity) context).startActivityForResult(i, FILE_CHOOSER_REQUEST);
//                    startActivityForResult(
//                            Intent.createChooser(intent, "Select a File to Upload"),
//                            FILE_CHOOSER_REQUEST);
                } catch (ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(context, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialogBuilder.show();
    }
}
