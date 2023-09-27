package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity  {
    ViewGroup viewGroup;
    EditText username, password;
    AlertDialog.Builder builder;
    DatabaseReference reference;
    public static String username_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        reference= FirebaseDatabase.getInstance().getReference("users");
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        viewGroup = findViewById(android.R.id.content);
        builder = new AlertDialog.Builder(this);
        MyHelper helper = new MyHelper(getApplicationContext());
        SQLiteDatabase database = helper.getWritableDatabase();

        Cursor c = database.rawQuery("select * from USER", new String[]{});
        if (c != null) {
            c.moveToFirst();
        }
        String status;
        do {
            status = c.getString(3);
            username_text=c.getString(1);
        } while (c.moveToNext());
        if (status.equals("true")) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
        else {
            setContentView(R.layout.activity_login);
            username = findViewById(R.id.l_email);
            password = findViewById(R.id.l_pass);
        }
    }

    public void Click_on_SignUp(View view) {
        startActivity(new Intent(getApplicationContext(),SignUp.class));
        finish();
    }


    public void Login_Button_click(View view) {
        if (!validateEmail() | !validatePassword()) {
            return;
        }
        else {
            isUser();
        }
    }

    public void forgetPasswordClick(View view) {

        final   EditText forget_username,forget_color,forget_friend;


        final View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.forgetpass_d, viewGroup, false);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.app_name);
        forget_username = dialogView.findViewById(R.id.forget_username);
        forget_color = dialogView.findViewById(R.id.forget_color);
        forget_friend = dialogView.findViewById(R.id.forget_friend);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final   String color_=forget_color.getText().toString().trim();
                final    String friend=forget_friend.getText().toString().trim();
                 String u=forget_username.getText().toString().trim();
                 String[] temp=u.split("\\.");
                final  String user=temp[0];
                if(color_.equals("") || friend.equals("") || user.equals("")){
                    Toast.makeText(getApplicationContext(),"Plz Enter Proper information",Toast.LENGTH_LONG).show();
                }else{

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(user).exists()){
                                if(dataSnapshot.child(user).child("color_").getValue(String.class).trim().equalsIgnoreCase(color_) && dataSnapshot.child(user).child("friend").getValue(String.class).trim().equalsIgnoreCase(friend)){
                                    AlertDialog.Builder a = new AlertDialog.Builder(LoginActivity.this);
                                    a.setTitle(R.string.app_name);
                                    a.setMessage("Congratulation\nYour Password: "+dataSnapshot.child(user).child("password").getValue(String.class));
                                    a.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    a.setIcon(R.mipmap.ic_launcher);
                                    a.show();
                                } else{
                                    Toast.makeText(getApplicationContext(),"Sorry,Wrong Info.",Toast.LENGTH_LONG).show();
                                }

                            }
                            else{
                                Toast.makeText(getApplicationContext(),"UserName Not Exists",Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }});

                }
            }
        });
        builder.show();
    }


    private Boolean validateEmail() {
        String val = username.getText().toString().toLowerCase().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            username.setError("Invalid email address");
            return false;
        } else {
            username.setError(null);
            // regEmail.setErrorEnabled(false);
            return true;
        }
    }



    private Boolean validatePassword() {
        String val = password.getText().toString();
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            //password.setErrorEnabled(false);
            return true;
        }
    }

    public void isUser() {
      //  String u=username.getText().toString().toLowerCase().trim();
        //String[] temp=u.split("\\.");
        //final  String user=temp[0];

            final String userEnteredUsername = username.getText().toString().toLowerCase().trim();;
            final String userEnteredPassword = password.getText().toString().trim();
        String[] temp=userEnteredUsername.split("\\.");
        final String u_without=temp[0];

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            Query checkUser = reference.orderByChild("email").equalTo(userEnteredUsername);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        username.setError(null);
                      //  username.setErrorEnabled(false);
                        String passwordFromDB = dataSnapshot.child(u_without).child("password").getValue(String.class);
                        if (passwordFromDB.equals(userEnteredPassword)) {
                            username.setError(null);
                          //  username.setErrorEnabled(false);
                            MyHelper helper=new MyHelper(getApplicationContext());
                            SQLiteDatabase database=helper.getWritableDatabase();
                            ContentValues values=new ContentValues();


                            values.put("USER_NAME",u_without);
                            values.put("PASSWORD",userEnteredPassword);
                            values.put("STATUS","true");
                            database.update("USER",values,"id=?",new String[]{"1"});

//
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            password.setError("Wrong Password");
                            password.requestFocus();
                        }
                    } else {
                        username.setError("No such User exist");
                        username.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


