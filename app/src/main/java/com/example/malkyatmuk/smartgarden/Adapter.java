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

/**
 * Created by malkyatmuk on 10/24/17.
 */


public class Adapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mList;
    private String[] spliter;
    private String modif;

    public Adapter(Context context,ArrayList<String> list){
        mContext=context;
        mList=list;
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
            holder.PlantImage = (ImageView) convertView.findViewById(R.id.image);
            holder.plantName = (TextView) convertView.findViewById(R.id.name);
            holder.plantName.setText(mList.get(position));
            holder.ViewButton = (Button) convertView.findViewById(R.id.button_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.ViewButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }

        });
        return convertView;
    }

        class ViewHolder {
            TextView plantName;
            Button ViewButton;
            ImageView PlantImage;
        }

}