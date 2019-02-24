package com.example.adam.universityofsilesiaapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import com.example.adam.universityofsilesiaapp.fragments.startup_panels.Login;
import com.example.adam.universityofsilesiaapp.fragments_replacement.FragmentReplacement;
import com.example.adam.universityofsilesiaapp.variables.GlobalVariables;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalVariables.setApiUrl("http://192.168.1.105:8080");
        FragmentReplacement.pushFragment(MainActivity.this, R.id.startup_frame_layout_id, new Login());

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
