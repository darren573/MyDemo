package com.darren.mydemo.widget.recyclerview.interfaces;

/**
 * Created by jayli on 2017/5/5 0005.
 */

public interface OnLoadMoreListener {
    /**
     * 加载更多的回调方法
     * @param isReload 是否是重新加载，只有加载失败后，点击重新加载时为true
     */
    void onLoadMore(boolean isReload);
}
