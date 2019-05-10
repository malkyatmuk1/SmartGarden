package com.example.malkyatmuk.smartgarden;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by malkyatmuk on 11/17/18.
 */

public class Contact_Us extends Fragment {
    Button sendButton;
    EditText emailAdress, textEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View myFragmentView = inflater.inflate(R.layout.fragment_contact_us, container, false);
        return myFragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Contact Us");
        sendButton=(Button)getView().findViewById(R.id.submitButton);
        emailAdress=(EditText)getView().findViewById(R.id.email);
        textEmail=(EditText)getView().findViewById(R.id.textEmail);
        sendButton.setOnClickListener(SendEmailListener);
    }
    View.OnClickListener SendEmailListener=new View.OnClickListener() {

        public void onClick(View view) {

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"smartgardendata@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
            i.putExtra(Intent.EXTRA_TEXT   , "body of email");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            }
            catch (android.content.ActivityNotFoundException ex) {}
        }

    };
}