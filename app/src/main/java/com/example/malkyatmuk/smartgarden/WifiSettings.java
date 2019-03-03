package com.example.malkyatmuk.smartgarden;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
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

public class WifiSettings extends Activity {
    EditText wifiPassEditText,wifiIdEditText,passwordEditText,usernameEditText;
    Button applyButton;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_settings);
        wifiPassEditText=(EditText) findViewById(R.id.wifiPass);
        wifiIdEditText=(EditText) findViewById(R.id.wifiId);
        passwordEditText=(EditText) findViewById(R.id.password);
        usernameEditText=(EditText) findViewById(R.id.username);
        applyButton=(Button) findViewById(R.id.applyButton);
        applyButton.setOnClickListener(ApplyButtonListener);
        backButton=(ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(BackButtonListener);

    }
    View.OnClickListener BackButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), UPorIn.class);
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener ApplyButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), SignIn.class);
            startActivity(intent);
            finish();
        }
    };
}