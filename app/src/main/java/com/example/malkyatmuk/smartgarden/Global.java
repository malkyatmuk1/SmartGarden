package com.example.malkyatmuk.smartgarden;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by malkyatmuk on 10/22/17.
 */

public class Global extends Application {

    public static String username="";
    public static String password="";
    public static char permission;
    public static int indexOfPlant=0;
    public static int numberOfPlants;
    public static double humidity=0.0;
    public static double temperature=0.0;
    public static double usedWater=0.0;
    public static int toDelete=0;
    public static ArrayList <Plants> myPlants=new ArrayList<Plants>();
    public static ArrayList<String> plants=new ArrayList<String>();
    public static double longetudeHome=0;
    public static double latitudeHome=0;
    public static float meters=100;
    public static String wifiusername="";
    public static boolean flagforNotify=true;
    public static String wifiPassword="";
    public static String ip="";
    public static String directip="192.168.4.1";
    public static boolean ipsignin=false;
    public static boolean signedIn=false;
    public static boolean checksignin=false;
    public static boolean checkProgress=false;
    public static boolean fromView=false;
    public static boolean goback;
    public static boolean fromDeleteShPr=false;

    public static ArrayList<String> users= new ArrayList<String>();

    public String getUsername() {
        return username;
    }
    public char getPermission()
    {
        return permission;
    }

    public void setSomeVariable(String user, char perm) {
        this.username = user;
        this.permission=perm;
    }
    public static void setIP(String ip, Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ip", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString("ip", ip);
        editor.apply();
    }
}