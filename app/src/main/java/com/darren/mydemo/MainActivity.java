package com.darren.mydemo;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.darren.mydemo.activity.NewsActivity;
import com.darren.mydemo.adapter.NewsAdapter;
import com.darren.mydemo.bean.News;
import com.darren.mydemo.common.Common;
import com.darren.mydemo.common.ServerConfig;
import com.darren.mydemo.fragment.JokeFragment;
import com.darren.mydemo.fragment.NewsFragment;
import com.darren.mydemo.fragment.PersonFragment;
import com.darren.mydemo.fragment.PictureFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {
    private NewsFragment newsFragment;
    private JokeFragment jokeFragment;
    private PictureFragment pictureFragment;
    private PersonFragment personFragment;
    private ListView lv_news;
    private NewsAdapter adapter;
    private List<News> data = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    newsFragment = new NewsFragment();
                    fragmentTransaction.replace(R.id.content, newsFragment);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_joke:
                    jokeFragment = new JokeFragment();
                    fragmentTransaction.replace(R.id.content, jokeFragment);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_picture:
                    pictureFragment = new PictureFragment();
                    fragmentTransaction.replace(R.id.content, pictureFragment);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_person:
                    personFragment = new PersonFragment();
                    fragmentTransaction.replace(R.id.content, personFragment);
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        findViews();
        setEvent();
    }

    private void setEvent() {
        adapter = new NewsAdapter(data);
        lv_news.setAdapter(adapter);
        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("news",data.get(position));
                intent.putExtras(bundle);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    private void findViews() {
        lv_news = (ListView) findViewById(R.id.lv_news);
    }

    @Override
    protected void onResume() {
        super.onResume();
        OkHttpUtils
                .post()
                .url(ServerConfig.BASE_URL)
                .addParams("key", Common.API_HEALTH_KEY)
                .addParams("type", "top")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(MainActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //把返回的结果JSON数据字符串转为json对象
                        JSONObject jsonobject = JSONObject.parseObject(response);
                        JSONArray jsonArray = jsonobject.getJSONObject("result").getJSONArray("data");
                        //data = jsonArray.toJavaList(News.class);
                        data.addAll(JSONArray.parseArray(jsonArray.toJSONString(), News.class));
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
