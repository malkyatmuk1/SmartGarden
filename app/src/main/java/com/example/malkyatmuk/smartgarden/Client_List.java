package com.example.malkyatmuk.smartgarden;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client_List extends Fragment {

    String[] gen=new String[]{"There are no other users!"};
    ProgressBar progressBar;

    public void readUsers(View view,boolean isProgressbar) {

        final ListView listView = (ListView)view.findViewById(R.id.list);
        if(isProgressbar)  progressBar.setVisibility(View.VISIBLE);
        Global.users.clear();
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        class LongOperation extends AsyncTask<String, Void, Void> {
            private static final int SERVERPORT = 3030;
            private String SERVER_IP;
            private Socket clientSocket;

            protected Void doInBackground(String... Param) {

                try
                {

                    Socket clientSocket;
                    clientSocket = new Socket(Global.ip, SERVERPORT);
                    BufferedReader inFromServer ;
                    String line;
                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    outToServer.writeBytes("list " + Global.username + " " + Global.password + '\n');
                    outToServer.flush();
                    String[] spliter;
                    while (true)
                    {
                        line = inFromServer.readLine();
                        if (line != null) {
                            if (!line.equals("stop") && !line.equals("error")) {
                                Global.users.add(line);
                            }
                            else break;
                        }
                    }
                }
                catch (IOException e){ e.printStackTrace();}
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                progressBar.setVisibility(View.GONE);
                ArrayAdapter mAdapter_List;
                if(Global.users.isEmpty()) {
                    mAdapter_List = new ArrayAdapter(getContext(), R.layout.listview_general,R.id.name_general, gen);
                    listView.setAdapter(mAdapter_List);
                    mAdapter_List.notifyDataSetChanged();
                }
                else{
                    Adapter_List adapter = new Adapter_List(getContext(), Global.users);

                    listView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            protected void onPreExecute() {}

            @Override
            protected void onProgressUpdate(Void... values) {}
        }
        new LongOperation().execute("");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_users, container, false);
        progressBar=(ProgressBar) view.findViewById(R.id.progressBar);
        readUsers(view,true);
        return view;


    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("User List");
        view.setFocusable(false);
        final SwipeRefreshLayout sr=(SwipeRefreshLayout) view.findViewById(R.id.mSwipeRefreshLayout);
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readUsers(view,false);
                sr.setRefreshing(false);
            }
        });
    }
}