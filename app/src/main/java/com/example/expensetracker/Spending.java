package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Spending extends AppCompatActivity implements ValueEventListener {
TextView total_spending,monthly_spending,day_spend,week_spend;
DatabaseReference d_ref;
int i;
ArrayList<String> arr;
Cursor c;
    int w_sum=0;
String username1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending);
        getSupportActionBar().hide();
        total_spending=findViewById(R.id.txtToShowTotalSpendings);
        monthly_spending=findViewById(R.id.txtToShowSpentThisMonth);
        day_spend=findViewById(R.id.txtToShowSpentToday);
        week_spend=findViewById(R.id.txtToShowSpentThisWeek);
        arr=new ArrayList<String>();
        MyHelper helper = new MyHelper(getApplicationContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        c = database.rawQuery("select * from USER", new String[]{});
        if (c != null) {
            c.moveToFirst();
        }
        do {
            username1 = c.getString(1);
        } while (c.moveToNext());
        d_ref=FirebaseDatabase.getInstance().getReference("users").child(username1);
        d_ref.addValueEventListener(this);
        //=FirebaseDatabase.getInstance().getReference("users").child(username1);

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        total_spending.setText(snapshot.child("total_spending").getValue().toString()+" Rs.");
        if(snapshot.child("EXpenseOut").child(getyear()).child(getmonth()).child("monthly_spend").exists()){
            monthly_spending.setText(snapshot.child("EXpenseOut").child(getyear()).child(getmonth()).
                    child("monthly_spend").getValue().toString()+" Rs.");
        }else{
            monthly_spending.setText("0 Rs.");
        }

        if(snapshot.child("EXpenseOut").child(getyear()).child(getmonth()).child(getcurrneDate()).exists()){

            int sum=0;
            for (DataSnapshot dataSnapshot : snapshot.child("EXpenseOut").child(getyear()).child(getmonth()).child(getcurrneDate()).getChildren()) {
              //  sum+=Integer.parseInt(dataSnapshot.child(dataSnapshot.getKey()).getValue().toString());
                String s=dataSnapshot.getKey().toString();
                sum+=Integer.parseInt(snapshot.child("EXpenseOut").child(getyear()).child(getmonth()).child(getcurrneDate()).child(s).child("day_amount").getValue().toString());
            }
            day_spend.setText(sum+" Rs.");
        }else{
            day_spend.setText("0 Rs.");
        }

        for (DataSnapshot dataSnapshot : snapshot.child("EXpenseOut").child(getyear()).child(getmonth()).getChildren()) {
             if (!(dataSnapshot.getKey().toString().equals("monthly_spend"))){
                 arr.add(dataSnapshot.getKey().toString());
             }
        }
        if (arr.size()==0) {
            week_spend.setText("0 Rs.");
           // Toast.makeText(this, "" + arr.get(0), Toast.LENGTH_SHORT).show();
        }else if (arr.size()>7){
            i=arr.size()-7;
        }else{i=0;}
        w_sum=0;
        while(i<arr.size()){
            String c_date=getcurrneDate();
            String date=arr.get(i);
            int d=Integer.parseInt(c_date.split("-")[0]) -Integer.parseInt(date.split("-")[0]);
            if(d>7){ i++;}
            else{

                for (DataSnapshot dataSnapshot : snapshot.child("EXpenseOut").child(getyear()).child(getmonth()).child(arr.get(i)).getChildren()) {
                    //  sum+=Integer.parseInt(dataSnapshot.child(dataSnapshot.getKey()).getValue().toString());
                    String s=dataSnapshot.getKey().toString();
                    w_sum+=Integer.parseInt(snapshot.child("EXpenseOut").child(getyear()).child(getmonth()).child(arr.get(i)).child(s).child("day_amount").getValue().toString());
                }i++;
            }
        }
        week_spend.setText(w_sum+" Rs.");


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
    String getcurrneDate() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        return currentDate;
    }

}