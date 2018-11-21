package com.example.malkyatmuk.smartgarden;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by malkyatmuk on 11/12/18.
 */

public class SignUp extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button nextButton= (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(NextButtonListener);

    }
    View.OnClickListener NextButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), Start_menu.class);
            startActivity(intent);
            finish();
        }
    };
}

