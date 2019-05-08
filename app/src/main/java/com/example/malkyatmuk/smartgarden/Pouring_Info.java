package com.example.malkyatmuk.smartgarden;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Pouring_Info extends Activity {

    ImageButton backButton;
    TextView usedWater,nextPouring,pouringTimes;
    Button Pouringbutton;

    String currentTime,currentDay,pour,pourType;
    int pourTypeInt;
    private Socket clientSocket;
    String send;
    CheckBox autoPouring;
    private static final int SERVERPORT = 3030;
    public void init()
    {
        backButton = (ImageButton) findViewById(R.id.backButtonPouringInfo);
        backButton.setOnClickListener(BackButtonListener);
        pouringTimes = (TextView) findViewById(R.id.plantPouringTimes);
        usedWater = (TextView) findViewById(R.id.plantUsedWater);
        Pouringbutton = (Button) findViewById(R.id.pourButton);
        nextPouring= (TextView) findViewById(R.id.nextPouring);
        autoPouring=(CheckBox)findViewById(R.id.autoPouring);
        Pouringbutton.setOnClickListener(PouringButtonListener);
        String usedWaterValue = String.valueOf(Global.usedWater);
        pour = String.valueOf(Global.myPlants.get(Global.indexOfPlant).pouring);
        pourTypeInt = Global.myPlants.get(Global.indexOfPlant).pouringType;
        if(pourTypeInt==0)pourType="daily";
        else if(pourTypeInt==1)pourType="weekly";
        else if(pourTypeInt==2)pourType="monthly";
        String lastPouring = String.valueOf(Global.myPlants.get(Global.indexOfPlant).lastPoured);
        usedWater.setText(usedWaterValue + " l");
        pouringTimes.setText(pour + " pouring times " + pourType);
        nextPouring.setText(Global.myPlants.get(Global.indexOfPlant).nextPouring);
        autoPouring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked==true) {
                    Global.myPlants.get(Global.indexOfPlant).autoPouring=true;
                    AutoPouring();
                }
                else {
                    Global.myPlants.get(Global.indexOfPlant).autoPouring=false;
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pouring_info);

        init();

        if(Global.myPlants.get(Global.indexOfPlant).autoPouring==true)AutoPouring();
        else {
            if (!Global.myPlants.get(Global.indexOfPlant).nextPouring.equals("Unknown")) {


                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                currentTime = mdformat.format(calendar.getTime());
                SimpleDateFormat day = new SimpleDateFormat("dd:mm:yyyy");
                currentDay = day.format(calendar.getTime());

                // last poured->to seconds;
                String lP = Global.myPlants.get(Global.indexOfPlant).lastPoured;
                String c = "" + lP.charAt(0);
                int secondsLastPoured = Integer.parseInt(c) * 36000;
                c = "" + lP.charAt(1);
                secondsLastPoured += Integer.parseInt(c) * 3600;
                c = "" + lP.charAt(3);
                secondsLastPoured += Integer.parseInt(c) * 600;
                c = "" + lP.charAt(4);
                secondsLastPoured += Integer.parseInt(c) * 60;
                c = "" + lP.charAt(6);
                secondsLastPoured += Integer.parseInt(c) * 10;
                c = "" + lP.charAt(7);
                secondsLastPoured += Integer.parseInt(c);

                /*String lastDay=Global.myPlants.get(Global.indexOfPlant).lastPouredDay;
                c=""+lastDay.charAt(0);
                int days=Integer.parseInt(c)*10;
                c=""+lastDay.charAt(1);
                days+=Integer.parseInt(c);
                c=""+lastDay.charAt(6);
                days+=Integer.parseInt(c)*365000;
                c=""+lastDay.charAt(7);
                days+=Integer.parseInt(c)*36500;
                c=""+lastDay.charAt(8);
                days+=Integer.parseInt(c)*3650;
                c=""+lastDay.charAt(9);
                days+=Integer.parseInt(c)*365;

                c=""+lastDay.charAt(3);
                int months=Integer.parseInt(c)*10;
                c=""+lastDay.charAt(4);
                months+=Integer.parseInt(c);
                */

                //currentTime-> to seconds;
                lP = currentTime;
                c = "" + lP.charAt(0);
                int secondsCurrentTime = Integer.parseInt(c) * 36000;
                c = "" + lP.charAt(1);
                secondsCurrentTime += Integer.parseInt(c) * 3600;
                c = "" + lP.charAt(3);
                secondsCurrentTime += Integer.parseInt(c) * 600;
                c = "" + lP.charAt(4);
                secondsCurrentTime += Integer.parseInt(c) * 60;
                c = "" + lP.charAt(6);
                secondsCurrentTime += Integer.parseInt(c) * 10;
                c = "" + lP.charAt(7);
                secondsCurrentTime += Integer.parseInt(c);
                //preminato 24 chasa
                if (secondsCurrentTime < secondsLastPoured) secondsCurrentTime += 24 * 3600;
                if(Global.myPlants.get(Global.indexOfPlant).pouringType==0) {
                    double pouringPeriod = ((double) (24.0 / (Integer.parseInt(pour)))) * 3600;
                    if (secondsCurrentTime - secondsLastPoured >= pouringPeriod) {
                        //TODO alarm
                    }
                }
                else if(Global.myPlants.get(Global.indexOfPlant).pouringType==1) {
                    if (Global.myPlants.get(Global.indexOfPlant).pouring == 1) {

                    }
                    else if (Global.myPlants.get(Global.indexOfPlant).pouring == 2) {

                    }
                    else if (Global.myPlants.get(Global.indexOfPlant).pouring == 3) {

                    }
                    else if (Global.myPlants.get(Global.indexOfPlant).pouring == 4) {

                    }
                    else if (Global.myPlants.get(Global.indexOfPlant).pouring == 5) {

                    }
                    else if (Global.myPlants.get(Global.indexOfPlant).pouring == 6) {

                    }
                }
                else if(Global.myPlants.get(Global.indexOfPlant).pouringType==2) {
                    if (Global.myPlants.get(Global.indexOfPlant).pouring == 1) {
                    }
                    else if (Global.myPlants.get(Global.indexOfPlant).pouring == 2) {

                    }
                    else if (Global.myPlants.get(Global.indexOfPlant).pouring == 3) {

                    }
                    else if (Global.myPlants.get(Global.indexOfPlant).pouring == 4) {

                    }
                    else if (Global.myPlants.get(Global.indexOfPlant).pouring == 5) {

                    }
                    else if (Global.myPlants.get(Global.indexOfPlant).pouring == 6) {

                    }
                }
            }
        }
    }
    View.OnClickListener BackButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), View_Plant.class);
            startActivity(intent);
            finish();
        }
    };
    public void AutoPouring()
    {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    clientSocket = new Socket(Global.ip, SERVERPORT);

                    send="water 1 "+pouringTimes.getText().toString()+" 1";//TODO its for daily only
                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    outToServer.writeBytes(send);
                    outToServer.flush();
                        /*OutputStreamWriter os=new OutputStreamWriter(clientSocket.getOutputStream());
                        PrintWriter out=new PrintWriter(os);
                        out.write(send);
                        os.flush();*/

                    clientSocket.close();
                    //izprastame na servera: <water> <1(1 za automatic - 0 za ednokratno)> <pouring times> <daily/weekly/monthly(1,2,3)>
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void DaysMaintaining(int daysAfter)
    {
        String c0=""+currentDay.charAt(0);
        String c1=""+currentDay.charAt(1);
        String c3=""+currentDay.charAt(3);
        String c4=""+currentDay.charAt(4);
        String c6=""+currentDay.charAt(6);
        String c7=""+currentDay.charAt(7);
        String c8=""+currentDay.charAt(8);
        String c9=""+currentDay.charAt(9);
        int dayy=Integer.parseInt(c0)*10+Integer.parseInt(c1);
        int month=Integer.parseInt(c3)*10+Integer.parseInt(c4);
        int yearr=Integer.parseInt(c6)*1000+Integer.parseInt(c7)*100+Integer.parseInt(c8)*10+Integer.parseInt(c9);
        dayy+=daysAfter;
        if(month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12) {
            if(dayy>31) {
                month++;
                dayy-=31;
                if(month==12)yearr++;
            }
        }
        else if(month==4 || month==6 || month==9 || month==11){
            if(dayy>30) {
                month++;
                dayy-=30;
            }
        }
        else if(month==2) {
            if(yearr%400==0 || (yearr%4==0 && yearr%100!=0))
            {
                if(dayy>29){
                    month++;
                    dayy-=29;
                }
            }
            else
            {
                if(dayy>28){
                    month++;
                    dayy-=28;
                }
            }
        }
        String dayyS,monthS;
        if(dayy<10)dayyS="0"+String.valueOf(dayy);
        else dayyS=String.valueOf(dayy);
        if(month<10)monthS="0"+String.valueOf(month);
        else monthS=String.valueOf(month);
        currentDay=dayyS+":"+monthS+":"+String.valueOf(yearr);
    }
    public void Pouring()
    {
        //water 0 (1 za automatic)
        //TODO ako e bilo polivano tvurde skoro -> ne
        //TODO pouring
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        currentTime = mdformat.format(calendar.getTime());
        SimpleDateFormat day= new SimpleDateFormat("dd:MM:yyyy");
        currentDay = day.format(calendar.getTime());
        String currDay=currentDay;
        String lP=currentTime;
        String c="";
        c=""+lP.charAt(0);
        int secondsCurrentTime=Integer.parseInt(c)*36000;
        c=""+lP.charAt(1);
        secondsCurrentTime+=Integer.parseInt(c)*3600;
        c=""+lP.charAt(3);
        secondsCurrentTime+=Integer.parseInt(c)*600;
        c=""+lP.charAt(4);
        secondsCurrentTime+=Integer.parseInt(c)*60;
        c=""+lP.charAt(6);
        secondsCurrentTime+=Integer.parseInt(c)*10;
        c=""+lP.charAt(7);
        secondsCurrentTime+=Integer.parseInt(c);


        if(Global.myPlants.get(Global.indexOfPlant).pouringType==0) {
            double pouringPeriod=((double)(24.0/(Integer.parseInt(pour))))*3600;
            int next=(int)pouringPeriod+secondsCurrentTime;
            if(next>=24*3600)
            {
                next-=24*3600;
                currentDay=currDay;
                String c0=""+currentDay.charAt(0);
                String c1=""+currentDay.charAt(1);
                String c3=""+currentDay.charAt(3);
                String c4=""+currentDay.charAt(4);
                String c6=""+currentDay.charAt(6);
                String c7=""+currentDay.charAt(7);
                String c8=""+currentDay.charAt(8);
                String c9=""+currentDay.charAt(9);
                int dayy=Integer.parseInt(c0)*10+Integer.parseInt(c1);
                int month=Integer.parseInt(c3)*10+Integer.parseInt(c4);
                int yearr=Integer.parseInt(c6)*1000+Integer.parseInt(c7)*100+Integer.parseInt(c8)*10+Integer.parseInt(c9);
                if(month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12) {
                    if(dayy==31) {
                        month++;
                        dayy=0;
                        if(month==12)yearr++;
                    }
                }
                else if(month==4 || month==6 || month==9 || month==11){
                    if(dayy==30) {
                        month++;
                        dayy = 0;
                    }
                }
                else if(month==2) {
                    if(yearr%400==0 || (yearr%4==0 && yearr%100!=0))
                    {
                        if(dayy==29){
                            month++;
                            dayy=0;
                        }
                    }
                    else
                    {
                        if(dayy==28){
                            month++;
                            dayy=0;
                        }
                    }
                }
                dayy++;
                String dayyS,monthS;
                if(dayy<10)dayyS="0"+String.valueOf(dayy);
                else dayyS=String.valueOf(dayy);
                if(month<10)monthS="0"+String.valueOf(month);
                else monthS=String.valueOf(month);
                currentDay=dayyS+":"+monthS+":"+String.valueOf(yearr);
            }
            String nextPouringTime=String.valueOf(next/36000);
            next-=(next/36000)*36000;
            nextPouringTime+=String.valueOf(next/3600)+":";
            next-=(next/3600)*3600;
            nextPouringTime+=String.valueOf(next/600);
            next-=(next/600)*600;
            nextPouringTime+=String.valueOf(next/60)+":";
            next-=(next/60)*60;
            nextPouringTime+=String.valueOf(next/10);
            next-=(next/10)*10;
            nextPouringTime+=String.valueOf(next%10);
            nextPouring.setText(nextPouringTime+" "+currentDay);
            Global.myPlants.get(Global.indexOfPlant).nextPouring=nextPouringTime+" "+currentDay;
            Global.myPlants.get(Global.indexOfPlant).nextPouringDay=currentDay;
            Global.myPlants.get(Global.indexOfPlant).lastPoured=currentTime;
            Global.myPlants.get(Global.indexOfPlant).lastPouredDay=currDay;
        }
        else if(Global.myPlants.get(Global.indexOfPlant).pouringType==1) {
            if (Global.myPlants.get(Global.indexOfPlant).pouring == 1) {
                currentDay=currDay;
                DaysMaintaining(7);
                nextPouring.setText(currentTime+" "+currentDay);
                Global.myPlants.get(Global.indexOfPlant).nextPouring=currentTime;
                Global.myPlants.get(Global.indexOfPlant).nextPouringDay=currentDay;
                Global.myPlants.get(Global.indexOfPlant).lastPoured=currentTime;
                Global.myPlants.get(Global.indexOfPlant).lastPouredDay=currDay;
            }
            else if (Global.myPlants.get(Global.indexOfPlant).pouring == 2) {
                currentDay=currDay;
                DaysMaintaining(3);
                nextPouring.setText(currentTime+" "+currentDay);
                Global.myPlants.get(Global.indexOfPlant).nextPouring=currentTime;
                Global.myPlants.get(Global.indexOfPlant).nextPouringDay=currentDay;
                Global.myPlants.get(Global.indexOfPlant).lastPoured=currentTime;
                Global.myPlants.get(Global.indexOfPlant).lastPouredDay=currDay;
            }
            else if (Global.myPlants.get(Global.indexOfPlant).pouring == 3) {
                currentDay=currDay;
                DaysMaintaining(2);
                nextPouring.setText(currentTime+" "+currentDay);
                Global.myPlants.get(Global.indexOfPlant).nextPouring=currentTime;
                Global.myPlants.get(Global.indexOfPlant).nextPouringDay=currentDay;
                Global.myPlants.get(Global.indexOfPlant).lastPoured=currentTime;
                Global.myPlants.get(Global.indexOfPlant).lastPouredDay=currDay;
            }
            else if (Global.myPlants.get(Global.indexOfPlant).pouring == 4) {
                currentDay=currDay;
                DaysMaintaining(2);
                nextPouring.setText(currentTime+" "+currentDay);
                Global.myPlants.get(Global.indexOfPlant).nextPouring=currentTime;
                Global.myPlants.get(Global.indexOfPlant).nextPouringDay=currentDay;
                Global.myPlants.get(Global.indexOfPlant).lastPoured=currentTime;
                Global.myPlants.get(Global.indexOfPlant).lastPouredDay=currDay;
            }
            else if (Global.myPlants.get(Global.indexOfPlant).pouring == 5) {
                currentDay=currDay;
                DaysMaintaining(1);
                nextPouring.setText(currentTime+" "+currentDay);
                Global.myPlants.get(Global.indexOfPlant).nextPouring=currentTime;
                Global.myPlants.get(Global.indexOfPlant).nextPouringDay=currentDay;
                Global.myPlants.get(Global.indexOfPlant).lastPoured=currentTime;
                Global.myPlants.get(Global.indexOfPlant).lastPouredDay=currDay;
            }
            else if (Global.myPlants.get(Global.indexOfPlant).pouring == 6) {
                currentDay=currDay;
                DaysMaintaining(1);
                nextPouring.setText(currentTime+" "+currentDay);
                Global.myPlants.get(Global.indexOfPlant).nextPouring=currentTime;
                Global.myPlants.get(Global.indexOfPlant).nextPouringDay=currentDay;
                Global.myPlants.get(Global.indexOfPlant).lastPoured=currentTime;
                Global.myPlants.get(Global.indexOfPlant).lastPouredDay=currDay;
            }
        }
        else if(Global.myPlants.get(Global.indexOfPlant).pouringType==2) {
            if (Global.myPlants.get(Global.indexOfPlant).pouring == 1) {
                currentDay=currDay;
                DaysMaintaining(30);
                nextPouring.setText(currentTime+" "+currentDay);
                Global.myPlants.get(Global.indexOfPlant).nextPouring=currentTime;
                Global.myPlants.get(Global.indexOfPlant).nextPouringDay=currentDay;
                Global.myPlants.get(Global.indexOfPlant).lastPoured=currentTime;
                Global.myPlants.get(Global.indexOfPlant).lastPouredDay=currDay;
            }
            else if (Global.myPlants.get(Global.indexOfPlant).pouring == 2) {
                currentDay=currDay;
                DaysMaintaining(15);
                nextPouring.setText(currentTime+" "+currentDay);
                Global.myPlants.get(Global.indexOfPlant).nextPouring=currentTime;
                Global.myPlants.get(Global.indexOfPlant).nextPouringDay=currentDay;
                Global.myPlants.get(Global.indexOfPlant).lastPoured=currentTime;
                Global.myPlants.get(Global.indexOfPlant).lastPouredDay=currDay;
            }
            else if (Global.myPlants.get(Global.indexOfPlant).pouring == 3) {
                currentDay=currDay;
                DaysMaintaining(10);
                nextPouring.setText(currentTime+" "+currentDay);
                Global.myPlants.get(Global.indexOfPlant).nextPouring=currentTime;
                Global.myPlants.get(Global.indexOfPlant).nextPouringDay=currentDay;
                Global.myPlants.get(Global.indexOfPlant).lastPoured=currentTime;
                Global.myPlants.get(Global.indexOfPlant).lastPouredDay=currDay;
            }
            else if (Global.myPlants.get(Global.indexOfPlant).pouring == 4) {
                currentDay=currDay;
                DaysMaintaining(8);
                nextPouring.setText(currentTime+" "+currentDay);
                Global.myPlants.get(Global.indexOfPlant).nextPouring=currentTime;
                Global.myPlants.get(Global.indexOfPlant).nextPouringDay=currentDay;
                Global.myPlants.get(Global.indexOfPlant).lastPoured=currentTime;
                Global.myPlants.get(Global.indexOfPlant).lastPouredDay=currDay;
            }
            else if (Global.myPlants.get(Global.indexOfPlant).pouring == 5) {
                currentDay=currDay;
                DaysMaintaining(6);
                nextPouring.setText(currentTime+" "+currentDay);
                Global.myPlants.get(Global.indexOfPlant).nextPouring=currentTime;
                Global.myPlants.get(Global.indexOfPlant).nextPouringDay=currentDay;
                Global.myPlants.get(Global.indexOfPlant).lastPoured=currentTime;
                Global.myPlants.get(Global.indexOfPlant).lastPouredDay=currDay;
            }
            else if (Global.myPlants.get(Global.indexOfPlant).pouring == 6) {
                currentDay=currDay;
                DaysMaintaining(5);
                nextPouring.setText(currentTime+" "+currentDay);
                Global.myPlants.get(Global.indexOfPlant).nextPouring=currentTime;
                Global.myPlants.get(Global.indexOfPlant).nextPouringDay=currentDay;
                Global.myPlants.get(Global.indexOfPlant).lastPoured=currentTime;
                Global.myPlants.get(Global.indexOfPlant).lastPouredDay=currDay;
            }
        }
    }
    View.OnClickListener PouringButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            Pouring();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        clientSocket = new Socket(Global.ip, SERVERPORT);
                        BufferedReader inFromServer;
                        String line;
                        send="water on";
                        //+pouringTimes.getText().toString()+" 1";//TODO its for daily only
                        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                        inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                       inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        outToServer.writeBytes(send);
                        outToServer.flush();
                        line = inFromServer.readLine();
                        usedWater.setText(line);
                        clientSocket.close();
                        //izprastame na servera: <water> <1(1 za automatic - 0 za ednokratno)> <pouring times> <daily/weekly/monthly(1,2,3)>
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    };
}




