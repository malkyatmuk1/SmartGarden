package com.example.malkyatmuk.smartgarden;

public  class Plants{
    public  String namePlant;
    public  int pouring;
    public  double humidity;
    public  double temperature;
    public  String type;
    public  boolean inside;
    public  String ipPlant;
    public  double usedWater;
    Plants()
    {
        namePlant="";
        type="";
        pouring=0;
        humidity=Global.humidity;
        temperature=Global.temperature;
        inside=true;
        ipPlant="";
        usedWater=0.0;

    }
    public String getName() {
        return namePlant;
    }
}