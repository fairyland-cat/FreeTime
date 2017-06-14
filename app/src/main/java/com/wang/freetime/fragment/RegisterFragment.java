package com.wang.freetime.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wang.freetime.R;
import com.wang.freetime.Utils.Assist;
import com.wang.freetime.Utils.Operation;
import com.wang.freetime.Utils.Variable;
import com.wang.freetime.model.User;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private Button register;
    private ImageView user_icon;
    private TextView re_login;
    private EditText user_name,password;
    loginOnClick loginOnClick;
    private Context context;
    private String icon_path=null;
    private BmobFile bmobfile=null;
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        if (context instanceof loginOnClick){
            loginOnClick= (RegisterFragment.loginOnClick) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_register, container, false);
        initview(view);
        myOnClick onClick=new myOnClick(view);
        user_icon.setOnClickListener(onClick);
        register.setOnClickListener(onClick);
        re_login.setOnClickListener(onClick);
        // Inflate the layout for this fragment
        return view;
    }

    class myOnClick implements View.OnClickListener{

        private View v;

        public myOnClick(View view) {
            v = view;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.login:
                    loginOnClick.login();
                    break;
                case R.id.icon:
                    if (checkPermission()){
                        startPopupwindows(v);
                    }
                    break;
                case R.id.register:
                    String name=user_name.getText().toString();
                    String pass=password.getText().toString();
                    User user=new User();
                    user.setUsername(name);
                    user.setPassword(pass);
                    user.setEmail(name);
                    if (bmobfile!=null){
                        user.setIcon(bmobfile);
                    }
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e!=null){
                                loginOnClick.exit();
                            }else {
                                Log.d("注册", "done: "+e.toString());
                            }
                        }
                    });
                    break;
            }
        }
    }

    private Boolean checkPermission(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CAMERA},
                    Variable.request_camera);
        }else {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
            startPopupwindows(LayoutInflater.from(context).inflate(R.layout.fragment_register,null,false));
        }
    }
    //启动选择头像获取方式的弹窗
    private void startPopupwindows(View v){
        View view=LayoutInflater.from(context).inflate(R.layout.popupwindows,null,false);
        Button for_camera,for_photo,cancel;
        for_camera= (Button) view.findViewById(R.id.for_camera);
        for_photo= (Button) view.findViewById(R.id.for_photo);
        cancel= (Button) view.findViewById(R.id.cancel);
        final PopupWindow popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.Photo_color,null));
        popupWindow.showAtLocation(v, Gravity.BOTTOM,0,0);

        for_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Assist.startCamera(RegisterFragment.this);
            }
        });
        for_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Assist.startPhoto(RegisterFragment.this);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Variable.request_camera_code:
                Assist.cropphoto(RegisterFragment.this, Uri.fromFile(new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"user_icon.jpg")));
                break;
            case Variable.request_photo:
                Assist.cropphoto(RegisterFragment.this,data.getData());
                break;
            case Variable.request_crop:
                Operation op=new Operation(context);
                String path=context.getExternalCacheDir()+
                        File.separator+"bmob"+File.separator+"user_icon.jpg";
                Bitmap icon=op.decodeBitmap(path);
                if (icon!=null){
                    user_icon.setImageBitmap(icon);
                    bmobfile=new BmobFile(new File(path));
                    bmobfile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){

                            }
                        }
                    });
                }

                break;

        }
        }

    public interface loginOnClick{
        void login();
        void exit();
    }

    private void initview(View view){
        register= (Button) view.findViewById(R.id.register);
        user_icon= (ImageView) view.findViewById(R.id.icon);
        re_login= (TextView) view.findViewById(R.id.login);
        user_name= (EditText) view.findViewById(R.id.user_name);
        password= (EditText) view.findViewById(R.id.password);
    }

}
