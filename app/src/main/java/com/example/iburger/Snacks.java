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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Snacks extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView navController, back;
    FirebaseAuth firebaseAuth;
    RadioGroup shawarmaRadioGroup, hotdogRadioGroup, chrispyRadioGroup, fahetaRadioGroup;
    RadioButton shawarmaRadioButton, hotdogRadioButton, chrispyRadioButton, fahetaRadioButton;
    ImageView shawarmaPlus, shawarmaMinus, hotdogPlus, hotdogMinus, chrispyPlus, chrispyMinus, fahetaPlus, fahetaMinus;
    TextView shawarmaCount, hotdogCount, chrispyCount, fahetaCount;
    int shCount = 0, hdCount = 0, chCount = 0, ftCount = 0;
    SharedPrefManager sharedPrefManager;
    List<OrderDetails> shawarmaOrderDetailsList, hotdogOrderDetailsList, chrispyOrderDetailsList, fahetaOrderDetailsList;
    List<List<OrderDetails>> totalList;
    OrderDetails snacksOrderDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snacks);

        //array lists
        shawarmaOrderDetailsList = new ArrayList<>();
        hotdogOrderDetailsList = new ArrayList<>();
        chrispyOrderDetailsList = new ArrayList<>();
        fahetaOrderDetailsList = new ArrayList<>();
        //Total list of burger lists
        totalList = new ArrayList<>();
        //shared pref manager
        sharedPrefManager = SharedPrefManager.getInstance(this);
        //fire base authentication
        firebaseAuth = FirebaseAuth.getInstance();
        //hook views
        drawerLayout = findViewById(R.id.drawer_layout);
        navController = findViewById(R.id.nav_controller);
        navigationView = findViewById(R.id.nav_view);
        back = findViewById(R.id.back);
        shawarmaRadioGroup = findViewById(R.id.shawarma_radio_group);
        hotdogRadioGroup = findViewById(R.id.hotdog_radio_group);
        chrispyRadioGroup = findViewById(R.id.chrispy_radio_group);
        fahetaRadioGroup = findViewById(R.id.faheta_radio_group);

        shawarmaPlus = findViewById(R.id.shawarma_plus);
        hotdogPlus = findViewById(R.id.hotdog_plus);
        chrispyPlus = findViewById(R.id.chrispy_plus);
        fahetaPlus = findViewById(R.id.faheta_plus);

        shawarmaMinus = findViewById(R.id.shawarma_minus);
        hotdogMinus = findViewById(R.id.hotdog_minus);
        chrispyMinus = findViewById(R.id.chrispy_minus);
        fahetaMinus = findViewById(R.id.faheta_minus);
        shawarmaCount = findViewById(R.id.shawarma_count);
        hotdogCount = findViewById(R.id.hotdog_count);
        chrispyCount = findViewById(R.id.chrispy_count);
        fahetaCount = findViewById(R.id.faheta_count);

        /*--- cheese burger plus on click listener ---*/
        shawarmaPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shCount++;
                shawarmaCount.setText(String.valueOf(shCount));
                shawarmaRadioButton = findViewById(shawarmaRadioGroup.getCheckedRadioButtonId());
                snacksOrderDetails = new OrderDetails("shawarma", shawarmaRadioButton.getText().toString());
                shawarmaOrderDetailsList.add(snacksOrderDetails);
//                totalList.add(shawarmaOrderDetailsList);
//                sharedPrefManager.fillSnacksDetails(totalList);
            }
        });
        shawarmaMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shCount > 0) {
                    shCount--;
                    shawarmaOrderDetailsList.remove(shawarmaOrderDetailsList.size() - 1);
//                    totalList.remove(shawarmaOrderDetailsList);
//                    sharedPrefManager.fillSnacksDetails(totalList);
                }
                shawarmaCount.setText(String.valueOf(shCount));
            }
        });
        hotdogPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hdCount++;
                hotdogCount.setText(String.valueOf(hdCount));
                hotdogRadioButton = findViewById(hotdogRadioGroup.getCheckedRadioButtonId());
                snacksOrderDetails = new OrderDetails("hotdog", hotdogRadioButton.getText().toString());
                hotdogOrderDetailsList.add(snacksOrderDetails);
//                totalList.add(hotdogOrderDetailsList);
//                sharedPrefManager.fillSnacksDetails(totalList);

            }
        });
        hotdogMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hdCount > 0) {
                    hdCount--;
                    hotdogOrderDetailsList.remove(hotdogOrderDetailsList.size() - 1);
//                    totalList.remove(hotdogOrderDetailsList);
//                    sharedPrefManager.fillSnacksDetails(totalList);
                }
                hotdogCount.setText(String.valueOf(hdCount));
            }
        });
        chrispyPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chCount++;
                chrispyCount.setText(String.valueOf(chCount));
                chrispyRadioButton = findViewById(chrispyRadioGroup.getCheckedRadioButtonId());
                snacksOrderDetails = new OrderDetails("chrispy", chrispyRadioButton.getText().toString());
                chrispyOrderDetailsList.add(snacksOrderDetails);
//                totalList.add(chrispyOrderDetailsList);
//                sharedPrefManager.fillSnacksDetails(totalList);
            }
        });
        chrispyMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chCount > 0) {
                    chCount--;
                    chrispyOrderDetailsList.remove(chrispyOrderDetailsList.size() - 1);
//                    totalList.remove(chrispyOrderDetailsList);
//                    sharedPrefManager.fillSnacksDetails(totalList);
                }
                chrispyCount.setText(String.valueOf(chCount));
            }
        });
        fahetaPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftCount++;
                fahetaCount.setText(String.valueOf(ftCount));
                fahetaRadioButton = findViewById(fahetaRadioGroup.getCheckedRadioButtonId());
                snacksOrderDetails = new OrderDetails("faheta", fahetaRadioButton.getText().toString());
                fahetaOrderDetailsList.add(snacksOrderDetails);
//                totalList.add(fahetaOrderDetailsList);
//                sharedPrefManager.fillSnacksDetails(totalList);
            }
        });
        fahetaMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ftCount > 0) {
                    ftCount--;
                    fahetaOrderDetailsList.remove(fahetaOrderDetailsList.size() - 1);
//                    totalList.remove(fahetaOrderDetailsList);
//                    sharedPrefManager.fillSnacksDetails(totalList);
                }
                fahetaCount.setText(String.valueOf(ftCount));
            }
        });

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
        navigationView.setCheckedItem(menu.findItem(R.id.snacks));

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
                startActivity(new Intent(Snacks.this, Home.class));
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
        for (int i = 0; i < menu.size()-1; i++) {
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
                startActivity(new Intent(Snacks.this, Profile.class));
                finish();
                break;
            case R.id.burger:
                startActivity(new Intent(Snacks.this, Burger.class));
                finish();
                break;
            case R.id.orders:
                startActivity(new Intent(Snacks.this, Orders.class));
                finish();
                break;

            case R.id.logout:
                firebaseAuth.signOut();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.END);
        //fill total list
        int totalPrice = 0;
        if (shawarmaOrderDetailsList.size() > 0) {
            totalList.add(shawarmaOrderDetailsList);
            totalPrice += shawarmaOrderDetailsList.get(0).getPrice() * shawarmaOrderDetailsList.size();
        }
        if (hotdogOrderDetailsList.size() > 0) {
            totalList.add(hotdogOrderDetailsList);
            totalPrice += hotdogOrderDetailsList.get(0).getPrice() * hotdogOrderDetailsList.size();
        }
        if (chrispyOrderDetailsList.size() > 0) {
            totalList.add(chrispyOrderDetailsList);
            totalPrice += chrispyOrderDetailsList.get(0).getPrice() * chrispyOrderDetailsList.size();
        }
        if (fahetaOrderDetailsList.size() > 0) {
            totalList.add(fahetaOrderDetailsList);
            totalPrice += fahetaOrderDetailsList.get(0).getPrice() * fahetaOrderDetailsList.size();
        }

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("orders", MODE_PRIVATE);
        totalPrice += sharedPreferences.getInt("totalPrice", 0);
        sharedPrefManager.finalPrice(totalPrice);
        sharedPrefManager.fillOrderDetails(totalList, "snacks");
        return true;
    }
}
