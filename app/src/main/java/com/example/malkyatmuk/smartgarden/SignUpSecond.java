package com.example.malkyatmuk.smartgarden;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class SignUpSecond extends AppCompatActivity {
    ImageButton backButton;
    Button signUpButton;
    TextView signedUp;
    EditText usernameEditText, passwordEditText, secondpassEditText;

    Thread thr;
    private Socket clientSocket;
    public String modifiedSentence;
    private static final int SERVERPORT = 3030;
    private static String SERVER_IP="46.35.165.136";
    public String send;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_second);
        usernameEditText=(EditText) findViewById(R.id.usernameEditText);
        passwordEditText =(EditText) findViewById(R.id.firstpassEditText);
        secondpassEditText= (EditText) findViewById(R.id.secondpassEditText);
        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(BackButtonListener);
        signUpButton= (Button) findViewById(R.id.signupButton);
        signUpButton.setOnClickListener(signupButtonListener);
    }
    View.OnClickListener AlreadySignedUp=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), SignIn.class);
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener BackButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), SignUp.class);
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener signupButtonListener= new View.OnClickListener(){

        public void onClick(View view){
             new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        //Thread.sleep(2000);
                        clientSocket = new Socket(Global.ip, SERVERPORT);
                        send = "signup " + usernameEditText.getText() + " " + passwordEditText.getText() + '\n';

                        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        outToServer.writeBytes(send);
                        outToServer.flush();
                        modifiedSentence = inFromServer.readLine();
                        Log.d("tanq",modifiedSentence);
                        if (modifiedSentence.equals("truesignup") && !modifiedSentence.equals("error")) {
                            Intent intent = new Intent(getApplicationContext(), Start_menu.class);
                            Global.permission = modifiedSentence.charAt(0);
                            Global.username = usernameEditText.getText().toString();
                            Global.password = passwordEditText.getText().toString();

                            startActivity(intent);
                            finish();
                        }
                        else if(modifiedSentence.equals("thereIsAPerson"))
                        {
                           usernameEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                           usernameEditText.setHint("There is a person with this username");
                        }
                        else
                        {
                            usernameEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                            passwordEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                            usernameEditText.setHint("Sorry, no free space");
                        }
                        clientSocket.close();
                    }
                    catch (IOException e) {
                        System.out.println("Exception " + e);
                    }
                }
            }).start();
        }
    };

}
