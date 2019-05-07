package com.example.malkyatmuk.smartgarden;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

/**
 * Created by malkyatmuk on 11/12/18.
 */


public class View_Plant extends Activity {
    EditText namePlantEditText,pouringTimesEditText;
    TextView humidityTextView,temperatureTextView;
    Button saveInfoButton;
    ImageButton backButton,pouringButton;
    AutoCompleteTextView actv,pouringTypeActv;
    Spinner spinner;
    LinearLayout pour;
    String[] language ={"C","C++","Java",".NET","iPhone","Android","ASP.NET","PHP"};
    String[] plantCommonNameEn ={"Amaryllis", "African Violet", "Angel Wing Begonia", "Barberton Daisy", "Beach Spider Lily", "Belladonna Lily", "Bird of Paradise",
            "Blushing Bromeliad", "Busy Lizzie", "Calla Lily", "Coral Berry", "Corsage Orchid", "Cyclamen Persicum", "Eternal Flame",
            "False Shamrock", "Flamingo Flower Plant", "Flaming Sword", "Flowering Maple Plant", "Kaffir Lily Plant",
            "Lollipop Plant", "Lycaste Orchid", "Madagascar Jasmine", "Medusa's Head Plant", "Moth Orchid", "Ornamental Pepper Plant",
            "Peace Lily", "Poinsettia", "Poison Primrose", "Queens Tears", "Rose of China", "Scarlet Star", "Slipper Orchid", "The One Colored Paphiopedilum Concolor", "Urn Plant", "Winter Cherry"
    };
    String[] plantScientificNameEn={"Hippeastrum", "Saintpaulia", "Begonia Coccinea", "Gerbera Jamesonii", "Hymenocallis Littoralis", "Amaryllis Bellandonna", "Strelitzia Reginae",
            "Neoregelia Carolinae", "Impatiens Walleriana", "Zantedeschia Aethiopica", "Ardisia Crenata", "Cattleya", "Cyclamen Persicum", "Calathea Crocata", "Oxalis Triangularis", "Anthurium scherzerianum",
            "Vriesea Splendens", "Abutilon Hybridum", "Clivia Miniata", "Pachystachys Lutea", "Lycaste", "Stephanotis Floribunda", "Tillandsia Caput Medusae", "Phalaenopsis", "Capsicum Annuum",
            "Spathiphyllum Wallisii", "Euphorbia Pulcherrima", "Primula Obconica", "Billbergia Nutans", "Hibiscus Rosa-Sinensis", "Guzmania Lingulata", "Paphiopedilum", "Paphiopedilum Concolor",
            "Aechmea Fansciata", "Solanum Capsicastrum"
    };
    String[] pouringTypes={"daily","weekly","monthly"};
    String colorString="#424CA6";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plant);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,plantCommonNameEn);
        //Getting the instance of AutoCompleteTextView
        actv= (AutoCompleteTextView)findViewById(R.id.plantType);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.parseColor(colorString));

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner_text, pouringTypes );
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(langAdapter);
        namePlantEditText = (EditText) findViewById(R.id.plantName);
        pouringTimesEditText = (EditText) findViewById(R.id.plantPouringTimes);
        temperatureTextView = (TextView) findViewById(R.id.plantTemperature);
        humidityTextView = (TextView) findViewById(R.id.plantHumidity);
        saveInfoButton=(Button) findViewById(R.id.saveInfo);
        backButton=(ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(BackButtonListener);
        pouringButton=(ImageButton)findViewById(R.id.pouringInfoButton);
        pouringButton.setOnClickListener(PouringInfoListener);
        saveInfoButton.setOnClickListener(SaveInfoListener);

        String pour=String.valueOf(Global.myPlants.get(Global.indexOfPlant).pouring);
        String temp=String.valueOf(Global.temperature);
        String hum=String.valueOf(Global.humidity);
        namePlantEditText.setText(Global.myPlants.get(Global.indexOfPlant).namePlant);
        actv.setText(Global.myPlants.get(Global.indexOfPlant).type);
        spinner.setSelection(Global.myPlants.get(Global.indexOfPlant).pouringType);
        pouringTimesEditText.setText(pour);
        temperatureTextView.setText(temp+" °C");
        humidityTextView.setText(hum+" HD");

    }
    View.OnClickListener PouringInfoListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), Pouring_Info.class);
            Global.fromView=true;
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener BackButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), Start_menu.class);
            Global.fromView=true;
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener SaveInfoListener=new View.OnClickListener() {

        public void onClick(View view) {
            Global.myPlants.get(Global.indexOfPlant).namePlant=namePlantEditText.getText().toString();
            Global.myPlants.get(Global.indexOfPlant).pouringType=spinner.getSelectedItemPosition();
            String s=pouringTimesEditText.getText().toString();
            boolean flag=true;
            for(int i=0;i<s.length();i++)
            {
                if(s.charAt(i)-'0'>=0 && s.charAt(i)-'0'<=9) {
                    continue;
                }
                else {
                    flag=false;
                    break;
                }
            }
            if(flag==true) Global.myPlants.get(Global.indexOfPlant).pouring= Integer.parseInt(pouringTimesEditText.getText().toString());
            Global.myPlants.get(Global.indexOfPlant).type=actv.getText().toString();
            Global.fromView=true;
            Intent intent = new Intent(view.getContext(), Start_menu.class);
            startActivity(intent);
            finish();
        }
    };
}



