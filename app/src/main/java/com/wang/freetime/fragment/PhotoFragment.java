package com.wang.freetime.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.wang.freetime.R;
import com.wang.freetime.Utils.Assist;
import com.wang.freetime.adapter.PhotoAdapter;
import com.wang.freetime.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends Fragment {

    private XRecyclerView recyclerView;
    private Context context;
    private int page=1;
    private PhotoAdapter m_Adapter;
    private List<Photo.ResultsBean> mlist=new ArrayList<>();

    public PhotoFragment() {
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
        StaggeredGridLayoutManager staggered=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggered);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page=1;
                getData();
                recyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                page++;
                getData();
                recyclerView.refreshComplete();
            }
        });
        getData();
        // Inflate the layout for this fragment
        return view;
    }
    private void getData(){
        Assist.getAssist().getBoon_Photo(new Assist.ResponseListener() {
            @Override
            public void onSuccess(Photo pic) {
                if (page==1){
                    mlist.clear();
                }
                mlist.addAll(pic.getResults());

                if (m_Adapter==null){
                    m_Adapter=new PhotoAdapter(context,mlist);
                    m_Adapter.setMyOnclick(new PhotoAdapter.OnClickView() {
                        @Override
                        public void OnClick_Love(String url) {

                        }

                        @Override
                        public void OnClick_Down(String url) {

                        }
                    });
                    recyclerView.setAdapter(m_Adapter);
                }
                m_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail() {

            }
        },page);
    }

}
