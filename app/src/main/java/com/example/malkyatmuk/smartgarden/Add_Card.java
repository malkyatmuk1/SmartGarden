package com.example.malkyatmuk.smartgarden;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

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


public class Add_Card extends AppCompatActivity {
    EditText name,ipPlant;
    Button newCard;
    AutoCompleteTextView typePlant;
    CheckBox ipMatch;

    String[] language ={"C","C++","Java",".NET","iPhone","Android","ASP.NET","PHP"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card);

        name = (EditText) findViewById(R.id.namePlant);
        //pouring = (EditText) findViewById(R.id.pouringTimes);
        newCard = (Button) findViewById(R.id.addButton);
        newCard.setOnClickListener(NewCardListener);
        ipPlant= (EditText) findViewById(R.id.ipPlant);
        ipMatch=(CheckBox) findViewById(R.id.ipMatch);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,language);
        //Getting the instance of AutoCompleteTextView
        typePlant= (AutoCompleteTextView)findViewById(R.id.plantType);
        typePlant.setThreshold(1);//will start working from first character
        typePlant.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        typePlant.setTextColor(Color.BLUE);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(Color.TRANSPARENT);
        drawable.setCornerRadius(20);
        drawable.setSize(180,280);
        getWindow().setBackgroundDrawable(drawable);
        getWindow().setLayout((int) (width), (int) (height));
    }

    View.OnClickListener NewCardListener = new View.OnClickListener() {
        public void onClick(final View v) {
            //String value = pouring.getText().toString();
            String nameOfPlant=name.getText().toString();
            String typeOfPlant=typePlant.getText().toString();

            /*if(nameOfPlant.length()!=0 && value.length()!=0) {
                boolean flag = true;
                for (int i = 0; i < value.length(); i++) {
                    if (value.charAt(i) - '0' >= 0 && value.charAt(i) - '0' <= 9) {
                        continue;
                    } else {
                        flag = false;
                        break;
                    }
                }
                if (flag == false) {
                    pouring.setText("Need a whole number");
                } else {
                    Plants pl = new Plants();
                    pl.namePlant = nameOfPlant;
                    pl.pouring = Integer.parseInt(pouring.getText().toString());
                    Global.myPlants.add(pl);
                    finish();
                }
            }*/
            if(ipMatch.isChecked()) {
                Plants pl = new Plants();
                pl.namePlant = nameOfPlant;
                pl.type=typeOfPlant;
                pl.ipPlant=Global.ip;
                Global.myPlants.add(pl);
                finish();
            }
            else {

                if (ipPlant.getText().toString().length() != 0) {
                    Plants pl = new Plants();
                    pl.namePlant = nameOfPlant;
                    pl.type = typeOfPlant;
                    pl.ipPlant = ipPlant.getText().toString();
                    Global.myPlants.add(pl);
                    finish();
                }
                else ipPlant.setHint(
                        "     must be entered"
                );
            }
        }
    };
}



