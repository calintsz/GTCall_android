package kr.co.sketchlab.gtcall.shlib.ui.viewdata;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by soohwan on 2015-04-01.
 */
public class ViewData {
    View v;
    public ViewData(View v) {
        this.v = v;
    }
    public View v() { return v; }
    public boolean selected() {
        return v.isSelected();
    }
    public ViewData selected(boolean b) {
        v.setSelected(b);
        return this;
    }
    public ImageView imageView() {
        return (ImageView)v;
    }
    public TextView textView() {
        return (TextView)v;
    }
    public EditText editText() {
        return (EditText)v;
    }
    public LinearLayout linearLayout() { return (LinearLayout)v; }
    public RelativeLayout relativeLayout() { return (RelativeLayout) v; }

    public boolean getChecked() {
        if(v instanceof CheckBox) {
            return ((CheckBox) v).isChecked();
        }
        return false;
    }
    public ViewData setChecked(boolean check) {
        if(v instanceof CheckBox) {
            ((CheckBox) v).setChecked(check);
        }
        return this;
    }

    public ViewData rotate(float degree) {
        v.setRotation(degree);
        return this;
    }
    public ViewData animate(Animation anim) {
        v.startAnimation(anim);
        return this;
    }
    public ViewData setTag(Object tag) {
        v.setTag(tag);
        return this;
    }
    public Object tag() {
        return v.getTag();
    }
    public ViewData measure() {
        v.measure(0, 0);
        return this;
    }
    public int measuredWidth() {
        return v.getMeasuredWidth();
    }
    public int measuredHeight() {
        return v.getMeasuredHeight();
    }
    public void setImage(int resId) {
        if(v instanceof ImageView) {
            ((ImageView) v).setImageResource(resId);
        }
    }
    public void setImage(Bitmap bm) {
        if(v instanceof ImageView) {
            ((ImageView) v).setImageBitmap(bm);
        }
    }
    public String text() {
        if(v instanceof TextView) {
            return ((TextView) v).getText().toString();
        } else if(v instanceof EditText) {
            return ((EditText) v).getText().toString();
        }
        return null;
    }
    public ViewData setText(int strResId) {
        ((TextView)v).setText(strResId);
        return this;
    }
    public ViewData setText(String text) {
        if(v instanceof TextView) {
            ((TextView) v).setText(text);
        } else if(v instanceof EditText) {
            ((EditText) v).setText(text);
        }
        return this;
    }
    public ViewData setTextColor(Context context, int res) {
        if(v instanceof TextView) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((TextView) v).setTextColor(context.getColor(res));
            } else {
                ((TextView) v).setTextColor(context.getResources().getColor(res));
            }
        }
        return this;
    }
    public ViewData setTextColor(int color) {
        if(v instanceof TextView) {
            ((TextView) v).setTextColor(color);
        }
        return this;
    }
    public ViewData setText(Spannable text) {
        if(v instanceof TextView) {
            ((TextView) v).setText(text);
        }
        return this;
    }
    public void setText(JSONObject obj, String name) {
        if(v instanceof TextView) {
            try {
                Object value = obj.get(name);
                String strValue = "";
                if (value instanceof Integer) {
                    strValue = Integer.toString((Integer) value);
                } else if (value instanceof Float) {
                    strValue = Float.toString((Float) value);
                } else if (value instanceof Boolean) {
                    strValue = Boolean.toString((Boolean) value);
                } else if (value instanceof Double) {
                    strValue = Double.toString((Double) value);
                } else if (value instanceof String) {
                    strValue = value.toString();
                }
                ((TextView) v).setText(strValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ViewData click(View.OnClickListener l) {
        if(v != null)
            v.setOnClickListener(l);
        return this;
    }

    public void visibility(int visibility) {
        v.setVisibility(visibility);
    }
    public int visibility() {
        return v.getVisibility();
    }
    public ViewData visible() {
        v.setVisibility(View.VISIBLE);
        return this;
    }
    public ViewData gone() {
        v.setVisibility(View.GONE);
        return this;
    }
    public void invisible() {
        v.setVisibility(View.INVISIBLE);
    }
    public boolean isVisible() {
        return v.getVisibility() == View.VISIBLE;
    }
    public boolean isGone() {
        return v.getVisibility() == View.GONE;
    }



    public ViewData marginTop(int t) {
        ViewGroup.MarginLayoutParams lParam = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
        lParam.topMargin = t;
        return this;
    }
}
