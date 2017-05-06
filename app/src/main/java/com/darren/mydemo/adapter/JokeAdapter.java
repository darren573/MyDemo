package com.darren.mydemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.darren.mydemo.R;
import com.darren.mydemo.bean.ResultJoke;

import java.util.List;

/**
 * Created by lenovo on 2017/5/6.
 */

public class JokeAdapter extends BaseAdapter {
    private List<ResultJoke.ResultBean.Joke> data;
    private ViewHolder holder;

    public JokeAdapter(List<ResultJoke.ResultBean.Joke> data) {
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
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.joke_item,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tv_joke.setText(data.get(position).getContent());
        return convertView;
    }
    class ViewHolder{
        TextView tv_joke;
        public ViewHolder(View view){
            tv_joke= (TextView) view.findViewById(R.id.tv_joke);
        }
    }
    public void setNewData(List<ResultJoke.ResultBean.Joke> newData){
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }
    public void loadMore(List<ResultJoke.ResultBean.Joke> newData){
        data.addAll(newData);
        notifyDataSetChanged();
    }
}
