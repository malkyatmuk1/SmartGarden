package com.example.malkyatmuk.smartgarden;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
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
import android.widget.Toast;

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
    public TextView signedUp,infoTextView;
    EditText usernameEditText, firstpassEditText,secondpassEditText;

    TextInputLayout firstpassLayout,secondpassLayout,usernameLayout;
    Thread thr;
    private Socket clientSocket;
    public String modifiedSentence;
    private static final int SERVERPORT = 3030;
    private static String SERVER_IP="46.35.165.136";
    public String send;
    boolean flag=false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_second);
        infoTextView= (TextView) findViewById(R.id.info);
        signedUp=(TextView) findViewById(R.id.signedUp);
        signedUp.setOnClickListener(AlreadySignedUp);
        usernameEditText=(EditText) findViewById(R.id.usernameEditText);
        firstpassEditText =(EditText) findViewById(R.id.firstpassEditText);
        secondpassEditText= (EditText) findViewById(R.id.secondpassEditText);
        firstpassLayout=(TextInputLayout) findViewById(R.id.firstpassLayout);
        secondpassLayout=(TextInputLayout) findViewById(R.id.secondpassLayout);
        usernameLayout=(TextInputLayout) findViewById(R.id.usernameLayout);
        firstpassEditText.addTextChangedListener(textWatcherFirstPass);
        secondpassEditText.addTextChangedListener(textWatcherSecondPass);

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

    TextWatcher textWatcherSecondPass= new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            String txt = firstpassEditText.getText().toString();
            String txt2 = secondpassEditText.getText().toString();

            if(txt2.length()==0) {
                secondpassLayout.setHint("Rewrite your password");
                secondpassEditText.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext));
                flag=true;
            }
            else if (!txt2.equals(txt))
            {
                secondpassLayout.setHint("The two passwords don't match!");
                secondpassEditText.getBackground().setColorFilter(Color.RED,PorterDuff.Mode.SRC_ATOP);
                flag=false;
            }
            else
            {
                secondpassLayout.setHint("The two passwords match!");
                secondpassEditText.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext));
                flag=true;
            }
        }
    };
    TextWatcher textWatcherFirstPass= new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            String txt = firstpassEditText.getText().toString();
            String txt2 = secondpassEditText.getText().toString();

            if (txt.length() < 5 && txt.length()>0)
            {
                firstpassLayout.setHint("Your password is too short!");
                firstpassEditText.getBackground().setColorFilter(Color.RED,PorterDuff.Mode.SRC_ATOP);
                flag=false;
            }
            else
            {
                firstpassLayout.setHint("Password");
                firstpassEditText.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext));
                flag=true;
            }

            if(txt2.length()==0) {
                secondpassLayout.setHint("Rewrite your password");
                secondpassEditText.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext));
                flag=true;
            }
            else if (!txt2.equals(txt))
            {
                secondpassLayout.setHint("The two passwords don't match!");
                secondpassEditText.getBackground().setColorFilter(Color.RED,PorterDuff.Mode.SRC_ATOP);
                flag=false;
            }
            else
            {
                secondpassLayout.setHint("The two passwords match!");
                secondpassEditText.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext));
                flag=true;
            }
        }
    };

    View.OnClickListener signupButtonListener= new View.OnClickListener(){

        public void onClick(final View view){
              new Thread(new Runnable() {

                @Override
                public void run() {
                    try {

                        //Thread.sleep(2000);
                        clientSocket = new Socket(Global.ip, SERVERPORT);
                        send = "signup " + usernameEditText.getText() + " " + firstpassEditText.getText() + '\n';

                        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        if(flag) {
                            outToServer.writeBytes(send);
                            outToServer.flush();
                        }
                        modifiedSentence = inFromServer.readLine();
                        Log.d("tanq",modifiedSentence);
                        if (modifiedSentence.equals("truesignup") && !modifiedSentence.equals("error")) {
                            Intent intent = new Intent(getApplicationContext(), Start_menu.class);
                            Global.permission = modifiedSentence.charAt(0);
                            Global.username = usernameEditText.getText().toString();
                            Global.password = firstpassEditText.getText().toString();
                            startActivity(intent);
                            finish();
                        }
                       else if(modifiedSentence.equals("thereIsAPerson"))
                        {
                            infoTextView.post(new Runnable() {
                                public void run() {
                                    infoTextView.setText("There is a person with this name!");
                                }
                            });
                        }
                        else
                        {
                            infoTextView.post(new Runnable() {
                                public void run() {
                                    infoTextView.setText("Sorry, no free space");
                                }
                            });
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
