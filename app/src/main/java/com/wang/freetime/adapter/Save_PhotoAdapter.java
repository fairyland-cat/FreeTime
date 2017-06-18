package com.wang.freetime.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wang.freetime.R;
import com.wang.freetime.model.Save_Love;

import java.util.ArrayList;
import java.util.List;

/**
 * FreeTime
 * Created by wang on 2017.6.15.
 */

public class Save_PhotoAdapter extends RecyclerView.Adapter<Save_PhotoAdapter.MyHolder> {

    private Context context;
    private List<Save_Love> mlist=new ArrayList<>();
    private PhotoAdapter.OnClickView myOnclick;

    public Save_PhotoAdapter(Context context, List<Save_Love> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.fragment_photo_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        Glide.with(context).asBitmap().load(mlist.get(position).getUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                holder.m_boon.setImageBitmap(resource);
            }
        });

        holder.m_boon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myOnclick.OnClick_img(mlist.get(position).getUrl());
            }
        });
    }

    public void setMyOnclick(PhotoAdapter.OnClickView myOnclick) {
        this.myOnclick = myOnclick;
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        private ImageView m_boon;

        public MyHolder(View itemView) {
            super(itemView);
            m_boon= (ImageView) itemView.findViewById(R.id.boon);

        }
    }

}
