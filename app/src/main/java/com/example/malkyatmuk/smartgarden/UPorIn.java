package com.example.malkyatmuk.smartgarden;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UPorIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_or_in);
        TextView beforeGreen=(TextView)findViewById(R.id.checkingIsEasy);
        String first= " Now checking ";
        String second = "<font color='#449b4e'><b>Garden</b></font>";
        String third= '\n'+" online is easy!";
        beforeGreen.setText(Html.fromHtml(first + second+ third));
        Button SignUpButton= (Button) findViewById(R.id.SignUpButton);
        SignUpButton.setOnClickListener(SignUpButtonListener);

    }
    View.OnClickListener SignUpButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), SignUp.class);
            startActivity(intent);
            finish();
        }
    };
}
