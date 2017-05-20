package com.darren.mydemo.widget.recyclerview.interfaces;


import com.darren.mydemo.widget.recyclerview.ViewHolder;

/**
 * Created by jayli on 2017/5/16 0016.
 */

public interface OnItemLongClickListener<T> {

    void onItemLongClick(ViewHolder viewHolder, T data, int position);

}
