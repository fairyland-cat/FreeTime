package com.wang.freetime.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wang.freetime.R;
import com.wang.freetime.model.Photo;

import java.util.List;

/**
 * FreeTime
 * Created by wang on 2017.6.11.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyHolder> {

    private Context mContext;
    private List<Photo.ResultsBean> mdata;
    private OnClickView myOnclick;

    public PhotoAdapter(Context mContext, List<Photo.ResultsBean> mdata) {
        this.mContext = mContext;
        this.mdata = mdata;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.fragment_photo_item,parent,false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        Glide.with(mContext).asBitmap().load(mdata.get(position).getUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                holder.m_boon.setImageBitmap(resource);
            }
        });
        MyOnclick myOnclick=new MyOnclick(mdata.get(position).getUrl());
        holder.m_love.setOnClickListener(myOnclick);
        holder.m_down.setOnClickListener(myOnclick);
    }
    class MyOnclick implements View.OnClickListener{
        private String url;

        public MyOnclick(String url) {
            this.url = url;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.photo_love:
                    myOnclick.OnClick_Love(url);
                    break;
                case R.id.photo_down:
                    myOnclick.OnClick_Down(url);
                    break;
            }
        }
    }

    public interface OnClickView{
        void OnClick_Love(String url);
        void OnClick_Down(String url);
    }

    public void setMyOnclick(OnClickView myOnclick) {
        this.myOnclick = myOnclick;
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        private ImageView m_boon;
        private ImageButton m_love,m_down;

        public MyHolder(View itemView) {
            super(itemView);
            m_boon= (ImageView) itemView.findViewById(R.id.boon);
            m_down= (ImageButton) itemView.findViewById(R.id.photo_down);
            m_love= (ImageButton) itemView.findViewById(R.id.photo_love);
        }
    }
}
