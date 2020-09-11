package com.example.watcher.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.watcher.R;
import com.example.watcher.Retrofit.RetrofitUtil;
import com.example.watcher.data.User;
import com.example.watcher.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding viewBinding;
    public static Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityLoginBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        initHandler();
        initView();
    }

    private void initView(){
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        String name = pref.getString("name","");
        viewBinding.username.setText(name);

        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("name", User.getInstance().getUserName());
        editor.putString("passwd","");
        editor.apply();

        viewBinding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewBinding.login.getText().equals("已有账号，去登录")){
                    viewBinding.login.setText("登录");
                    viewBinding.passwordConfirm.setVisibility(View.GONE);
                    viewBinding.register.setText("未有账号，去注册");
                }
                //登录
                else{
                    String name = viewBinding.username.getText().toString();
                    String passwd = viewBinding.password.getText().toString();
                    if (name.equals("") || passwd.equals("")){
                        Toast.makeText(MyApplication.getContext(),"用户名和密码不能为空！！",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        RetrofitUtil.getInstance().loginUser(name,passwd);
                    }
                }
            }
        });
        viewBinding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewBinding.register.getText().equals("未有账号，去注册")){
                    viewBinding.login.setText("已有账号，去登录");
                    viewBinding.passwordConfirm.setVisibility(View.VISIBLE);
                    viewBinding.username.setText("");
                    viewBinding.password.setText("");
                    viewBinding.register.setText("注册");
                }
                //注册
                else{
                    String name = viewBinding.username.getText().toString();
                    String passwd = viewBinding.password.getText().toString();
                    String passwdConfirm = viewBinding.passwordConfirm.getText().toString();
                    if (!passwd.equals(passwdConfirm)){
                        Toast.makeText(MyApplication.getContext(),"两次密码输入不一致！请核实",Toast.LENGTH_SHORT).show();
                        return;
                    }else if (name.equals("") || passwd.equals("") || passwdConfirm.equals("")){
                        Toast.makeText(MyApplication.getContext(),"用户名和密码不能为空！！",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        RetrofitUtil.getInstance().registerUser(name,passwd);
                    }
                }
            }
        });
    }

    private void initHandler(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                //成功
                if (msg.what == 1){
                    SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putString("name", User.getInstance().getUserName());
                    editor.putString("passwd",User.getInstance().getPasswd());
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                //名字已被使用
                else if (msg.what == 2){
                    Toast.makeText(MyApplication.getContext(),"用户名已被使用，请更换注册",Toast.LENGTH_SHORT).show();
                }
                //账号不存在
                else if (msg.what == 3){
                    Toast.makeText(MyApplication.getContext(),"账号未注册，请前往注册",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MyApplication.getContext(),"登陆失败，请检查后重试",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}
