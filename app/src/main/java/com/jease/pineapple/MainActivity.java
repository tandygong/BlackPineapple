package com.jease.pineapple;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.jease.pineapple.base.BaseActivity;

public class MainActivity extends BaseActivity {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        loadMainFragment();
    }


    private void loadMainFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.frame_root);
        if (null == fragment) {
            fragment = MainFragment.newInstance();
        }
        addFragment(fm, fragment, R.id.frame_root);
    }

}
