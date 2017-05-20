package com.darren.mydemo.utils;

import android.content.Intent;

import com.darren.mydemo.R;
import com.darren.mydemo.bean.Account;
import com.darren.mydemo.common.BaseApplication;
import com.darren.mydemo.common.Constant;
import com.darren.mydemo.manager.PreferencesManager;

import org.json.JSONObject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by lenovo on 2017/5/19.
 */

public class LoginUtils {
    public static boolean isLogin(){
        return PreferencesManager.getInstance(BaseApplication.getInstance()).get(Constant.IS_LOGIN,false);
    }

    public static void checkLogin(boolean needLogin){
        if(isLogin()){
            LogUtils.d("是否登录:"+true);
            Account account = BmobUser.getCurrentUser(BaseApplication.getInstance(),Account.class);
            if(account == null){
                //保存了登录信息还没有登录就自动登录，用于初始化时
                autoLogin();
            }
        }else if(needLogin){
            LogUtils.d("是否登录:"+false);
            //需要检查登录状态，没有登录的话跳转到LoginActivity
            jump2Login();
        }
    }

    private static void jump2Login(){
//        BaseApplication.getInstance().startActivity(new Intent(BaseApplication.getInstance(), LoginActivity.class));
        BaseApplication.getInstance().startActivity(new Intent("com.darren.mydemo.activity.LoginActivity").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void autoLogin(){
        String userPhoto = PreferencesManager.getInstance(BaseApplication.getInstance()).get(Constant.USER_PHOTO);
        String userName = PreferencesManager.getInstance(BaseApplication.getInstance()).get(Constant.USER_NAME);
        String userPwd = PreferencesManager.getInstance(BaseApplication.getInstance()).get(Constant.USER_PWD);
        BmobUser.BmobThirdUserAuth authInfo = (BmobUser.BmobThirdUserAuth) PreferencesManager.getInstance(BaseApplication.getInstance()).get(BmobUser.BmobThirdUserAuth.class);
        int loginType = PreferencesManager.getInstance(BaseApplication.getInstance()).get(Constant.LOGINTYPE,0);
        switch (loginType){
            case Constant.LOGIN_TYPE_NORMAL:
                loginByUser(userName,userPwd);
                break;
            case Constant.LOGIN_TYPE_THIRD:
                loginByThird(authInfo);
                break;
            default:
                ToastUtils.shortToast(BaseApplication.getInstance(),BaseApplication.getInstance().getString(R.string.auto_login_failed));
                break;
        }
    }

    public static void loginByThird(BmobUser.BmobThirdUserAuth authInfo) {
        BmobUser.loginWithAuthData(BaseApplication.getInstance(), authInfo, new OtherLoginListener() {

            @Override
            public void onSuccess(JSONObject userAuth) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                ToastUtils.shortToast(BaseApplication.getInstance(), BaseApplication.getInstance().getString(R.string.auto_login_third_failed) + msg);
            }
        });
    }

    public static void loginByUser(String userName, final String userPwd) {
        //使用BmobSDK提供的登录功能
        Account user = new Account();
        user.setUsername(userName);
        user.setPassword(userPwd);
        user.login(BaseApplication.getInstance(), new SaveListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.shortToast(BaseApplication.getInstance(), s);
            }
        });
    }
}

