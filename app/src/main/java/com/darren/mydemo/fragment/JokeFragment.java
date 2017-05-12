package com.darren.mydemo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.darren.mydemo.R;
import com.darren.mydemo.adapter.JokeAdapter;
import com.darren.mydemo.bean.ResultJoke;
import com.darren.mydemo.common.Common;
import com.darren.mydemo.common.Constant;
import com.darren.mydemo.common.ServerConfig;
import com.darren.mydemo.utils.TimeUtils;
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

public class JokeFragment extends Fragment {
    private View rootView;
    private PullToRefreshListView prf_listView;
    private JokeAdapter jokeAdapter;
    private static int page = 1;
    public static final int TYPE_REFRESH = 0X01;
    public static final int TYPE_LOADMORE = 0X02;
    private List<ResultJoke.ResultBean.Joke> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_items, container, false);
        initView();
        return rootView;
    }

    private void initView() {
        prf_listView = (PullToRefreshListView) rootView.findViewById(R.id.prf_listView);
        jokeAdapter = new JokeAdapter(data);
        prf_listView.setAdapter(jokeAdapter);
        prf_listView.setMode(PullToRefreshBase.Mode.BOTH);
        prf_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), data.get(position-1).getContent(),Toast.LENGTH_SHORT).show();
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
                .url(ServerConfig.JOKE_URL)
                .addParams("sort", "desc")
                .addParams("page", String.valueOf(page))
                .addParams("pagesize", Constant.PAGE_SIZE)
                .addParams("time", TimeUtils.getTime())
                .addParams("key",Common.API_JOKE_KEY)
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
                        ResultJoke resultJoke= JSON.parseObject(response,ResultJoke.class);
                        //data.addAll(resultJoke.getResult().getData());
                        //jokeAdapter.notifyDataSetChanged();
                        switch (type) {
                            case TYPE_REFRESH:
                                jokeAdapter.setNewData(resultJoke.getResult().getData());
                                break;
                            case TYPE_LOADMORE:
                                jokeAdapter.loadMore(resultJoke.getResult().getData());
                                break;
                        }
                    }
                });
    }
}
