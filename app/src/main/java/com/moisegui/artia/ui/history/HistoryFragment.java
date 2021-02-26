package com.moisegui.artia.ui.history;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.moisegui.artia.R;
import com.moisegui.artia.data.model.History;
import com.moisegui.artia.data.model.Motif;
import com.moisegui.artia.service.HistoryService;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    //
    private HistoryService historyService;

    private HistoriesViewModel historiesViewModel;


    View root;
    public Context context;

    FirebaseAuth auth;
    private static final int RC_SIGN_IN = 123;

    List<History> historyList = new ArrayList<>();

    List<Motif> motifList = new ArrayList<>();

    private static final String TAG = "ReadAndWriteSnippets";

    Button btnLogin;
    TextView textView;

    ConstraintLayout notConnectedView;
    Button btnLogout;

    ListView lv;
    TextView emptyTextView;



    AppAdapter appAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        historiesViewModel = new ViewModelProvider(this).get(HistoriesViewModel.class);

        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme2);
        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        root = localInflater.inflate(R.layout.fragment_history, container, false);
        //final TextView textView = root.findViewById(R.id.text_dashboard);

        notConnectedView = root.findViewById(R.id.not_connected_view);
        btnLogin = root.findViewById(R.id.btnConnexion);

        lv = root.findViewById(R.id.lv);
        emptyTextView = root.findViewById(R.id.enty_list_text);
        lv.setEmptyView(textView);

        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setLogo(R.drawable.logo)
                                .build(),
                        RC_SIGN_IN);
                startIfLogin();
            }
        });

        startIfLogin();


        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        startIfLogin();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(getContext(), R.string.logged_in, Toast.LENGTH_SHORT).show();
                // ...
                startIfLogin();
            } else {
//                Toast.makeText(getContext(), R.string.login_failed, Toast.LENGTH_SHORT).show();
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(getContext(), R.string.log_in_cancelled, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getContext(), R.string.unknown_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void startIfLogin() {
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            historyService = new HistoryService();
            // already signed in
            historyService.findAll(root, lv);

            lv.setVisibility(View.VISIBLE);
            lv.setEmptyView(emptyTextView);
//            emptyTextView.setVisibility(View.VISIBLE);
            notConnectedView.setVisibility(View.INVISIBLE);

        } else {
            // not signed in
            notConnectedView.setVisibility(View.VISIBLE);
            lv.setVisibility(View.INVISIBLE);
            emptyTextView.setVisibility(View.INVISIBLE);

        }
    }





}