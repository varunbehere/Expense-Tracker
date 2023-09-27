package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Savings extends AppCompatActivity implements ValueEventListener {
TextView total_saving,day_saving,month_saving,week_saving;
int total_spend;
DatabaseReference d_ref;
Cursor c;
    long differenceDates;
    int i;
int monthly_spending=0;
String username1;
int w_sum;
ArrayList<String> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savings);
        total_saving=findViewById(R.id.txtToShowTotalSavings);
        day_saving=findViewById(R.id.txtTOSavedToday);
        month_saving=findViewById(R.id.textView13);
        week_saving=findViewById(R.id.txtToShowWeeklySavings);
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
        d_ref= FirebaseDatabase.getInstance().getReference("users").child(username1);
        d_ref.addValueEventListener(this);



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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        total_spend=Integer.parseInt(snapshot.child("total_spending").getValue().toString());
        if(snapshot.child("EXpenseOut").child(getyear()).child(getmonth()).child("monthly_spend").exists()){
            monthly_spending=Integer.parseInt(snapshot.child("EXpenseOut").child(getyear()).child(getmonth()).
                    child("monthly_spend").getValue().toString());
        }



        int spend=Integer.parseInt(snapshot.child("spend").getValue(String.class));
        int day_buget= spend/returnMonthDays();

        if(snapshot.child("EXpenseOut").child(getyear()).child(getmonth()).child(getcurrneDate()).exists()){
            int sum=0;
            for (DataSnapshot dataSnapshot : snapshot.child("EXpenseOut").child(getyear()).child(getmonth()).child(getcurrneDate()).getChildren()) {
                //  sum+=Integer.parseInt(dataSnapshot.child(dataSnapshot.getKey()).getValue().toString());
                String s=dataSnapshot.getKey().toString();
                sum+=Integer.parseInt(snapshot.child("EXpenseOut").child(getyear()).child(getmonth()).child(getcurrneDate()).child(s).child("day_amount").getValue().toString());
            }
            day_saving.setText((day_buget-sum)+" Rs.");
        }
        else{
            day_saving.setText(day_buget+" Rs.");
        }
   //Monthly Store
        String[] temp=snapshot.child("total_saving").getValue().toString().split("-");
        if(temp[1].equals(getmonth()) && temp[2].equals(getyear())){
            int diff=(Integer.parseInt(getcurrneDate().split("-")[0])-Integer.parseInt(temp[0]))+1;
            month_saving.setText(((diff*day_buget)-monthly_spending)+" Rs.");
        }else{
            month_saving.setText((((Integer.parseInt(getcurrneDate().split("-")[0])*day_buget)-monthly_spending)+" Rs.")+" Rs.");
        }

       //week saving

        for (DataSnapshot dataSnapshot : snapshot.child("EXpenseOut").child(getyear()).child(getmonth()).getChildren()) {
            if (!(dataSnapshot.getKey().toString().equals("monthly_spend"))){
                arr.add(dataSnapshot.getKey().toString());
            }
        }
        if (arr.size()>7){
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
        int d=Integer.parseInt(getcurrneDate().split("-")[0]) -Integer.parseInt(temp[0])+1;
      if(d>=7){
          week_saving.setText(""+((7*day_buget)-w_sum)+" Rs.");
      } else {
          week_saving.setText(""+((d*day_buget)-w_sum)+" Rs.");
      }
        try {
            long l=numberofDaysBetweenTwoDays(snapshot.child("total_saving").getValue().toString(),getcurrneDate());
           // Toast.makeText(getApplicationContext(), ""+l, Toast.LENGTH_SHORT).show();
            total_saving.setText(""+((l*day_buget)-total_spend)+" Rs.");
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) { }


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

    long numberofDaysBetweenTwoDays(String befor, String after) throws ParseException {
        differenceDates=1;
        try {
            Date date1;
            Date date2;
            SimpleDateFormat dates = new SimpleDateFormat("dd-MM-yyyy");
            date1 = dates.parse(after);
            date2 = dates.parse(befor);
            long difference = Math.abs(date1.getTime() - date2.getTime());
            differenceDates = difference / (24 * 60 * 60 * 1000);
            return differenceDates+1;
           // String dayDifference = Long.toString(differenceDates);
            //textView.setText("The difference between two dates is " + dayDifference + " days");
        }catch (Exception e){return differenceDates;}
       // return differenceDates;
    }
}