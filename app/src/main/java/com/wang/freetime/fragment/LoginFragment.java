package com.wang.freetime.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.freetime.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    OnClickView register_click;
    private EditText use_name,pass_word;
    private TextView register;
    private ImageView img_icon;
    public LoginFragment() {
        // Required empty public constructor
    }

    public interface OnClickView{
        void OnClick_Register();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        // Inflate the layout for this fragment
        return view;
    }

    class myOnClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            register_click.OnClick_Register();
        }
    }

    private void initview(View view){
        use_name= (EditText) view.findViewById(R.id.user_name);
        pass_word= (EditText) view.findViewById(R.id.password);
        register= (TextView) view.findViewById(R.id.register);
    }

}
