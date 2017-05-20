package com.darren.mydemo.widget.recyclerview.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.darren.mydemo.widget.recyclerview.ViewHolder;
import com.darren.mydemo.widget.recyclerview.interfaces.OnMultiItemClickListeners;

import java.util.List;

/**
 * Created by jayli on 2017/5/5 0005.
 */

public abstract class MultiBaseAdapter<T> extends BaseAdapter<T> {
    private OnMultiItemClickListeners<T> mItemClickListener;

    public MultiBaseAdapter(Context context, List<T> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    protected abstract void convert(ViewHolder holder, T data, int viewType);

    protected abstract int getItemLayoutId(int viewType);

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (isCommonItemView(viewType)) {
                return ViewHolder.create(mContext, getItemLayoutId(viewType), parent);
            }
            return super.onCreateViewHolder(parent, viewType);
        }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (isCommonItemView(viewType)) {
            bindCommonItem(holder, position, viewType);
        }
    }

    private void bindCommonItem(RecyclerView.ViewHolder holder, final int position, final int viewType) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        convert(viewHolder, mDatas.get(position), viewType);

        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(viewHolder, mDatas.get(position), position, viewType);
                }
            }
        });
    }

    public void setOnMultiItemClickListener(OnMultiItemClickListeners<T> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

}
