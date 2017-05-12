package com.darren.mydemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.darren.mydemo.R;
import com.darren.mydemo.bean.ResultPicture;

import java.util.List;


/**
 * Created by lenovo on 2017/5/10.
 */

public class WaterfallAdapter extends RecyclerView.Adapter <WaterfallAdapter.ViewHolder>{
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ResultPicture> data;
    private View rootView;
    private OnItemActionListener mOnItemActionListener;

    public WaterfallAdapter(Context context, List<ResultPicture> data) {
        this.data = data;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rootView = mInflater.inflate(R.layout.pic_item, parent, false);
        ViewHolder holder = new ViewHolder(rootView);
        holder.setIsRecyclable(true);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Glide.with(mContext)
                .load(data.get(position).getPicUrl())
                .error(R.mipmap.error)
                .into(holder.item_img);
        holder.item_title.setText(data.get(position).getTitle());
        if(mOnItemActionListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemActionListener.onItemClickListener(v,holder.getPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mOnItemActionListener.onItemLongClickListener(v, holder.getPosition());
                }
            });
        }

    }
    @Override
    public int getItemCount() {
         return data.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView item_title;
        public ViewHolder(View itemView) {
            super(itemView);
            item_img= (ImageView) itemView.findViewById(R.id.item_img);
            item_title= (TextView) itemView.findViewById(R.id.item_title);
        }
    }

    //下拉刷新
    public void setNewData(List<ResultPicture> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public void setMoreData(List<ResultPicture> newData) {
        data.addAll(newData);
        notifyDataSetChanged();
    }
    public interface OnItemActionListener {
        public void onItemClickListener(View v, int position);
        public boolean onItemLongClickListener(View v, int position);
    }

    public void setOnItemActionListener(OnItemActionListener onItemActionListener) {
        this.mOnItemActionListener = onItemActionListener;
    }
}

