package com.darren.mydemo.common;

import android.app.Application;
import android.content.Context;

import com.darren.mydemo.R;
import com.darren.mydemo.bean.Account;
import com.darren.mydemo.manager.PreferencesManager;
import com.darren.mydemo.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.SaveListener;
import cn.sharesdk.framework.ShareSDK;
import okhttp3.OkHttpClient;

/**
 * Created by lenovo on 2017/5/2.
 */

public class BaseApplication extends Application {
    private static final String TAG = "Six";
    private static BaseApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = (BaseApplication) getApplicationContext();
        initOkHttp();
        initData();
        initBmob();
        initShareSDK();
    }

    private void initBmob() {
        //初始化
        Bmob.initialize(this, "8c8d7caa55be568ba20e6b9baa5865f8");
    }

    private void initData() {
        if (PreferencesManager.getInstance(getApplicationContext()).get(Constant.IS_LOGIN, false)) {
            String userPhoto = PreferencesManager.getInstance(getApplicationContext()).get(Constant.USER_PHOTO);
            String userName = PreferencesManager.getInstance(getApplicationContext()).get(Constant.USER_NAME);
            String userPwd = PreferencesManager.getInstance(getApplicationContext()).get(Constant.USER_PWD);
            BmobUser.BmobThirdUserAuth authInfo = (BmobUser.BmobThirdUserAuth) PreferencesManager.getInstance(getApplicationContext()).get(BmobUser.BmobThirdUserAuth.class);
            int loginType = PreferencesManager.getInstance(getApplicationContext()).get(Constant.LOGINTYPE, 0);
            switch (loginType) {
                case Constant.LOGIN_TYPE_NORMAL:
                    loginByUser(userName, userPwd);
                    break;
                case Constant.LOGIN_TYPE_THIRD:
                    loginByThird(authInfo);
                    break;
                default:
                    ToastUtils.shortToast(getApplicationContext(), getString(R.string.auto_login_failed));
                    break;
            }
        }
    }

    private void loginByThird(BmobUser.BmobThirdUserAuth authInfo) {
        BmobUser.loginWithAuthData(getApplicationContext(), authInfo, new OtherLoginListener() {

            @Override
            public void onSuccess(JSONObject userAuth) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                ToastUtils.shortToast(getApplicationContext(), getString(R.string.auto_login_third_failed) + msg);
            }
        });
    }

    private void loginByUser(String userName, String userPwd) {
        //使用BmobSDK提供的登录功能
        Account user = new Account();
        user.setUsername(userName);
        user.setPassword(userPwd);
        user.login(getApplicationContext(), new SaveListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.shortToast(getApplicationContext(), s);
            }
        });
    }

    public static synchronized BaseApplication getInstance() {
        return instance;
    }

    private void initShareSDK() {
        ShareSDK.initSDK(this);
    }

    private void initOkHttp() {
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("HTTP"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }
}
