package com.example.malkyatmuk.smartgarden;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by malkyatmuk on 11/12/18.
 */

public class SignUp extends Activity {
    ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button nextButton= (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(NextButtonListener);

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(BackButtonListener);
    }
    View.OnClickListener BackButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), UPorIn.class);
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener NextButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), SignUpSecond.class);
            startActivity(intent);
            finish();
        }
    };
}

