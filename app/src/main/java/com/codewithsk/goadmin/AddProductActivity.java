package com.codewithsk.goadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.codewithsk.goadmin.databinding.ActivityAddProductBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {
    ActivityAddProductBinding binding;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    //Image Pick gallary code
    private static final int IMAGE_PICK_GALLARY_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE = 500;

    //location permition
    private String[] cameraPermissions;
    private String[] storagePermissions;

    private ProgressDialog progressDialog;

    //Image Picking Uri
    private Uri image_uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cameraPermissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        storagePermissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickingDialog();
            }
        });
        binding.btnAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.titleUpload.getText().toString().trim();
                String desc = binding.descUpload.getText().toString().trim();
                String price = binding.priceUpload.getText().toString().trim();
                String qunt = binding.qunatityUpload.getText().toString().trim();
                String quntIn = binding.quantityInUpload.getText().toString().trim();
                if (title.isEmpty() || desc.isEmpty() || price.isEmpty() || qunt.isEmpty()
                || quntIn.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Some Fields are empty", Toast.LENGTH_SHORT).show();
                }else {
                    if (image_uri != null) {
                        progressDialog.show();
                        String timetamp = ""+System.currentTimeMillis();
                        progressDialog.setMessage("adding Icon...");
                        String filePathAndName = "product_images/"+""+timetamp;
                        StorageReference sref = FirebaseStorage.getInstance().getReference(filePathAndName);
                        sref.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> imgUri = taskSnapshot.getStorage().getDownloadUrl();
                                while(!imgUri.isSuccessful());
                                Uri downloadImgUri = imgUri.getResult();
                                if (imgUri.isSuccessful()){
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Products");
                                    Products  products = new Products();
                                    products.setProductId(timetamp);
                                    products.setImg(downloadImgUri.toString());
                                    products.setDesc(desc);
                                    products.setPrice(price);
                                    products.setTitle(title);
                                    products.setQuantity(qunt);
                                    products.setQuantityIn(quntIn);
                                    ref.child(timetamp).setValue(products).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AddProductActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();

                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            Toast.makeText(AddProductActivity.this, "Product Added!!..", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(AddProductActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(), "Image No Selected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }

    private void showImagePickingDialog() {
        String[] options = {"CAMERA","GALLARY"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("PICK IMAGE")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            //camera clicked
                            if (checkCameraPermission()){
                                //check camera permesstion
                                pickFromCamera();

                            }else{
                                //not allowed request
                                requestCameraPermission();
                            }
                        }else {
                            //Gallary Clicked
                            if (checkStoragePermission()){
                                //check camera permesstion
                                pickFromGallary();

                            }else{
                                //not allowed request
                                requestStoragePermission();
                            }
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void pickFromGallary(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLARY_REQUEST_CODE);
    }

    private void pickFromCamera(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(intent,IMAGE_PICK_CAMERA_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted){
                        pickFromCamera();
                    }else{
                        Toast.makeText(this, "Camera permission is nessesory...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length > 0){
                    boolean gallaryAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (gallaryAccepted){
                        pickFromGallary();
                    }else{
                        Toast.makeText(this, "Storage permission is nessesory...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLARY_REQUEST_CODE){
                //get picked image
                image_uri = data.getData();

                //set image
                binding.imgUpload.setImageURI(image_uri);
            }else if (requestCode == IMAGE_PICK_CAMERA_REQUEST_CODE){
                binding.imgUpload.setImageURI(image_uri);
            }
        }
    }
}