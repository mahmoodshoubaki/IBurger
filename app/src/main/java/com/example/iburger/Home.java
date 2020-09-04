package com.example.iburger;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;

import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView navController, burger, snacks;
    FirebaseAuth firebaseAuth;
    SharedPrefManager sharedPrefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //shared preferences manager
        sharedPrefManager = SharedPrefManager.getInstance(this);
        sharedPrefManager.clearOrders();
        //firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //hook views
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navController = (ImageView) findViewById(R.id.nav_controller);
        burger = findViewById(R.id.burger);
        snacks = findViewById(R.id.snacks);
        /*---navigation view---*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.drawer_navigation_open,
                R.string.drawer_navigation_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        Menu nav_menu = navigationView.getMenu();

        //change logout color
        SpannableString s = new SpannableString(nav_menu.findItem(R.id.logout).getTitle());
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 0, s.length(), 0);
        nav_menu.findItem(R.id.logout).setTitle(s);
        for (int i = 0; i < nav_menu.size() - 1; i++) {
            MenuItem item = nav_menu.getItem(i);
            SpannableString s1 = new SpannableString(item.getTitle());
            s1.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE), 0, s.length(), 0);
            item.setTitle(s1);
        }
        //nav controller on click click listener
        navController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });

        //burger on click listener
        burger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Burger.class));
            }
        });

        //snacks on click listener
        snacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent here
                startActivity(new Intent(Home.this, Snacks.class));
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                //go to profile
                startActivity(new Intent(Home.this, Profile.class));
                break;
            case R.id.burger:
                //go to burger activity
                startActivity(new Intent(Home.this, Burger.class));
                break;
            case R.id.snacks:
                //go to burger activity
                startActivity(new Intent(Home.this, Snacks.class));
                break;
            case R.id.orders:
                //go to burger activity
                startActivity(new Intent(Home.this, Orders.class));
                break;
            case R.id.location:
                //go to burger activity
                startActivity(new Intent(Home.this, Location.class));
                break;
            case R.id.logout:
                //logout
                firebaseAuth.signOut();
                startActivity(new Intent(Home.this, Login.class));
                finishAffinity();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END))
            drawerLayout.closeDrawer(GravityCompat.END);
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        navigationView.setCheckedItem(navigationView.getMenu().findItem(R.id.menu_none));
    }
}
