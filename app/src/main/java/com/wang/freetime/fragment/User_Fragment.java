package com.wang.freetime.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.wang.freetime.R;
import com.wang.freetime.Utils.Operation;
import com.wang.freetime.Utils.Variable;
import com.wang.freetime.activity.AccountActivity;
import com.wang.freetime.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;


public class User_Fragment extends Fragment {

    private TextView user_name;
    private ImageView user_icon;
    private ListView user_menu;
    private Context context;
    private List<Map<String,Object>> my_menu=new ArrayList<>();

    public User_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user, container, false);
        Operation operation=new Operation(context);
        initmenu();
        user_name= (TextView) view.findViewById(R.id.user_name);
        user_icon= (ImageView) view.findViewById(R.id.user_icon);
        user_menu= (ListView) view.findViewById(R.id.user_menu);
        user_icon.setMaxWidth(operation.getWidth()/4);
        user_name.setOnClickListener(new myOnclick());
        user_menu.setOnItemClickListener(new myItemOnClick());
        user_icon.setOnClickListener(new myOnclick());

        User user= BmobUser.getCurrentUser(User.class);
        if (user!=null){
            user_name.setText(user.getUsername());
            setUser_pic(user.getIcon());
        }
        user_menu.setAdapter(new SimpleAdapter(context,my_menu,R.layout.item_user_menu,
                new String[]{"menu"},new int[]{R.id.menu_name}));
        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Created by wang on 2017.6.13
     * 登录点击事件
     */
    class myOnclick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.user_icon:
                    break;
                case R.id.user_name:
                    if (BmobUser.getCurrentUser(User.class)==null){
                        //启动登陆页面
                        startActivityForResult(new Intent(context, AccountActivity.class), Variable.request_account);
                    }
                    break;
            }
        }
    }

    /**
     * Created by wang on 2017.6.13
     * 我的菜单点击事件
     */
    class myItemOnClick implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
    private void setUser_pic(BmobFile file){
        Operation operation=new Operation(context);
        File save_path=new File(context.getExternalCacheDir()+File.separator+"bmob");
        String path=context.getExternalCacheDir()+File.separator+"bmob"+File.separator+file.getFilename();
        if (new File(path).exists()){
            user_icon.setImageBitmap(operation.decodeBitmap(path));
        }
    }

    private void initmenu(){
        String[] value=new String[]{"喜欢的图片","反馈","关于","退出"};
        for (Object i:value) {
            Map<String,Object> map=new HashMap<>();
            map.put("menu",i);
            my_menu.add(map);
        }

    }

}
