package com.darren.mydemo.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.darren.mydemo.R;
import com.darren.mydemo.common.Constant;
import com.darren.mydemo.manager.PreferencesManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_update:
                action();
                break;
            case R.id.btn_loginout:
                loginout();
                break;
        }
    }

    private void action() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this)
                .setTitle("友情提示")
                .setMessage("当前版本为最高版本\n从此以后再无更新 23333")
                .setPositiveButton("确定", null);
        builer.create().show();
    }

    private void loginout() {
        BmobUser.logOut(SettingsActivity.this);   //清除缓存用户对象
        PreferencesManager.getInstance(SettingsActivity.this).put(Constant.IS_LOGIN, false);
        SettingsActivity.this.finish();
    }
}
