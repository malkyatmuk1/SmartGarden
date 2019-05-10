package com.example.malkyatmuk.smartgarden;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class DeleteSharedPreferences extends AsyncTask<Context, Void, Void> {

     @Override
    protected Void doInBackground(Context... params) {
         SharedPreferences sp;
         Context context = params[0];
         SharedPreferences prefs =
                 context.getSharedPreferences("login", Context.MODE_PRIVATE);

        String myBackgroundPreference = prefs.getString("preference name", "default value");
        String indx=String.valueOf(Global.toDelete);
        String ss="plantName"+indx;
        prefs.edit().remove(ss).apply();
        ss="plantType"+indx;
        prefs.edit().remove(ss).apply();
        ss="plantPouring"+indx;
         prefs.edit().remove(ss).apply();
        ss="plantIp"+indx;
         prefs.edit().remove(ss).apply();
        ss="plantLastPoured"+indx;
         prefs.edit().remove(ss).apply();
        ss="plantNextPouring"+indx;
         prefs.edit().remove(ss).apply();
        ss="plantPouringType"+indx;
         prefs.edit().remove(ss).apply();
        ss="plantAutoPouring"+indx;
         prefs.edit().remove(ss).apply();
        ss="plantLastPouredDay"+indx;
         prefs.edit().remove(ss).apply();
        ss="plantNextPouringDay"+indx;
         prefs.edit().remove(ss).apply();
        Global.fromDeleteShPr=true;
        return null;
    }
}
