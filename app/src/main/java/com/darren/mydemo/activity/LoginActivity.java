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
import com.darren.mydemo.bean.Account;
import com.darren.mydemo.bean.User;
import com.darren.mydemo.common.Constant;
import com.darren.mydemo.utils.LogUtils;
import com.darren.mydemo.utils.ToastUtils;

import org.json.JSONObject;

import java.util.HashMap;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.weibo.TencentWeibo;

public class LoginActivity extends AppCompatActivity implements   PlatformActionListener{
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
            case R.id.btn_phone_register:
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
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(this);
        qq.authorize();
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

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
    }

//    private void loginWithAuth(final BmobUser.BmobThirdUserAuth authInfo,final PlatformDb data) {
//        BmobUser.loginWithAuthData(LoginActivity.this, authInfo, new OtherLoginListener() {
//
//            @Override
//            public void onSuccess(JSONObject userAuth) {
//                // TODO Auto-generated method stub
//                LogUtils.i(authInfo.getSnsType() + "登陆成功返回:" + userAuth);
//                Account user = BmobUser.getCurrentUser(LoginActivity.this, Account.class);
//                //更新登录的账户信息
//                updateUserInfo(user, data, authInfo);
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//                // TODO Auto-generated method stub
//                ToastUtils.shortToast(LoginActivity.this, "第三方登录失败:" + msg);
//            }
//        });
//    }
//    private void updateUserInfo(Account user, PlatformDb data, final BmobUser.BmobThirdUserAuth authInfo) {
//        Account newUser = new Account();
//        newUser.setPhoto(data.getUserIcon());
//        newUser.setSex("男".equals(data.getUserGender()) ? true : false);
//        newUser.setUsername(data.getUserName());
//        Account bmobUser = BmobUser.getCurrentUser(LoginActivity.this, Account.class);
//        newUser.update(LoginActivity.this, bmobUser.getObjectId(), new UpdateListener() {
//            @Override
//            public void onSuccess() {
//                // TODO Auto-generated method stub
//                ToastUtils.shortToast(LoginActivity.this, getString("更新用户成功"));
//                //保存登录信息到本地
//                saveUserInfo(Constant.LOGIN_TYPE_THIRD, authInfo);
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//                // TODO Auto-generated method stub
//                ToastUtils.shortToast(LoginActivity.this, getString("更新用户失败") + msg);
//            }
//        });
//    }
//    private void saveUserInfo(int loginType, BmobUser.BmobThirdUserAuth authInfo) {
//        /*
//         * TODO 把用户的登录信息保存到本地：sp\sqlite：（登录状态，登录类别，登录账户信息）
//         * 注意:为了保证数据安全，一般对数据进行加密
//         * 通过BmobUser user = BmobUser.getCurrentUser(context)获取登录成功后的本地用户信息
//         * 如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(context,MyUser.class)获取自定义用户信息
//         * */
//        Account user = BmobUser.getCurrentUser(LoginActivity.this, Account.class);
//        PreferencesManager preferences = PreferencesManager.getInstance(LoginActivity.this);
//        preferences.put(Constant.IS_LOGIN, true);
//        preferences.put(Constant.LOGINTYPE, loginType);
//        preferences.put(Constant.USER_NAME, user.getUsername());
//        preferences.put(Constant.USER_PHOTO, user.getPhoto());
//        preferences.put(Constant.USER_PWD, etPwd.getText().toString());
//        if(authInfo != null){
//            preferences.put(authInfo);
//        }
//        LoginActivity.this.finish();
//    }
}
