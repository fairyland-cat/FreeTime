package com.wang.freetime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang.freetime.R;
import com.wang.freetime.model.Photo;

import java.util.List;

/**
 * FreeTime
 * Created by wang on 2017.6.12.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyHolder> {

    private Context context;
    private List<Photo.ResultsBean> mdata;
    private Video_OnItemClick onItemClick;


    public VideoAdapter(Context context, List<Photo.ResultsBean> mdata) {
        this.context = context;
        this.mdata = mdata;
    }

    @Override
    public VideoAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_fragment_video,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        holder.name.setText(mdata.get(position).getDesc());
        holder.time.setText(mdata.get(position).getCreatedAt().substring(0,9));
        holder.myItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.ClickItem(mdata.get(position).getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }
    public interface Video_OnItemClick{
        void ClickItem(String url);
    }

    public void setOnItemClick(Video_OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    class MyHolder extends RecyclerView.ViewHolder{
        private TextView name,time;
        private RelativeLayout myItem;
        public MyHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.video_name);
            time= (TextView) itemView.findViewById(R.id.create_time);
            myItem= (RelativeLayout) itemView.findViewById(R.id.video_item);
        }
    }
}
