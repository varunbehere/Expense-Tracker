package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SignUp extends AppCompatActivity {
    AlertDialog.Builder builder;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    Intent intent;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        builder = new AlertDialog.Builder(this);
    }
    public void loginButton(View view) {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }

    public void register_User(View view) {

       if(!validateName() | !validatePassword() | !validateincome() | !validateEmail() | !validatespend()){
           return;
       } else{
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");
            final String userfullname=((EditText)findViewById(R.id.FullName)).getText().toString().trim();
            final String email_id=((EditText)findViewById(R.id.eEmailAddress)).getText().toString().toLowerCase().trim();
            final String income=((EditText)findViewById(R.id.income)).getText().toString().trim();
            final String spend=((EditText)findViewById(R.id.spend)).getText().toString().trim();
            final String pass=((EditText)findViewById(R.id.r_Password)).getText().toString().trim();
            final String[] temp=email_id.split("\\.");
            final String email=temp[0];
            final String totalspend="0";
            final String totalsaving=getcurrneDate();

       //Toast.makeText(getApplicationContext(),""+email,Toast.LENGTH_LONG).show();
       // final String c_pass=((EditText)findViewById(R.id.con_pass)).getText().toString().trim();

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(email).exists()){
                        ((EditText)findViewById(R.id.eEmailAddress)).setError("Email Id Already Register");
                    }
                    else{
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        final View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_d, viewGroup, false);
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setTitle(R.string.app_name);
                        builder.setMessage("This information is usefull when you forget password");
                        final EditText input_color = dialogView.findViewById(R.id.amount);
                        final EditText input_friend=dialogView.findViewById(R.id.user_id);
                        input_color.setInputType(InputType.TYPE_CLASS_TEXT);
                        input_friend.setInputType(InputType.TYPE_CLASS_TEXT);
                        // ( (TextView)dialogView.findViewById(R.id.t2)).setText("Color: ");
                        // ( (TextView)dialogView.findViewById(R.id.t1)).setText("Friend: ");
                        // input_color.setHint("Enter Favourite Color");
                        // input_friend.setHint("Enter Best Friend Name");
                        builder.setView(dialogView);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String color_=input_color.getText().toString().trim();
                                String friend=input_friend.getText().toString().trim();
                                if(color_.equals("") || friend.equals("")){
                                    Toast.makeText(getApplicationContext(),"Plz Enter Proper information",Toast.LENGTH_LONG).show();
                                }else{
                                    student = new Student( pass,email_id,userfullname,spend,income,color_,friend,totalspend,totalsaving);
                                    reference.child(email).setValue(student);
                                    MyHelper helper=new MyHelper(getApplicationContext());
                                    SQLiteDatabase database=helper.getWritableDatabase();
                                    ContentValues values=new ContentValues();
                                    values.put("USER_NAME",email);
                                    values.put("PASSWORD",pass);
                                    values.put("STATUS","true");
                                    database.update("USER",values,"id=?",new String[]{"1"});
                                    startActivity(new Intent(SignUp.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });

        }



        }


    private Boolean validatePassword(){
        String val = ((EditText)findViewById(R.id.r_Password)).getText().toString().trim();
        String c_val=((EditText)findViewById(R.id.con_pass)).getText().toString().trim();
       // Toast.makeText(getApplicationContext(),""+c_val,Toast.LENGTH_LONG).show();
        String passwordVal = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.equals("")) {
            ((EditText)findViewById(R.id.r_Password)).setError("Field cannot be empty");
            return false;
        } else if (c_val.equals("")) {
            ((EditText)findViewById(R.id.con_pass)).setError("Field cannot be empty");
            return false;
        }else if (!val.equals(c_val)) {
            ((EditText) findViewById(R.id.con_pass)).setError("Password and Confirm pass must be same");
            return false;
        } else if (!val.matches(passwordVal)) {
            ((EditText)findViewById(R.id.r_Password)).setError("Password is too weak");
            return false;
        } else {
            ((EditText)findViewById(R.id.r_Password)).setError(null);
            //((EditText)findViewById(R.id.FullName)).setErrorEnabled(false);
            return true;
        }
    }


    private Boolean validateincome() {
        String val = ((EditText)findViewById(R.id.income)).getText().toString().trim();

        if (val.isEmpty()) {
            ((EditText)findViewById(R.id.income)).setError("Field cannot be empty");
            return false;
        } else {
            ((EditText)findViewById(R.id.income)).setError(null);

            return true;
        }
    }


    private Boolean validatespend() {
        String val = ((EditText)findViewById(R.id.spend)).getText().toString().trim();

        if (val.isEmpty()) {
            ((EditText)findViewById(R.id.spend)).setError("Field cannot be empty");
            return false;
        } else {
            ((EditText)findViewById(R.id.spend)).setError(null);

            return true;
        }
    }



    private Boolean validateName() {
        String val = ((EditText)findViewById(R.id.FullName)).getText().toString().trim();

        if (val.isEmpty()) {
            ((EditText)findViewById(R.id.FullName)).setError("Field cannot be empty");
            return false;
        } else {
            ((EditText)findViewById(R.id.FullName)).setError(null);

            return true;
        }
    }


    private Boolean validateEmail() {
        String val = ((EditText)findViewById(R.id.eEmailAddress)).getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            ((EditText)findViewById(R.id.eEmailAddress)).setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            ((EditText)findViewById(R.id.eEmailAddress)).setError("Invalid email address");
            return false;
        } else {
            ((EditText)findViewById(R.id.eEmailAddress)).setError(null);
           // regEmail.setErrorEnabled(false);
            return true;
        }
    }




    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
        //  super.onBackPressed();

    }
    String getcurrneDate() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        return currentDate;
    }

}