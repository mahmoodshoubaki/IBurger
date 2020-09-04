package com.example.iburger;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {

    private static final String TAG = "OrdersAdapter";
    private Context context;
    private List<OrderDetails> myList;
    private List<List<OrderDetails>> myTotalList;
    SharedPrefManager sharedPrefManager;
    int totalPrice=0;

    public OrdersAdapter(Context context, List<List<OrderDetails>> myTotalList) {
        this.context = context;
        this.myTotalList = myTotalList;
        sharedPrefManager = SharedPrefManager.getInstance(context);
    }

    @NonNull
    @Override
    public OrdersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.order_item_fragment, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OrdersAdapter.MyViewHolder holder, int position) {

        myList = myTotalList.get(position);
        final int price = myList.get(0).getPrice();
        if (!myList.get(0).getChoice().contains("burger")) {
            holder.image.setImageResource(R.drawable.chicken_bucket);
        }
        if (!myList.get(0).getType().toLowerCase().equals("meal")) {
            holder.name.setText(myList.get(0).getChoice());
        } else {
            holder.name.setText(myList.get(0).getChoice().concat(" " + myList.get(0).getType()));
        }
        holder.count.setText(String.valueOf(myList.size()));
        holder.price.setText(myList.get(0).getPrice() * myList.size() + "JD");
//        totalPrice = totalPrice + price * myList.size();
//        sharedPrefManager.finalPrice(totalPrice);
        SharedPreferences sharedPreferences = context.getSharedPreferences("orders",Context.MODE_PRIVATE);
        totalPrice = sharedPreferences.getInt("totalPrice",0);
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(holder.count.getText().toString()) + 1;
                holder.count.setText(String.valueOf(count));
                holder.price.setText((String.valueOf(price * count).concat(" JD")));
                totalPrice += price;
                sharedPrefManager.finalPrice(totalPrice);
                Log.d(TAG, "onClick: total price after plus is : " + totalPrice);
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(holder.count.getText().toString());
                if (count > 0)
                    count--;
                holder.count.setText(String.valueOf(count));
                holder.price.setText(price * count + "JD");
                totalPrice -= price;
                sharedPrefManager.finalPrice(totalPrice);
                Log.d(TAG, "onClick: total price after minus is : " + totalPrice);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (myTotalList != null)
            return myTotalList.size();
        else
            return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name, count, price;
        private ImageView plus, minus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.order_image);
            name = itemView.findViewById(R.id.order_name);
            price = itemView.findViewById(R.id.order_price);
            count = itemView.findViewById(R.id.order_count);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
        }
    }
    public int getTotalPrice (){
        return totalPrice;
    }

}
