package com.example.malkyatmuk.smartgarden;

import java.util.Date;

public  class Plants{
    public  String namePlant;
    public  String type;
    public  String ipPlant;
    public  String lastPoured;
    public  String nextPouring;
    public  String pouringType;
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
        pouringType="daily";

    }
    public String getName() {
        return namePlant;
    }
}