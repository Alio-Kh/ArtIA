package com.moisegui.artia.ui.history;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.moisegui.artia.MainActivity;
import com.moisegui.artia.R;
import com.moisegui.artia.ResultActivity;
import com.moisegui.artia.data.model.History;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private HistoriesViewModel historiesViewModel;

    View root;
    public Context context;

    FirebaseAuth auth;
    private static final int RC_SIGN_IN = 123;

    private static final String TAG = "ReadAndWriteSnippets";

    Button btnLogin;
    TextView textView;

    ConstraintLayout notConnectedView;
    Button btnLogout;



    ListView lv;
    String titles[] = {"motif num 1", "motif num 2", "motif num 3"};
    String dates[] = {"12/12/2021", "12/12/2021", "12/12/2021"};

    int images[] = {R.drawable.motif_bg_1, R.drawable.motif_bg_2, R.drawable.motif_bg_3};

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

        AppAdapter appAdapter = new AppAdapter(root.getContext(), titles, images, dates);
        lv.setAdapter(appAdapter);
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
            // already signed in
            lv.setVisibility(View.VISIBLE);
            notConnectedView.setVisibility(View.INVISIBLE);

        } else {
            // not signed in
            notConnectedView.setVisibility(View.VISIBLE);
            lv.setVisibility(View.INVISIBLE);
        }
    }


    private void addHistoryEventListener(DatabaseReference historyReference, ArrayList<History> historyList) {
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot_ : dataSnapshot.getChildren()){

                }
                // Get Post object and use the values to update the UI
                History history = dataSnapshot.getValue(History.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadHistory cancelled", databaseError.toException());
            }
        };
        historyReference.addValueEventListener(postListener);
        // [END post_value_event_listener]
    }

}