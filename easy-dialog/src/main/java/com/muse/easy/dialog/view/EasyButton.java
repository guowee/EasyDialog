package com.muse.easy.dialog.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.muse.easy.dialog.R;

/**
 * Created by GuoWee on 2018/2/6.
 */

@SuppressLint("AppCompatCustomView")
public class EasyButton extends TextView {

    private EasyButtonType easyButtonType;

    public EasyButton(Context context) {
        super(context);
        init();
    }

    public EasyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EasyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EasyButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public EasyButtonType getEasyButtonType() {
        return easyButtonType;
    }

    public void setEasyButtonType(EasyButtonType easyButtonType) {
        this.easyButtonType = easyButtonType;
    }

    private void init() {
        setBackgroundResource(R.drawable.press_rect_selector);
    }


    public static enum EasyButtonType {
        PositiveBtn,
        NegativeBtn,
        NeutralBtn
    }


}
