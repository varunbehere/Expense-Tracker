package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      getSupportActionBar().hide();
     // startActivity(new Intent(getApplicationContext(),MainActivity.class));finish();
        drawerLayout=findViewById(R.id.draw_layout);
        toolbar= findViewById(R.id.toolbar);
        navigationView=findViewById(R.id.nav_view);
       navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.open_Drawebal,R.string.CLOSE_DRAWEBLE);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
   }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_help:
                startActivity(new Intent(getApplicationContext(),HelpActivity.class));
                break;
            case R.id.menu_logout:
                MyHelper helper=new MyHelper(getApplicationContext());
                SQLiteDatabase database=helper.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("USER_NAME","null");
                values.put("PASSWORD","null");
                values.put("STATUS","false");
                database.update("USER",values,"id=?",new String[]{"1"});
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.menu_user_Profile:
                startActivity(new Intent(getApplicationContext(),userProfile.class));
                break;
            case R.id.menu_monthly:
                startActivity(new Intent(getApplicationContext(),Month.class));
                break;


        }
        return true;
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }


    public void setBugetButtonClick(View view) {
        startActivity(new Intent(getApplicationContext(),Budget.class));
    }

    public void saving_button(View view) {
        startActivity(new Intent(getApplicationContext(),Savings.class));
    }

    public void transportationHistory(View view) {
        startActivity(new Intent(getApplicationContext(),TransporttationHistory.class));
    }

    public void spendingButton(View view) {
        startActivity(new Intent(getApplicationContext(),Spending.class));
    }

    public void ExpanceINButton(View view) {
        startActivity(new Intent(getApplicationContext(),ExpenseIn.class));
    }

    public void ExpanceOUTButton(View view) {
        startActivity(new Intent(getApplicationContext(),ExpenseOut.class));
    }
}