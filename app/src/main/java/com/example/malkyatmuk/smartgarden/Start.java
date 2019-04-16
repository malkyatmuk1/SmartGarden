package com.example.malkyatmuk.smartgarden;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        TextView beforeGreen=(TextView)findViewById(R.id.BeforeGarden);
        String first = "Let's check your ";
        String next = "<font color='#449b4e'>Garden</font>";
        beforeGreen.setText(Html.fromHtml(first + next));
        Button LetsGoButton= (Button) findViewById(R.id.letsGo);
        LetsGoButton.setOnClickListener(LetsGoButtonListener);

    }
    View.OnClickListener LetsGoButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            SharedPreferences sp;
            sp= getSharedPreferences("login", Context.MODE_PRIVATE);
            Global.ip=sp.getString("ip","");
            Global.setIP(Global.ip,getApplicationContext());
            if(sp.getBoolean("logged",false)){
                Global.username=sp.getString("username","");
                Global.password=sp.getString("password","");
                if(sp.getString("permission","").equals("a"))
                {
                    Global.permission='a';
                }
                else if(sp.getString("permission","").equals("n"))
                {
                    Global.permission='n';
                }
                else
                {
                    Global.permission='u';
                }
                Intent intent = new Intent(getApplicationContext(), Start_menu.class);
                startActivity(intent);
                finish();
            }
            else {
                Intent intent = new Intent(view.getContext(), UPorIn.class);
                startActivity(intent);
                finish();
            }
        }
    };
}
