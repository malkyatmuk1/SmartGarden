package com.example.malkyatmuk.smartgarden;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Start_menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_drawer_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.left_arrow_aqua);
        toolbar.setTitleTextColor(Color.parseColor("#67f7d1"));



        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View headerView=(View) navigationView.getHeaderView(0);
        TableLayout tableLayout=(TableLayout) headerView.findViewById(R.id.table);
        TableRow row=(TableRow)  tableLayout.getChildAt(0);
        TextView txtUsername= (TextView) headerView.findViewById(R.id.usernameProfile);
/*
        BottomNavigationView navigationViewBottom = (BottomNavigationView) navigationView.findViewById(R.id.navigationbottom);
        ImageButton logoutButton= (ImageButton) navigationViewBottom.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(LogoutClickListener);
        */
        TextView txtAdress=(TextView) headerView.findViewById(R.id.adressProfile);

        txtUsername.setText(Global.username);
        if(Global.permission=='a')txtAdress.setText("Administrator");
        else txtAdress.setText("User");



        ImageButton slideMenuButton=(ImageButton) row.findViewById(R.id.slideMenu);
        slideMenuButton.setOnClickListener(SlideMenuListener);

        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.my_address);


    }
    View.OnClickListener LogoutClickListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClassName("com.example.malkyatmuk.smartgarden","com.example.malkyatmuk.smartgarden.SignIn");
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener SlideMenuListener=new View.OnClickListener() {

        public void onClick(View view) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            drawer.closeDrawer(Gravity.LEFT,true);
            //finish();
        }
    };
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }
    private void displaySelectedScreen(int itemId) {
        //creating fragment object
        Fragment fragment = null;
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.my_address:
               fragment = new MyAddress();
                break;
            case R.id.garden_items:

                fragment = new Plant_List();

                break;
            case R.id.contact_us:

                    fragment = new Contact_Us();

                break;
            case R.id.share:

                break;
            case R.id.about_us:
              fragment=new About_Us();
                break;

            case R.id.logoutButton:
                Intent intent = new Intent();
                intent.setClassName("com.example.malkyatmuk.smartgarden","com.example.malkyatmuk.smartgarden.SignIn");
                startActivity(intent);
                finish();
                break;
        }


        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}
