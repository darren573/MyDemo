package com.darren.mydemo.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.darren.mydemo.R;
import com.darren.mydemo.activity.PictureActivity;
import com.darren.mydemo.adapter.WaterfallAdapter;
import com.darren.mydemo.bean.ResultPicture;
import com.darren.mydemo.common.Common;
import com.darren.mydemo.common.Constant;
import com.darren.mydemo.common.ServerConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by lenovo on 2017/5/10.
 */

public class PicFragment extends Fragment {
    private RecyclerView recyclerView;
    private View view;
    private WaterfallAdapter waterfallAdapter;
    private List<ResultPicture> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recycler, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        initView();
        return view;
    }

    private void initView() {
        waterfallAdapter = new WaterfallAdapter(getActivity(), data);
        recyclerView.setAdapter(waterfallAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        waterfallAdapter.setOnItemActionListener(new WaterfallAdapter.OnItemActionListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Intent intent=new Intent(getActivity(),PictureActivity.class);
                Bundle bundle=new Bundle();
                bundle.putParcelable("picture",data.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClickListener(View v, int position) {
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        OkHttpUtils
                .get()
                .url(ServerConfig.PICTURE_URL)
                .addParams("key", Common.API_PICTURE_KEY)
                .addParams("page", Constant.pagesize)
                .addParams("num","10")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        //解析数据
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("newslist");
                        data.addAll(JSONArray.parseArray(jsonArray.toJSONString(), ResultPicture.class));
                        waterfallAdapter.notifyDataSetChanged();
                    }
                });
    }
}
