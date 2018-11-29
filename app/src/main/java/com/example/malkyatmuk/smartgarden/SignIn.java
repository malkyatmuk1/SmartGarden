package com.example.malkyatmuk.smartgarden;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by malkyatmuk on 11/12/18.
 */


public class SignIn extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    TextView forgotPasswordTextView, createNewAccountTextView;
    Button signInButton;

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

        usernameEditText= (EditText) findViewById(R.id.username);
        passwordEditText= (EditText) findViewById(R.id.password);
        forgotPasswordTextView= (TextView) findViewById(R.id.forgotpassword);
        createNewAccountTextView= (TextView) findViewById(R.id.createnewaccount);
        signInButton=(Button) findViewById(R.id.SignInButton);
        signInButton.setOnClickListener(NextButtonListener);
    }
    View.OnClickListener NextButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), Start_menu.class);
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener SignInButtonListener = new View.OnClickListener() {
        public void onClick(final View v) {
            Global.username = usernameEditText.getText().toString();
            Global.password = passwordEditText.getText().toString();
            if(!Global.users.contains(usernameEditText.getText().toString()))Global.users.add(usernameEditText.getText().toString());

            if (true) {
                SERVER_IP=Global.directip;
                //Global.setIP(Global.directip, getApplicationContext());
               //Global.setIP(Global.directip, getApplicationContext());
            } else {
                if (Global.ip.isEmpty()) {
                    //  WifiDialog wifidialog=new WifiDialog();
                    // wifidialog.show(getFragmentManager(),"wifi");
                    //TODO
                }
                SERVER_IP=Global.ip;
                Global.setIP(Global.ip, getApplicationContext());
            }

            thr = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InetAddress ip = InetAddress.getByName(SERVER_IP);
                        //Thread.sleep(2000);
                        clientSocket = new Socket(ip, SERVERPORT);
                        send = "signin " + usernameEditText.getText() + " " + passwordEditText.getText() + '\n';

                        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        outToServer.writeBytes(send);
                        outToServer.flush();
                        modifiedSentence = inFromServer.readLine();
                        if (!modifiedSentence.equals("errorsignin") && !modifiedSentence.equals("error")) {
                            Intent intent = new Intent(getApplicationContext(), Start_menu.class);
                            Global.permission = modifiedSentence.charAt(0);
                            Global.username = usernameEditText.getText().toString();
                            Global.password = passwordEditText.getText().toString();

                            startActivity(intent);
                            finish();
                        }
                        clientSocket.close();
                    } catch (IOException e) {
                        System.out.println("Exception " + e);
                    }
                    return;
                }
            });
            thr.start();

            thr.interrupt();
        }

    };

}

