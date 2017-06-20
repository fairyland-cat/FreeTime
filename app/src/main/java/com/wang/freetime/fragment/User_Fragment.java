package com.wang.freetime.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.freetime.R;
import com.wang.freetime.Utils.Assist;
import com.wang.freetime.Utils.Operation;
import com.wang.freetime.Utils.Variable;
import com.wang.freetime.activity.AccountActivity;
import com.wang.freetime.activity.DetailsActivity;
import com.wang.freetime.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


public class User_Fragment extends Fragment implements AdapterView.OnItemClickListener {

    private TextView user_name;
    private ImageView user_icon;
    private ListView user_menu;
    private ImageButton exit;
    private Context context;
    private user_action action;
    private List<Map<String, Object>> my_menu = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof user_action){
            action=(user_action) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        Operation operation = new Operation(context);
        initmenu();
        user_name = (TextView) view.findViewById(R.id.user_name);
        user_icon = (ImageView) view.findViewById(R.id.user_icon);
        user_menu = (ListView) view.findViewById(R.id.user_menu);
        exit = (ImageButton) view.findViewById(R.id.quit);
        user_icon.setMaxWidth(operation.getWidth() / 4);
        user_name.setOnClickListener(new myOnclick());
        user_icon.setOnClickListener(new myOnclick());

        setUserData();
        user_menu.setAdapter(new SimpleAdapter(context, my_menu, R.layout.item_user_menu,
                new String[]{"menu"}, new int[]{R.id.menu_name}));
        user_menu.setOnItemClickListener(this);
        // Inflate the layout for this fragment
        return view;
    }

    public User_Fragment() {
        // Required empty public constructor
    }

    public void setUserData() {
        final User user = BmobUser.getCurrentUser(User.class);
        if (user != null) {
            user_name.setText(user.getUsername());
            if (user.getIcon() != null) {
                setUser_pic(user.getIcon());
            }
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BmobUser.logOut();
                    setUserData();
                }
            });
            exit.setVisibility(View.VISIBLE);
        } else {
            exit.setVisibility(View.INVISIBLE);
            user_name.setText(R.string.login);
            user_icon.setImageResource(R.drawable.icon);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                User user = BmobUser.getCurrentUser(User.class);
                if (user != null) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("type", Variable.result_save);
                    startActivity(intent);
                } else {
                    startActivityForResult(new Intent(context, AccountActivity.class), Variable.request_login);
                }
                break;
            case 1:
                Intent intent=new Intent(context,DetailsActivity.class);
                intent.putExtra("type",Variable.feed_back);
                startActivity(intent);
                break;
            case 2:
                startPopupwindows(view);
                break;
            case 3:
                action.exit();
                break;
        }
    }

    /**
     * Created by wang on 2017.6.20
     * 启动图片选择裁剪弹窗
     */
    private void startIconCrop(View v){
        final PopupWindow pop;
        View contentview = LayoutInflater.from(context).inflate(R.layout.popupwindows, null, false);
        pop = new PopupWindow(contentview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(getResources().getDrawable(R.color.Photo_color, null));
        pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        pop.setTouchable(true);
        pop.setFocusable(true);
        pop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                pop.dismiss();
                return false;
            }
        });
        Button for_camera,for_photo,cancel;
        for_camera= (Button) contentview.findViewById(R.id.for_camera);
        for_photo= (Button) contentview.findViewById(R.id.for_photo);
        cancel= (Button) contentview.findViewById(R.id.cancel);
        for_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Assist.startCamera(User_Fragment.this);
            }
        });
        for_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Assist.startPhoto(User_Fragment.this);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });
    }

    /**
     * Created by wang on 2017.6.18
     * 关于选项的弹窗
     */
    private void startPopupwindows(View view){
        final PopupWindow pop;
        int width = getResources().getDisplayMetrics().widthPixels;
        View contentview = LayoutInflater.from(context).inflate(R.layout.about_pop, null, false);
        pop = new PopupWindow(contentview, width * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(getResources().getDrawable(R.color.Photo_color, null));
        pop.showAtLocation(view, Gravity.CENTER, 0, 0);
        pop.setTouchable(true);
//        pop.setFocusable(true);
        pop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                pop.dismiss();
                return false;
            }
        });

    }

    /**
     * Created by wang on 2017.6.13
     * 登录点击事件
     */
    class myOnclick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.user_icon:
                    startIconCrop(view);
                    break;
                case R.id.user_name:
                    if (BmobUser.getCurrentUser(User.class) == null) {
                        //启动登陆页面
                        startActivityForResult(new Intent(context, AccountActivity.class), Variable.request_account);
                    }
                    break;
            }
        }
    }

    public interface user_action{
        void exit();
    }

    private void setUser_pic(BmobFile file) {
        final Operation operation = new Operation(context);
        final String path = context.getExternalCacheDir() + File.separator + "bmob" + File.separator + file.getFilename();
        File save_path = new File(path);
        if (!new File(path).exists()) {
            file.download(save_path, new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        user_icon.setImageBitmap(operation.decodeBitmap(path));
                    }
                }

                @Override
                public void onProgress(Integer integer, long l) {

                }
            });
        } else {
            user_icon.setImageBitmap(operation.decodeBitmap(path));
        }
    }

    private void initmenu() {
        String[] value = new String[]{"喜欢的图片", "反馈", "关于", "退出"};
        for (Object i : value) {
            Map<String, Object> map = new HashMap<>();
            map.put("menu", i);
            my_menu.add(map);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Variable.result_true) {
            setUserData();
        }
        switch (requestCode){
            case Variable.request_camera_code:
                Assist.cropphoto(User_Fragment.this, Uri.fromFile(new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"user_icon.jpg")));
                break;
            case Variable.request_photo:
                Assist.cropphoto(User_Fragment.this,data.getData());
                break;
            case Variable.request_crop:
                Operation op=new Operation(context);
                String path=context.getExternalCacheDir()+
                        File.separator+"bmob"+File.separator+"user_icon.jpg";
                final Bitmap icon=op.decodeBitmap(path);
                if (icon!=null){
                   final BmobFile bmobfile=new BmobFile(new File(path));
                    final User user=BmobUser.getCurrentUser(User.class);
                    final User newUser=new User();
                    bmobfile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                newUser.setIcon(bmobfile);
                                newUser.update(user.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        user_icon.setImageBitmap(icon);
                                    }
                                });
                            }else {
                                Toast.makeText(context, "文件上传失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                break;

        }
    }
}
