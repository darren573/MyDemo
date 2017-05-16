package com.darren.mydemo.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import com.darren.mydemo.utils.SharedUtil;
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
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Toast.makeText(getActivity(), data.get(position-1).getContent(),Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builer = new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.ic_shares)
                        .setTitle("分享")
                        .setMessage(data.get(position-1).getContent())
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedUtil.showShare(getActivity(),
                                        "笑话",
                                        "",
                                        data.get(position-1).getContent(),
                                        "http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E7%88%86%E7%AC%91%E5%9B%BE%E7%89%87&step_word=&hs=2&pn=5&spn=0&di=18767771440&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=-1&cs=801328703%2C413325531&os=667654980%2C1592508706&simid=3403602997%2C499126129&adpicid=0&lpn=0&ln=1963&fr=&fmq=1494896611930_R&fm=rs1&ic=undefined&s=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=%E6%90%9E%E7%AC%91%E5%9B%BE%E7%89%87&objurl=http%3A%2F%2Fhimg2.huanqiu.com%2Fattachment2010%2F2015%2F0629%2F14%2F07%2F20150629020717282.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fri5p5_z%26e3Bi7wgqt7_z%26e3Bv54AzdH3Fu7ggyrtvp76jAzdH3Fda8c-a0AzdH3Fd0b98am_dd_z%26e3Bip4s&gsm=0&rpstart=0&rpnum=0",
                                        "http://www.jianshu.com/p/7c14d7d0c6b2");
                            }
                        });
                builer.create().show();
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
