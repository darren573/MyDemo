package com.darren.mydemo.activity;

import android.os.Bundle;
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

    @BindView(R.id.layout_update)
    LinearLayout layoutUpdate;
    @BindView(R.id.btn_loginout)
    Button btnLoginout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_loginout)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_update:
                break;
            case R.id.btn_loginout:
                loginout();
                break;
        }
    }

    private void loginout() {
        BmobUser.logOut(SettingsActivity.this);   //清除缓存用户对象
        PreferencesManager.getInstance(SettingsActivity.this).put(Constant.IS_LOGIN, false);
        SettingsActivity.this.finish();
    }
}
