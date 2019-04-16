package com.example.malkyatmuk.smartgarden;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Plant_List extends Fragment {

    String[] gen=new String[]{"There are no other users!"};
    ProgressBar progressBar;
    FloatingActionButton fab;
    Button viewPlant;
    View view;
    GridView listView;

    public void readPlants(View view,boolean isProgressbar) {

//        if (isProgressbar) progressBar.setVisibility(View.VISIBLE);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        if(Global.permission=='u' || Global.permission=='a'){
           view  = inflater.inflate(R.layout.fragment_list_of_plants, container, false);
            listView = (GridView) view.findViewById(R.id.card_listView);
            fab=(FloatingActionButton) view.findViewById(R.id.fab);
            fab.setOnClickListener(FabButtonListener);
            //progressBar=(ProgressBar) view.findViewById(R.id.progressBar);
            readPlants(view,false);
            Global.fromView=false;


            Adapter adapter = new Adapter(getContext(),Global.myPlants);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            new LongOperation().execute("");
            return view;
                       }
        else
        {
             view = inflater.inflate(R.layout.fragment_users_nopermission, container, false);
            return view;
        }


    }
    View.OnClickListener FabButtonListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(view.getContext(),Add_Card.class));
        }
    };
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        getActivity().setTitle("Plants List");
        view.setFocusable(false);
        if (Global.permission == 'u' || Global.permission == 'a') {
            final SwipeRefreshLayout sr = (SwipeRefreshLayout) view.findViewById(R.id.mSwipeRefreshLayout);
            sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    readPlants(view, false);
                    sr.setRefreshing(false);
                }
            });

        }
    }
    class LongOperation extends AsyncTask<String, Void, Void> {
        private static final int SERVERPORT = 3030;
        private String SERVER_IP;
        private Socket clientSocket;

        protected Void doInBackground(String... Param) {

            try {

                Socket clientSocket;
                clientSocket = new Socket(Global.ip, SERVERPORT);
                BufferedReader inFromServer;
                String line;
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                outToServer.writeBytes("Humidity true" + '\n');
                outToServer.flush();
                
                line = inFromServer.readLine();
                Global.humidity=Double.parseDouble(line);
                clientSocket.close();

                clientSocket = new Socket(Global.ip, SERVERPORT);
                outToServer = new DataOutputStream(clientSocket.getOutputStream());
                inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                outToServer.writeBytes("Humidity false" + '\n');
                outToServer.flush();

                line = inFromServer.readLine();
                Global.temperature=Double.parseDouble(line);
                clientSocket.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}