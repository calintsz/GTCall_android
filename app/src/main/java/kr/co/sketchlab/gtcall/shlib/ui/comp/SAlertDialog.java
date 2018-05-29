package kr.co.sketchlab.gtcall.shlib.ui.comp;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import kr.co.sketchlab.gtcall.R;
import kr.co.sketchlab.gtcall.shlib.ui.viewdata.ViewDataBinder;


/**
 * Created by shp on 2018. 3. 18..
 */

public class SAlertDialog {
    Activity activity;
    AlertDialog alertDialog;

    public static SAlertDialog show(final Activity activity, String msg, final View.OnClickListener onCloseClick) {
        return new SAlertDialog(activity, msg, "확인", null, onCloseClick, null);
    }
    public static SAlertDialog show(final Activity activity, String msg, String btnClose, final View.OnClickListener onCloseClick) {
        return new SAlertDialog(activity, msg, btnClose, null, onCloseClick, null);
    }
    public static SAlertDialog show(final Activity activity, String msg, String btnClose, String btnOk, final View.OnClickListener onCloseClick, View.OnClickListener onOkClick) {
        return new SAlertDialog(activity, msg, btnClose, btnOk, onCloseClick, onOkClick);
    }

    private SAlertDialog(final Activity activity, String msg, String btnClose, String btnOk, final View.OnClickListener onCloseClick, final View.OnClickListener onOkClick) {
        this.activity = activity;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.dialog_salert, null);
        dialogBuilder.setView(rootView);
        dialogBuilder.setCancelable(false);

        final ViewDataBinder v = new ViewDataBinder(rootView);

        // 메세지 설정
        v.v(R.id.txtMsg).setText(msg);

        if(btnOk == null) { // 원버튼
            v.v(R.id.btnClose).gone();

            v.v(R.id.btnOk).setText(btnClose);
            v.v(R.id.btnOk).click(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onCloseClick != null) {
                        onCloseClick.onClick(view);
                    }
                    dismiss();
                }
            });
        } else { // 투버튼
            v.v(R.id.btnClose).setText(btnClose);

            // 닫기 클릭
            v.v(R.id.btnClose).click(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onCloseClick != null)
                        onCloseClick.onClick(view);
                    dismiss();
                }
            });

            v.v(R.id.btnOk).setText(btnOk);
            v.v(R.id.btnOk).click(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onOkClick != null) {
                        onOkClick.onClick(view);
                    }
                    dismiss();
                }
            });
        }




        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void dismiss() {
        alertDialog.dismiss();
    }
}
