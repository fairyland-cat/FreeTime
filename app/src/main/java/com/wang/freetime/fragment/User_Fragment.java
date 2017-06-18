package com.wang.freetime.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.wang.freetime.R;
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


public class User_Fragment extends Fragment implements AdapterView.OnItemClickListener {

    private TextView user_name;
    private ImageView user_icon;
    private ListView user_menu;
    private ImageButton exit;
    private Context context;
    private user_action action;
    private List<Map<String, Object>> my_menu = new ArrayList<>();

    public User_Fragment() {
        // Required empty public constructor
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
                    user.logOut();
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
                Log.d("tag", "onItemClick: 反馈");
                break;
            case 2:
                break;
            case 3:
                action.exit();
                break;
        }
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
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof user_action){
            action=(user_action) context;
        }
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
    }
}
