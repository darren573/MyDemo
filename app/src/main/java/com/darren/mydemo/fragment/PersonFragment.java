package com.darren.mydemo.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.darren.mydemo.R;
import com.darren.mydemo.activity.LoginActivity;
import com.darren.mydemo.activity.RegisterActivity;
import com.darren.mydemo.utils.SharedUtil;

/**
 * Created by lenovo on 2017/5/2.
 */

public class PersonFragment extends Fragment implements View.OnClickListener {
    private Button btn_register, btn_to_login,btn_share;
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_item, container, false);
        btn_to_login = (Button) view.findViewById(R.id.btn_to_login);
        btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_share= (Button) view.findViewById(R.id.btn_share);
        btn_register.setOnClickListener(this);
        btn_to_login.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_to_login:
                intent=new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_register:
                intent=new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_share:
                SharedUtil.showShare(getActivity(),
                        "我是标题",
                        "",
                        "我是内容",
                        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492086731390&di=2069856b87f96a5778b5dee99044260a&imgtype=0&src=http%3A%2F%2Fwww.csdyx.com%2Fuploadfile%2F2016%2F0923%2F20160923084104779.jpg",
                        "https://www.baidu.com");
                break;
        }
    }
}
