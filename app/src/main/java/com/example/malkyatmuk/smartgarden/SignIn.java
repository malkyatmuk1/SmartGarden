package com.example.malkyatmuk.smartgarden;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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


public class SignIn extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    TextView createNewAccountTextView, incorrectUserOrPass,wifi;
    Button signInButton;
    ImageButton backButton,ipButton;
    SharedPreferences sp;
    Thread thr;
    private Socket clientSocket;
    public String modifiedSentence;
    private static final int SERVERPORT = 3030;
    private static String SERVER_IP;
    public String send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        sp= getSharedPreferences("login",Context.MODE_PRIVATE);
        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        createNewAccountTextView = (TextView) findViewById(R.id.createNewAccount);
        signInButton = (Button) findViewById(R.id.SignInButton);
        signInButton.setOnClickListener(SignInButtonListener);
        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(BackButtonListener);
        ipButton = (ImageButton) findViewById(R.id.ipButton);
        ipButton.setOnClickListener(IPButtonListener);
        createNewAccountTextView = (TextView) findViewById(R.id.createNewAccount);
        createNewAccountTextView.setOnClickListener(CreateNewAccountListener);
        incorrectUserOrPass=(TextView) findViewById(R.id.incorrectUserOrPass);
        wifi=(TextView)findViewById(R.id.wifiSettings);
        wifi.setOnClickListener(WifiSettingsListener);
    }
    View.OnClickListener WifiSettingsListener=new View.OnClickListener() {

        public void onClick(View view) {
            Global.goback=true;
            Intent intent = new Intent(view.getContext(), WifiSettings.class);
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener CreateNewAccountListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), SignUp.class);
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener BackButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), UPorIn.class);
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener IPButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            Global.goback=true;
            WifiDialog wifidialog=new WifiDialog();
            wifidialog.show(getFragmentManager(),"wifi");
        }
    };
    View.OnClickListener SignInButtonListener = new View.OnClickListener() {
        public void onClick(final View v) {

            Global.username = usernameEditText.getText().toString();
            Global.password = passwordEditText.getText().toString();
            if (!Global.users.contains(usernameEditText.getText().toString()))
                Global.users.add(usernameEditText.getText().toString());


            if (true) {
                SERVER_IP = Global.directip;
            } else {
                if (Global.ip.isEmpty()) {
                    //  WifiDialog wifidialog=new WifiDialog();
                    // wifidialog.show(getFragmentManager(),"wifi");
                    //TODO
                }
                SERVER_IP = Global.ip;
                Global.setIP(Global.ip, getApplicationContext());
            }

            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        clientSocket = new Socket(Global.ip, SERVERPORT);
                                             send = "signin " + usernameEditText.getText() + " " + passwordEditText.getText() + '\n';

                        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        outToServer.writeBytes(send);
                        outToServer.flush();
                        modifiedSentence=inFromServer.readLine();

                        if (modifiedSentence.equals("IncorrectPass")){
                            incorrectUserOrPass.post(new Runnable() {
                                public void run() {
                                    incorrectUserOrPass.setText("Incorrect username or password");
                                }
                            });
                            usernameEditText.post(new Runnable() {
                                public void run() {
                                    usernameEditText.setText("");
                                }
                            });
                            passwordEditText.post(new Runnable() {
                                public void run() {
                                    passwordEditText.setText("");
                                }
                            });

                        } else if (modifiedSentence.equals("NoPermission")) {
                            Intent intent = new Intent(getApplicationContext(), Start_menu.class);
                            Global.signedIn=true;
                            Global.permission = 'n';
                            Global.username = usernameEditText.getText().toString();
                            Global.password = passwordEditText.getText().toString();
                            sp.edit().putBoolean("logged",true).apply();
                            sp.edit().putString("username",Global.username).apply();
                            sp.edit().putString("password",Global.password).apply();
                            sp.edit().putString("permission","n").apply();
                            startActivity(intent);
                            finish();

                        } else if (modifiedSentence.equals("a") || modifiedSentence.equals("u")) {
                            Intent intent = new Intent(getApplicationContext(), Start_menu.class);
                            Global.signedIn=true;

                            Global.permission = modifiedSentence.charAt(0);
                            Global.username = usernameEditText.getText().toString();
                            Global.password = passwordEditText.getText().toString();
                            sp.edit().putBoolean("logged",true).apply();
                            sp.edit().putString("username",Global.username).apply();
                            sp.edit().putString("password",Global.password).apply();
                            if(modifiedSentence.equals("a"))
                            {
                                sp.edit().putString("permission", "a").apply();
                            }
                            else {
                                sp.edit().putString("permission","u").apply();
                            }
                            startActivity(intent);
                            finish();
                        }
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



