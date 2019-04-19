package com.example.malkyatmuk.smartgarden;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Pouring_Info extends Activity {

    ImageButton backButton;
    TextView usedWater,nextPouring,pouringTimes;
    Button saveInfoButton;
    String currentTime,pour,pourType;
    CheckBox autoPouring;
    public void init()
    {
        backButton = (ImageButton) findViewById(R.id.backButtonPouringInfo);
        pouringTimes = (TextView) findViewById(R.id.plantPouringTimes);
        usedWater = (TextView) findViewById(R.id.plantUsedWater);
        saveInfoButton = (Button) findViewById(R.id.saveInfoPlantInfo);
        nextPouring= (TextView) findViewById(R.id.nextPouring);
        autoPouring=(CheckBox)findViewById(R.id.autoPouring);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pouring_info);

        init();

        if(autoPouring.isChecked()) {
            //TODO
        }

        String usedWaterValue = String.valueOf(Global.usedWater);
        pour=String.valueOf(Global.myPlants.get(Global.indexOfPlant).pouring);
        pourType=Global.myPlants.get(Global.indexOfPlant).pouringType;
        String lastPouring=String.valueOf(Global.myPlants.get(Global.indexOfPlant).lastPoured);
        usedWater.setText(usedWaterValue + " l");
        pouringTimes.setText(pour+" pouring times "+pourType);
        nextPouring.setText(Global.myPlants.get(Global.indexOfPlant).nextPouring);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        currentTime = mdformat.format(calendar.getTime());

        // last poured->to seconds;
        String lP=Global.myPlants.get(Global.indexOfPlant).lastPoured;
        String c=""+lP.charAt(0);
        int secondsLastPoured=Integer.parseInt(c)*36000;
        c=""+lP.charAt(1);
        secondsLastPoured+=Integer.parseInt(c)*3600;
        c=""+lP.charAt(3);
        secondsLastPoured+=Integer.parseInt(c)*600;
        c=""+lP.charAt(4);
        secondsLastPoured+=Integer.parseInt(c)*60;
        c=""+lP.charAt(6);
        secondsLastPoured+=Integer.parseInt(c)*10;
        c=""+lP.charAt(7);
        secondsLastPoured+=Integer.parseInt(c);

        //currentTime-> to seconds;
        lP=currentTime;
        c=""+lP.charAt(0);
        int secondsCurrentTime=Integer.parseInt(c)*36000;
        c=""+lP.charAt(1);
        secondsCurrentTime+=Integer.parseInt(c)*3600;
        c=""+lP.charAt(3);
        secondsCurrentTime+=Integer.parseInt(c)*600;
        c=""+lP.charAt(4);
        secondsCurrentTime+=Integer.parseInt(c)*60;
        c=""+lP.charAt(6);
        secondsCurrentTime+=Integer.parseInt(c)*10;
        c=""+lP.charAt(7);
        secondsCurrentTime+=Integer.parseInt(c);

        //preminato 24 chasa
        if(secondsCurrentTime<secondsLastPoured)secondsCurrentTime+=24*3600;


        double pouringPeriod=((double)(24.0/(Integer.parseInt(pour))))*3600;

        if(secondsCurrentTime-secondsLastPoured>=pouringPeriod) {
            if(secondsCurrentTime>=24*3600)secondsCurrentTime-=24*3600;
            int next=(int)pouringPeriod+secondsCurrentTime;
            if(next>=24*3600)next-=24*3600;
            String nextPouringTime=String.valueOf(next/36000);
            next-=(next/36000)*36000;
            nextPouringTime+=String.valueOf(next/3600)+":";
            next-=(next/3600)*3600;
            nextPouringTime+=String.valueOf(next/600);
            next-=(next/600)*600;
            nextPouringTime+=String.valueOf(next/60)+":";
            next-=(next/60)*60;
            nextPouringTime+=String.valueOf(next/10);
            next-=(next/10)*10;
            nextPouringTime+=String.valueOf(next%10);
            nextPouring.setText(nextPouringTime);
            Global.myPlants.get(Global.indexOfPlant).nextPouring=nextPouringTime;
            Global.myPlants.get(Global.indexOfPlant).lastPoured=currentTime;
        }
        backButton.setOnClickListener(BackButtonListener);
        saveInfoButton.setOnClickListener(SaveInfoListener);
    }
    View.OnClickListener BackButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), View_Plant.class);
            startActivity(intent);
            finish();
        }
    };
    public void Pouring()
    {
        //TODO ako e bilo polivano tvurde skoro -> ne
        //TODO pouring
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        currentTime = mdformat.format(calendar.getTime());
        String lP=currentTime;
        String c="";
        c=""+lP.charAt(0);
        int secondsCurrentTime=Integer.parseInt(c)*36000;
        c=""+lP.charAt(1);
        secondsCurrentTime+=Integer.parseInt(c)*3600;
        c=""+lP.charAt(3);
        secondsCurrentTime+=Integer.parseInt(c)*600;
        c=""+lP.charAt(4);
        secondsCurrentTime+=Integer.parseInt(c)*60;
        c=""+lP.charAt(6);
        secondsCurrentTime+=Integer.parseInt(c)*10;
        c=""+lP.charAt(7);
        secondsCurrentTime+=Integer.parseInt(c);

        double pouringPeriod=((double)(24.0/(Integer.parseInt(pour))))*3600;

        int next=(int)pouringPeriod+secondsCurrentTime;
        if(next>=24*3600)next-=24*3600;
        String nextPouringTime=String.valueOf(next/36000);
        next-=(next/36000)*36000;
        nextPouringTime+=String.valueOf(next/3600)+":";
        next-=(next/3600)*3600;
        nextPouringTime+=String.valueOf(next/600);
        next-=(next/600)*600;
        nextPouringTime+=String.valueOf(next/60)+":";
        next-=(next/60)*60;
        nextPouringTime+=String.valueOf(next/10);
        next-=(next/10)*10;
        nextPouringTime+=String.valueOf(next%10);
        nextPouring.setText(nextPouringTime);
        Global.myPlants.get(Global.indexOfPlant).nextPouring=nextPouringTime;
        Global.myPlants.get(Global.indexOfPlant).lastPoured=currentTime;
    }
    View.OnClickListener SaveInfoListener=new View.OnClickListener() {

        public void onClick(View view) {
            Pouring();
            //TODO polivane
        }
    };
}



