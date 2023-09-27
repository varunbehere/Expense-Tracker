package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Budget extends AppCompatActivity implements ValueEventListener {
EditText budget_income,budget_limit,budget_daily_limit,saving_mode;
    String username1;
    Cursor c;
    private DatabaseReference d_ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        getSupportActionBar().hide();
        budget_income=findViewById(R.id.budgetIncome);
        budget_limit=findViewById(R.id.budgetLimit);
        budget_daily_limit=findViewById(R.id.budgetDailyLimit);
        saving_mode=findViewById(R.id.budgetApproSave);
        MyHelper helper = new MyHelper(getApplicationContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        c = database.rawQuery("select * from USER", new String[]{});
        if (c != null) {
            c.moveToFirst();
        }
        do {
            username1 = c.getString(1);
        } while (c.moveToNext());
        d_ref = FirebaseDatabase.getInstance().getReference("users").child(username1);
        d_ref.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        int income=Integer.parseInt(snapshot.child("income").getValue(String.class));
        int spend=Integer.parseInt(snapshot.child("spend").getValue(String.class));
        budget_income.setText(snapshot.child("income").getValue(String.class));
        budget_limit.setText(snapshot.child("spend").getValue(String.class));
        saving_mode.setText(""+(income-spend));
        budget_daily_limit.setText(""+(spend/returnMonthDays()));
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    public void editButtonClick(View view) {
        String button_name=((Button)findViewById(view.getId())).getText().toString().trim();
        if (button_name.equals("EDIT")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setMessage("Are you sure to Edit Given Data?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    budget_income.setEnabled(true);
                    budget_limit.setEnabled(true);
                    ((Button)findViewById(view.getId())).setText("SUBMIT");
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }});
            AlertDialog alert = builder.create();
            alert.show();
        }else{
            String in,sp;
            in=budget_income.getText().toString().trim();
            sp=budget_limit.getText().toString().trim();
            if (in.isEmpty() && sp.isEmpty()){
                Toast.makeText(getApplication(),"Something Wrong...",Toast.LENGTH_LONG).show();
            }else{
                d_ref.child("income").setValue(in);
                d_ref.child("spend").setValue(sp);
                budget_income.setEnabled(false);
                budget_limit.setEnabled(false);
                ((Button)findViewById(view.getId())).setText("EDIT");
                //d_ref.addValueEventListener()

            }
        }
    }
    String getmonth() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String arr[] = currentDate.split("-");
        return arr[1];
    }
    int returnMonthDays() {
        switch (getmonth()) {
            case "01":
                return 31;

            case "02":
                if (check_leap_year(Integer.parseInt(getyear()))) {
                    return 29;
                }else {
                    return 28;
                }
            case "03":
                return 31;

            case "04":
                return 30;

            case "05":
                return 31;

            case "06":
                return 30;

            case "07":
                return 31;

            case "08":
                return 31;

            case "09":
                return 30;

            case "10":
                return 31;

            case "11":
                return 30;
            case "12":
                return 31;

        }
        return 1;
    }
boolean check_leap_year(int year){
    if (((year % 4 == 0) && (year % 100!= 0)) || (year%400 == 0))
        return true;
    else
        return false;
}
String getyear() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String arr[] = currentDate.split("-");
        return arr[2];
}


}


