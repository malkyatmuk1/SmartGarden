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



public class Pouring_Info extends Activity {

    ImageButton backButton;
    EditText pouringTimes;
    TextView usedWater;
    Button saveInfoButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pouring_info);
        backButton=(ImageButton) findViewById(R.id.backButtonPouringInfo);
        pouringTimes=(EditText)findViewById(R.id.plantPouringTimes);
        usedWater=(TextView)findViewById(R.id.plantUsedWater);
        saveInfoButton=(Button)findViewById(R.id.saveInfoPlantInfo);

        String usedWaterValue=String.valueOf(Global.usedWater);
        usedWater.setText(usedWaterValue+" l");
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



