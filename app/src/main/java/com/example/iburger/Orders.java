package com.example.iburger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static com.example.iburger.SharedPrefManager.SHARED_PREF_ORDERS;

public class Orders extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    int cheeseBCount, bfrBCount, beefBCount, mashBCount, shawarmaCount, hotdogCount, chrispyCount, fahetaCount;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ImageView navController, back, checkout;
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    OrdersAdapter ordersAdapter;
    List<List<OrderDetails>> snacksArrayList, burgerArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

//        getSnacks();

        /*--- Fire base Auth ---*/
        firebaseAuth = FirebaseAuth.getInstance();
        /*-- hook views --*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navController = findViewById(R.id.nav_controller);
        navigationView = findViewById(R.id.nav_view);
        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recycler);
        checkout = findViewById(R.id.checkout);

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
                startActivity(new Intent(Orders.this, Home.class));
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

        getBurgers();
       checkout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(Orders.this , Checkout.class));
               finish();
           }
       });
    }

    private void getBurgers() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREF_ORDERS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("burger_list", null);
        if (json != null) {
            Type type = new TypeToken<List<List<OrderDetails>>>() {
            }.getType();
            burgerArrayList = gson.fromJson(json, type);
//            Toast.makeText(this, "total list size is "+arrayList.size(), Toast.LENGTH_LONG).show();
//            List<BurgerOrderDetails> list = arrayList.get(0);
//            Toast.makeText(this, "index 0 list size is " + list.size(), Toast.LENGTH_LONG).show();

        }
        getSnacks();
    }

    private void getSnacks() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREF_ORDERS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("snacks_list", null);
        if (json != null) {
            Type type = new TypeToken<List<List<OrderDetails>>>() {
            }.getType();
            snacksArrayList = gson.fromJson(json, type);

        }
        if (burgerArrayList != null && snacksArrayList != null &&
                (burgerArrayList.size() > 0 || snacksArrayList.size() > 0)) {
            burgerArrayList.addAll(snacksArrayList);
            checkout.setVisibility(View.VISIBLE);
        } else if (burgerArrayList == null && snacksArrayList != null && snacksArrayList.size() > 0) {
            burgerArrayList = snacksArrayList;
            checkout.setVisibility(View.VISIBLE);
        } else if (burgerArrayList != null && burgerArrayList.size() > 0 && snacksArrayList == null)
            checkout.setVisibility(View.VISIBLE);
        else
            checkout.setVisibility(View.GONE);
        setupRecuclerView(burgerArrayList);
    }


    private void setupRecuclerView(List<List<OrderDetails>> myTotalList) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        ordersAdapter = new OrdersAdapter(getApplicationContext(), myTotalList);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(ordersAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            Toast.makeText(this, "orders cleared", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(Orders.this, Profile.class));
                finish();
                break;
            case R.id.burger:
                startActivity(new Intent(Orders.this, Burger.class));
                finish();
                break;
            case R.id.snacks:
                startActivity(new Intent(Orders.this, Snacks.class));
                finish();
                break;

            case R.id.logout:
                firebaseAuth.signOut();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

//    @Override
//    protected void onPause() {
//        SharedPrefManager.getInstance(this).clearOrders();
//        super.onPause();
//    }

    //    @Override
//    protected void onStop() {
//        SharedPrefManager.getInstance(this).clearOrders();
//        super.onStop();
//    }
//
    @Override
    protected void onDestroy() {
        SharedPrefManager.getInstance(this).clearOrders();
        super.onDestroy();
    }
}
