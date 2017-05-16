package com.darren.mydemo.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.darren.mydemo.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UpdatePwdActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_update_back;
    private EditText et_up_name, et_up_new_pwd;
    private Button btn_commit;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        findViews();
    }

    private void findViews() {
        tv_update_back = (TextView) findViewById(R.id.tv_update_back);
        Drawable drawable_go_back = ContextCompat.getDrawable(this, R.drawable.ic_go_back);
        drawable_go_back.setBounds(0, 0, drawable_go_back.getMinimumWidth(), drawable_go_back.getMinimumHeight());
        tv_update_back.setCompoundDrawables(drawable_go_back, null, null, null);
        et_up_name = (EditText) findViewById(R.id.et_up_name);
        et_up_new_pwd = (EditText) findViewById(R.id.et_up_new_pwd);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
        tv_update_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                initData();
                break;
            case R.id.tv_update_back:
                goBack();
                break;
        }
    }

    private void goBack() {
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    private void initData() {
        String username = et_up_name.getText().toString();
        String pwd = et_up_new_pwd.getText().toString();
        if (username == BmobUser.getObjectByKey("username")) {
            BmobUser newUser = new BmobUser();
            newUser.setPassword(pwd);
            BmobUser bmobUser = BmobUser.getCurrentUser();
            newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(UpdatePwdActivity.this, "更新用户信息成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UpdatePwdActivity.this, "更新用户信息失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "您输入的用户名不正确", Toast.LENGTH_SHORT).show();
        }
    }
}
