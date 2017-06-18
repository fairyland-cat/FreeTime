package com.wang.freetime.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wang.freetime.R;
import com.wang.freetime.Utils.Assist;
import com.wang.freetime.Utils.Variable;
import com.wang.freetime.activity.DetailsActivity;
import com.wang.freetime.adapter.PhotoAdapter;
import com.wang.freetime.adapter.Save_PhotoAdapter;
import com.wang.freetime.model.Photo;
import com.wang.freetime.model.Save_Love;

import java.util.ArrayList;
import java.util.List;

import static com.wang.freetime.R.id.mRecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends Fragment {

    private XRecyclerView recyclerView;
    private Context context;
    private int page=1;
    private PhotoAdapter m_Adapter;
    private List<Photo.ResultsBean> mlist=new ArrayList<>();
    private List<Save_Love> mdata=new ArrayList<>();
    private Photo_Listening myListening;
    private String type=null;
    private myBroadcast mybroadcast;
    private Save_PhotoAdapter save_photoAdapter;

    public PhotoFragment() {
        // Required empty public constructor
    }

    public interface Photo_Listening{
        void upData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        if (context instanceof Photo_Listening){
            myListening=(Photo_Listening) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_hand_work, container, false);
        recyclerView= (XRecyclerView) view.findViewById(mRecyclerView);
        StaggeredGridLayoutManager staggered=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggered);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        if (getArguments()!=null){
            type=getArguments().getString("save");
            Log.d("TAG", "onCreateView: "+type);
        }
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page=1;
                getData(type);
                recyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                page++;
                getData(type);
                recyclerView.loadMoreComplete();
            }
        });
        getData(type);
        mybroadcast=new myBroadcast();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.wang.free_time.photo_fragment.broadcast");
        context.registerReceiver(mybroadcast,intentFilter);
        // Inflate the layout for this fragment
        return view;
    }
    private void getData(@Nullable String type){
        class photo_item_onclick implements PhotoAdapter.OnClickView{

            @Override
            public void OnClick_img(String url) {
                Intent intent=new Intent(context, DetailsActivity.class);
                intent.putExtra("type", Variable.content_photo);
                intent.putExtra("url",url);
                startActivity(intent);
            }
        }
        final photo_item_onclick onclick=new photo_item_onclick();
        if (type!=null){
            Assist.getSave_Photo(new Assist.Requst_Save() {
                @Override
                public void setSave_list(List<Save_Love> list) {
                    Log.d("tag", "setSave_list: "+list);
                    if (page==1){
                        mdata.clear();
                    }
                    mdata.addAll(list);

                    if (save_photoAdapter==null){
                        save_photoAdapter=new Save_PhotoAdapter(context,mdata);
                        save_photoAdapter.setMyOnclick(onclick);
                        recyclerView.setAdapter(save_photoAdapter);
                    }
                    save_photoAdapter.notifyDataSetChanged();
                }
            },page);
        }else {
            Assist.getAssist().getBoon_Photo(new Assist.ResponseListener() {
                @Override
                public void onSuccess(Photo pic) {
                    if (page==1){
                        mlist.clear();
                    }
                    mlist.addAll(pic.getResults());

                    if (m_Adapter==null){
                        m_Adapter=new PhotoAdapter(context,mlist);
                        m_Adapter.setMyOnclick(onclick);
                        recyclerView.setAdapter(m_Adapter);
                    }
                    m_Adapter.notifyDataSetChanged();
                }

                @Override
                public void onFail() {

                }
            },page,"福利");
        }
    }

    class myBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            myListening.upData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(mybroadcast);
    }
}
