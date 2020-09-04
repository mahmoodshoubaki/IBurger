package com.example.iburger;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

public class SharedPrefManager {
    private static SharedPrefManager instance;
    private static final String TAG = "SharedPrefManager";
    private static Context ctx;
    public static final String SHARED_PREF_ORDERS = "orders";
    private static final String SHARED_PREF_BURGER_LIST = "burger_list";
    private static final String SHARED_PREF_SNACKS_LIST = "snacks_list";
    private static final String SHARED_PREF_ORDERS_LIST = "orders_list";
    private static final String SHARED_PREF_CHEESE_BURGER = "snacks_list";
    private static final String SHARED_PREF_BFR_BURGER = "snacks_list";
    private static final String SHARED_PREF_BEEF_BURGER = "snacks_list";
//    private static final String SHARED_PREF_ = "snacks_list";
//    private static final String SHARED_PREF_SNACKS_LIST = "snacks_list";
//    private static final String SHARED_PREF_SNACKS_LIST = "snacks_list";
//    private static final String SHARED_PREF_SNACKS_LIST = "snacks_list";
//    private static final String SHARED_PREF_SNACKS_LIST = "snacks_list";
//    private static final String SHARED_PREF_SNACKS_LIST = "snacks_list";


    private SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void clearOrders() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_ORDERS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }



    public void fillOrderDetails(List<List<OrderDetails>> orderDetailsList,String type) {
//        Set<BurgerOrderDetails> stringSet = new HashSet<>();

        Gson gson = new Gson();
        String json = gson.toJson(orderDetailsList);
//        stringSet.addAll(burgerOrderDetailsList);
//        Log.d(TAG, "fillBurgerDetails: " + stringSet);
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_ORDERS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (type.equals("burger"))
        editor.putString(SHARED_PREF_BURGER_LIST, json);
        else
            editor.putString(SHARED_PREF_SNACKS_LIST , json);
        editor.apply();
    }

//    public void fillSnacksDetails(List<List<SnacksOrderDetails>> snacksOrderDetailsList) {
//
//        Gson gson = new Gson();
//        String json = gson.toJson(snacksOrderDetailsList);
//        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_ORDERS, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(SHARED_PREF_SNACKS_LIST, json);
//        editor.apply();
//    }
    public void finalPrice(int totalPrice){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_ORDERS , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("totalPrice", totalPrice);
        editor.apply();
    }
}
