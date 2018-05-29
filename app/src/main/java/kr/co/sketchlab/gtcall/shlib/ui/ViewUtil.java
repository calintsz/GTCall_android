package kr.co.sketchlab.gtcall.shlib.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewUtil {
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static float px2dp(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().density;
        return px / density;
    }
    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int)(dp * density);
    }


    public static void setViewWidthHeight(View v, int width, int height) {
        ViewGroup.LayoutParams lParam = v.getLayoutParams();
        lParam.height = height;
        lParam.width = width;
    }
    public static void setViewWidth(View v, int width) {
        v.getLayoutParams().width = width;
    }
    public static void setViewHeight(View v, int height) {
        v.getLayoutParams().height = height;
    }
    public static void setViewHeightWrapContent(View v) {
        v.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
    }
    public static int getViewHeight(View v) {
        return v.getLayoutParams().height;
    }
    public static int getViewWidth(View v) {
        return v.getLayoutParams().width;
    }

    public static void setTextViewAlpha(TextView v, int alpha) {
        int color = v.getCurrentTextColor();
        int newColor = Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
        v.setTextColor(newColor);
    }

    public static void setViewMarginLeft(View v, int left, boolean requestLayout) {
        ViewGroup.MarginLayoutParams lParam = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
        lParam.leftMargin = left;
        if(requestLayout)
            v.requestLayout();
    }
    public static int getViewMarginLeft(View v) {
        return ((ViewGroup.MarginLayoutParams)v.getLayoutParams()).leftMargin;
    }
    public static int getViewMarginRight(View v) {
        ViewGroup.MarginLayoutParams lParam = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
        return lParam.rightMargin;
    }
    public static int getViewMarginBottom(View v) {
        ViewGroup.MarginLayoutParams lParam = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
        return lParam.bottomMargin;
    }
    public static int getViewMarginTop(View v) {
        ViewGroup.MarginLayoutParams lParam = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
        return lParam.topMargin;
    }
    public static void setViewMarginRight(View v, int right, boolean requestLayout) {
        ViewGroup.MarginLayoutParams lParam = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
        lParam.rightMargin = right;
        if(requestLayout)
            v.requestLayout();
    }
    public static void setViewMargin(View v, int left, int top, boolean requestLayout) {
        ViewGroup.MarginLayoutParams lParam = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
        lParam.topMargin = top;
        lParam.leftMargin = left;
        if(requestLayout)
            v.requestLayout();
    }
    public static void setViewMarginTop(View v, int top, boolean requestLayout) {
        ViewGroup.MarginLayoutParams lParam = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
        lParam.topMargin = top;
        if(requestLayout)
            v.requestLayout();
    }
    public static void setViewMarginTopAndBottom(View v, int top) {
        ViewGroup.MarginLayoutParams lParam = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
        lParam.bottomMargin = lParam.bottomMargin - (top - lParam.topMargin);
        lParam.topMargin = top;
        v.requestLayout();
    }
    public static void setViewMarginBottom(View v, int bottom, boolean requestLayout) {
        ViewGroup.MarginLayoutParams lParam = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
        lParam.bottomMargin = bottom;
        if(requestLayout)
            v.requestLayout();
    }
    public static void setViewWeight(View v, float f) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)v.getLayoutParams();
        params.weight = f;
        v.setLayoutParams(params);
    }


    public static void DragView(final View v, final int amount, int duration, Animator.AnimatorListener al) {
        AnimUtil.startValueAnimator(0, amount, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int toValue = (Integer) animation.getAnimatedValue();
                if (toValue == 0) {
                    v.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                    v.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE, 0, toValue, 0));
                } else if (toValue == amount) {
                    v.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE, 0, toValue, 0));
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            v.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
                        }
                    }, 100);
                } else {
                    v.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE, 0, toValue, 0));
                }
            }
        }, al, duration, new DecelerateInterpolator());
    }




    public static int getDeviceWidth( Context context ) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getDeviceHeight( Context context ) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }
}
