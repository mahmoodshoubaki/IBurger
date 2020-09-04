package com.example.iburger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    TextInputEditText fullName, phoneNumber, email, password, rePassword, location;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    Button signup;
    FusedLocationProviderClient fusedLocationProviderClient;
    String id, fName = "", pNumber, emailAddress, pass, rePass;
    private Pattern PHONE_NUMBER = Pattern.compile("((079)|(078)|(077)){1}[0-9]{7}");
    private Pattern PASSWORD = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$");
    private double latitude, longitude;
    private static final String TAG = "Signup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //reference to firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        //firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        //hook views
        fullName = findViewById(R.id.full_name);
        phoneNumber = findViewById(R.id.phone_number);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        rePassword = findViewById(R.id.re_password);
        signup = findViewById(R.id.signup);
        // register user and store data in Firebase database
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        TextWatcher fullNameTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fName = fullName.getText().toString().trim();
                if (fName.isEmpty()) {
                    fullName.setError("please enter your full name");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        fullName.addTextChangedListener(fullNameTextWatcher);

        //phone number text watcher
        TextWatcher phoneNumberTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pNumber = phoneNumber.getText().toString();
                if (pNumber.isEmpty()) {
                    phoneNumber.setError("please enter your phone number");
                } else if (!PHONE_NUMBER.matcher(pNumber).matches()) {
                    phoneNumber.setError("Must start with 079|077|078");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        phoneNumber.addTextChangedListener(phoneNumberTextWatcher);

        //email address text watccher
        TextWatcher emailTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailAddress = email.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                    email.setError("Please enter a valid email");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        email.addTextChangedListener(emailTextWatcher);

        //password text watcher
        TextWatcher passwordTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pass = password.getText().toString().trim();

                if (!PASSWORD.matcher(pass).matches()) {
                    password.setError("password must have: \n" +
                            "• min 6 characters \n" +
                            "• min 1 upper case \n" +
                            "• min 1 lower case\n" +
                            "• min 1 number");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        password.addTextChangedListener(passwordTextWatcher);
        //re-password text watcher
        TextWatcher rePasswordTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rePass = rePassword.getText().toString().trim();
                if (!rePass.equals(pass)) {
                    rePassword.setError("Password does not match");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        rePassword.addTextChangedListener(rePasswordTextWatcher);

        // sign up text watcher
        TextWatcher signupTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //enable button if all fields are filled correctly
                signup.setEnabled(!fName.isEmpty() &&
                        PHONE_NUMBER.matcher(pNumber).matches() &&
                        Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches() &&
                        PASSWORD.matcher(pass).matches() &&
                        rePass.equals(pass));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        signup.addTextChangedListener(signupTextWatcher);
        //initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        location = (TextInputEditText)

                findViewById(R.id.location);
        //location manager
//                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                if (locationManager != null) {
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//                }
//                getAddress();
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);

            Log.v("IGA", "Address" + add);
            location.setText(add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addUser() {
        // create new user with new id
        firebaseAuth.createUserWithEmailAndPassword(emailAddress, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "signed up successfully", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    id = user.getUid();
                    UserInfo userInfo = new UserInfo(id, fName, pNumber, emailAddress, latitude, longitude, pass);
                    databaseReference.child(id).setValue(userInfo);
                    startActivity(new Intent(Signup.this, Login.class));
                    finishAffinity();
                } else {
                    Toast.makeText(getApplicationContext(), "signed up failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            System.exit(0);
        }
    }

    public void getLocation() {
        location.setEnabled(false);
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    getAddress(latitude, longitude);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //request location permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
        }
    }
}