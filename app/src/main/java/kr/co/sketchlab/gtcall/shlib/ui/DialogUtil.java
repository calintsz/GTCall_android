package kr.co.sketchlab.gtcall.shlib.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import kr.co.sketchlab.gtcall.R;

public class DialogUtil {
    static ProgressDialog searchProgressDialog = null;
    public static void ShowProgressDialog(Context context, int progressDialogResId, boolean bShow) {
        ShowProgressDialog(context, progressDialogResId, bShow, null);
    }
    public static void ShowProgressDialog(Context context, int progressDialogResId, boolean bShow, String msg) {
        if(bShow) {
            if(searchProgressDialog != null) {
                try {
                    searchProgressDialog.dismiss();
                    searchProgressDialog = null;
                } catch(Exception e) {
                }
            }
            try {
                searchProgressDialog = new ProgressDialog(context);
                searchProgressDialog.setCancelable(false);
                searchProgressDialog.show();
                searchProgressDialog.setContentView(progressDialogResId);
                if (msg != null) {
                    TextView txt = (TextView) searchProgressDialog.findViewById(R.id.txt);
                    txt.setText(msg);
                } else {
                    searchProgressDialog.findViewById(R.id.txt).setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if(searchProgressDialog != null) {
                try {
                    searchProgressDialog.dismiss();
                    searchProgressDialog = null;
                } catch(Exception e) {
                }
            }
        }
    }

    public static void alertDialog(Context context, String title, String msg, DialogInterface.OnClickListener positiveButtonOnClickListener) {
        alertDialog(context, title, msg, false, positiveButtonOnClickListener);
    }

    public static void alertDialog(Context context, String title, String msg, DialogInterface.OnClickListener positiveButtonOnClickListener, DialogInterface.OnClickListener negativeButtonOnClickListener) {
        alertDialog(context, title, msg, false, positiveButtonOnClickListener, negativeButtonOnClickListener);
    }

    public static void alertDialog(Context context, String title, String msg, boolean cancelable, DialogInterface.OnClickListener positiveButtonOnClickListener) {
        alertDialog(context, title, msg, cancelable, positiveButtonOnClickListener, (DialogInterface.OnClickListener)null);
    }

    public static void alertDialog(Context context, String title, String msg, boolean cancelable, DialogInterface.OnClickListener positiveButtonOnClickListener, DialogInterface.OnClickListener negativeButtonOnClickListener) {
        alertDialog(context, title, msg, cancelable, "확인", "취소", positiveButtonOnClickListener, negativeButtonOnClickListener);
    }

    public static void alertDialog(Context context, String title, String msg, boolean cancelable, String positiveBtn, String negativeBtn, DialogInterface.OnClickListener positiveButtonOnClickListener, DialogInterface.OnClickListener negativeButtonOnClickListener) {
        AlertDialog.Builder dialogBuilder = (new AlertDialog.Builder(context)).setTitle(title).setMessage(msg).setCancelable(cancelable).setPositiveButton(positiveBtn, positiveButtonOnClickListener);
        if(negativeButtonOnClickListener != null) {
            dialogBuilder.setNegativeButton(negativeBtn, negativeButtonOnClickListener);
        }

        dialogBuilder.create().show();
    }
}
