package kr.co.sketchlab.gtcall.shlib.ui;

import android.content.Context;
import android.os.Handler;

import kr.co.sketchlab.gtcall.R;


/**
 * Created by soohwan on 2015-05-20.
 */
public class Progress {
    private static Handler hShow = null;
    private static Runnable rShow = null;
    public static void showProgress(final Context context, final String msg, boolean noLoadingDelay) {
        if(hShow != null) {
            return;
        }
        if(rShow != null) {
            return;
        }
        hShow = new Handler();
        rShow = new Runnable() {
            @Override
            public void run() {
                rShow = null;
                hShow = null;

                DialogUtil.ShowProgressDialog(context, R.layout.dialog_progress, true, msg);
            }
        };
        hShow.postDelayed(rShow, noLoadingDelay?0:1000);
    }
    public static void showProgress(Context context, boolean noLoadingDelay) {
        showProgress(context, "Loading...", noLoadingDelay);
    }
    public static void hideProgress() {
        if(hShow != null && rShow != null) {
            hShow.removeCallbacks(rShow);
            hShow = null;
            rShow = null;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DialogUtil.ShowProgressDialog(null, -1, false);
            }
        }, 300);
    }
}
