package com.muse.easy.dialog;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.muse.easy.dialog.adapter.EasySimpleAdapter;
import com.muse.easy.dialog.utils.Utils;
import com.muse.easy.dialog.view.EasyButton;
import com.muse.easy.dialog.view.EasyDialogLayout;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by GuoWee on 2018/2/1.
 */

public class EasyDialog extends BaseDialog implements View.OnClickListener {

    public EasyDialogLayout rootView;
    private Builder mBuilder;
    protected ListView listView;
    protected TextView title;
    protected FrameLayout customViewFrame;
    protected TextView content;
    protected ProgressBar mProgress;
    protected TextView mProgressLabel;
    protected TextView mProgressMinMax;

    protected EasyButton positiveButton;
    protected EasyButton neutralButton;
    protected EasyButton negativeButton;

    @SuppressLint("InflateParams")
    protected EasyDialog(Builder builder) {
        super(builder.context, R.style.easy_light);
        mBuilder = builder;
        final LayoutInflater inflater = LayoutInflater.from(builder.context);
        rootView = (EasyDialogLayout) inflater.inflate(EasyDialogController.getInflateLayout(builder), null);
        EasyDialogController.init(this);
    }

    public final Builder getBuilder() {
        return mBuilder;
    }

    @Override
    public void onClick(View view) {

        if (view instanceof EasyButton) {
            EasyButton btn = (EasyButton) view;

            switch (btn.getEasyButtonType()) {
                case NeutralBtn:
                    if (mBuilder.onNeutralCallback != null) {
                        mBuilder.onNeutralCallback.onClick(this, EasyButton.EasyButtonType.NeutralBtn);
                    }
                    break;
                case PositiveBtn:
                    if (mBuilder.onPositiveCallback != null) {
                        mBuilder.onPositiveCallback.onClick(this, EasyButton.EasyButtonType.PositiveBtn);
                    }
                    break;
                case NegativeBtn:
                    if (mBuilder.onNegativeCallback != null) {
                        mBuilder.onNegativeCallback.onClick(this, EasyButton.EasyButtonType.NegativeBtn);
                    }
                    break;
                default:
                    break;
            }

            if (null != mBuilder.onAnyCallback) {
                mBuilder.onAnyCallback.onClick(this, btn.getEasyButtonType());
            }

            if (mBuilder.autoDismiss) {
                dismiss();
            }


        }

    }

    public final boolean isCancelled() {
        return !isShowing();
    }

    public final EasyButton getButton(@NonNull EasyButton.EasyButtonType which) {
        switch (which) {
            default:
                return positiveButton;
            case NeutralBtn:
                return neutralButton;
            case NegativeBtn:
                return negativeButton;
        }
    }

    @Nullable
    public final View getCustomView() {
        return mBuilder.customView;
    }

    public void setupListCallback() {
        if (listView == null) {
            return;
        } else if ((mBuilder.items == null || mBuilder.items.size() == 0) && mBuilder.adapter == null) {
            return;
        }

        listView.setAdapter(getBuilder().adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getBuilder().autoDismiss) {
                    dismiss();
                }
                if (getBuilder().listCallbackCustom != null) {
                    getBuilder().listCallbackCustom.onItemClick(EasyDialog.this, view, position, getBuilder().adapter.getItem(position));
                    return;
                }
                if (getBuilder().listCallback != null) {
                    getBuilder().listCallback.onItemClick(EasyDialog.this, view, position, getBuilder().adapter.getItem(position));
                }
            }
        });
    }


    public final int getCurrentProgress() {
        if (mProgress == null) return -1;
        return mProgress.getProgress();
    }

    public ProgressBar getProgressBar() {
        return mProgress;
    }

    public final void incrementProgress(final int by) {
        setProgress(getCurrentProgress() + by);
    }

    public final int getMaxProgress() {
        if (mProgress == null) return -1;
        return mProgress.getMax();
    }

    public final void setProgress(final int progress) {
        if (mBuilder.progress <= -2)
            throw new IllegalStateException("Cannot use setProgress() on this dialog.");
        mProgress.setProgress(progress);
        mProgress.post(new Runnable() {
            @Override
            public void run() {
                if (mProgressLabel != null) {
                    mProgressLabel.setText(mBuilder.progressPercentFormat.format(
                            (float) getCurrentProgress() / (float) getMaxProgress()));
                }
                if (mProgressMinMax != null) {
                    mProgressMinMax.setText(String.format(mBuilder.progressNumberFormat,
                            getCurrentProgress(), getMaxProgress()));
                }
            }
        });
    }

    public interface SingleButtonCallback {
        void onClick(@NonNull EasyDialog dialog, @NonNull EasyButton.EasyButtonType which);
    }

    public interface ListCallback<T> {
        void onItemClick(@NonNull EasyDialog dialog, @NonNull View view, @NonNull int position, @NonNull T item);
    }

    public static class Builder<T> {
        protected final Context context;
        //标题相关
        protected CharSequence title;
        protected int titleColor;
        protected int titleGravity = Gravity.CENTER;
        protected float titleTextSize = 17F;
        protected int titleTypeFace = Typeface.BOLD;

        //内容相关
        protected CharSequence content;
        protected int contentColor;
        protected float contentLineSpacingMultiplier = 1.2f;
        protected int contentGravity = Gravity.CENTER;
        protected View customView;
        protected boolean wrapCustomViewInScroll;
        protected float contentTextSize = 16F;

        protected List<CharSequence> items;
        protected ListCallback listCallback;
        protected int dividerColorRes;
        protected int dividerHeight = 1;
        protected int itemColor;
        protected int itemsGravity = Gravity.CENTER;
        protected int itemsBackgroundRes;
        protected ListCallback<T> listCallbackCustom;
        protected float itemsTextSize = 16F;
        protected int itemsHeight = Utils.dp2px(46);

        protected ListAdapter adapter;

        protected boolean indeterminateProgress;
        protected boolean showMinMax;
        protected int progress = -2;
        protected int progressMax = 0;
        protected String progressNumberFormat;
        protected NumberFormat progressPercentFormat;
        protected int progressColor;

        protected CharSequence positiveText;
        protected CharSequence neutralText;
        protected CharSequence negativeText;
        protected float buttonTextSize = 17F;
        protected ColorStateList positiveColor;
        protected ColorStateList negativeColor;
        protected ColorStateList neutralColor;
        protected SingleButtonCallback onPositiveCallback;
        protected SingleButtonCallback onNegativeCallback;
        protected SingleButtonCallback onNeutralCallback;
        protected SingleButtonCallback onAnyCallback;
        protected int bottomSpColor;
        protected int bottomSpHeight = 1;

        protected boolean autoDismiss = true;
        protected OnDismissListener dismissListener;
        protected OnCancelListener cancelListener;
        protected OnKeyListener keyListener;
        protected OnShowListener showListener;

        protected boolean cancelable = true;
        protected boolean canceledOnTouchOutside = true;


        public Builder(Context context) {
            this.context = context;
            int fontBlue = Utils.getColor(context, R.color.font_blue);
            int fontNormal = Utils.getColor(context, R.color.font_normal);
            int fontBlack = Utils.getColor(context, R.color.font_black);

            ColorStateList wrapedFontBlue = Utils.wrapColor(fontBlue);
            ColorStateList wrapedFontNormal = Utils.wrapColor(fontNormal);
            positiveColor = wrapedFontBlue;
            negativeColor = wrapedFontNormal;
            neutralColor = wrapedFontBlue;
            contentColor = fontNormal;
            titleColor = fontBlack;
            itemColor = fontNormal;
            progressColor = fontBlue;

            dividerColorRes = R.color.pers10_black;
            itemsBackgroundRes = R.drawable.press_rect_selector;
            bottomSpColor = Utils.getColor(context, R.color.pers10_black);

            progressPercentFormat = NumberFormat.getPercentInstance();
            progressNumberFormat = "%1d/%2d";
        }

        public Builder title(@StringRes int titleRes) {
            title(this.context.getText(titleRes));
            return this;
        }

        public Builder title(@NonNull CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder titleGravity(@NonNull int gravity) {
            this.titleGravity = gravity;
            return this;
        }

        public Builder titleColor(@ColorInt int color) {
            this.titleColor = color;
            return this;
        }

        public Builder titleColorRes(@ColorRes int colorRes) {
            return titleColor(Utils.getColor(this.context, colorRes));
        }


        public Builder titleTextSize(float titleTextSize) {
            this.titleTextSize = titleTextSize;
            return this;
        }

        public Builder titleTypeFace(int titleTypeFace) {
            this.titleTypeFace = titleTypeFace;
            return this;
        }

        public Builder content(@StringRes int contentRes) {
            content(this.context.getText(contentRes));
            return this;
        }

        public Builder content(@NonNull CharSequence content) {
            if (this.customView != null)
                throw new IllegalStateException("You cannot set content() when you're using a custom view.");
            this.content = content;
            return this;
        }

        public Builder content(@StringRes int contentRes, Object... formatArgs) {
            content(this.context.getString(contentRes, formatArgs));
            return this;
        }

        public Builder contentColor(@ColorInt int color) {
            this.contentColor = color;
            return this;
        }

        public Builder contentColorRes(@ColorRes int colorRes) {
            contentColor(Utils.getColor(this.context, colorRes));
            return this;
        }

        public Builder contentGravity(@NonNull int gravity) {
            this.contentGravity = gravity;
            return this;
        }

        public Builder contentLineSpacing(float multiplier) {
            this.contentLineSpacingMultiplier = multiplier;
            return this;
        }


        public Builder contentTextSize(float contentTextSize) {
            this.contentTextSize = contentTextSize;
            return this;
        }

        public Builder items(@NonNull List<CharSequence> data) {
            if (this.customView != null)
                throw new IllegalStateException("You cannot set items() when you're using a custom view.");
            if (null != data && data.size() > 0) {
                this.items = data;
            }
            return this;
        }

        public Builder items(@NonNull CharSequence... items) {
            items(Arrays.asList(items));
            return this;
        }

        public Builder itemsCallback(@NonNull ListCallback callback) {
            this.listCallback = callback;
            return this;
        }

        public Builder itemsColor(@ColorInt int color) {
            this.itemColor = color;
            return this;
        }

        public Builder itemsColorRes(@ColorRes int colorRes) {
            return itemsColor(Utils.getColor(this.context, colorRes));
        }


        public Builder itemsGravity(@NonNull int gravity) {
            this.itemsGravity = gravity;
            return this;
        }

        public Builder itemsBackground(@DrawableRes int itemsBackgroundRes) {
            this.itemsBackgroundRes = itemsBackgroundRes;
            return this;
        }

        public Builder itemsTextSize(float itemsTextSize) {
            this.itemsTextSize = itemsTextSize;
            return this;
        }

        public Builder itemsHeight(@DrawableRes int itemsHeight) {
            this.itemsHeight = itemsHeight;
            return this;
        }

        public Builder dividerColor(@ColorRes int colorRes) {
            this.dividerColorRes = colorRes;
            return this;
        }

        public Builder dividerHeight(@IntRange(from = 0) int dividerHeight) {
            this.dividerHeight = dividerHeight;
            return this;
        }

        public Builder<T> adapterSimple(@NonNull EasySimpleAdapter<T> adapter, @Nullable ListCallback<T> callback) {
            this.adapter = adapter;
            this.listCallbackCustom = callback;
            return this;
        }

        public Builder<T> adapter(@NonNull ListAdapter adapter, @Nullable ListCallback<T> callback) {
            this.adapter = adapter;
            this.listCallbackCustom = callback;
            return this;
        }


        public Builder customView(@LayoutRes int layoutRes, boolean wrapInScrollView) {
            return customView(LayoutInflater.from(this.context).inflate(layoutRes, null), wrapInScrollView);
        }

        public Builder customView(@NonNull View view, boolean wrapInScrollView) {
            if (this.content != null)
                throw new IllegalStateException("You cannot use customView() when you have content set.");
            else if (this.items != null)
                throw new IllegalStateException("You cannot use customView() when you have items set.");
            if (view.getParent() != null && view.getParent() instanceof ViewGroup)
                ((ViewGroup) view.getParent()).removeView(view);
            this.customView = view;
            this.wrapCustomViewInScroll = wrapInScrollView;
            return this;
        }


        public Builder progress(boolean indeterminate, int max) {
            if (this.customView != null)
                throw new IllegalStateException("You cannot set progress() when you're using a custom view.");
            if (indeterminate) {
                this.indeterminateProgress = true;
                this.progress = -2;
            } else {
                this.indeterminateProgress = false;
                this.progress = -1;
                this.progressMax = max;
            }
            return this;
        }

        public Builder progress(boolean indeterminate, int max, boolean showMinMax) {
            this.showMinMax = showMinMax;
            return progress(indeterminate, max);
        }

        public Builder progressNumberFormat(@NonNull String format) {
            this.progressNumberFormat = format;
            return this;
        }


        public Builder progressPercentFormat(@NonNull NumberFormat format) {
            this.progressPercentFormat = format;
            return this;
        }

        public Builder progressColor(@ColorInt int color) {
            this.progressColor = color;
            return this;
        }

        public Builder progressColorRes(@ColorRes int colorRes) {
            return progressColor(Utils.getColor(this.context, colorRes));
        }

        public Builder buttonTextSize(float buttonTextSize) {
            this.buttonTextSize = buttonTextSize;
            return this;
        }

        public Builder positiveText(@StringRes int postiveRes) {
            if (postiveRes == 0) return this;
            positiveText(this.context.getText(postiveRes));
            return this;
        }

        public Builder positiveText(@NonNull CharSequence message) {
            this.positiveText = message;
            return this;
        }

        public Builder positiveColor(@ColorInt int color) {
            return positiveColor(Utils.wrapColor(color));
        }

        public Builder positiveColorRes(@ColorRes int colorRes) {
            return positiveColor(Utils.wrapColor(Utils.getColor(context, colorRes)));
        }

        public Builder positiveColor(@NonNull ColorStateList colorStateList) {
            this.positiveColor = colorStateList;
            return this;
        }

        public Builder neutralText(@StringRes int neutralRes) {
            if (neutralRes == 0) return this;
            return neutralText(this.context.getText(neutralRes));
        }

        public Builder neutralText(@NonNull CharSequence message) {
            this.neutralText = message;
            return this;
        }

        public Builder negativeColor(@ColorInt int color) {
            return negativeColor(Utils.wrapColor(color));
        }

        public Builder negativeColorRes(@ColorRes int colorRes) {
            return negativeColor(Utils.wrapColor(Utils.getColor(this.context, colorRes)));
        }

        public Builder negativeColor(@NonNull ColorStateList colorStateList) {
            this.negativeColor = colorStateList;
            return this;
        }

        public Builder negativeText(@StringRes int negativeRes) {
            if (negativeRes == 0) return this;
            return negativeText(this.context.getText(negativeRes));
        }

        public Builder negativeText(@NonNull CharSequence message) {
            this.negativeText = message;
            return this;
        }

        public Builder neutralColor(@ColorInt int color) {
            return neutralColor(Utils.wrapColor(color));
        }

        public Builder neutralColorRes(@ColorRes int colorRes) {
            return neutralColor(Utils.wrapColor(Utils.getColor(this.context, colorRes)));
        }

        public Builder neutralColor(@NonNull ColorStateList colorStateList) {
            this.neutralColor = colorStateList;
            return this;
        }

        public Builder onPositive(@NonNull SingleButtonCallback callback) {
            this.onPositiveCallback = callback;
            return this;
        }

        public Builder onNegative(@NonNull SingleButtonCallback callback) {
            this.onNegativeCallback = callback;
            return this;
        }

        public Builder onNeutral(@NonNull SingleButtonCallback callback) {
            this.onNeutralCallback = callback;
            return this;
        }

        public Builder onAny(@NonNull SingleButtonCallback callback) {
            this.onAnyCallback = callback;
            return this;
        }

        public Builder bottomSpColor(@ColorInt int bottomSpColor) {
            this.bottomSpColor = bottomSpColor;
            return this;
        }

        public Builder bottomSpHeight(int bottomSpHeight) {
            this.bottomSpHeight = bottomSpHeight;
            return this;
        }


        public Builder cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            this.canceledOnTouchOutside = cancelable;
            return this;
        }

        public Builder canceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public Builder autoDismiss(boolean dismiss) {
            this.autoDismiss = dismiss;
            return this;
        }

        public Builder showListener(@NonNull OnShowListener listener) {
            this.showListener = listener;
            return this;
        }

        public Builder dismissListener(@NonNull OnDismissListener listener) {
            this.dismissListener = listener;
            return this;
        }

        public Builder cancelListener(@NonNull OnCancelListener listener) {
            this.cancelListener = listener;
            return this;
        }

        public Builder keyListener(@NonNull OnKeyListener listener) {
            this.keyListener = listener;
            return this;
        }

        @UiThread
        public EasyDialog create() {
            return new EasyDialog(this);
        }

        @UiThread
        public EasyDialog show() {
            EasyDialog dialog = create();
            try {
                dialog.show();
            } catch (Throwable e) {
            }
            return dialog;
        }

    }

}
