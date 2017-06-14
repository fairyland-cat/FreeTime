package com.wang.freetime.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.wang.freetime.R;
import com.wang.freetime.Utils.Variable;
import com.wang.freetime.fragment.LoginFragment;
import com.wang.freetime.fragment.RegisterFragment;

public class AccountActivity extends BaseActivity implements LoginFragment.OnClickView,
        RegisterFragment.loginOnClick{
    private FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initview();
    }

    private void initview(){
        manager=getSupportFragmentManager();
        loadlogin();
    }

    private void loadlogin(){
        LoginFragment loginFragment=new LoginFragment();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.account,loginFragment);
        transaction.commit();
    }

    private void loadregister(){
        RegisterFragment registerFragment=new RegisterFragment();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.account,registerFragment);
        transaction.commit();
    }

    @Override
    public void OnClick_Register() {
        loadregister();
    }

    @Override
    public void login() {
        loadlogin();
    }

    @Override
    public void exit() {
        setResult(Variable.result_true);
        finish();
    }
}
