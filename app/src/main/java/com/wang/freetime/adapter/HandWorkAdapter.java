package com.wang.freetime.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wang.freetime.R;
import com.wang.freetime.model.HandWork_One;

import java.util.ArrayList;

/**
 * FreeTime
 * Created by wang on 2017.6.8.
 */

public class HandWorkAdapter extends RecyclerView.Adapter<HandWorkAdapter.MyHolder> {

    private ArrayList<HandWork_One> list;
    private Context context;
    private onClickItem mClickItem;
    private int[] mColor=new int[]{R.color.one_color,R.color.two_color,
            R.color.three_color,R.color.four_color,R.color.five_color};

    public HandWorkAdapter(ArrayList<HandWork_One> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_handwork,parent,false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        HandWork_One mhandwork=list.get(position);
        holder.title.setText(mhandwork.getTitle());
        holder.love.setText(mhandwork.getLovecount());
        Drawable drawable=context.getResources().getDrawable(R.drawable.love,null);
        drawable.setBounds(0,0,64,64);
        holder.love.setCompoundDrawables(drawable,null,null,null);
        holder.read.setText(mhandwork.getReadcount());
        Glide.with(context).load(mhandwork.getImg()).into(holder.img);
        if (position<mColor.length){
            holder.mItem.setBackgroundResource(mColor[position]);
        }else {
            holder.mItem.setBackgroundResource(mColor[position%mColor.length]);
        }

        holder.mItem.setOnClickListener(new myClick(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView title,read,love;
        RelativeLayout mItem;

        public MyHolder(View itemView) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.imageView);
            title= (TextView) itemView.findViewById(R.id.title);
            read= (TextView) itemView.findViewById(R.id.read);
            love= (TextView) itemView.findViewById(R.id.love);
            mItem= (RelativeLayout) itemView.findViewById(R.id.item_handwork);
        }
    }

    public void setmClickItem(onClickItem mClickItem) {
        this.mClickItem = mClickItem;
    }

    public interface onClickItem{
        void onClick(int position);
    }
    class myClick implements View.OnClickListener{

        private int position;

        public myClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            mClickItem.onClick(position);
        }
    }
}
