package com.moisegui.artia.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.moisegui.artia.R;

public class AccountsFragment extends Fragment {
    // Choose an arbitrary request code value
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "AccountsFragment";

    private AccountsViewModel accountsViewModel;
    FirebaseAuth auth;

    Button btnConnxion;
    TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountsViewModel =
                new ViewModelProvider(this).get(AccountsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        textView = root.findViewById(R.id.text_notifications);
//        accountsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        btnConnxion = root.findViewById(R.id.btnConnexion);
        auth = FirebaseAuth.getInstance();

        refreshInterface();

        return root;
    }

    public void refreshInterface() {
        if (auth.getCurrentUser() != null) {
            // already signed in
            textView.setText("You are Logged in");
            btnConnxion.setText(R.string.log_out);

            btnConnxion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logOut();
                }
            });

        } else {
            // not signed in
            textView.setText("You are not Logged in");
            btnConnxion.setText(R.string.log_in);

            btnConnxion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(
                            // Get an instance of AuthUI based on the default app
                            AuthUI.getInstance().createSignInIntentBuilder().build(),
                            RC_SIGN_IN);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.v(TAG, "Logged in");
                Toast.makeText(getContext(), String.valueOf(R.string.logged_in), Toast.LENGTH_SHORT).show();
                // ...
                refreshInterface();
            } else {
                Log.v(TAG, String.valueOf(R.string.login_failed));
                Toast.makeText(getContext(), String.valueOf(R.string.login_failed), Toast.LENGTH_SHORT).show();
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(getContext(), String.valueOf(R.string.log_in_cancelled), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(getContext(), String.valueOf(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getContext(), String.valueOf(R.string.unknown_error), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    public void logOut() {
        AuthUI.getInstance()
                .signOut(getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        refreshInterface();
                    }
                });
    }
}