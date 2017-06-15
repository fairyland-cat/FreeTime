package com.wang.freetime.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.wang.freetime.R;
import com.wang.freetime.model.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    OnClickView register_click;
    private EditText use_name,pass_word;
    private TextView register;
    private Button log_in;
    private Context context;
    public LoginFragment() {
        // Required empty public constructor
    }

    public interface OnClickView{
        void OnClick_Register();
        void exit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        if (context instanceof OnClickView){
            register_click= (OnClickView) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login, container, false);
        initview(view);
        myOnClick OnClick=new myOnClick();
        register.setOnClickListener(OnClick);
        log_in.setOnClickListener(OnClick);
        // Inflate the layout for this fragment
        return view;
    }

    class myOnClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.register:
                    register_click.OnClick_Register();
                    break;
                case R.id.log_in:
                    String name=use_name.getText().toString();
                    String pass=pass_word.getText().toString();
                    User user=new User();
                    user.loginByAccount(name, pass, new LogInListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e==null){
                                register_click.exit();
                                Toast.makeText(context, "登陆成功", Toast.LENGTH_SHORT).show();
                            }else {
                                Logger.d(e);
                                Toast.makeText(context, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
            }
        }
    }

    private void initview(View view){
        use_name= (EditText) view.findViewById(R.id.user_name);
        pass_word= (EditText) view.findViewById(R.id.password);
        register= (TextView) view.findViewById(R.id.register);
        log_in= (Button) view.findViewById(R.id.log_in);
    }

}
