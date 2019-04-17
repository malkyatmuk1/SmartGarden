package com.example.malkyatmuk.smartgarden;

public  class Plants{
    public  String namePlant;
    public  int pouring;
    public  double humidity;
    public  double temperature;
    public  String type;
    public  boolean inside;
    public  String ipPlant;
    Plants()
    {
        namePlant="";
        type="";
        pouring=0;
        humidity=Global.humidity;
        temperature=Global.temperature;
        inside=true;
        ipPlant="";
    }
    public String getName() {
        return namePlant;
    }
}