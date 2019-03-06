package com.example.malkyatmuk.smartgarden;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Change_Password extends Fragment {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View myFragmentView = inflater.inflate(R.layout.fragment_change_pass, container, false);
        oldPass=(EditText) myFragmentView.findViewById(R.id.oldPassword);
        newPass=(EditText) myFragmentView.findViewById(R.id.passwordOne);
        confPass=(EditText) myFragmentView.findViewById(R.id.passwordTwo);
        changePass=(Button) myFragmentView.findViewById(R.id.changePassButton);
        noMatch=(TextView) myFragmentView.findViewById(R.id.noMatching);

        changePass.setOnClickListener(ChangePasswordListener);
        return myFragmentView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("");
    }
    View.OnClickListener ChangePasswordListener= new View.OnClickListener() {
        public void onClick(final View v) {

            oldPassw=oldPass.getText().toString();
            newPassw=newPass.getText().toString();
            confPassw=confPass.getText().toString();
            noMatch.setText("");
            if(!newPassw.equals((confPassw))){
                noMatch.setText("You need to confirm your password");
                oldPass.setText("");
                newPass.setText("");
                confPass.setText("");
            }
            else
            {
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
                            send = "setPassWord " + Global.username + " " + oldPassw + " " + newPassw + '\n';

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
                            if (modifiedSentence.equals("IncorectPass")){
                                noMatch.post(new Runnable() {
                                    public void run() {
                                        noMatch.setText("Incorrect password");
                                    }
                                });
                                newPass.post(new Runnable() {
                                    public void run() {
                                        newPass.setText("");
                                    }
                                });
                                oldPass.post(new Runnable() {
                                    public void run() {
                                        oldPass.setText("");
                                    }
                                });
                                confPass.post(new Runnable() {
                                    public void run() {
                                        confPass.setText("");
                                    }
                                });
                                //startActivity(intent);
                                //finish();
                                //incorrectUserOrPass.setVisibility(View.VISIBLE);
                                //incorrectUserOrPass.setText(modifiedSentence);
                            }
                            else if (modifiedSentence.equals("ready!")) {
                                noMatch.post(new Runnable() {
                                    public void run() {
                                        noMatch.setText("Password changed successfully!");
                                    }
                                });
                                newPass.post(new Runnable() {
                                    public void run() {
                                        newPass.setText("");
                                    }
                                });
                                oldPass.post(new Runnable() {
                                    public void run() {
                                        oldPass.setText("");
                                    }
                                });
                                confPass.post(new Runnable() {
                                    public void run() {
                                        confPass.setText("");
                                    }
                                });

                            }

                                //incorrectUserOrPass.setVisibility(View.GONE);

                            clientSocket.close();
                        } catch (IOException e) {
                            System.out.println("Exception " + e);
                        }
                        return;
                    }
                }).start();

        }}

    };

}
