package com.example.iburger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Confirm extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView navController, back;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        /*---Hook views---*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navController = findViewById(R.id.nav_controller);
        navigationView = findViewById(R.id.nav_view);
        back = findViewById(R.id.back);

        //fire base authentication
        firebaseAuth = FirebaseAuth.getInstance();
        /*--- navigation view ---*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.drawer_navigation_open,
                R.string.drawer_navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //get navigation menu
        Menu menu = navigationView.getMenu();
        navigationView.setCheckedItem(menu.findItem(R.id.orders));

        /*--- navController on click listener ---*/
        navController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        /*--- back on click listener ---*/
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Confirm.this, Home.class));
                finishAffinity();
                drawerLayout.clearFocus();
            }
        });

        /*---  change logout color */
        //change logout color
        SpannableString s = new SpannableString(menu.findItem(R.id.logout).getTitle());
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 0, s.length(), 0);
        menu.findItem(R.id.logout).setTitle(s);
        /*-- align navigation menu center ---*/
        for (int i = 0; i < menu.size() - 1; i++) {
            MenuItem item = menu.getItem(i);
            SpannableString s1 = new SpannableString(item.getTitle());
            s1.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE), 0, s.length(), 0);
            item.setTitle(s1);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(Confirm.this, Profile.class));
                finish();
                break;
            case R.id.burger:
                startActivity(new Intent(Confirm.this, Burger.class));
                finish();
                break;
            case R.id.snacks:
                startActivity(new Intent(Confirm.this, Snacks.class));
                finish();
                break;
            case R.id.location:
                startActivity(new Intent(Confirm.this, Location.class));
                finish();
                break;

            case R.id.logout:
                firebaseAuth.signOut();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }
}
