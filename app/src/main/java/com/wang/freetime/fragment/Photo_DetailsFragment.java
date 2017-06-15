package com.wang.freetime.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wang.freetime.R;
import com.wang.freetime.Utils.Assist;
import com.wang.freetime.Utils.Operation;
import com.wang.freetime.Utils.Variable;
import com.wang.freetime.activity.AccountActivity;
import com.wang.freetime.model.Save_Love;
import com.wang.freetime.model.User;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class Photo_DetailsFragment extends Fragment {

    private Context context;
    private ImageButton down,save;
    private Boolean isSave;
    private String photo_id;
    public Photo_DetailsFragment() {
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
        View v=inflater.inflate(R.layout.fragment_photo__details, container, false);
        ImageView img= (ImageView) v.findViewById(R.id.big_img);
        down= (ImageButton) v.findViewById(R.id.photo_down);
        save= (ImageButton) v.findViewById(R.id.photo_love);
        MyOnclick onclick=new MyOnclick(getArguments().getString("url"));
        down.setOnClickListener(onclick);
        save.setOnClickListener(onclick);
        Glide.with(context).load(getArguments().getString("url")).into(img);
        Assist.getAssist().setSave(new Assist.is_Save() {
            @Override
            public void issave(String id) {
                isSave=true;
                photo_id=id;
                save.setImageResource(R.drawable.save);
            }

            @Override
            public void unsave() {
                isSave=false;
                save.setImageResource(R.drawable.no_save);
            }
        });
        Assist.isSave(getArguments().getString("url"));
        // Inflate the layout for this fragment
        return v;
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
                    BmobUser user = BmobUser.getCurrentUser(User.class);
                    if (user!=null){
                        if (isSave) {
                            final Save_Love love = new Save_Love();
                            love.setObjectId(photo_id);
                            love.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        save.setImageResource(R.drawable.no_save);
                                        Toast.makeText(context, "取消收藏", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            final Save_Love love = new Save_Love();
                            love.setUrl(url);
                            love.setUser(user.getUsername());
                            love.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        save.setImageResource(R.drawable.save);
                                        Toast.makeText(context, "已收藏", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }else {
                        startActivityForResult(new Intent(context, AccountActivity.class),Variable.request_login);
                    }
                    break;
                case R.id.photo_down:
                    final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "boon");
                    Operation.savePicture(url, file);
                    Operation.setMyListening(new Operation.MyListening() {
                        @Override
                        public void downOver() {
                            Toast.makeText(context, "图片已存储到" + file, Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Variable.result_true){
            context.sendBroadcast(new Intent("com.wang.free_time.photo_fragment.broadcast"));
        }
    }
}
