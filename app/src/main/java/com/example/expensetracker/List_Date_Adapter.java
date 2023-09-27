package com.example.expensetracker;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
public class List_Date_Adapter extends ArrayAdapter<String> {
    ArrayList<String> list_date;
    ArrayList<String> list_time;
    String user,expanse,year,month;
Activity activity;
    public  List_Date_Adapter(Activity activity, ArrayList<String> list_date,
                              ArrayList<String> list_time,String user,String expanse,String year,String month){

        super(activity,R.layout.date_time_view,list_date);
        this.activity=activity;
        this.list_date=list_date;
        this.list_time=list_time;
        this.user=user;
        this.expanse=expanse;
        this.year=year;
        this.month=month;
    }
    class ViewHolder{

        TextView list_date_tv,list_time_tv;
        CardView cardView;

        ViewHolder(View v){
            list_date_tv=(TextView) v.findViewById(R.id.id_list_date);
            list_time_tv=(TextView) v.findViewById(R.id.id_list_time);
            cardView=(CardView)v.findViewById(R.id.id_date_time_singlerow);
           // delete_tv=(TextView) v.findViewById(R.id.id_list_delete);


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  Intent intent=new Intent(view.getContext(),DisplayHistory.class);

                    intent.putExtra("MONTH",month);
                    intent.putExtra("YEAR",year);
                    intent.putExtra("EXPANSE",expanse);
                    intent.putExtra("USERNAME",user);
                    intent.putExtra("DATE",""+((TextView)view.findViewById(R.id.id_list_date)).getText().toString());
                    intent.putExtra("TIME",""+((TextView)view.findViewById(R.id.id_list_time)).getText().toString());
                  view.getContext().startActivity(intent);

                }
            });
        }
    }
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r=convertView;
        ViewHolder viewHolder=null;
        if(r==null){
            LayoutInflater layoutInflater=activity.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.date_time_view,null,true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder) r.getTag();
        }
        viewHolder.list_date_tv.setText(list_date.get(position));
        viewHolder.list_time_tv.setText(list_time.get(position));

        return  r;
    }

}
