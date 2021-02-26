package com.moisegui.artia.ui.admin;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.moisegui.artia.ItemResultActivity;
import com.moisegui.artia.R;
import com.moisegui.artia.data.model.Motif;
import com.moisegui.artia.services.MotifCallback;
import com.moisegui.artia.services.MotifService;
import com.moisegui.artia.services.MyCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ListMotifsFragment extends Fragment {

    View root;
    public Context context;
    ArrayList<Items> items = new ArrayList<>();
    ListView listMotifs;
    String libelles[] = {"motif1", "motif2", "motif3", "motif4", "motif5", "motif6", "motif7"};
    int images[] = {R.drawable.camera_icon, R.drawable.camera_icon, R.drawable.camera_icon};

    View alertDialogView;
    MaterialAlertDialogBuilder materialAlertDialogBuilder;
    TextInputLayout libelle;
    TextInputLayout signification;
    Button telecharger;
    ImageView new_motif;
    final int REQUEST_CODE = 1;

    private final int FILE_CHOOSER_REQUEST = 112;
    String picturePath;
    TextView txt_empty_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme2);
        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        root = localInflater.inflate(R.layout.fragment_list_motifs, container, false);
        listMotifs = root.findViewById(R.id.listMotifs);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(root.getContext(), R.layout.item_motif, R.id.lib_motif, libelles);
        txt_empty_list = root.findViewById(R.id.txt_empty_list_motif);
        listMotifs.setEmptyView(txt_empty_list);

        MotifService.findAll(new MotifCallback() {
            @Override
            public void onCallback(List<Motif> motifs) {
                Log.w("ListMotifsFragment", "onCallback: findAll motif");
                CustomAdapter adapter = new CustomAdapter(root.getContext(), R.layout.item_motif, motifs);
                listMotifs.setAdapter(adapter);
            }
        });

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(root.getContext());

        listMotifs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ItemResultActivity.class);
                intent.putExtra("image", images[position]);
                intent.putExtra("libelles", libelles[position]);

                context.startActivity(intent);
            }
        });

        FloatingActionButton captureButton = (FloatingActionButton) root.findViewById(R.id.btn_add);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialogView = LayoutInflater.from(root.getContext())
                                .inflate(R.layout.add_motif_custom_dialog, null, false);
                        launchAlertDialog();

                    }
                });

        return root;
    }

    public void launchAlertDialog() {
        libelle = alertDialogView.findViewById(R.id.libelle);
        signification = alertDialogView.findViewById(R.id.signification);
        telecharger = alertDialogView.findViewById(R.id.telecharger);
        new_motif = alertDialogView.findViewById(R.id.new_motif);

        //Building alert dialog
        materialAlertDialogBuilder.setView(alertDialogView);
        materialAlertDialogBuilder.setTitle("Ajouter un nouveau motif");
        materialAlertDialogBuilder.setMessage("Nouveau motif");
        materialAlertDialogBuilder.setCancelable(false);
        materialAlertDialogBuilder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (libelle.getEditText().getText() == null || signification.getEditText().getText() == null
                        || picturePath == null) {
                    Toast.makeText(context, "You must fill in all the fields!", Toast.LENGTH_LONG).show();
                } else {
                    String libelle_ = libelle.getEditText().getText().toString();
                    String signification_ = signification.getEditText().getText().toString();

                    MotifService.addMotif(libelle_, signification_, picturePath, new MyCallback() {
                        @Override
                        public void onCallback(List<String> values) {
                            MotifService.saveMotif(values);
                            Log.i("ListMotifFragment", "onCallback save motif");
                        }
                    });
                }

            }
        });
        materialAlertDialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
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
                    Toast.makeText(root.getContext(), "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        materialAlertDialogBuilder.show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == FILE_CHOOSER_REQUEST && data != null) {
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePath[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            thumbnail = getResizedBitmap(thumbnail, 1000);
            new_motif.setImageBitmap(thumbnail);
            new_motif.setVisibility(View.VISIBLE);
            telecharger.setVisibility(View.GONE);
            //BitMapToString(thumbnail);
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


}