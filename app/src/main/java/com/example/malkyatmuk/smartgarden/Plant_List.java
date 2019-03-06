package com.example.malkyatmuk.smartgarden;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class Plant_List extends Fragment {

    String[] gen=new String[]{"There are no other users!"};
    ProgressBar progressBar;
    FloatingActionButton fab;
    Button viewPlant;
    public void readPlants(View view,boolean isProgressbar) {

//        if (isProgressbar) progressBar.setVisibility(View.VISIBLE);
        Global.plants.clear();
        for(int i=0;i<Global.numberOfPlants;i++) {
            Global.plants.add(Global.myPlants[i].namePlant);
        }
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_list_of_plants, container, false);
        final GridView listView = (GridView) view.findViewById(R.id.card_listView);
        FloatingActionButton fab=(FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(FabButtonListener);
        //progressBar=(ProgressBar) view.findViewById(R.id.progressBar);
        readPlants(view,true);
        Adapter adapter = new Adapter(getContext(),Global.plants);
        listView.setAdapter(adapter);
        return view;
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