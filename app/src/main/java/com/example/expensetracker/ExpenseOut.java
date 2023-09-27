package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class ExpenseOut extends AppCompatActivity implements ValueEventListener{
    private final int PICK_IMAGE_REQUEST = 22;
    DatabaseReference d_ref,reference;
    private Uri filePath;
    Cursor c;
    int old_spend,old_globle_spend;
    String username1;
    TextInputEditText select_image,e_amount,e_note;
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_out);
        getSupportActionBar().hide();
        select_image = findViewById(R.id.image_select);
        e_amount=findViewById(R.id.edit_text_amount);
        e_note=findViewById(R.id.edit_note);
        MyHelper helper = new MyHelper(getApplicationContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        c = database.rawQuery("select * from USER", new String[]{});
        if (c != null) {
            c.moveToFirst();
        }
        do {
            username1 = c.getString(1);
        } while (c.moveToNext());
      reference=FirebaseDatabase.getInstance().getReference("users").child(username1);
      reference.addValueEventListener(this);


        d_ref= FirebaseDatabase.getInstance().getReference("users").child(username1).child("EXpenseOut").
                child(getyear()).child(getmonth()).child(getcurrneDate()).child(getcurrentTime());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }
    public void selectImage(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);


        //Toast.makeText(getApplicationContext(),"I am jagruti",Toast.LENGTH_LONG).show();
    }


    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
               // BitmapDrawable drawableLeft = new BitmapDrawable(getResources(), bitmap);
                //  select_image.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableLeft, null);
                select_image.setText("Image Selected");

            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }


    String getcurrentTime() {
        String currentTime = new SimpleDateFormat("HH:mm:ss a", Locale.getDefault()).format(new Date());
        return currentTime;
    }

    String getcurrneDate() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        return currentDate;
    }

    String getyear() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String arr[] = currentDate.split("-");
        return arr[2];
    }

    String getmonth() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String arr[] = currentDate.split("-");
        return arr[1];
    }


    // UploadImage method
    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Wait Image Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(ExpenseOut.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            //You will get donwload URL in uri
                                            // Log.d(TAG, "Download URL = "+ uri.toString());
                                            //Adding that URL to Realtime database
                                            d_ref.child("imageUrl").setValue(uri.toString());
                                        }
                                    });


                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(ExpenseOut.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }

    public void submitButton(View view) {

        String am=e_amount.getText().toString();
        String no=e_note.getText().toString();
        String im=select_image.getText().toString();
        if(am.isEmpty()){
            Toast.makeText(getApplicationContext(),"plz enter amount...",Toast.LENGTH_LONG).show();
        }else if(no.isEmpty()){
            Toast.makeText(getApplicationContext(),"plz enter some important notes or category...",Toast.LENGTH_LONG).show();
        }else if (im.equals("Image Selected")){
            int g_total=old_globle_spend+Integer.parseInt(am);
            int total=old_spend+Integer.parseInt(am);
            d_ref.child("day_amount").setValue(am);
            d_ref.child("day_note").setValue(no);
            reference.child("total_spending").setValue(""+g_total);
            reference.child("EXpenseOut").
                    child(getyear()).child(getmonth()).child("monthly_spend").setValue(""+total);
            Toast.makeText(getApplicationContext(),"Data Upload Successfully",Toast.LENGTH_LONG).show();
            uploadImage();
        }else{
            int g_total=old_globle_spend+Integer.parseInt(am);
            int total=old_spend+Integer.parseInt(am);
            d_ref.child("day_amount").setValue(am);
            d_ref.child("day_note").setValue(no);
            d_ref.child("imageUrl").setValue("empty");
            reference.child("total_spending").setValue(""+g_total);
            reference.child("EXpenseOut").child(getyear()).child(getmonth()).child("monthly_spend").setValue(""+total);
            Toast.makeText(getApplicationContext(),"Data Upload Successfully",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        old_globle_spend=Integer.parseInt(snapshot.child("total_spending").getValue().toString());
        if (!(snapshot.child("EXpenseOut").child(getyear()).child(getmonth()).child("monthly_spend").exists())){
            reference.child("EXpenseOut").child(getyear()).child(getmonth()).child("monthly_spend").setValue("0");
        }else{
            old_spend=Integer.parseInt(snapshot.child("EXpenseOut").
                    child(getyear()).child(getmonth()).child("monthly_spend").getValue().toString());
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
