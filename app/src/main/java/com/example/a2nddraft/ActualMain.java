package com.example.a2nddraft;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;



import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class ActualMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ImageView call;

    BottomNavigationView.OnNavigationItemSelectedListener nav = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    //  toolbar.setTitle("Home");
                    loadFragment(new Home());
                    return true;
                case R.id.who:
                    // toolbar.setTitle("GET PASS");
                    loadFragment(new RoomNews());
                    return true;
                case R.id.getpass:
                    loadFragment(new getpass());
                    return true;

            }
            return false;
        }
    };
    NavigationView.OnNavigationItemSelectedListener nav2 = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.India:
                    loadFragment(new IndiaFargment());
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                case R.id.world:
                    // toolbar.setTitle("GET PASS");
                    loadFragment(new WorldFargment());
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                case R.id.Donate:
                    loadFragment(new Donate());
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                case R.id.faq:
                    loadFragment(new FAQFargment());
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                case R.id.immunity:
                    loadFragment(new ImmunityFargment());
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
            }
            return false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_main);

        FloatingActionButton floatingActionButton = findViewById(R.id.chat);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActualMain.this, ChatActivity.class);
                startActivity(i);
            }
        });

        //Alarm Manager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent p = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, p, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0 ,1000*60*30, pendingIntent);
        call = (ImageButton)findViewById(R.id.tot);
        call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:+91-11-23978046"));
                    startActivity(callIntent);
                }catch (Exception e){
                    Log.e("Calling a Phone Number", "Call failed", e);
                }
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(nav2);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.topbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(nav);
        toolbar.setTitle("HOME");
        loadFragment(new Home());
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(ActualMain.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}

