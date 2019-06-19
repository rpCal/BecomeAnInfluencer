package com.s9986.becomeaninfluencer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AuthFirebaseActivity extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "AuthFirebaseActivity";

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_firebase_activity);

        mStatusTextView = findViewById(R.id.status);
        mDetailTextView = findViewById(R.id.detail);
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);

        findViewById(R.id.emailSignInButton).setOnClickListener(this);
        findViewById(R.id.emailCreateAccountButton).setOnClickListener(this);
        findViewById(R.id.signOutButton).setOnClickListener(this);
        findViewById(R.id.verifyEmailButton).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        reactToModelChange(currentUser);
    }

    private void handleNewAccount(String email, String password) {
        Log.d(TAG, "tworzenie konta:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user = mAuth.getCurrentUser();
                    reactToModelChange(user);
                } else {

                    Toast.makeText(AuthFirebaseActivity.this, "Nie udalo sie zalogowac.",
                            Toast.LENGTH_SHORT).show();
                    reactToModelChange(null);
                }

                hideProgressDialog();
                }
            });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {

                FirebaseUser user = mAuth.getCurrentUser();
                reactToModelChange(user);
            } else {

                Toast.makeText(AuthFirebaseActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
                reactToModelChange(null);
            }

            if (task.isSuccessful() == false) {
                mStatusTextView.setText(R.string.auth_failed);
            }

            hideProgressDialog();
                }
        });
    }

    private void signOut() {
        mAuth.signOut();
        reactToModelChange(null);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Wymagane pole");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Wymagane pole");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void reactToModelChange(FirebaseUser user) {

        hideProgressDialog();

        if (user != null) {
            mStatusTextView.setText(getString(R.string.email_login_status, user.getEmail(), user.isEmailVerified()));

            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);
            findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);
            findViewById(R.id.verifyEmailButton).setEnabled(!user.isEmailVerified());
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
            findViewById(R.id.signedInButtons).setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.emailCreateAccountButton) {

            handleNewAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.emailSignInButton) {

            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.signOutButton) {

            signOut();
        } else if (i == R.id.verifyEmailButton) {

            Intent intent = new Intent(AuthFirebaseActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}