package com.example.adam.universityofsilesiaapp.fragments_replacement;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.adam.universityofsilesiaapp.MainActivity;
import com.example.adam.universityofsilesiaapp.R;

public class FragmentReplacement {
    FragmentManager manager;

    public static void pushFragment(Activity activity, int id, Fragment fragment) {
        FragmentManager manager = ((FragmentActivity) activity).getSupportFragmentManager();



//        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.startup_frame_layout_id, fragment);
        transaction.commit();
    }
}
