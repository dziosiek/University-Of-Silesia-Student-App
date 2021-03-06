package com.example.adam.universityofsilesiaapp.fragments_replacement;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.adam.universityofsilesiaapp.R;

public class FragmentReplacement {

    FragmentManager manager;

    public static void pushFragment(Activity activity, int id, Fragment fragment) {
        FragmentManager manager = ((FragmentActivity) activity).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.addToBackStack(null);
        transaction.replace(R.id.startup_frame_layout_id, fragment);
        transaction.commit();
    }

    public static void pushFragment(Activity activity, int id, Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        pushFragment(activity, id, fragment);
    }

    public static <T> T getObjectFromBundle(Bundle bundle, String key) {
        T t = (T) bundle.getSerializable(key);
        return t;
    }
}
