package fr.sushi.app.util;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import fr.sushi.app.ui.base.BaseFragment;

public class FragmentFunctions {

    public static void commitFragment(int viewId, AppCompatActivity activity, Fragment baseFragment) {
        if (baseFragment == null) return;

        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(viewId, baseFragment, baseFragment.getClass().getName())
                .addToBackStack(baseFragment.getClass().getName())
                .commit();

        // mCurrentFragment = baseFragment;
    }
}

