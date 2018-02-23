package com.uowee.easy.dialog.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

public abstract class EasySimpleAdapter<T> extends EasyCommonAdapter<T> {

    private int layoutId;


    public EasySimpleAdapter(List<T> items, int layoutId) {
        super(items);
        this.layoutId = layoutId;
    }

    protected EasySimpleAdapter(int layoutId) {
        super();
        this.layoutId = layoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bindView(convertView, position, viewHolder);
        return convertView;
    }


    protected abstract void bindView(View convertView, int position, ViewHolder viewHolder);

}
