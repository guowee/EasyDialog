package com.muse.easy.dialog.adapter;

import android.widget.BaseAdapter;

import java.util.List;


public abstract class EasyCommonAdapter<T> extends BaseAdapter {

    private List<T> items = null;


    public EasyCommonAdapter(List<T> items) {
        this.items = items;
    }

    protected EasyCommonAdapter() {
    }


    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return null == items ? 0 : items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
