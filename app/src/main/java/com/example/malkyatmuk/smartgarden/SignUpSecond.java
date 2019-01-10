package com.example.malkyatmuk.smartgarden;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class SignUpSecond extends AppCompatActivity {
    ImageButton backButton;
    TextView signedUp;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_second);
        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(BackButtonListener);
        signedUp = (TextView) findViewById(R.id.signedUp);
        signedUp.setOnClickListener(AlreadySignedUp);

    }
    View.OnClickListener AlreadySignedUp=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), SignIn.class);
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener BackButtonListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), SignUp.class);
            startActivity(intent);
            finish();
        }
    };
}
