package com.example.malkyatmuk.smartgarden;

public  class Plants{
    public  String namePlant;
    public  int pouring;
    public  double humidity;
    public  double temperature;
    Plants()
    {
        namePlant="";
        pouring=0;
        humidity=Global.humidity;
        temperature=Global.temperature;
    }
    public String getName() {
        return namePlant;
    }
}