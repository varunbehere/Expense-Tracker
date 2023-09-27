package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TransporttationHistory extends AppCompatActivity implements  ValueEventListener{
    Intent intent;
Spinner year,month;
    TextInputEditText bal;
    //String[] courses = { "Select Year", "2022"};
    DatabaseReference d_ref;
    String[] m = { "Select Month Number", "01","02","03","04","05","06","07","08","09","10","11","12"};
    ArrayAdapter ad;
    String username1;
    ArrayList<String> arr;
    Cursor c;
    int b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transporttation_history);
        getSupportActionBar().hide();
        bal=findViewById(R.id.edit_bal);
    arr=new ArrayList<>();
    arr.add("Select Year");
        MyHelper helper = new MyHelper(getApplicationContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        c = database.rawQuery("select * from USER", new String[]{});
        if (c != null) { c.moveToFirst(); }
        do { username1 = c.getString(1);
        } while (c.moveToNext());
        d_ref= FirebaseDatabase.getInstance().getReference("users").child(username1);
d_ref.addValueEventListener(this);
       d_ref.child("EXpenseIn").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                   arr.add(childSnapshot.getKey().toString());
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) { }});
      if (arr.size()==1){
          d_ref.child("EXpenseOut").addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                      arr.add(childSnapshot.getKey().toString());
                  }
              }
              @Override
              public void onCancelled(@NonNull DatabaseError error) { }});
      }


        year=findViewById(R.id.year_spinner);
        month=findViewById(R.id.month_spinner);
        ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arr);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(ad);

        ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, m);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month.setAdapter(ad);
        month.getSelectedItem().toString();




    }
    boolean checkdata(){
        String yy=year.getSelectedItem().toString();
        String mm=month.getSelectedItem().toString();
        if (yy.equals("Select Year")){
            Toast.makeText(getApplicationContext(),"Plz Select Year",Toast.LENGTH_LONG).show();
            return false;
        }else if (mm.equals("Select Month Number")){
            Toast.makeText(getApplicationContext(),"Plz Select Month",Toast.LENGTH_LONG).show();
            return false;
        }else{return true;
        }
    }
    public void expanse_In(View view) {
        if(checkdata()){
            intent=new Intent(getApplicationContext(),ListView_expances.class);
            intent.putExtra("MONTH",month.getSelectedItem().toString());
            intent.putExtra("YEAR",year.getSelectedItem().toString());
            intent.putExtra("EXPANSE","EXpenseIn");
            intent.putExtra("USERNAME",username1);
            startActivity(intent);
           // finish();
        }

    }

    public void expanse_Out(View view) {
        if(checkdata()){
            //startActivity(new Intent(getApplicationContext(),ListView_expances.class));
            intent=new Intent(getApplicationContext(),ListView_expances.class);
            intent.putExtra("MONTH",month.getSelectedItem().toString());
            intent.putExtra("YEAR",year.getSelectedItem().toString());
            intent.putExtra("EXPANSE","EXpenseOut");
            intent.putExtra("USERNAME",username1);
            startActivity(intent);
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        b=Integer.parseInt(snapshot.child("spend").getValue().toString());
        if (snapshot.child("EXpenseIn").child(getyear()).child(getmonth()).child("monthly_in").exists()){
            b=b+Integer.parseInt(snapshot.child("EXpenseIn").child(getyear()).child(getmonth()).child("monthly_in").getValue().toString());
        }
        if (snapshot.child("EXpenseOut").child(getyear()).child(getmonth()).child("monthly_spend").exists()){
            b=b-Integer.parseInt(snapshot.child("EXpenseOut").child(getyear()).child(getmonth()).child("monthly_spend").getValue().toString());
        }
        bal.setText(b+" Rs.");
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) { }


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



}
