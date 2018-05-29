package kr.co.sketchlab.gtcall.shlib.ui;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;

public class AnimUtil {
    public interface OnAnimationUpdateListener {
        void onAnimationUpdate(int toValue);
    }


    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
//        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(300);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
//        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(300);
        v.startAnimation(a);
    }

    public static Animation fadeIn(View v, int duration, Animation.AnimationListener al) {
        Animation fade = new AlphaAnimation(0, 1);
        fade.setInterpolator(new AccelerateInterpolator());
        fade.setDuration(duration);
        if(al != null)
            fade.setAnimationListener(al);
        if(v != null)
            v.startAnimation(fade);
        return fade;
    }
    public static Animation fadeIn(View v) {
        return fadeIn(v, 200, null);
    }
    public static Animation fadeOut(View v) {
        return fadeOut(v, 200, null);
    }
    public static Animation fadeOut(View v, int duration, Animation.AnimationListener al) {
        Animation fade = new AlphaAnimation(1, 0);
        fade.setInterpolator(new DecelerateInterpolator());
        fade.setDuration(duration);
        if(al != null)
            fade.setAnimationListener(al);
        if(v != null)
            v.startAnimation(fade);
        return fade;
    }
    public static void scaleHide(View v) {
        scaleHide(v, 200, null);
    }
    public static void scaleHide(View v, int duration, Animation.AnimationListener l) {
        Animation anim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(duration);
        if(l != null)
            anim.setAnimationListener(l);
        v.startAnimation(anim);
    }
    public static void scalePop(View v) {
        scalePop(v, 200, null);
    }
    public static void scalePop(View v, int duration, Animation.AnimationListener l) {
        Animation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(duration);
        v.startAnimation(anim);
    }
    public static void scaleAnimation(View v, float initialScale, float finalScale, int duration) {
        Animation anim = new ScaleAnimation(initialScale, finalScale, initialScale, finalScale, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(duration);
        v.startAnimation(anim);
    }
    public static void scaleBounce(View v, float scale, int duration) {
        AnimationSet as = new AnimationSet(false);

        Animation anim = new ScaleAnimation(1.0f, scale, 1.0f, scale, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(duration);
        as.addAnimation(anim);

        anim = new ScaleAnimation(1.0f, 1.0f/scale, 1.0f, 1.0f/scale, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(duration);
        anim.setStartOffset(duration);
        as.addAnimation(anim);

        v.startAnimation(as);
    }
    public static ValueAnimator startValueAnimator(int from, int to, ValueAnimator.AnimatorUpdateListener updateListener, ValueAnimator.AnimatorListener animatorListener, int duration) {
        ValueAnimator ani = ValueAnimator.ofInt(from, to);
        ani.setInterpolator(new LinearInterpolator());
        if(updateListener != null)
            ani.addUpdateListener(updateListener);
        if(animatorListener != null)
            ani.addListener(animatorListener);
        ani.setDuration(duration);
        ani.start();
        return ani;
    }
    public static ValueAnimator startValueAnimator(int from, int to, ValueAnimator.AnimatorUpdateListener updateListener, ValueAnimator.AnimatorListener animatorListener, int duration, TimeInterpolator interpolator) {
        ValueAnimator ani = ValueAnimator.ofInt(from, to);
        ani.setInterpolator(interpolator);
        if(updateListener != null)
            ani.addUpdateListener(updateListener);
        if(animatorListener != null)
            ani.addListener(animatorListener);
        ani.setDuration(duration);
        ani.start();
        return ani;
    }
    public static void swipeView(View v, int toMarginLeft, ValueAnimator.AnimatorListener animatorListener) {
        swipeView(v, toMarginLeft, animatorListener, 700);
    }

    public static void swipeView(final View v, int toMarginLeft, ValueAnimator.AnimatorListener animatorListener, int duration) {
        final ViewGroup.MarginLayoutParams lParam = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
        ValueAnimator ani = ValueAnimator.ofInt(lParam.leftMargin, toMarginLeft);
        ani.setInterpolator(AnimationUtils.loadInterpolator(v.getContext(), android.R.interpolator.decelerate_cubic));
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int toValue = (Integer) animation.getAnimatedValue();
                lParam.leftMargin = toValue;
                v.requestLayout();
            }
        });
        if(animatorListener != null)
            ani.addListener(animatorListener);
        ani.setDuration(duration);
        ani.start();
    }
    public static void swipeUpView(final View v, int toMarginBottom, ValueAnimator.AnimatorListener animatorListener) {
        final ViewGroup.MarginLayoutParams lParam = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
        ValueAnimator ani = ValueAnimator.ofInt(lParam.bottomMargin, toMarginBottom);
        ani.setInterpolator(AnimationUtils.loadInterpolator(v.getContext(), android.R.interpolator.decelerate_cubic));
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int toValue = (Integer) animation.getAnimatedValue();
                lParam.bottomMargin = toValue;
                v.requestLayout();
            }
        });
        if(animatorListener != null)
            ani.addListener(animatorListener);
        ani.setDuration(300);
        ani.start();
    }
    public static void swipeDownView(View v, int toMarginTop, ValueAnimator.AnimatorListener animatorListener, int duration) {
        swipeDownView(v, toMarginTop, animatorListener, null, duration);
    }
    public static void swipeDownView(final View v, int toMarginTop, ValueAnimator.AnimatorListener animatorListener, final OnAnimationUpdateListener onAnimationUpdateListener, int duration) {
        if(v == null)
            return;
        final ViewGroup.MarginLayoutParams lParam = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
        ValueAnimator ani = ValueAnimator.ofInt(lParam.topMargin, toMarginTop);
        ani.setInterpolator(AnimationUtils.loadInterpolator(v.getContext(), android.R.interpolator.decelerate_cubic));
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int toValue = (Integer) animation.getAnimatedValue();
                lParam.topMargin = toValue;
                v.requestLayout();
                if(onAnimationUpdateListener != null)
                    onAnimationUpdateListener.onAnimationUpdate(toValue);
            }
        });
        if(animatorListener != null)
            ani.addListener(animatorListener);
        ani.setDuration(duration);
        ani.start();
    }
    public static ValueAnimator animateHeight(final View v, int toHeight, int duration, ValueAnimator.AnimatorListener animatorListener) {
        final ViewGroup.LayoutParams lParam = v.getLayoutParams();

        ValueAnimator ani = ValueAnimator.ofInt(lParam.height, toHeight);
        ani.setInterpolator(AnimationUtils.loadInterpolator(v.getContext(), android.R.interpolator.decelerate_cubic));
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int toValue = (Integer) animation.getAnimatedValue();
                lParam.height = toValue;
                v.requestLayout();
            }
        });
        if(animatorListener != null)
            ani.addListener(animatorListener);
        ani.setDuration(duration);
        ani.start();
        return ani;
    }
    public static void collapseHeight(final View v, int duration, ValueAnimator.AnimatorListener animatorListener) {
        int curHeight = v.getHeight();

        final ViewGroup.LayoutParams lParam = v.getLayoutParams();

        ValueAnimator ani = ValueAnimator.ofInt(curHeight, 0);
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int toValue = (Integer) animation.getAnimatedValue();
                lParam.height = toValue;
                v.requestLayout();
            }
        });
        if(animatorListener != null)
            ani.addListener(animatorListener);
        ani.setDuration(duration);
        ani.start();
    }
    public static void animateHeight(final View v, int from, int to, int duration, ValueAnimator.AnimatorListener animatorListener) {
        final ViewGroup.LayoutParams lParam = v.getLayoutParams();

        ValueAnimator ani = ValueAnimator.ofInt(from, to);
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int toValue = (Integer) animation.getAnimatedValue();
                lParam.height = toValue;
                v.requestLayout();
            }
        });
        if(animatorListener != null)
            ani.addListener(animatorListener);
        ani.setDuration(duration);
        ani.start();
    }
    public static void animateWidth(final View v, int from, int to, int duration, ValueAnimator.AnimatorListener animatorListener) {
        final ViewGroup.LayoutParams lParam = v.getLayoutParams();

        ValueAnimator ani = ValueAnimator.ofInt(from, to);
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int toValue = (Integer) animation.getAnimatedValue();
                lParam.width = toValue;
                v.requestLayout();
            }
        });
        if(animatorListener != null)
            ani.addListener(animatorListener);
        ani.setDuration(duration);
        ani.start();
    }
}
