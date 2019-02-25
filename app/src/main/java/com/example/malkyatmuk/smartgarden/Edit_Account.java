package com.example.malkyatmuk.smartgarden;

import android.app.Activity;
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

public class Edit_Account extends Activity {
    EditText oldPass,newPass,confPass;
    Button changePass;
    TextView cancelChanging,noMatch;
    ImageButton backButton;
    Thread thr;
    private Socket clientSocket;
    public String oldPassw,newPassw,confPassw;
    public String modifiedSentence;
    private static final int SERVERPORT = 3030;
    private static String SERVER_IP;
    public String send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        oldPass=(EditText) findViewById(R.id.oldPassword);
        newPass=(EditText) findViewById(R.id.passwordOne);
        confPass=(EditText) findViewById(R.id.passwordTwo);
        changePass=(Button) findViewById(R.id.changePassButton);
        cancelChanging=(TextView) findViewById(R.id.cancelLink);
        noMatch=(TextView) findViewById(R.id.noMatching);
        backButton=(ImageButton) findViewById(R.id.backButton);

        changePass.setOnClickListener(ChangePasswordListener);
        backButton.setOnClickListener(BackButtonListener);
        cancelChanging.setOnClickListener(BackButtonListener);

    }
    View.OnClickListener BackButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), Start_menu.class);
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener ChangePasswordListener= new View.OnClickListener() {
        public void onClick(final View v) {

            oldPassw=oldPass.getText().toString();
            newPassw=newPass.getText().toString();
            confPassw=confPass.getText().toString();
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
                        send = "signin " + Global.username + " " + oldPassw + '\n';

                        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        outToServer.writeBytes(send);
                        outToServer.flush();
                        /*OutputStreamWriter os=new OutputStreamWriter(clientSocket.getOutputStream());
                        PrintWriter out=new PrintWriter(os);
                        out.write(send);
                        os.flush();

                        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        */
                        modifiedSentence=inFromServer.readLine();

                        //incorrectUserOrPass.setText(modifiedSentence);
                        //Intent intent = new Intent(getApplicationContext(), Start_menu.class);
                        //startActivity(intent);
                        //finish();
                        //TODO tova ne znam zashto si go napravil taka, no spored men ne stava taka da se pishe, ne e i imalo problem ... tezi post metodi sa izlishni!
                        if (modifiedSentence.equals("IncorrectPass")){
                                    noMatch.setText("Incorrect old password");
                                    oldPass.setText("");
                                    newPass.setText("");
                                    confPass.setText("");
                                }

                        else if (modifiedSentence.equals("a") || modifiedSentence.equals("u")) {

                            if(newPassw.equals(confPassw))
                            {
                                if(newPassw.length()>5)
                                {
                                    noMatch.post(new Runnable() {
                                        public void run() {
                                            noMatch.setText("Password changed successfully");
                                        }
                                    });
                                    oldPass.post(new Runnable() {
                                        public void run() {
                                            oldPass.setText("");
                                        }
                                    });
                                    newPass.post(new Runnable() {
                                        public void run() {
                                            newPass.setText("");
                                        }
                                    });
                                    confPass.post(new Runnable() {
                                        public void run() {
                                            confPass.setText("");
                                        }
                                    });

                                }
                                else {
                                    noMatch.post(new Runnable() {
                                        public void run() {
                                            noMatch.setText("Too short password");
                                        }
                                    });
                                    oldPass.post(new Runnable() {
                                        public void run() {
                                            oldPass.setText("");
                                        }
                                    });
                                    newPass.post(new Runnable() {
                                        public void run() {
                                            newPass.setText("");
                                        }
                                    });
                                    confPass.post(new Runnable() {
                                        public void run() {
                                            confPass.setText("");
                                        }
                                    });

                                }
                            }
                            else {
                                noMatch.post(new Runnable() {
                                    public void run() {
                                        noMatch.setText("You need to confirm your password");
                                    }
                                });
                                oldPass.post(new Runnable() {
                                    public void run() {
                                        oldPass.setText("");
                                    }
                                });
                                newPass.post(new Runnable() {
                                    public void run() {
                                        newPass.setText("");
                                    }
                                });
                                confPass.post(new Runnable() {
                                    public void run() {
                                        confPass.setText("");
                                    }
                                });

                            }

                            //incorrectUserOrPass.setVisibility(View.GONE);
                            //incorrectUserOrPass.setText(modifiedSentence);
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
