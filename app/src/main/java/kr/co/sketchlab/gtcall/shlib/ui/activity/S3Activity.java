package kr.co.sketchlab.gtcall.shlib.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.co.sketchlab.gtcall.shlib.ui.viewdata.ViewData;
import kr.co.sketchlab.gtcall.shlib.ui.viewdata.ViewDataBinder;

/**
 * Created by soohwanpark on 2015-03-11.
 */
public class S3Activity extends FragmentActivity {

    protected String TAG = this.getClass().getSimpleName();

    private boolean resumed = false;
    ArrayList<BroadcastReceiver> broadcastReceivers = new ArrayList<>();

    protected ViewDataBinder q;

    protected Activity mActivity;

    private static S3Activity lastActivity = null;
    public static S3Activity getLastActivity() {
        return lastActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;

        q = new ViewDataBinder(this);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = mActivity.getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(Color.parseColor("#30313c"));
//        }

    }

    protected ViewData v(int resId) {
        return q.v(resId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lastActivity = this;
        resumed = true;
        Log.d(TAG, "Activity Resumed");
    }
    public void addLocalBroadcastReceiver(String[] actions, BroadcastReceiver receiver) {

        if (actions == null || actions.length == 0) return;

        IntentFilter intentFilter = new IntentFilter();

        for (String action : actions) {
            intentFilter.addAction(action);
        }

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, intentFilter);
        broadcastReceivers.add(receiver);

    }

    public void addLocalBroadcastReceiver(List<String> actions, BroadcastReceiver receiver) {

        if (actions == null || actions.size() == 0) return;

        IntentFilter intentFilter = new IntentFilter();

        for (String action : actions) {
            intentFilter.addAction(action);
        }

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, intentFilter);
        broadcastReceivers.add(receiver);

    }

    public void addLocalBroadcastReceiver(String action, BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(action);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, intentFilter);
        broadcastReceivers.add(receiver);
    }

    public void removeLocalBroadcastReceiver(BroadcastReceiver receiver) {
        Log.d(TAG, "removeLocalBroadcastReceiver");

        broadcastReceivers.remove(receiver);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver);
    }

    public void sendLocalBroadcast(String action) {
        sendLocalBroadcast(getApplicationContext(), action);
    }
    public static void sendLocalBroadcast(Context applicationContext, String action) {
        sendLocalBroadcast(applicationContext, action, null);
    }
    public void sendLocalBroadcast(String action, Bundle data) {
        sendLocalBroadcast(getApplicationContext(), action, data);
    }
    public static void sendLocalBroadcast(Context applicationContext, String action, Bundle data) {
        Intent intent = new Intent();
        intent.setAction(action);
        if(data != null)
            intent.putExtra("data", data);
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent);
    }

    protected void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }
    protected void startActivity(Class<?> cls, int requestCode) {
        startActivityForResult(new Intent(this, cls), requestCode);
    }

    protected void startActivity(Class<?> cls, Intent intent) {
        intent.setComponent(new ComponentName(this, cls));
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        for(BroadcastReceiver receiver : broadcastReceivers) {
            localBroadcastManager.unregisterReceiver(receiver);
        }
        broadcastReceivers.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        resumed = false;
        Log.d(TAG, "Activity Paused");
    }

    protected S3Activity getThis() {
        return this;
    }

    public boolean isActivityResumed() {
        Log.d("CHECK RESUMED", getLocalClassName() + " : " + Boolean.toString(resumed));
        return resumed;
    }


    protected void setTextTo(int resTxtView, int resString) {
        ((TextView)findViewById(resTxtView)).setText(resString);
    }

    protected void setTextTo(int resTxtView, String str) {
        ((TextView)findViewById(resTxtView)).setText(str);
    }
    protected void setTextTo(int resTxtView, Spannable str) {
        ((TextView)findViewById(resTxtView)).setText(str);
    }

    protected String getTextFromTextView(int resTxtView) {
        return ((TextView)findViewById(resTxtView)).getText().toString();
    }
    protected String getTextFromEditText(int resTxtView) {
        return ((EditText)findViewById(resTxtView)).getText().toString();
    }

    public static void setTextTo(View v, int resTxtView, String str) {
        ((TextView)v.findViewById(resTxtView)).setText(str);
    }
    public static void setTextTo(View v, int resTxtView, Spannable str) {
        ((TextView)v.findViewById(resTxtView)).setText(str);
    }
    public void setTextToEditText(int resEditText, String str) {
        ((EditText)findViewById(resEditText)).setText(str);
    }

    public void setCheck(int resChk, boolean chk) {
        ((CheckBox)findViewById(resChk)).setChecked(chk);
    }
    public boolean getCheck(int resChk) {
        return ((CheckBox)findViewById(resChk)).isChecked();
    }

    public void setImageTo(int resImageView, int resImage) {
        ((ImageView)findViewById(resImageView)).setImageResource(resImage);
    }
    public static void setImageTo(View rootView, int resImageView, int resImage) {
        ((ImageView)rootView.findViewById(resImageView)).setImageResource(resImage);
    }

    public static void setMultipleOnClickListener(View rootView, int[] resIds, View.OnClickListener onClickListener) {
        for(int i = 0; i < resIds.length; i++) {
            rootView.findViewById(resIds[i]).setOnClickListener(onClickListener);
        }
    }
    public void setMultipleOnClickListener(int[] resIds, View.OnClickListener onClickListener) {
        for(int i = 0; i < resIds.length; i++) {
            findViewById(resIds[i]).setOnClickListener(onClickListener);
        }
    }

    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
    public void showToast(int resStr) {
        showToast(this, resStr);
    }
    public static void showToast(Activity activity, int resStr) {
        Toast.makeText(activity, resStr, Toast.LENGTH_SHORT).show();
    }

    protected void postDelayed(Runnable r, long delayMillis) {
        (new Handler()).postDelayed(r, delayMillis);
    }
    public void hideKeyboard(View edit_view){

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(edit_view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

}
