package com.darren.mydemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.darren.mydemo.R;
import com.darren.mydemo.bean.User;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_register_name, et_register_pwd, et_register_email;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
    }

    private void findViews() {
        et_register_name = (EditText) findViewById(R.id.et_register_name);
        et_register_pwd = (EditText) findViewById(R.id.et_register_pwd);
        et_register_email = (EditText) findViewById(R.id.et_register_email);
    }

    public void putOn(View view) {
        switch (view.getId()) {
            case R.id.btn_putOn:
                commit();
                break;
            case R.id.btn_goBack:
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void commit() {
        String name = et_register_name.getText().toString();
        String pwd = et_register_pwd.getText().toString();
        String email = et_register_email.getText().toString();
        if (name.equals("") || et_register_name.equals("")) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if (pwd.equals("") || et_register_pwd.equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if (email.equals("") || et_register_email.equals("")) {
            Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_LONG).show();
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

        BmobUser user = new BmobUser();
        user.setUsername(name);
        user.setPassword(pwd);
        user.setEmail(email);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User users, BmobException e) {
                if (e == null) {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                   final Intent intent =new Intent(RegisterActivity.this,LoginActivity.class);
                    Timer timer=new Timer();
                    TimerTask task=new TimerTask() {
                        @Override
                        public void run() {
                            startActivity(intent);
                        }
                    };
                    timer.schedule(task,100*2);
                } else {
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "e");
                }
            }
        });
    }
    /***
     * 判断邮箱格式是否正确
     */
  /*  private boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }*/
}
