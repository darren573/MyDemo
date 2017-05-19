package com.darren.mydemo.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.darren.mydemo.R;
import com.darren.mydemo.activity.LoginActivity;
import com.darren.mydemo.activity.MoreInfoActivity;
import com.darren.mydemo.activity.SettingsActivity;
import com.darren.mydemo.common.Constant;
import com.darren.mydemo.manager.PreferencesManager;
import com.darren.mydemo.utils.ImageLoader;
import com.darren.mydemo.utils.LoginUtils;
import com.darren.mydemo.utils.SharedUtil;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by lenovo on 2017/5/2.
 */

public class PersonFragment extends Fragment implements View.OnClickListener {
    private TextView tv_login, tv_collection, tv_share, tv_system;
    private Button btn_aboutUs;
    private RoundedImageView riv_item;
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
        initData();
        return rootView;
    }

    private void initData() {
        if (PreferencesManager.getInstance(getActivity()).get(Constant.IS_LOGIN, false)) {
            riv_item.setVisibility(View.VISIBLE);
            loadUserInfo();
        } else {
            riv_item.setVisibility(View.GONE);
        }
    }

    private void loadUserInfo() {
        String userPhoto = PreferencesManager.getInstance(getActivity()).get(Constant.USER_PHOTO);
        ImageLoader.getInstance().displayImageTarget(riv_item, userPhoto);
    }

    private void initView() {
        tv_login.setOnClickListener(this);
        Drawable drawable_login = ContextCompat.getDrawable(getActivity(), R.drawable.ic_logins);
        drawable_login.setBounds(0, 0, drawable_login.getMinimumWidth(), drawable_login.getMinimumHeight());
        tv_login.setCompoundDrawables(drawable_login, null, null, null);

        tv_collection.setOnClickListener(this);
        Drawable drawable_collection = ContextCompat.getDrawable(getActivity(), R.drawable.ic_collections);
        drawable_collection.setBounds(0, 0, drawable_collection.getMinimumWidth(), drawable_collection.getMinimumHeight());
        tv_collection.setCompoundDrawables(drawable_collection, null, null, null);

        tv_share.setOnClickListener(this);
        Drawable drawable_share = ContextCompat.getDrawable(getActivity(), R.drawable.ic_share);
        drawable_share.setBounds(0, 0, drawable_share.getMinimumWidth(), drawable_share.getMinimumHeight());
        tv_share.setCompoundDrawables(drawable_share, null, null, null);

        tv_system.setOnClickListener(this);
        Drawable drawable_system = ContextCompat.getDrawable(getActivity(), R.drawable.ic_system);
        drawable_system.setBounds(0, 0, drawable_system.getMinimumWidth(), drawable_system.getMinimumHeight());
        tv_system.setCompoundDrawables(drawable_system, null, null, null);

        btn_aboutUs.setOnClickListener(this);
        riv_item.setOnClickListener(this);
    }

    private void findViews() {
        tv_login = (TextView) rootView.findViewById(R.id.tv_login);
        tv_collection = (TextView) rootView.findViewById(R.id.tv_collection);
        tv_share = (TextView) rootView.findViewById(R.id.tv_share);
        tv_system = (TextView) rootView.findViewById(R.id.tv_system);
        riv_item= (RoundedImageView) rootView.findViewById(R.id.riv_item);
        btn_aboutUs = (Button) rootView.findViewById(R.id.btn_aboutUs);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.riv_item:
                intent=new Intent(v.getContext(), MoreInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_login:
                intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_collection:
                LoginUtils.checkLogin(true);
                intent =new Intent(v.getContext(),CollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_share:
                share();
                break;
            case R.id.tv_system:
                intent = new Intent(v.getContext(), SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_aboutUs:
                aboutUs();
                break;
        }
    }

    private void aboutUs() {
        AlertDialog.Builder builer = new AlertDialog.Builder(getActivity())
                .setTitle("关于我们")
                .setMessage("开发人:darren573\n地址:https://github.com/darren573/MyDemo")
                .setPositiveButton("确定", null);
        builer.create().show();
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
