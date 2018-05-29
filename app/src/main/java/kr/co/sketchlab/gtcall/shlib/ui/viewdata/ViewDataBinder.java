package kr.co.sketchlab.gtcall.shlib.ui.viewdata;

import android.app.Activity;
import android.view.View;

/**
 * Created by soohwan on 2015-04-01.
 */
public class ViewDataBinder {
    Object tag;

    Activity a = null;
    View v = null;

    public Object getTag() {
        return tag;
    }
    public void setTag(Object tag) {
        this.tag = tag;
    }

    public ViewDataBinder(Activity a) {
        this.a = a;
    }
    public ViewDataBinder(View v) {
        this.v = v;
    }

    public ViewData v(int resId) {
        if(a != null) {
            return new ViewData(a.findViewById(resId));
        }
        if(v != null) {
            return new ViewData(v.findViewById(resId));
        }
        return null;
    }

    public ViewData self() {
        if(v != null) {
            return new ViewData(v);
        } else {
            return null;
        }
    }
}
