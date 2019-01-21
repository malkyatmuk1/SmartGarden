package com.example.malkyatmuk.smartgarden;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class SignUpSecond extends Activity {
    ImageButton backButton;
    Button signUpButton;
    TextView signedUp;
    EditText usernameEditText;

    TextInputEditText passwordEditText, secondpassEditText;
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
        passwordEditText =(TextInputEditText) findViewById(R.id.firstpassEditText);
        secondpassEditText= (TextInputEditText) findViewById(R.id.secondpassEditText);
        passwordEditText.addTextChangedListener(textWatcherPass);
        secondpassEditText.addTextChangedListener(textWatcherPassAgain);
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
    TextWatcher textWatcherPass= new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {

           String txt = passwordEditText.getText().toString();
           String txt2 = secondpassEditText.getText().toString();

            if (passwordEditText.length() < 5)
            {
                passwordEditText.setHint("Your password is too short!");
            }
            if(passwordEditText.length()<5)
                passwordEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            else {passwordEditText.setBackgroundResource(R.drawable.edittext);passwordEditText.setHint("Password");}
            if(txt.length()==0)
            {
                passwordEditText.setHint("Password");
                passwordEditText.setBackgroundResource(R.drawable.edittext);
                secondpassEditText.setBackgroundResource(R.drawable.edittext);
            }
        }
    };
    TextWatcher textWatcherPassAgain = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable arg0) {
            String txt = passwordEditText.getText().toString();
            String txt2 = secondpassEditText.getText().toString();

            if (!txt2.equals(txt)) {
                secondpassEditText.setHint("The two passwords don't match!");
                passwordEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                secondpassEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

            } else if(txt.equals(txt2) && txt.length()>=5 && txt2.length()>=5) {
                secondpassEditText.setHint("The two passwords match!");
                passwordEditText.setBackgroundResource(R.drawable.edittext);
                secondpassEditText.setBackgroundResource(R.drawable.edittext);
            }


            if(txt2.length()==0) {
                secondpassEditText.setHint("Rewrite your password");
                passwordEditText.setBackgroundResource(R.drawable.edittext);
                secondpassEditText.setBackgroundResource(R.drawable.edittext);
            }
        }
    };
    View.OnClickListener signupButtonListener= new View.OnClickListener(){

        public void onClick(final View view){
            Log.d("tanq","predi");
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
                    return;
                }
            }).start();
        }
    };
}
