package com.example.malkyatmuk.smartgarden;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class IP extends AppCompatActivity {
    EditText ip;
    Button apply;
    TextView goback;
    ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);

        backButton=(ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(gobacklistener);
        ip=(EditText) findViewById(R.id.ip);
        goback=(TextView) findViewById(R.id.goback);
        goback.setOnClickListener(gobacklistener);
        apply= (Button) findViewById(R.id.applyIpButton);
        apply.setOnClickListener(apllylistener);
    }
    View.OnClickListener gobacklistener=new View.OnClickListener() {

        public void onClick(View view) {
            if(Global.goback==true) {
                Intent intent = new Intent(view.getContext(), SignIn.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Intent intent = new Intent(view.getContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        }
    };
    View.OnClickListener apllylistener=new View.OnClickListener() {

        public void onClick(View view) {
            Global.ip=ip.getText().toString();
            SharedPreferences sp;
            sp= getSharedPreferences("login", Context.MODE_PRIVATE);
            sp.edit().putString("ip",Global.ip).apply();
            Global.setIP(Global.ip,getApplicationContext());
            Toast toast=Toast.makeText(getApplicationContext(),"The IP was set",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
            toast.show();
        }
    };
}