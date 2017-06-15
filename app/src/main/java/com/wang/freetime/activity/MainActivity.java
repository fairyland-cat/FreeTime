package com.wang.freetime.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.wang.freetime.R;
import com.wang.freetime.fragment.HandWorkFragment;
import com.wang.freetime.fragment.PhotoFragment;
import com.wang.freetime.fragment.User_Fragment;
import com.wang.freetime.fragment.VideoFragment;

import java.util.ArrayList;

import cn.bmob.v3.Bmob;

public class MainActivity extends BaseActivity implements PhotoFragment.Photo_Listening{
    private BottomBar bottomBar;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private ArrayList<Fragment> fm_list=new ArrayList<>();
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //生成fragment集合
        create_fm_list();
        //初始化组件
        init_view();
        /**
         * Created by wang on 2017.6.8
         * 设置viewpager适配器
         */
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(3);
        mPager.setCurrentItem(0);
        /**
         * Created by wang on 2017.6.12
         * viewpager滑动监听事件
         */
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        title.setText(R.string.handwork);
                        break;
                    case 1:
                        title.setText(R.string.photo);
                        break;
                    case 2:
                        title.setText(R.string.video);
                        break;
                    case 3:
                        title.setText(R.string.user);
                        break;
                }
                bottomBar.setDefaultTabPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /**
         * Created by wang on 2017.6.8
         * 实现底部菜单的点击事件
         */
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.tab_handwork:
                        mPager.setCurrentItem(0);
                        title.setText(R.string.handwork);
                        break;
                    case R.id.tab_photo:
                        mPager.setCurrentItem(1);
                        title.setText(R.string.photo);
                        break;
                    case R.id.tab_user:

                        mPager.setCurrentItem(3);
                        title.setText(R.string.user);
                        break;
                    case R.id.tab_video:
                        mPager.setCurrentItem(2);
                        title.setText(R.string.video);
                        break;
                }
            }
        });
    }
    private void init_view(){
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mPager= (ViewPager) findViewById(R.id.contentContainer);
        title= (TextView) findViewById(R.id.title);
        mPagerAdapter=new com.wang.freetime.adapter.PagerAdapter(getSupportFragmentManager(),fm_list);
        Bmob.initialize(this,"3f5daa81e7fc346d1d07fef5c2105f58");
    }
    /**
     * Created by wang on 2017.6.8
     * 生成viewpager适配器的数据集合
     */
    private void create_fm_list(){
        HandWorkFragment fragment=new HandWorkFragment();
        fm_list.add(fragment);
        PhotoFragment photoFragment=new PhotoFragment();
        fm_list.add(photoFragment);
        VideoFragment videoFragment=new VideoFragment();
        fm_list.add(videoFragment);
        User_Fragment user_fragment=new User_Fragment();
        fm_list.add(user_fragment);
    }

    @Override
    public void upData() {
        User_Fragment user= (User_Fragment) fm_list.get(3);
        user.setUserData();
    }
}
