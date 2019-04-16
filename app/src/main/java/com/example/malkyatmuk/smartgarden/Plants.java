package com.example.malkyatmuk.smartgarden;

public  class Plants{
    public  String namePlant;
    public  int pouring;
    public  double humidity;
    public  double temperature;
    public  String type;
    public  boolean inside;
    Plants()
    {
        namePlant="";
        type="";
        pouring=0;
        humidity=Global.humidity;
        temperature=Global.temperature;
        inside=true;
    }
    public String getName() {
        return namePlant;
    }
}