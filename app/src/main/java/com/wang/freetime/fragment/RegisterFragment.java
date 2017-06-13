package com.wang.freetime.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.freetime.R;
import com.wang.freetime.model.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private Button register;
    private ImageView user_icon;
    private TextView re_login;
    private EditText user_name,password;
    loginOnClick loginOnClick;
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof loginOnClick){
            loginOnClick= (RegisterFragment.loginOnClick) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_register, container, false);
        initview(view);
        myOnClick onClick=new myOnClick();
        user_icon.setOnClickListener(onClick);
        register.setOnClickListener(onClick);
        re_login.setOnClickListener(onClick);
        // Inflate the layout for this fragment
        return view;
    }

    class myOnClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.login:
                    loginOnClick.login();
                    break;
                case R.id.user_icon:
                    break;
                case R.id.register:
                    String name=user_name.getText().toString();
                    String pass=password.getText().toString();
                    User user=new User();
                    user.setUsername(name);
                    user.setPassword(pass);
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e!=null){
                                loginOnClick.exit();
                            }
                        }
                    });
                    break;
            }
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
