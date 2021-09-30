package com.codewithsk.goadmin;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterOrders extends RecyclerView.Adapter<AdapterOrders.HolderOrders> {

    Context context;
    ArrayList<Order> orderList;

    public AdapterOrders(Context context,ArrayList<Order> orderList){
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public HolderOrders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.row_order,parent,false);
        return new HolderOrders(root);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrders holder, int position) {
        Order order = orderList.get(position);
        String id = order.getOrderId();
        String addr = order.getDeleveryAddress();
        String tPrice = order.getTotal_price();
        String email = order.getEmail();
        String status = order.getStatus();
        String uid = order.getUid();
        String contact = order.getUser_contact();

        holder.tvAddr.setText(status);
        holder.tvEmail.setText(email);
        holder.tvId.setText(id);

//        if (status.equals("accepted")){
//            holder.tvAddr.setText("order accepted");
//            holder.tvAddr.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
//        }else if (status.equals("rejected")){
//            holder.tvAddr.setText("order rejected");
//            holder.tvAddr.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
//        }else if (status.equals("pending")){
//            holder.tvAddr.setText("order Pending");
//            holder.tvAddr.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
//
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,OrderDatailsActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("addr",addr);
                intent.putExtra("tPrice",tPrice);
                intent.putExtra("email",email);
                intent.putExtra("status",status);
                intent.putExtra("uid",uid);
                intent.putExtra("contact",contact);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class HolderOrders extends RecyclerView.ViewHolder {
       TextView tvId,tvEmail,tvAddr;
       public HolderOrders(@NonNull View itemView) {
           super(itemView);

           tvId = itemView.findViewById(R.id.orderId);
           tvEmail = itemView.findViewById(R.id.orderEmail);
           tvAddr = itemView.findViewById(R.id.orderStatus);
       }
   }
}
