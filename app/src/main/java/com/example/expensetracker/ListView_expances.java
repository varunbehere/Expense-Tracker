package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListView_expances extends AppCompatActivity {
    TextView text;
    ListView listView;
    DatabaseReference d_ref;
    ArrayList<String> date;
    ArrayList<String> time;
    List_Date_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_expances);
        getSupportActionBar().hide();
        text=findViewById(R.id.title_text);
        text.setText(getIntent().getStringExtra("EXPANSE"));
        listView=findViewById(R.id.list);
        date=new ArrayList<String>();
        time=new ArrayList<String>();
        adapter=new List_Date_Adapter(this,date,time,getIntent().getStringExtra("USERNAME"),getIntent().getStringExtra("EXPANSE"),
                getIntent().getStringExtra("YEAR"),getIntent().getStringExtra("MONTH"));
        d_ref= FirebaseDatabase.getInstance().getReference("users").
                child(""+getIntent().getStringExtra("USERNAME")).
                child(""+getIntent().getStringExtra("EXPANSE")).
                child(""+getIntent().getStringExtra("YEAR")).
                child(""+getIntent().getStringExtra("MONTH"));

        d_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot child : snapshot.child(childSnapshot.getKey().toString()).getChildren()) {
                            date.add(childSnapshot.getKey().toString());
                            time.add(child.getKey().toString());
                        }
                    }
                    listView.setAdapter(adapter);
                }else{
                    Toast.makeText(ListView_expances.this, "No Such Data Present...", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }});
    }
}