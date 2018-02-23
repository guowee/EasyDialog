package com.uowee.easy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;

import com.uowee.easy.dialog.view.EasyDialogLayout;

/**
 * Created by GuoWee on 2018/2/6.
 */

public class BaseDialog extends Dialog implements DialogInterface.OnShowListener {

    private EasyDialogLayout mDialogLayout;
    private OnShowListener mShowListener;

    public BaseDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return mDialogLayout.findViewById(id);
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        if (mShowListener != null) {
            mShowListener.onShow(dialogInterface);
        }
    }


    protected final void setOnShowListenerInternal() {
        setOnShowListener(this);
    }

    protected final void setContentViewInternal(View view) {
        setContentView(view);
    }

    @Override
    public void setOnShowListener(@Nullable OnShowListener listener) {
        super.setOnShowListener(listener);
    }

    @Override
    public void setContentView(@NonNull View view) {
        super.setContentView(view);
    }
}
