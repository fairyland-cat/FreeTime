package com.wang.freetime.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.wang.freetime.R;
import com.wang.freetime.Utils.Variable;
import com.wang.freetime.fragment.HandWork_DetailsFragment;

/**
 * FreeTime
 * Created by wang on 2017.6.9.
 */

public class DetailsActivity extends BaseActivity {
    private Fragment fragment;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        fragmentManager=getSupportFragmentManager();
        loadView(getIntent().getIntExtra("type",Variable.content_handwork));
    }
    //加载合适的fragment
    private void loadView(int type){
        switch (type){
            case Variable.content_handwork:
                fragment=HandWork_DetailsFragment.newInstance(getIntent().getStringExtra("title"),
                        getIntent().getStringExtra("url"));
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.m_fragment,fragment);
                fragmentTransaction.commit();
                break;
            case Variable.content_photo:
                break;

        }
    }

}
