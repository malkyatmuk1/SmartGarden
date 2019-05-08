package com.example.malkyatmuk.smartgarden;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class DeleteSharedPreferences extends Activity {
    SharedPreferences sp;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        String ss="plantName"+"1";
        sp.edit().remove(ss).commit();
        ss="plantType"+"1";
        sp.edit().remove(ss).commit();
        ss="plantPouring"+"1";
        sp.edit().remove(ss).commit();
        ss="plantIp"+"1";
        sp.edit().remove(ss).commit();
        ss="plantLastPoured"+"1";
        sp.edit().remove(ss).commit();
        ss="plantNextPouring"+"1";
        sp.edit().remove(ss).commit();
        ss="plantPouringType"+"1";
        sp.edit().remove(ss).commit();
        ss="plantAutoPouring"+"1";
        sp.edit().remove(ss).commit();
        ss="plantLastPouredDay"+"1";
        sp.edit().remove(ss).commit();
        ss="plantNextPouringDay"+"1";
        sp.edit().remove(ss).commit();
        Global.fromDeleteShPr=true;
        Intent intent = new Intent(getApplicationContext(), Start_menu.class);
        startActivity(intent);
    }
}
