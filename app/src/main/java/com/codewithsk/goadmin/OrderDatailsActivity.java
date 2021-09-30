package com.codewithsk.goadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codewithsk.goadmin.databinding.ActivityOrderDatailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderDatailsActivity extends AppCompatActivity {

    ActivityOrderDatailsBinding binding;
    AdapterInner inner;
    ArrayList<innerOrder> innerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDatailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
        //Asking request Permissions
        ActivityCompat.requestPermissions(OrderDatailsActivity.this, PERMISSIONS_STORAGE, 9);

        String id = getIntent().getStringExtra("id");
        String addr = getIntent().getStringExtra("addr");
        String tPrice = getIntent().getStringExtra("tPrice");
        String email = getIntent().getStringExtra("email");
        String status = getIntent().getStringExtra("status");
        String uid = getIntent().getStringExtra("uid");
        String contact = getIntent().getStringExtra("contact");

        binding.orderStatuses.setText(status);
        binding.orderIds.setText(id);
        binding.userAddr.setText(addr);
        binding.userPhone.setText(contact);
        binding.userEmail.setText(email);
        binding.totalPrice.setText(tPrice);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Orders")
                .child(id).child("Items");

        laodInnerItems(ref);

        binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Orders")
                        .child(id).child("status");
                ref.setValue("accepted");
                onBackPressed();
            }
        });
        binding.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Orders")
                        .child(id).child("status");
                ref.setValue("rejected");
                onBackPressed();
            }
        });
        binding.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneCall(OrderDatailsActivity.this,contact);
            }
        });

    }

    private void laodInnerItems(DatabaseReference ref) {
        innerList = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                innerList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    innerOrder o = ds.getValue(innerOrder.class);
                    innerList.add(o);
                }
                inner = new AdapterInner(OrderDatailsActivity.this,innerList);
                binding.innerItemsRv.setAdapter(inner);
                inner.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean permissionGranted = false;
        switch(requestCode){
            case 9:
                permissionGranted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                break;
        }
        if(permissionGranted){
            Toast.makeText(getApplicationContext(), "Granted", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private static void phoneCall(Context context,String no) {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            String uri = "tel:" + no.trim() ;
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(uri));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(callIntent);
        } else {
            Toast.makeText(context, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }


}
class HolderInnerOrder extends RecyclerView.ViewHolder {
    TextView tvId,tvEmail,tvAddr;
    public HolderInnerOrder(@NonNull View itemView) {
        super(itemView);
        tvId = itemView.findViewById(R.id.orderId);
        tvEmail = itemView.findViewById(R.id.orderEmail);
        tvAddr = itemView.findViewById(R.id.orderStatus);
    }
}
class AdapterInner extends RecyclerView.Adapter<HolderInnerOrder>{
    Context context;
    ArrayList<innerOrder> oList;
    public AdapterInner(Context context,ArrayList<innerOrder> oList){
        this.context = context;
        this.oList = oList;
    }
    @NonNull
    @Override
    public HolderInnerOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.row_order,parent,false);
        return new HolderInnerOrder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderInnerOrder holder, int position) {
        innerOrder order = oList.get(position);
        holder.tvId.setText("Title: "+order.getTitle());
        holder.tvAddr.setText("Total Quantity: "+order.getQuantity());
        holder.tvEmail.setText("Total Price: "+order.getPrice()+"Rs");
    }

    @Override
    public int getItemCount() {
        return oList.size();
    }

}