package com.darren.mydemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.darren.mydemo.MainActivity;
import com.darren.mydemo.R;
import com.darren.mydemo.bean.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {
    private Intent intent;
    private EditText et_name, et_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化
        Bmob.initialize(this, "8c8d7caa55be568ba20e6b9baa5865f8");
        findViews();
    }

    private void findViews() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_forget:
                intent = new Intent(LoginActivity.this, UpdatePwdActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void login() {
        String name = et_name.getText().toString();
        String pwd = et_pwd.getText().toString();
        if (name.equals("") || et_name.equals("")) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_LONG).show();
            return;
        }if (pwd.equals("") || et_pwd.equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if (name.length() < 6) {
            Toast.makeText(this, "帐号不能少于小于6位", Toast.LENGTH_LONG).show();
            return;
        }
        if (pwd.length() < 6) {
            Toast.makeText(this, "密码不能少于6位", Toast.LENGTH_LONG).show();
            return;
        }
        BmobUser bu = new BmobUser();
        bu.setUsername(name);
        bu.setPassword(pwd);
        bu.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
