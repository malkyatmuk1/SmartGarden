package com.example.malkyatmuk.smartgarden;

import java.util.Date;

public  class Plants{
    public  String namePlant;
    public  String type;
    public  String ipPlant;
    public  String lastPoured;
    public  String nextPouring;
    public  int pouringType;
    public  int pouring;
    public  double humidity;
    public  double temperature;
    public  double usedWater;
    public  boolean inside;
    Plants()
    {
        namePlant="";
        type="";
        pouring=1;
        humidity=Global.humidity;
        temperature=Global.temperature;
        inside=true;
        ipPlant="";
        lastPoured="00:00:00";
        usedWater=0.0;
        nextPouring="00:00:00";
        pouringType=0;

    }
    public String getName() {
        return namePlant;
    }
}