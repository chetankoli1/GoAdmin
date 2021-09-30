package com.codewithsk.goadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterProducts extends RecyclerView.Adapter<AdapterProducts.HolderProducts> {
    Context context;
    ArrayList<Products> orderList;

    public AdapterProducts(Context context,ArrayList<Products> orderList){
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public HolderProducts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.row_product,parent,false);

        return new HolderProducts(root);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProducts holder, int position) {
        Products products = orderList.get(position);
        try {
            Picasso.get().load(products.getImg()).into(holder.img);
        } catch (Exception e) {
            e.printStackTrace();
            holder.img.setVisibility(View.GONE);
        }

        holder.title.setText(products.getTitle());
        holder.quantity.setText(products.getQuantity());
        holder.price.setText(products.getPrice());
        holder.desc.setText(products.getDesc());
        holder.delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Products")
                        .child(products.getProductId());

                ref.removeValue();
            }
        });


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class HolderProducts extends RecyclerView.ViewHolder {
       ImageView img;
       TextView title,desc,price,quantity;
       Button delet;
       public HolderProducts(@NonNull View itemView) {
           super(itemView);
           img = itemView.findViewById(R.id.imgI);
           title = itemView.findViewById(R.id.titleI);
           desc = itemView.findViewById(R.id.descI);
           price = itemView.findViewById(R.id.priceI);
           quantity = itemView.findViewById(R.id.quantityI);

           delet = itemView.findViewById(R.id.deletBtn);
       }
   }
}
