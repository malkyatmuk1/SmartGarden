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

/**
 * Created by malkyatmuk on 11/12/18.
 */


public class View_Plant extends Activity {
    EditText namePlantEditText,pouringTimesEditText;
    TextView humidityTextView,temperatureTextView;
    Button saveInfoButton;
    ImageButton backButton,pouringButton;
    AutoCompleteTextView actv,pouringTypeActv;

    String[] language ={"C","C++","Java",".NET","iPhone","Android","ASP.NET","PHP"};
    String[] pouringTypes={"daily","weekly","monthly"};
    String colorString="blue";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plant);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,language);
        //Getting the instance of AutoCompleteTextView
        actv= (AutoCompleteTextView)findViewById(R.id.plantType);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.BLUE);


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,pouringTypes);
        //Getting the instance of AutoCompleteTextView
        pouringTypeActv= (AutoCompleteTextView)findViewById(R.id.pouringType);
        pouringTypeActv.setThreshold(1);//will start working from first character
        pouringTypeActv.setAdapter(adapter1);//setting the adapter data into the AutoCompleteTextView
        pouringTypeActv.setTextColor(Color.parseColor(colorString));


        namePlantEditText = (EditText) findViewById(R.id.plantName);
        pouringTimesEditText = (EditText) findViewById(R.id.plantPouringTimes);
        temperatureTextView = (TextView) findViewById(R.id.plantTemperature);
        humidityTextView = (TextView) findViewById(R.id.plantHumidity);
        saveInfoButton=(Button) findViewById(R.id.saveInfo);
        backButton=(ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(BackButtonListener);
        pouringButton=(ImageButton)findViewById(R.id.pouringInfoButton);
        pouringButton.setOnClickListener(PouringInfoListener);
        saveInfoButton.setOnClickListener(SaveInfoListener);

        String pour=String.valueOf(Global.myPlants.get(Global.indexOfPlant).pouring);
        String temp=String.valueOf(Global.temperature);
        String hum=String.valueOf(Global.humidity);
        namePlantEditText.setText(Global.myPlants.get(Global.indexOfPlant).namePlant);
        actv.setText(Global.myPlants.get(Global.indexOfPlant).type);
        pouringTypeActv.setText(Global.myPlants.get(Global.indexOfPlant).pouringType);
        pouringTimesEditText.setText(pour);
        temperatureTextView.setText(temp+" °C");
        humidityTextView.setText(hum+" HD");
    }
    View.OnClickListener PouringInfoListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), Pouring_Info.class);
            Global.fromView=true;
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener BackButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), Start_menu.class);
            Global.fromView=true;
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener SaveInfoListener=new View.OnClickListener() {

        public void onClick(View view) {
            Global.myPlants.get(Global.indexOfPlant).namePlant=namePlantEditText.getText().toString();
            Global.myPlants.get(Global.indexOfPlant).pouringType=pouringTypeActv.getText().toString();
            String s=pouringTimesEditText.getText().toString();
            boolean flag=true;
            for(int i=0;i<s.length();i++)
            {
                if(s.charAt(i)-'0'>=0 && s.charAt(i)-'0'<=9) {
                    continue;
                }
                else {
                    flag=false;
                    break;
                }
            }
            if(flag==true) Global.myPlants.get(Global.indexOfPlant).pouring= Integer.parseInt(pouringTimesEditText.getText().toString());
            Global.myPlants.get(Global.indexOfPlant).type=actv.getText().toString();
            Global.fromView=true;
            Intent intent = new Intent(view.getContext(), Start_menu.class);
            startActivity(intent);
            finish();
        }
    };
}



