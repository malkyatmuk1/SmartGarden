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
        humidity=0.0;
        temperature=0.0;
    }
    public String getName() {
        return namePlant;
    }
}