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

/**
 * Created by malkyatmuk on 11/12/18.
 */


public class View_Plant extends Activity {
    EditText namePlantEditText,pouringTimesEditText,humidityEditText,temperatureEditText;
    Button saveInfoButton;
    ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plant);
        namePlantEditText = (EditText) findViewById(R.id.plantName);
        pouringTimesEditText = (EditText) findViewById(R.id.plantPouringTimes);
        temperatureEditText = (EditText) findViewById(R.id.plantTemperature);
        humidityEditText = (EditText) findViewById(R.id.plantHumidity);
        saveInfoButton=(Button) findViewById(R.id.saveInfo);
        backButton=(ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(BackButtonListener);
        saveInfoButton.setOnClickListener(SaveInfoListener);
        String pour=String.valueOf(Global.myPlants[Global.indexOfPlant].pouring);
        String temp=String.valueOf(Global.myPlants[Global.indexOfPlant].temperature);
        String hum=String.valueOf(Global.myPlants[Global.indexOfPlant].humidity);
        namePlantEditText.setText(Global.myPlants[Global.indexOfPlant].namePlant);
        pouringTimesEditText.setText(pour);
        temperatureEditText.setText(temp);
        humidityEditText.setText(hum);
    }
    View.OnClickListener BackButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), Start_menu.class);
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener SaveInfoListener=new View.OnClickListener() {

        public void onClick(View view) {
            Global.myPlants[Global.indexOfPlant].namePlant=namePlantEditText.getText().toString();
            Global.myPlants[Global.indexOfPlant].pouring= Integer.parseInt(pouringTimesEditText.getText().toString());
            Global.myPlants[Global.indexOfPlant].temperature=Double.parseDouble(temperatureEditText.getText().toString());
            Global.myPlants[Global.indexOfPlant].humidity=Double.parseDouble(humidityEditText.getText().toString());
            Intent intent = new Intent(view.getContext(), View_Plant.class);
            startActivity(intent);
            finish();
        }
    };
}



