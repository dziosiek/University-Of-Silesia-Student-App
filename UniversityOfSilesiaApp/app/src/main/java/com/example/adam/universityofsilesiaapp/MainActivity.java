package com.example.adam.universityofsilesiaapp;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.adam.universityofsilesiaapp.fragments.main_panels.schedule.Schedule;
import com.example.adam.universityofsilesiaapp.fragments.startup_panels.Login;
import com.example.adam.universityofsilesiaapp.fragments_replacement.FragmentReplacement;
import com.example.adam.universityofsilesiaapp.resources.User;
import com.example.adam.universityofsilesiaapp.variables.GlobalVariables;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalVariables.setApiUrl("http://192.168.6.82:8080");
        FragmentReplacement.pushFragment(MainActivity.this,R.id.startup_frame_layout_id,new Login());


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
