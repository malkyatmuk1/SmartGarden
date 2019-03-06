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
    Socket clientSocket;
    public String send,modifiedSentence;
    private static final int SERVERPORT = 3030;

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
            if(Global.goback==true) {
                Intent intent = new Intent(view.getContext(), SignIn.class);
                startActivity(intent);
                finish();
            }
            else {
                Intent intent = new Intent(view.getContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        }
    };
    View.OnClickListener ApplyButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            final String wifiName,wifiPass,pass,username;
            wifiName=wifiIdEditText.getText().toString();
            wifiPass=wifiPassEditText.getText().toString();
            pass=passwordEditText.getText().toString();
            username=usernameEditText.getText().toString();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        //Log.d("dani","tuk");
                        //Thread.sleep(2000);
                        clientSocket = new Socket(Global.ip, SERVERPORT);
                        /*
                                    send = "signin " + usernameEditText.getText() + " " + passwordEditText.getText() + '\n';
                                    incorrectUserOrPass.setText(send);
                                    incorrectUserOrPass.setVisibility(View.VISIBLE);
                            this put here force the program to shut down after clicking on signin button
                         */
                        send = "setWifi " + wifiName + " " + wifiPass + " " + username + " " + pass + '\n';

                        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        outToServer.writeBytes(send);
                        outToServer.flush();

                        clientSocket.close();
                    } catch (IOException e) {
                        System.out.println("Exception " + e);
                    }
                    return;
                }
            }).start();
        }
    };
}