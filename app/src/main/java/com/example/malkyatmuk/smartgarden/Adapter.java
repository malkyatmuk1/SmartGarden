package com.example.malkyatmuk.smartgarden;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by malkyatmuk on 10/24/17.
 */


public class Adapter extends BaseAdapter {
    private Context mContext;
    private  ArrayList<Plants> mList;
    private String[] spliter;
    private String modif;

    public Adapter(Context context, ArrayList<Plants> list){
        mContext=context;
        mList= list;
    }

    @Override
    public int getCount() {
     return    mList.size();
    }

    @Override
    public Object getItem(int position) {
       return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        //use convertView recycle
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.card, parent, false);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.PlantImage = (ImageView) convertView.findViewById(R.id.image);
        holder.plantName = (TextView) convertView.findViewById(R.id.name);
        holder.ViewButton = (Button) convertView.findViewById(R.id.button_view);
        holder.DellButton = (Button) convertView.findViewById(R.id.button_dell);

        Plants pl = (Plants) this.getItem(position);

        holder.plantName.setText(pl.getName());
        holder.PlantImage.setBackgroundResource(R.drawable.plant);
        holder.ViewButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Global.indexOfPlant=position;
                Intent intent = new Intent(mContext, View_Plant.class);
                mContext.startActivity(intent);

            }

        });
        holder.DellButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                notifyDataSetChanged();
                mList.remove(position);

            }

        });
        return convertView;

    }

        class ViewHolder {
            TextView plantName;
            Button ViewButton;
            Button DellButton;
            ImageView PlantImage;
        }

}