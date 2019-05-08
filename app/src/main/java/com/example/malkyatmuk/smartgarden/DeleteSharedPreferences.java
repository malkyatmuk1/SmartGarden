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

        String indx=String.valueOf(Global.toDelete);
        String ss="plantName"+indx;
        sp.edit().remove(ss).commit();
        ss="plantType"+indx;
        sp.edit().remove(ss).commit();
        ss="plantPouring"+indx;
        sp.edit().remove(ss).commit();
        ss="plantIp"+indx;
        sp.edit().remove(ss).commit();
        ss="plantLastPoured"+indx;
        sp.edit().remove(ss).commit();
        ss="plantNextPouring"+indx;
        sp.edit().remove(ss).commit();
        ss="plantPouringType"+indx;
        sp.edit().remove(ss).commit();
        ss="plantAutoPouring"+indx;
        sp.edit().remove(ss).commit();
        ss="plantLastPouredDay"+indx;
        sp.edit().remove(ss).commit();
        ss="plantNextPouringDay"+indx;
        sp.edit().remove(ss).commit();
        Global.fromDeleteShPr=true;
        Intent intent = new Intent(getApplicationContext(), Start_menu.class);
        startActivity(intent);
    }
}
