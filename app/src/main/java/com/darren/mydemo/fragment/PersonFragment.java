package com.darren.mydemo.fragment;

import android.app.Fragment;
import android.app.Notification;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.darren.mydemo.R;
import com.darren.mydemo.activity.LoginActivity;
import com.darren.mydemo.activity.RegisterActivity;
import com.darren.mydemo.utils.SharedUtil;

/**
 * Created by lenovo on 2017/5/2.
 */

public class PersonFragment extends Fragment implements View.OnClickListener {
    private TextView tv_login, tv_register, tv_collection, tv_share;
    private Button btn_aboutUs;
    private Intent intent;
    private View rootView;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.person_item, container, false);
        findViews();
        initView();
        return rootView;
    }

    private void initView() {
        tv_login.setOnClickListener(this);
        Drawable drawable_login = ContextCompat.getDrawable(getActivity(), R.drawable.ic_login);
        drawable_login.setBounds(0, 0, drawable_login.getMinimumWidth(), drawable_login.getMinimumHeight());
        tv_login.setCompoundDrawables(drawable_login, null, null, null);

        tv_register.setOnClickListener(this);
        Drawable drawable_register = ContextCompat.getDrawable(getActivity(), R.drawable.ic_register);
        drawable_register.setBounds(0, 0, drawable_register.getMinimumWidth(), drawable_register.getMinimumHeight());
        tv_register.setCompoundDrawables(drawable_register, null, null, null);

        tv_collection.setOnClickListener(this);
        Drawable drawable_collection = ContextCompat.getDrawable(getActivity(), R.drawable.ic_collections);
        drawable_collection.setBounds(0, 0, drawable_collection.getMinimumWidth(), drawable_collection.getMinimumHeight());
        tv_collection.setCompoundDrawables(drawable_collection, null, null, null);

        tv_share.setOnClickListener(this);
        Drawable drawable_share = ContextCompat.getDrawable(getActivity(), R.drawable.ic_shares);
        drawable_share.setBounds(0, 0, drawable_share.getMinimumWidth(), drawable_share.getMinimumHeight());
        tv_share.setCompoundDrawables(drawable_share, null, null, null);

        btn_aboutUs.setOnClickListener(this);
    }

    private void findViews() {
        tv_login = (TextView) rootView.findViewById(R.id.tv_login);
        tv_register = (TextView) rootView.findViewById(R.id.tv_register);
        tv_collection = (TextView) rootView.findViewById(R.id.tv_collection);
        tv_share = (TextView) rootView.findViewById(R.id.tv_share);
        btn_aboutUs = (Button) rootView.findViewById(R.id.btn_aboutUs);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_register:
                intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_collection:
                break;
            case R.id.tv_share:
                share();
                break;
            case R.id.btn_aboutUs:
                aboutUs();
                break;
        }
    }

    private void aboutUs() {
        Uri uri = Uri.parse("http://www.jianshu.com/u/950461f07346");   //指定网址
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);           //指定Action
        intent.setData(uri);                            //设置Uri
        PersonFragment.this.startActivity(intent);        //启动Activity
    }

    private void share() {
        SharedUtil.showShare(getActivity(),
                "打油诗",
                "",
                "潇潇雨中流水漏，雾迷止惑淡去留" +
                        "江边冷舟无人舵，得失任人观焦灼",
                "http://i0.hdslb.com/bfs/archive/add6c3559067121f71181a2a98da38c16f13d100.jpg",
                "http://www.jianshu.com/p/7c14d7d0c6b2");
    }
}
