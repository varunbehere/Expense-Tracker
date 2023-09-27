package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Month extends AppCompatActivity implements ValueEventListener {
    double m_in=0,m_out=0;
    Cursor c;
    boolean flag=true;
    String selected_month;
    String username1;
DatabaseReference d_ref;
    float per_In=0.0f;
    float per_Out=0.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);
        MyHelper helper = new MyHelper(getApplicationContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        c = database.rawQuery("select * from USER", new String[]{});
        if (c != null) {
            c.moveToFirst();
        }
        do {
            username1 = c.getString(1);
        } while (c.moveToNext());
        d_ref= FirebaseDatabase.getInstance().getReference("users").child(username1);

    }

    public void monthClick(View view) {
        selected_month=((Button)findViewById(view.getId())).getText().toString();
        d_ref.addValueEventListener(this);
//        Intent intent=new Intent(getApplicationContext(), PieChartActivity.class);
//       // Toast.makeText(getApplicationContext(), ""+per_Out+"     "+per_in, Toast.LENGTH_SHORT).show();
//        intent.putExtra("IN",per_In);
//        intent.putExtra("OUT",per_Out);
//        startActivity(intent);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.child("EXpenseIn").child(getyear()).child(selected_month).child("monthly_in").exists()) {
            m_in = Double.parseDouble(snapshot.child("EXpenseIn").child(getyear()).child(selected_month).child("monthly_in").getValue(String.class));
        }
        if (snapshot.child("EXpenseOut").child(getyear()).child(selected_month).child("monthly_spend").exists()) {
            m_out = Double.parseDouble(snapshot.child("EXpenseOut").child(getyear()).child(selected_month).child("monthly_spend").getValue(String.class));
        }
        if ((m_in + m_out) != 0) {
            per_In = (float) ((m_in / (m_in + m_out)) * 100);
            per_Out = (float) (m_out / (m_in + m_out)) * 100;
        }
            Intent intent = new Intent(getApplicationContext(), PieChartActivity.class);
            // Toast.makeText(getApplicationContext(), ""+per_Out+"     "+per_in, Toast.LENGTH_SHORT).show();
            intent.putExtra("IN", per_In);
            intent.putExtra("OUT", per_Out);
            startActivity(intent);
            d_ref.removeEventListener(this);
            //Toast.makeText(getApplicationContext(), "" + per_Out + "     " + per_In, Toast.LENGTH_SHORT).show();
            finish();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
    }
    String getyear() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String arr[] = currentDate.split("-");
        return arr[2];
    }

}