package com.example.malkyatmuk.smartgarden;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Plant_List extends Fragment {

    String[] gen=new String[]{"There are no other users!"};
    ProgressBar progressBar;


    public void readPlants(View view,boolean isProgressbar) {

//        if (isProgressbar) progressBar.setVisibility(View.VISIBLE);
        Global.plants.clear();
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

  @Nullable
  @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_list_of_plants, container, false);
        final GridView listView = (GridView) view.findViewById(R.id.card_listView);
        //progressBar=(ProgressBar) view.findViewById(R.id.progressBar);
        readPlants(view,true);
        Global.plants.add("tanq");
        Global.plants.add("ivan");
        Adapter adapter = new Adapter(getContext(),Global.plants);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Plants List");
        view.setFocusable(false);
       final SwipeRefreshLayout sr=(SwipeRefreshLayout) view.findViewById(R.id.mSwipeRefreshLayout);
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readPlants(view,false);
                sr.setRefreshing(false);
            }
        });
    }
}