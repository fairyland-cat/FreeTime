package com.wang.freetime.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

/**
 * FreeTime
 * Created by wang on 2017.6.8.
 */

public class BaseActivity extends AppCompatActivity {
    private ArrayList<Activity> mActivity=new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Logger.addLogAdapter(new AndroidLogAdapter());
        mActivity.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivity.remove(this);
    }
}
