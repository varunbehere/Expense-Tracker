package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DisplayHistory extends AppCompatActivity implements ValueEventListener {
ImageView imageView;
TextView note,amount,exa;
DatabaseReference d_ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_history);
       // getSupportActionBar().hide();
        imageView=findViewById(R.id.h_image);
        note=findViewById(R.id.h_note);
        amount=findViewById(R.id.h_amount);
       // exa=findViewById(R.id.ex);
       // exa.setText(getIntent().getStringExtra("EXPANSE"));
        d_ref= FirebaseDatabase.getInstance().getReference("users").
                child(""+getIntent().getStringExtra("USERNAME")).
                child(""+getIntent().getStringExtra("EXPANSE")).
                child(""+getIntent().getStringExtra("YEAR")).
                child(""+getIntent().getStringExtra("MONTH")).
                child(""+getIntent().getStringExtra("DATE")).
                child(""+getIntent().getStringExtra("TIME"));
        d_ref.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
 amount.setText(""+snapshot.child("day_amount").getValue().toString()+" Rs");
 note.setText(""+snapshot.child("day_note").getValue().toString());
 if (!(snapshot.child("imageUrl").getValue().toString().equals("empty"))){
     Picasso.with(getApplicationContext()).load(snapshot.child("imageUrl").getValue().toString()).into(imageView);
 }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) { }
}