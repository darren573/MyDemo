package com.darren.mydemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.darren.mydemo.R;
import com.darren.mydemo.bean.News;

import java.util.List;

/**
 * Created by lenovo on 2017/5/2.
 */

public class NewsAdapter extends BaseAdapter {
    private List<News> data;
    private ViewHolder holder;
    public NewsAdapter(List<News> data) {
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //加载缓存网络图片
        Glide.with(viewGroup.getContext())
                .load(data.get(position).getThumbnail_pic_s())
                .error(R.mipmap.error)
                .into(holder.iv_image);

        holder.tv_title.setText(data.get(position).getTitle());
        return view;
    }

    class ViewHolder {
        ImageView iv_image;
        TextView tv_title;

        public ViewHolder(View view) {
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
        }
    }

    public void setNewData(List<News> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public void loadMoreData(List<News> newData) {
        data.addAll(newData);
        notifyDataSetChanged();
    }
}
