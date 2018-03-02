package com.muse.easy.dialog.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.muse.easy.dialog.utils.Utils;

/**
 * Created by GuoWee on 2018/2/6.
 */

public class EasyDialogLayout extends LinearLayout {

    private ScrollView mScrollView;
    private View mBottom;

    public EasyDialogLayout(Context context) {
        super(context);
    }

    public EasyDialogLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EasyDialogLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EasyDialogLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        //mBottom = findViewById(R.id.ed_btn_layout);
        //mScrollView = (ScrollView) findViewById(R.id.ed_content_scroll);
    }

    public void setCustomScrollView(ScrollView scrollView) {
        mScrollView = scrollView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (null != mScrollView) {
            int measuredScrollViewHeight = mScrollView.getMeasuredHeight();
            int dp_320 = Utils.dp2px(320);
            if (measuredScrollViewHeight > dp_320) {
                int width = getMeasuredWidth();
                int height = getMeasuredHeight() - measuredScrollViewHeight + dp_320;

                if (null != mBottom && mBottom.getVisibility() == VISIBLE) {
                    height += mBottom.getMeasuredHeight();
                } else {
                    height += Utils.dp2px(10);
                }

                mScrollView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(dp_320, MeasureSpec.AT_MOST));

                setMeasuredDimension(width, height);
            }
        }
    }
}
