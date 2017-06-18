package com.wang.freetime.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.wang.freetime.R;
import com.wang.freetime.Utils.Variable;
import com.wang.freetime.fragment.HandWork_DetailsFragment;
import com.wang.freetime.fragment.PhotoFragment;
import com.wang.freetime.fragment.Photo_DetailsFragment;
import com.wang.freetime.fragment.Video_DetailsFragment;

/**
 * FreeTime
 * Created by wang on 2017.6.9.
 */

public class DetailsActivity extends BaseActivity implements Video_DetailsFragment.Activity_inf_fragmnet{
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
                fragmentTransaction.replace(R.id.m_fragment,fragment);
                fragmentTransaction.commit();
                break;
            case Variable.content_photo:
                Photo_DetailsFragment photo_detailsFragment=new Photo_DetailsFragment();
                Bundle bundle=new Bundle();
                bundle.putString("url",getIntent().getStringExtra("url"));
                photo_detailsFragment.setArguments(bundle);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.m_fragment,photo_detailsFragment);
                fragmentTransaction.commit();
                break;
            case Variable.result_save:
                PhotoFragment Photo=new PhotoFragment();
                Bundle b=new Bundle();
                b.putString("save","love");
                Photo.setArguments(b);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.m_fragment,Photo);
                fragmentTransaction.commit();
                break;
            case Variable.content_video:
                Video_DetailsFragment video_detailsFragment=new Video_DetailsFragment();
                Bundle url=new Bundle();
                url.putString("url",getIntent().getStringExtra("url"));
                video_detailsFragment.setArguments(url);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.m_fragment,video_detailsFragment);
                fragmentTransaction.commit();
                break;

        }
    }

    @Override
    public void goback() {
        finish();
    }
}
