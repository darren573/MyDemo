package com.darren.mydemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.darren.mydemo.R;
import com.darren.mydemo.bean.Account;
import com.darren.mydemo.common.Constant;
import com.darren.mydemo.manager.PreferencesManager;
import com.darren.mydemo.utils.LogUtils;
import com.darren.mydemo.utils.ToastUtils;

import org.json.JSONObject;

import java.util.HashMap;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

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
            case R.id.btn_register:
                commit();
                break;
            case R.id.Ib_QQ:
                loginByQQ();
                break;
            case R.id.Ib_weibo:
                loginBySina();
                break;
        }
    }

    private void login() {
        String name = et_name.getText().toString();
        String pwd = et_pwd.getText().toString();
        BmobUser bu = new BmobUser();
        bu.setUsername(name);
        bu.setPassword(pwd);
        if (name.equals("") || et_name.equals("")) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_LONG).show();
        } else if (pwd.equals("") || et_pwd.equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
        } else if (name.length() < 6) {
            Toast.makeText(this, "帐号不能少于小于6位", Toast.LENGTH_LONG).show();
        } else if (pwd.length() < 6) {
            Toast.makeText(this, "密码不能少于6位", Toast.LENGTH_LONG).show();
        } else {
            bu.login(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    ToastUtils.shortToast(LoginActivity.this, "登录成功！");
                    saveUserInfo(Constant.LOGIN_TYPE_NORMAL, null);
                }

                @Override
                public void onFailure(int i, String s) {
                    ToastUtils.shortToast(LoginActivity.this, s);
                    clearInput();
                }
            });
        }
    }

    private void commit() {
        String uname = et_name.getText().toString();
        String upwd = et_pwd.getText().toString();
        if (TextUtils.isEmpty(uname) || TextUtils.isEmpty(upwd)) {
            ToastUtils.shortToast(this, "账户或密码不能为空！");
            return;
        }
        //使用BmobSDK提供的注册功能
        Account user = new Account();
        user.setUsername(uname);
        user.setPassword(upwd);
        user.signUp(LoginActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                ToastUtils.shortToast(LoginActivity.this, "注册成功，请登录！");
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.shortToast(LoginActivity.this, s);
            }
        });
    }

    private void loginByQQ() {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
        qq.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                // TODO Auto-generated method stub
                arg2.printStackTrace();
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                // TODO Auto-generated method stub
                //输出所有授权信息
                PlatformDb data = arg0.getDb();
                BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth("qq", data.getToken(), String.valueOf(data.getExpiresIn()), data.getUserId());
                loginWithAuth(authInfo, data);
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });
        //authorize与showUser单独调用一个即可
        //weibo.authorize();//单独授权,OnComplete返回的hashmap是空的
        qq.showUser(null);//授权并获取用户信息
    }

    private void loginBySina() {
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
        weibo.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                // TODO Auto-generated method stub
                arg2.printStackTrace();
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                // TODO Auto-generated method stub
                //输出所有授权信息
                PlatformDb data = arg0.getDb();
                BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth("weibo", data.getToken(), String.valueOf(data.getExpiresIn()), data.getUserId());
                loginWithAuth(authInfo, data);
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });
        //authorize与showUser单独调用一个即可
        //weibo.authorize();//单独授权,OnComplete返回的hashmap是空的
        weibo.showUser(null);//授权并获取用户信息
    }

    public void loginWithAuth(final BmobUser.BmobThirdUserAuth authInfo, final PlatformDb data) {
        BmobUser.loginWithAuthData(LoginActivity.this, authInfo, new OtherLoginListener() {

            @Override
            public void onSuccess(JSONObject userAuth) {
                // TODO Auto-generated method stub
                LogUtils.i(authInfo.getSnsType() + "登陆成功返回:" + userAuth);
                Account user = BmobUser.getCurrentUser(LoginActivity.this, Account.class);
                //更新登录的账户信息
                updateUserInfo(user, data, authInfo);
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                ToastUtils.shortToast(LoginActivity.this, "第三方登录失败:" + msg);
            }
        });
    }

    private void updateUserInfo(Account user, PlatformDb data, final BmobUser.BmobThirdUserAuth authInfo) {
        Account newUser = new Account();
        newUser.setPhoto(data.getUserIcon());
        newUser.setSex("男".equals(data.getUserGender()) ? true : false);
        newUser.setUsername(data.getUserName());
        Account bmobUser = BmobUser.getCurrentUser(LoginActivity.this, Account.class);
        newUser.update(LoginActivity.this, bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                ToastUtils.shortToast(LoginActivity.this, getString(R.string.update_userinfo_success));
                //保存登录信息到本地
                saveUserInfo(Constant.LOGIN_TYPE_THIRD, authInfo);
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                ToastUtils.shortToast(LoginActivity.this, getString(R.string.update_userinfo_failed) + msg);
            }
        });
    }

    private void saveUserInfo(int loginType, BmobUser.BmobThirdUserAuth authInfo) {
        /*
         * TODO 把用户的登录信息保存到本地：sp\sqlite：（登录状态，登录类别，登录账户信息）
         * 注意:为了保证数据安全，一般对数据进行加密
         * 通过BmobUser user = BmobUser.getCurrentUser(context)获取登录成功后的本地用户信息
         * 如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(context,MyUser.class)获取自定义用户信息
         * */
        Account user = BmobUser.getCurrentUser(LoginActivity.this, Account.class);
        PreferencesManager preferences = PreferencesManager.getInstance(LoginActivity.this);
        preferences.put(Constant.IS_LOGIN, true);
        preferences.put(Constant.LOGINTYPE, loginType);
        preferences.put(Constant.USER_NAME, user.getUsername());
        preferences.put(Constant.USER_PHOTO, user.getPhoto());
        preferences.put(Constant.USER_PWD, et_pwd.getText().toString());
        if (authInfo != null) {
            preferences.put(authInfo);
        }
        LoginActivity.this.finish();
    }

    private void clearInput() {
        et_name.setText("");
        et_pwd.setText("");
    }
}
