package com.example.malkyatmuk.smartgarden;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Start_menu<LogoutClickListener> extends AppCompatActivity
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
        View headerView = (View) navigationView.getHeaderView(0);
        TableLayout tableLayout = (TableLayout) headerView.findViewById(R.id.table);
        TableRow row = (TableRow) tableLayout.getChildAt(0);
        TableRow rowSettings = (TableRow) tableLayout.getChildAt(0);
        TextView txtUsername = (TextView) headerView.findViewById(R.id.usernameProfile);
        TextView txtAdress = (TextView) headerView.findViewById(R.id.adressProfile);

        txtUsername.setText(Global.username);
        char c = Global.permission;
        if (c == 'a') txtAdress.setText("Administrator");
        else if(c == 'u')txtAdress.setText("User");
        else txtAdress.setText("No user");

         ImageButton slideMenuButton = (ImageButton) row.findViewById(R.id.slideMenu);
        ImageButton settingsButton = (ImageButton) rowSettings.findViewById(R.id.settingsButton);

        settingsButton.setOnClickListener(NewNicknameOrPassword);
        slideMenuButton.setOnClickListener(SlideMenuListener);
        navigationView.setNavigationItemSelectedListener(this);
        if(Global.fromView)  displaySelectedScreen(R.id.garden_items);
        else displaySelectedScreen(R.id.users);
        //View footerView =(View) navigationViewBottom.findViewById(R.id.)


        //txt.setText(Global.username.toString());

        BottomNavigationView navigation=(BottomNavigationView) findViewById(R.id.navigationBottom);

        MenuItem logoutButton=(MenuItem) navigation.getMenu().findItem(R.id.itemLogout);

        logoutButton.setOnMenuItemClickListener(LogoutClickListener);
        if(Global.fromDeleteShPr==true)
        {
            Global.fromDeleteShPr=false;
            Fragment fragment= new Plant_List();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }
    View.OnClickListener NewNicknameOrPassword=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fragment fragment=new Change_Password();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    };
    SharedPreferences sp;
    void func()
    {
        Global.signedIn=false;
        sp= getSharedPreferences("login", Context.MODE_PRIVATE);
        sp.edit().putBoolean("logged",false).apply();
        Intent intent=new Intent(this,SignIn.class);
        startActivity(intent);
        finish();
    }
    MenuItem.OnMenuItemClickListener LogoutClickListener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            func();
            return false;
        }
    };

    View.OnClickListener SlideMenuListener=new View.OnClickListener() {

        public void onClick(View view) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(Gravity.LEFT,true);
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
    };
    private void displaySelectedScreen(int itemId) {
        //creating fragment object
        Fragment fragment = null;
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.users:

                fragment = new Client_List();

                break;
            case R.id.garden_items:

                fragment = new Plant_List();

                break;
            case R.id.contact_us:

                fragment = new Contact_Us();

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