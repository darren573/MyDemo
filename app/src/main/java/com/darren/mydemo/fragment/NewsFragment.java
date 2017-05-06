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

import com.darren.mydemo.activity.NewsActivity;
import com.darren.mydemo.adapter.NewsAdapter;
import com.darren.mydemo.bean.News;
import com.darren.mydemo.common.Common;
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

public class NewsFragment extends Fragment {
    private View rootView;
    private  static int page = 1;
    private NewsAdapter adapter;
    public static final int TYPE_REFRESH = 0X01;
    public static final int TYPE_LOADMORE = 0X02;
    private PullToRefreshListView prf_listView;
    private List<News> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_items, container, false);
        initView();
        return rootView;
    }
    private void initView() {
        prf_listView = (PullToRefreshListView) rootView.findViewById(R.id.prf_listView);
        adapter = new NewsAdapter(data);
        prf_listView.setAdapter(adapter);
        prf_listView.setMode(PullToRefreshBase.Mode.BOTH);
        prf_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("news", data.get(position-1));
                intent.putExtras(bundle);
                NewsFragment.this.startActivity(intent);
            }
        });
        prf_listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新:刷新第一页数据，先清空原数据，再添加新数据，然后更新UI
                getAsyncData(1, TYPE_REFRESH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //加载更多：加载下一页数据，那么请求page++，只需要把新数据添加到原数据的后面，然后更新UI
                getAsyncData(page++, TYPE_LOADMORE);

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
    private void initData() {
        getAsyncData(1, TYPE_REFRESH);
    }

    private void getAsyncData(int page, final int type) {
        OkHttpUtils
                .get()
                .url(ServerConfig.NEWS_URL)
                .addParams("key", Common.API_NEWS_KEY)
                .addParams("type", "top")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        prf_listView.onRefreshComplete();
                        Toast.makeText(getActivity(), R.string.ask_error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        prf_listView.onRefreshComplete();
                        //把返回的结果JSON数据字符串转为json对象
                        JSONObject jsonobject = JSONObject.parseObject(response);
                        JSONArray jsonArray = jsonobject.getJSONObject("result").getJSONArray("data");
                        switch (type) {
                            case TYPE_REFRESH:
                                adapter.setNewData(JSONArray.parseArray(jsonArray.toJSONString(), News.class));
                                break;
                            case TYPE_LOADMORE:
                                adapter.loadMoreData(JSONArray.parseArray(jsonArray.toJSONString(), News.class));
                                break;
                        }
//                    data.addAll(JSONArray.parseArray(jsonArray.toJSONString(), News.class));
//                    adapter.notifyDataSetChanged();
                    }
                });
    }
}
