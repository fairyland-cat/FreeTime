package com.wang.freetime.fragment;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.wang.freetime.R;
import com.wang.freetime.Utils.Operation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class HandWork_DetailsFragment extends Fragment implements View.OnTouchListener{

    private Context context;
    private TextView mTitle,mContent;
    private int content_flg=0x001;
    private Operation operation;
    public HandWork_DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context=getActivity();
    }*/

    /**
     * Created by wang on 2017.6.10
     * 与DetailsActivity通讯
     * 获取展示的标题与链接
     */

    public static HandWork_DetailsFragment newInstance(String title,String url) {

        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("url",url);
        HandWork_DetailsFragment fragment = new HandWork_DetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_hand_work__details, container, false);
        Logger.addLogAdapter(new AndroidLogAdapter());
        operation=new Operation(context);
        mTitle= (TextView) view.findViewById(R.id.h_title);
        mTitle.setText(getArguments().getString("title"));
        mContent= (TextView) view.findViewById(R.id.content);
        mContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        getDetails(getArguments().getString("url"));
        // Inflate the layout for this fragment
        return view;
    }
    private void getDetails(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String useragent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";
                try {
                    Document doc= Jsoup.connect(url).userAgent(useragent).get();
                    Elements elements=doc.select("article.article-content");
                    Logger.d(elements);
                    Message msg=new Message();
                    msg.obj=elements;
                    myHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    Handler myHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            final String content=message.obj.toString();
            final Html.ImageGetter imageGetter=new Html.ImageGetter() {

                @Override
                public Drawable getDrawable(final String s) {
                    String photo=context.getExternalCacheDir()+File.separator+"photo"+File.separator+String.valueOf(s.hashCode())+".jpg";
                    Logger.d(photo);
                    if (new File(photo).exists()){
                        Drawable drawable=Drawable.createFromPath(photo);
                        if (drawable!=null){
                            int ds=(drawable.getIntrinsicHeight()*operation.getWidth())/drawable.getIntrinsicWidth();
                            drawable.setBounds(0,0,operation.getWidth(),ds);
                        }else {
                            drawable=getResources().getDrawable(R.drawable.load,null);
                            drawable.setBounds(0,0,64,64);
                        }
                        /*Bitmap bitmap= BitmapFactory.decodeFile(photo);
                        Drawable drawable=null;
                        if (bitmap!=null){
                            int ds=bitmap.getHeight()/bitmap.getWidth();
                            Logger.d(bitmap.getHeight()+bitmap.getWidth());
                            drawable = new BitmapDrawable(getResources(), bitmap);
                            drawable.setBounds(0,0,operation.getWidth(),operation.getWidth()*ds);
                        }*/
                        return drawable;
                    }else {
                        Drawable drawable=getResources().getDrawable(R.drawable.load,null);
                        drawable.setBounds(0,0,64,64);
                        File file=new File(context.getExternalCacheDir(),"photo");
                        operation.savePicture(s,file);
                        return drawable;
                    }
                }
            };

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mContent.setText(Html.fromHtml(content, content_flg,imageGetter, null));
            }else {
                mContent.setText(Html.fromHtml(content,imageGetter,null));
            }
            operation.setMyListening(new Operation.MyListening() {
                @Override
                public void downOver() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mContent.setText(Html.fromHtml(content,content_flg,imageGetter,null));
                    }else {
                        mContent.setText(Html.fromHtml(content,imageGetter,null));
                    }
                }
            });
            return true;
        }
    });

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
            //通知父控件不要干扰
            view.getParent().requestDisallowInterceptTouchEvent(true);
        }
        if(motionEvent.getAction()==MotionEvent.ACTION_MOVE){
            //通知父控件不要干扰
            view.getParent().requestDisallowInterceptTouchEvent(true);
        }
        if(motionEvent.getAction()==MotionEvent.ACTION_UP){
            view.getParent().requestDisallowInterceptTouchEvent(false);
        }
        return false;
    }

}
