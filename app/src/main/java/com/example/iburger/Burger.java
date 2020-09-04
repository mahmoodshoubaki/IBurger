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

public class Burger extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView navController, back;
    FirebaseAuth firebaseAuth;
    RadioGroup cheeseBurgerRadioGroup, bfrBurgerRadioGroup, beefBurgerRadioGroup, mashBurgerRadioGroup;
    RadioButton cheeseBurgerRadioButton, bfrBurgerRadioButton, beefBurgerRadioButton, mashBurgerRadioButton;
    ImageView cheeseBurgerPlus, cheeseBurgerMinus, bfrBurgerPlus, bfrBurgerMinus, beefBurgerPlus, beefBurgerMinus, mashBurgerPlus, mashBurgerMinus;
    TextView cheeseBurgerCount, bfrBurgerCount, beefBurgerCount, mashBurgerCount;
    int cBCount = 0, bfrBCount = 0, beefBCount = 0, mashBCount = 0;
    SharedPrefManager sharedPrefManager;
    List<OrderDetails> cheeseOrderDetailsList, bfrOrderDetailsList, beefOrderDetailsList, mashOrderDetailsList;
    List<List<OrderDetails>> totalList;
    OrderDetails orderDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burger);

        //Array lists
        cheeseOrderDetailsList = new ArrayList<>();
        bfrOrderDetailsList = new ArrayList<>();
        beefOrderDetailsList = new ArrayList<>();
        mashOrderDetailsList = new ArrayList<>();
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
        cheeseBurgerRadioGroup = findViewById(R.id.cheese_burger_radio_group);
        bfrBurgerRadioGroup = findViewById(R.id.bfr_burger_radio_group);
        beefBurgerRadioGroup = findViewById(R.id.beef_burger_radio_group);
        mashBurgerRadioGroup = findViewById(R.id.mash_burger_radio_group);

        cheeseBurgerPlus = findViewById(R.id.cheese_burger_plus);
        bfrBurgerPlus = findViewById(R.id.bfr_burger_plus);
        beefBurgerPlus = findViewById(R.id.beef_burger_plus);
        mashBurgerPlus = findViewById(R.id.mash_burger_plus);

        cheeseBurgerMinus = findViewById(R.id.cheese_burger_minus);
        bfrBurgerMinus = findViewById(R.id.bfr_burger_minus);
        beefBurgerMinus = findViewById(R.id.beef_burger_minus);
        mashBurgerMinus = findViewById(R.id.mash_burger_minus);
        cheeseBurgerCount = findViewById(R.id.cheese_burger_count);
        bfrBurgerCount = findViewById(R.id.bfr_burger_count);
        beefBurgerCount = findViewById(R.id.beef_burger_count);
        mashBurgerCount = findViewById(R.id.mash_burger_count);

        /*--- cheese burger plus on click listener ---*/
        cheeseBurgerPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cBCount++;
                cheeseBurgerCount.setText(String.valueOf(cBCount));
                cheeseBurgerRadioButton = findViewById(cheeseBurgerRadioGroup.getCheckedRadioButtonId());
                orderDetails = new OrderDetails("cheese burger", cheeseBurgerRadioButton.getText().toString());
                cheeseOrderDetailsList.add(orderDetails);
//                totalList.add(cheeseBurgerOrderDetailsList);
                //sharedPrefManager.fillBurgerDetails(totalList);
            }
        });
        cheeseBurgerMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cBCount > 0) {
                    cBCount--;
                    cheeseOrderDetailsList.remove(cheeseOrderDetailsList.size() - 1);
//                    totalList.remove(cheeseBurgerOrderDetailsList);
                    //sharedPrefManager.fillBurgerDetails(totalList);

                }
                cheeseBurgerCount.setText(String.valueOf(cBCount));

            }
        });
        bfrBurgerPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bfrBCount++;
                bfrBurgerCount.setText(String.valueOf(bfrBCount));
                bfrBurgerRadioButton = findViewById(bfrBurgerRadioGroup.getCheckedRadioButtonId());
                orderDetails = new OrderDetails("bfr burger", bfrBurgerRadioButton.getText().toString());
                bfrOrderDetailsList.add(orderDetails);
//                totalList.add(bfrBurgerOrderDetailsList);
                //sharedPrefManager.fillBurgerDetails(totalList);

            }
        });
        bfrBurgerMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bfrBCount > 0) {
                    bfrBCount--;
                    bfrOrderDetailsList.remove(bfrOrderDetailsList.size() - 1);
//                    totalList.remove(bfrBurgerOrderDetailsList);
                    //sharedPrefManager.fillBurgerDetails(totalList);
                }
                bfrBurgerCount.setText(String.valueOf(bfrBCount));
            }
        });
        beefBurgerPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beefBCount++;
                beefBurgerCount.setText(String.valueOf(beefBCount));
                beefBurgerRadioButton = findViewById(beefBurgerRadioGroup.getCheckedRadioButtonId());
                orderDetails = new OrderDetails("beef burger", beefBurgerRadioButton.getText().toString());
                beefOrderDetailsList.add(orderDetails);
//                totalList.add(beefBurgerOrderDetailsList);
                //sharedPrefManager.fillBurgerDetails(totalList);
            }
        });
        beefBurgerMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beefBCount > 0) {
                    beefBCount--;
                    beefOrderDetailsList.remove(beefOrderDetailsList.size() - 1);
//                    totalList.remove(beefBurgerOrderDetailsList);
                    //sharedPrefManager.fillBurgerDetails(totalList);
                }
                beefBurgerCount.setText(String.valueOf(beefBCount));
            }
        });
        mashBurgerPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mashBCount++;
                mashBurgerCount.setText(String.valueOf(mashBCount));
                mashBurgerRadioButton = findViewById(mashBurgerRadioGroup.getCheckedRadioButtonId());
                orderDetails = new OrderDetails("mash burger", mashBurgerRadioButton.getText().toString());
                mashOrderDetailsList.add(orderDetails);
//                totalList.remove(mashBurgerOrderDetailsList);
                //sharedPrefManager.fillBurgerDetails(totalList);

            }
        });
        mashBurgerMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mashBCount > 0) {
                    mashBCount--;
                    mashOrderDetailsList.remove(mashOrderDetailsList.size() - 1);
//                    totalList.add(mashBurgerOrderDetailsList);
                    //sharedPrefManager.fillBurgerDetails(totalList);
                }
                mashBurgerCount.setText(String.valueOf(mashBCount));
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
        navigationView.setCheckedItem(menu.findItem(R.id.burger));

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
                startActivity(new Intent(Burger.this, Home.class));
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
                startActivity(new Intent(Burger.this, Profile.class));
                finish();
                break;
            case R.id.snacks:
                startActivity(new Intent(Burger.this, Snacks.class));
                finish();
                break;
            case R.id.orders:
                startActivity(new Intent(Burger.this, Orders.class));
                finish();
                break;
            case R.id.location:
                startActivity(new Intent(Burger.this, Location.class));
                finish();
                break;
            case R.id.logout:
                firebaseAuth.signOut();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.END);
        //fill the total list
        int totalPrice = 0;
        if (cheeseOrderDetailsList.size() > 0) {
            totalList.add(cheeseOrderDetailsList);
            totalPrice += cheeseOrderDetailsList.get(0).getPrice() * cheeseOrderDetailsList.size();
        }
        if (bfrOrderDetailsList.size() > 0) {
            totalList.add(bfrOrderDetailsList);
            totalPrice += bfrOrderDetailsList.get(0).getPrice() * bfrOrderDetailsList.size();
        }
        if (beefOrderDetailsList.size() > 0) {
            totalList.add(beefOrderDetailsList);
            totalPrice += beefOrderDetailsList.get(0).getPrice() * beefOrderDetailsList.size();
        }
        if (mashOrderDetailsList.size() > 0) {
            totalList.add(mashOrderDetailsList);
            totalPrice += mashOrderDetailsList.get(0).getPrice() * mashOrderDetailsList.size();
        }
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("orders", MODE_PRIVATE);
        totalPrice += sharedPreferences.getInt("totalPrice", 0);
        sharedPrefManager.finalPrice(totalPrice);
        sharedPrefManager.fillOrderDetails(totalList, "burger");
        return true;
    }
}
