package kr.co.sketchlab.gtcall.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import kr.co.sketchlab.gtcall.BuildConfig;
import kr.co.sketchlab.gtcall.GTCallActivity;
import kr.co.sketchlab.gtcall.R;
import kr.co.sketchlab.gtcall.model.Api;
import kr.co.sketchlab.gtcall.model.Pref;
import kr.co.sketchlab.gtcall.model.obj.AccountObj;
import kr.co.sketchlab.gtcall.shlib.ui.comp.SAlertDialog;

public class MapActivity extends GTCallActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent data = getIntent();
        double lat = data.getDoubleExtra("lat", 0);
        double lng = data.getDoubleExtra("lng", 0);
        String addr = data.getStringExtra("addr");

        v(R.id.txtAddr).setText(addr);

        v(R.id.btnBack).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 현재위치 버튼 클릭
        v(R.id.btnGps).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:moveToCurrentLocation()");
            }
        });


        webView = (WebView) v(R.id.webview).v();

        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(false);

        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setGeolocationDatabasePath( getFilesDir().getPath() );

        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 250811
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        // 웹뷰 디버깅 가능하게
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (BuildConfig.DEBUG) { // 디버그 모드에서만
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }

        try {
            webView.loadUrl(Api.PAGE_MAP + Pref.getAccount().get(AccountObj.F.login_key) + "&lat=" + URLEncoder.encode(Double.toString(lat), "UTF-8") + "&lng=" + URLEncoder.encode(Double.toString(lng), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    boolean doAppCommand(String url) {
        if (url.startsWith("app://")) {
            String cmd = url.substring("app://".length());
            String[] fields = cmd.split("\\?");

            cmd = fields[0];
            if(cmd.equals("close")) {
                finish();
            }

            return true;
        }

        return false;
    }

    class MyWebChromeClient extends WebChromeClient {
        public MyWebChromeClient() {

        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//            return super.onJsAlert(view, url, message, result);
            SAlertDialog.show(mActivity, message, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    result.confirm();
                }
            });
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
//            return super.onJsConfirm(view, url, message, result);
            SAlertDialog.show(mActivity, message, "취소", "확인", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 취소
                    result.cancel();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 확인
                    result.confirm();
                }
            });
            return true;
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
//            super.onGeolocationPermissionsShowPrompt(origin, callback);
            callback.invoke(origin, true, false);
        }
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return myShouldOverrideUrlLoading(view, url);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return myShouldOverrideUrlLoading(view, request.getUrl().toString());
        }


        private boolean myShouldOverrideUrlLoading(WebView view, String url) {
            Log.d("URL", url);
            if (url.startsWith("http") && url.contains(Api.WEB_BASE)) { //
                // 안드로이드에서는 전화번호가 있으면, 휴대전화 인증과정을 건너뜀
//                if(url.endsWith("register/phone_confirm")) {
//                    String phone = Pref.getPhoneNumber();
//                    if(phone != null) {
//                        try {
//                            url = url + "?phone=" + URLEncoder.encode(phone, "UTF-8");
//                            Log.d("URL", url);
//                            webView.loadUrl(url);
//                            return true;
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
                return false;
            } else { //
                if (!doAppCommand(url)) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);


        }
    }
}
