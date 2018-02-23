package com.uowee.easy.dialog.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {
    private SparseArray<View> views;
    private View parent;

    public ViewHolder(View view) {
        parent = view;
        views = new SparseArray<>();
    }


    public <E extends View> E get(int id) {
        View childView = views.get(id);
        if (null == childView) {
            childView = parent.findViewById(id);
            views.put(id, childView);
        }
        return (E) childView;
    }


    public TextView getTextView(int id) {
        return get(id);
    }


    public ImageView getImageView(int id) {
        return get(id);
    }
}
