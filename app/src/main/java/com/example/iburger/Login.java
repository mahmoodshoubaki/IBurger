package com.example.iburger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Login extends AppCompatActivity {

    TextView doNotHaveAccount, forgetPassword;
    Button login;
    TextInputEditText email, password;
    FirebaseAuth firebaseAuth;
    AuthStateListener authStateListener;
    AlertDialog forgetPasswordAlertDialog;
    EditText forgetPasswordEditText;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //fire base auth
        firebaseAuth = FirebaseAuth.getInstance();
        // hook views
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        doNotHaveAccount = (TextView) findViewById(R.id.do_not_have_account);
        forgetPassword = (TextView) findViewById(R.id.forget_password);
        login = (Button) findViewById(R.id.login);

        //progress dialog
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("please wait...");

        //auto login -check state-
        authStateListener = new AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    startActivity(new Intent(Login.this, Home.class));
                    finishAffinity();
                }
            }
        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Login.this, "Login successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finishAffinity();
                                    progressDialog.dismiss();
                                } else {
                                    Toast.makeText(Login.this, "Invalid email or password , please try again", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
            }
        });

        doNotHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Signup.class));
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPasswordAlertDialog = new AlertDialog.Builder(Login.this).create();
                forgetPasswordAlertDialog.setTitle("Enter your email please");
                forgetPasswordEditText = new EditText(getApplicationContext());
                forgetPasswordAlertDialog.setView(forgetPasswordEditText);
                forgetPasswordAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // reset password
                        firebaseAuth.sendPasswordResetEmail(forgetPasswordEditText.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(Login.this, "Email sent", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(Login.this, "No account assigned with this email", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
                forgetPasswordAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
                forgetPasswordAlertDialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
