package com.example.iburger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextInputEditText fullName, phoneNumber, email, password, rePassword, location;
    String userId, fName = "", pNumber, emailAddress, pass, rePass, currentEmail, currentPass;
    double latitude, longitude;
    Button save;
    ImageView back, navController;
    FusedLocationProviderClient fusedLocationProviderClient;
    private Pattern PHONE_NUMBER = Pattern.compile("((079)|(078)|(077)){1}[0-9]{7}");
    private Pattern PASSWORD = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$");
    private int PLACE_PICKER = 1;
    private static final String TAG = "Profile";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        /* ---- hook views ----*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        fullName = findViewById(R.id.full_name);
        phoneNumber = findViewById(R.id.phone_number);
        email = findViewById(R.id.email);
        location = findViewById(R.id.location);
        password = findViewById(R.id.password);
        rePassword = findViewById(R.id.re_password);
        save = (Button) findViewById(R.id.save);
        back = findViewById(R.id.back);
        navController = findViewById(R.id.nav_controller);
        //fused location provider client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //firebase auth.
        firebaseAuth = FirebaseAuth.getInstance();
        //get current user id
        firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        //get reference to firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullName.setText(snapshot.child("fullName").getValue().toString());
                phoneNumber.setText(snapshot.child("phoneNumber").getValue().toString());
                currentEmail = snapshot.child("email").getValue().toString();
                email.setText(currentEmail);
                //get location
                latitude = (double) snapshot.child("latitude").getValue();
                longitude = (double) snapshot.child("longitude").getValue();
                getAddress(latitude, longitude);
                currentPass = snapshot.child("password").getValue().toString();
                password.setText(currentPass);
                rePassword.setText(currentPass);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

        //email address text watcher
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
                    password.setError(getResources().getString(R.string.password_restrictions));
                }
                if (!rePassword.getText().toString().equals(pass)) {
                    rePassword.setError("Password does not match");
                } else {
                    rePassword.setError(null);
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
                } else {
                    rePassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!rePassword.getText().toString().equals(pass)) {
                    rePassword.setError("Password does not match");
                }
            }
        };
        rePassword.addTextChangedListener(rePasswordTextWatcher);

        //save changes
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!fName.isEmpty() &&
                        PHONE_NUMBER.matcher(pNumber).matches() &&
                        Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches() &&
                        PASSWORD.matcher(pass).matches() &&
                        rePass.equals(pass)))
                    editProfile();
            }
        });
        /* -- location click listener -- */
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //using google places API :
//                pickLocation();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
                }
            }
        });
        /* -- back controller click listener -- */
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, Home.class));
                finishAffinity();
                drawerLayout.clearFocus();
            }
        });
        /* -- navigation view image controller click listener -- */
        navController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.drawer_navigation_open,
                R.string.drawer_navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Menu navMenu = navigationView.getMenu();
        navigationView.setCheckedItem(navMenu.findItem(R.id.profile));

        //change logout color
        SpannableString s = new SpannableString(navMenu.findItem(R.id.logout).getTitle());
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 0, s.length(), 0);
        navMenu.findItem(R.id.logout).setTitle(s);
        for (int i = 0; i < navMenu.size()-1; i++) {
            MenuItem item = navMenu.getItem(i);
            SpannableString s1 = new SpannableString(item.getTitle());
            s1.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE), 0, s.length(), 0);
            item.setTitle(s1);
        }
        navigationView.setNavigationItemSelectedListener(this);

    }

    // get location using google places API
//    private void pickLocation() {
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//        Intent intent;
//        try {
//            intent = builder.build(this);
//            startActivityForResult(intent, PLACE_PICKER);
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
//    }

    private void editProfile() {
        final UserInfo userInfo = new UserInfo(
                userId,
                fullName.getText().toString(),
                phoneNumber.getText().toString(),
                emailAddress,
                latitude,
                longitude,
                password.getText().toString());

        firebaseUser.reauthenticate(EmailAuthProvider.getCredential(currentEmail, currentPass)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    firebaseUser.updateEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                firebaseUser.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            databaseReference.setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(Profile.this, "Edited successfully", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Toast.makeText(Profile.this, "could not edit database", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(Profile.this, "Failed to update password", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(Profile.this, "failed to update email", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Profile.this, "user not authenticated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.burger:
                //go to burger activity
                startActivity(new Intent(Profile.this, Burger.class));
                finish();
                break;
            case R.id.snacks:
                //go to snacks activity
                startActivity(new Intent(Profile.this, Snacks.class));
                finish();
                break;
            case R.id.orders:
                //go to orders activity
                startActivity(new Intent(Profile.this, Orders.class));
                finish();
                break;
            case R.id.location:
                //go to location
                startActivity(new Intent(Profile.this, Location.class));
                finish();
                break;
            case R.id.logout:
                firebaseAuth.signOut();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);

            Log.v("IGA", "Address" + add + "\n " + obj.getSubLocality());
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode==PLACE_PICKER && resultCode == RESULT_OK){
//            Place place = PlacePicker.getPlace(data,this);
//            String address = (String) place.getAddress();
//            String city = (String) place.getName();
//            location.setText(address);
//            Log.d(TAG, "onActivityResult: city is "+ address);
//        }
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            System.exit(0);
        }
    }

    public void getLocation() {
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


}