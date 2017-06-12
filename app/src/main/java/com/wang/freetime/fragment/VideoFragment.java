package com.wang.freetime.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wang.freetime.R;
import com.wang.freetime.Utils.Assist;
import com.wang.freetime.adapter.VideoAdapter;
import com.wang.freetime.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

    private XRecyclerView recyclerView;
    private Context context;
    private int page=1;
    private VideoAdapter myAdapter;
    private List<Photo.ResultsBean> mList=new ArrayList<>();

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_hand_work, container, false);
        recyclerView= (XRecyclerView) view.findViewById(R.id.mRecyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page=1;
                getVideo();
                recyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                page++;
                getVideo();
                recyclerView.loadMoreComplete();
            }
        });
        getVideo();
        // Inflate the layout for this fragment
        return view;
    }

    private void getVideo(){
        Assist.getAssist().getBoon_Photo(new Assist.ResponseListener() {
            @Override
            public void onSuccess(Photo pic) {
                if (page==1){
                    mList.clear();
                }
                mList.addAll(pic.getResults());
                if (myAdapter==null){
                    myAdapter=new VideoAdapter(context,mList);
                    recyclerView.setAdapter(myAdapter);
                    myAdapter.setOnItemClick(new VideoAdapter.Video_OnItemClick() {
                        @Override
                        public void ClickItem(String url) {

                        }
                    });
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail() {

            }
        },page,"休息视频");
    }

}
