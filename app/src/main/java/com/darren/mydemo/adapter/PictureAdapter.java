package com.darren.mydemo.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.darren.mydemo.R;
import com.darren.mydemo.bean.ResultPicture;

import java.util.List;

/**
 * Created by lenovo on 2017/5/7.
 */

public class PictureAdapter extends BaseAdapter {
    private List<ResultPicture> data;
    private ViewHolder holder;
    public PictureAdapter(List<ResultPicture> data) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //加载缓存网络图片
        Glide.with(parent.getContext())
                .load(data.get(position).getPicUrl())
                .error(R.mipmap.error)
                .into(holder.iv_picture);
        holder.tv_pic_title.setText(data.get(position).getTitle());
        return convertView;
    }

    class ViewHolder {
        ImageView iv_picture;
        TextView tv_pic_title;
        public ViewHolder(View view) {
            iv_picture = (ImageView) view.findViewById(R.id.iv_picture);
            tv_pic_title= (TextView) view.findViewById(R.id.tv_pic_title);
        }
    }
    public void setNewData(List<ResultPicture> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }
    public void loadMore(List<ResultPicture> newData) {
        data.addAll(newData);
        notifyDataSetChanged();
    }
}
