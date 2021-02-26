package com.moisegui.artia.ui.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.moisegui.artia.AdminActivity;
import com.moisegui.artia.R;
import com.moisegui.artia.services.AdminService;
import com.moisegui.artia.services.MyCallback;

import java.util.List;
import java.util.Map;

public class AccountsFragment extends Fragment {
    // Choose an arbitrary request code value
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "AccountsFragment";

    private AccountsViewModel accountsViewModel;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    Button btnLogin;
    TextView textView;
    ConstraintLayout notConnectedView;
    ScrollView settingsView;
    TextView btnLogout;
    Button btnUpdateAccount;
    EditText emailEditText;
    EditText nameEditText;
    EditText oldPassEditText;
    EditText newPassEditText;

   TextView btnAdmin;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountsViewModel =
                new ViewModelProvider(this).get(AccountsViewModel.class);
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme2);
        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        View root = inflater.inflate(R.layout.fragment_account, container, false);
        textView = root.findViewById(R.id.text_notifications);
//        accountsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        notConnectedView = root.findViewById(R.id.not_connected_view);
        settingsView = root.findViewById(R.id.settings_view);
        btnLogin = root.findViewById(R.id.btnConnexion);
        btnLogout = (TextView)root.findViewById(R.id.btnLogOut);
        btnUpdateAccount = root.findViewById(R.id.btnUpdateAccount);
        emailEditText = root.findViewById(R.id.emailEditText);
        nameEditText = root.findViewById(R.id.nameEditText);
        oldPassEditText = root.findViewById(R.id.oldPassEditText);
        newPassEditText = root.findViewById(R.id.newPassEditText);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        // Get an instance of AuthUI based on the default app
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setLogo(R.drawable.logo)
                                .build(),
                        RC_SIGN_IN);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        btnUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserAccount();
            }
        });

        btnAdmin = (TextView)root.findViewById(R.id.btn_admin);

        if (user != null) {
            AdminService.findAll(new MyCallback() {
                @Override
                public void onCallback(List<String> values, Map<String, Object> result) {
                    if (!values.contains(user.getUid()))
                        btnAdmin.setVisibility(View.GONE);
                }
            });

        }
        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminActivity.class);
                startActivity(intent);
            }
        });
        refreshInterface();

        return root;
    }


    public void refreshInterface() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            // already signed in

            emailEditText.setText(user.getEmail());
            nameEditText.setText(user.getDisplayName());

            settingsView.setVisibility(View.VISIBLE);
            notConnectedView.setVisibility(View.INVISIBLE);

        } else {
            // not signed in
            notConnectedView.setVisibility(View.VISIBLE);
            settingsView.setVisibility(View.INVISIBLE);
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
                Toast.makeText(getContext(), R.string.logged_in, Toast.LENGTH_SHORT).show();
                // ...
                refreshInterface();
            } else {
                Log.v(TAG, "Login failed");
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

    public void updateUserAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(getContext(), R.string.not_logged_in_message, Toast.LENGTH_SHORT).show();
            refreshInterface();
            return;
        }

        String oldPass = oldPassEditText.getText().toString();
        String newPass = newPassEditText.getText().toString();
        String email = user.getEmail();

        // detect if the name has changed
        boolean nameChanged = user.getDisplayName().equals(nameEditText.getText().toString()) ? false : true;

        // if changing the password then both fields are required
        if (oldPass.length() == 0 && newPass.length() > 0
                || newPass.length() == 0 && oldPass.length() > 0) {
            Toast.makeText(getContext(), R.string.two_pass_fields_required, Toast.LENGTH_SHORT).show();
            return;
        }

        // In this case the password is being changed
        if (oldPass.length() > 0 && newPass.length() > 0) {
            //make sure we have minimum 6 caracters
            if (newPassEditText.length() < 6) {
                Toast.makeText(getContext(), R.string.pass_must_be_6_or_more, Toast.LENGTH_SHORT).show();
                return;
            }

            // prepare the request to check the validity of the old password
            AuthCredential credential = EmailAuthProvider
                    .getCredential(email, oldPass);

            // Check the validity of the credentials
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // user provided a good old password
                                // we can proceed to update his password
                                user.updatePassword(newPass)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User password updated.");

                                                    // We can return at this point if the name is unchanged
                                                    if (!nameChanged) {
                                                        Toast.makeText(getContext(), R.string.account_updated, Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }

                                                    // update the name if it has changed
                                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                            .setDisplayName(nameEditText.getText().toString())
                                                            .build();

                                                    user.updateProfile(profileUpdates)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Log.d(TAG, "User profile updated.");
                                                                        Toast.makeText(getContext(), R.string.account_updated, Toast.LENGTH_SHORT).show();
                                                                        return;
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(getContext(), R.string.old_pass_incorrect, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
            oldPassEditText.setText("");
            newPassEditText.setText("");
            return;
        }

        // if we are here it means the password is not being updated
        // So if the name also is no being updated we can return here
        if (!nameChanged) {
            Toast.makeText(getContext(), R.string.no_change_detected, Toast.LENGTH_SHORT).show();
            return;
        }

        // update the name if it has changed
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nameEditText.getText().toString())
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            Toast.makeText(getContext(), R.string.account_updated, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void deleteAccount() {
        AuthUI.getInstance()
                .delete(getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Deletion succeeded
                            Toast.makeText(getContext(), R.string.account_deleted, Toast.LENGTH_SHORT).show();
                        } else {
                            // Deletion failed
                            Toast.makeText(getContext(), R.string.unknown_error, Toast.LENGTH_SHORT).show();
                        }
                        refreshInterface();
                    }
                });
    }


}