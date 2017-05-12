package com.darren.mydemo.activity;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.darren.mydemo.MainActivity;
import com.darren.mydemo.R;
import com.darren.mydemo.bean.User;

import java.util.HashMap;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.weibo.TencentWeibo;

public class LoginActivity extends AppCompatActivity implements PlatformActionListener {
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
            case R.id.Ib_weibo:
                loginBySina();
                break;
            case R.id.Ib_QQ:
                loginByQQ();
                break;
            case R.id.Ib_tencentweibo:
                loginByTencentweibo();
                break;
        }
    }

    private void loginByTencentweibo() {
        Platform tencentWeibo = ShareSDK.getPlatform(this, TencentWeibo.NAME);
        tencentWeibo.setPlatformActionListener(this);
        //authorize与showUser单独调用一个即可
        tencentWeibo.authorize();//单独授权,OnComplete返回的hashmap是空的
        //pay.showUser(null);//授权并获取用户信息
        //移除授权
        //pay.removeAccount(true);
    }

    private void loginByQQ() {
        Platform qq = ShareSDK.getPlatform(this, QQ.NAME);
        qq.setPlatformActionListener(this);
        //authorize与showUser单独调用一个即可
        qq.authorize();//单独授权,OnComplete返回的hashmap是空的
        //qq.showUser(null);//授权并获取用户信息
        //移除授权
        //qq.removeAccount(true);
    }

    private void loginBySina() {
        Platform weibo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
        weibo.setPlatformActionListener(this);
        //authorize与showUser单独调用一个即可
        weibo.authorize();//单独授权,OnComplete返回的hashmap是空的
        //weibo.showUser(null);//授权并获取用户信息
        //移除授权
        //weibo.removeAccount(true);
    }

    private void login() {
        String name = et_name.getText().toString();
        String pwd = et_pwd.getText().toString();
        if (name.equals("") || et_name.equals("")) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if (pwd.equals("") || et_pwd.equals("")) {
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

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Looper.prepare();
        String result = platform.getDb().exportData();
        Log.i("result",result);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        throwable.printStackTrace();
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Toast.makeText(this, "授权取消", Toast.LENGTH_SHORT).show();
    }
}
