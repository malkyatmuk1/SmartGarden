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
    EditText pouringTimes;
    TextView usedWater,nextPouring;
    Button saveInfoButton;
    String currentTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pouring_info);
        backButton = (ImageButton) findViewById(R.id.backButtonPouringInfo);
        pouringTimes = (EditText) findViewById(R.id.plantPouringTimes);
        usedWater = (TextView) findViewById(R.id.plantUsedWater);
        saveInfoButton = (Button) findViewById(R.id.saveInfoPlantInfo);
        nextPouring= (TextView) findViewById(R.id.nextPouring);
        String usedWaterValue = String.valueOf(Global.usedWater);
        String pour=String.valueOf(Global.myPlants.get(Global.indexOfPlant).pouring);
        String lastPouring=String.valueOf(Global.myPlants.get(Global.indexOfPlant).lastPoured);
        usedWater.setText(usedWaterValue + " l");
        pouringTimes.setText(pour);


        /*Date time;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            time = dateFormat.parse(lastPouring);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        currentTime = mdformat.format(calendar.getTime());
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

        c=""+lP.charAt(0);
        lP=currentTime;
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

        if(secondsCurrentTime<secondsLastPoured)secondsCurrentTime+=24*3600;
        double pouringPeriod=((double)(24.0/(Integer.parseInt(pour))))*3600;
        if(secondsCurrentTime-secondsLastPoured>=pouringPeriod) {
            if(secondsCurrentTime>=24*3600)secondsCurrentTime-=24*3600;
            int next=(int)pouringPeriod+secondsCurrentTime;
            String nextPouringTime=String.valueOf(next/36000)+String.valueOf(next/3600%10)+":"+String.valueOf(next/600%10)+String.valueOf(next/60%10)+":"+String.valueOf(next/10%10)+String.valueOf(next%10);
            nextPouring.setText(nextPouringTime);
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
    View.OnClickListener SaveInfoListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), Start_menu.class);
            startActivity(intent);
            finish();
        }
    };
}



