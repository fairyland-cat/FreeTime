package com.wang.freetime.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wang.freetime.R;
import com.wang.freetime.Utils.Variable;
import com.wang.freetime.activity.DetailsActivity;
import com.wang.freetime.adapter.HandWorkAdapter;
import com.wang.freetime.model.HandWork_One;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * FreeTime
 * Created by wang on 2017.6.8.
 */

public class HandWorkFragment extends Fragment {
    private ArrayList<HandWork_One> mdata=new ArrayList();
    private int Pager=1;
    private XRecyclerView mRecyclerView;
    private HandWorkAdapter mAdapter;
    private Context mContext;
    private Boolean isRead=false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_hand_work,container,false);
        mRecyclerView= (XRecyclerView) v.findViewById(R.id.mRecyclerView);
        LinearLayoutManager mLayout=new LinearLayoutManager(mContext);
        mLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayout);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);

        getPaper();
        //实现上拉加载，下拉刷新
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                Pager=1;
                getPaper();
                mRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                Pager++;
                getPaper();
                mRecyclerView.refreshComplete();
            }
        });
        return v;
    }
    /**
     * Created by wang on 2017.6.8
     *创建handler，并为list设置适配器
     */
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (Pager==1){
                mdata.clear();
            }
            mdata.addAll((Collection<? extends HandWork_One>) message.obj);
            if (mAdapter==null){
                mAdapter=new HandWorkAdapter(mdata,mContext);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setmClickItem(new HandWorkAdapter.onClickItem() {
                    @Override
                    public void onClick(int position) {
                        checkPermission();
                        if (isRead){
                            Intent intent=new Intent(mContext, DetailsActivity.class);
                            intent.putExtra("title",mdata.get(position).getTitle());
                            intent.putExtra("url",mdata.get(position).getLink());
                            intent.putExtra("type", Variable.content_handwork);
                            startActivity(intent);
                        }else {
                            Toast.makeText(mContext, "请开启读取权限", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
            mAdapter.notifyDataSetChanged();
            return true;
        }
    });
    /**
     * Created by wang on 2017.6.8
     * 获取手工艺数据
     */
    public void getPaper()  {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<HandWork_One> mHandwork=new ArrayList();
                String url="http://www.shouyihuo.com/paper/list93_"+Pager+".html";
                String useragent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";
                Document doc= null;
                try {
                    doc = Jsoup.connect(url).userAgent(useragent).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements paper=doc.select("li.related_box");
                for (Element e:paper) {
                    String titles=e.select("span.r_title").text();
                    String imgs=e.select("img[src$=.jpg]").attr("src");
                    String links=e.select("a[href$=.html]").attr("href");
                    String read=e.select("span.post-view").text();
                    String love=e.select("a#love").text();
                    Log.d("Tag",titles+imgs+links+read+love);
                    mHandwork.add(new HandWork_One(imgs,links,titles,read,love));
                }
                Message msg=new Message();
                msg.obj=mHandwork;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Variable.request_code);
        }else {
            isRead=true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Variable.request_code)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                isRead=true;
            } else
            {
                // Permission Denied
                Toast.makeText(mContext, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
