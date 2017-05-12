package com.darren.mydemo.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.darren.mydemo.R;

import com.darren.mydemo.activity.PictureActivity;
import com.darren.mydemo.adapter.PictureAdapter;

import com.darren.mydemo.bean.ResultPicture;
import com.darren.mydemo.common.Common;
import com.darren.mydemo.common.Constant;
import com.darren.mydemo.common.ServerConfig;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by lenovo on 2017/5/2.
 */

public class PictureFragment extends Fragment {
    private View rootView;
    private PullToRefreshListView prf_listView;
    private static int page = 1;
    public static final int TYPE_REFRESH = 0X01;
    public static final int TYPE_LOADMORE = 0X02;
    private PictureAdapter pictureAdapter;
    private List<ResultPicture> data=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_items, container, false);
        initView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        page =1;
        getAsyncData(TYPE_REFRESH,page);
    }

    private void getAsyncData(int page, final int type) {
        OkHttpUtils
                .get()
                .url(ServerConfig.PICTURE_URL)
                .addParams("key", Common.API_PICTURE_KEY)
                .addParams("page", String.valueOf(page))
                .addParams("num", Constant.NUM)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        prf_listView.onRefreshComplete();
                        Toast.makeText(getActivity(), R.string.load_error, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        prf_listView.onRefreshComplete();
                        JSONObject jsonobject = JSONObject.parseObject(response);
                        JSONArray jsonArray = jsonobject.getJSONArray("newslist");
                        switch (type) {
                            case TYPE_REFRESH:
                                pictureAdapter.setNewData(JSONArray.parseArray(jsonArray.toJSONString(), ResultPicture.class));
                                break;
                            case TYPE_LOADMORE:
                                pictureAdapter.loadMore(JSONArray.parseArray(jsonArray.toJSONString(), ResultPicture.class));
                                break;
                        }
                    }
                });
    }
    private void initView() {
        prf_listView = (PullToRefreshListView) rootView.findViewById(R.id.prf_listView);
        pictureAdapter = new PictureAdapter(data);
        prf_listView.setMode(PullToRefreshBase.Mode.BOTH);
        prf_listView.setAdapter(pictureAdapter);
        prf_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("picture",data.get(position-1));
                intent.putExtras(bundle);
                PictureFragment.this.startActivity(intent);
            }
        });
        prf_listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getAsyncData(1,TYPE_REFRESH);
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getAsyncData(page++,TYPE_LOADMORE);
            }
        });
    }
}
