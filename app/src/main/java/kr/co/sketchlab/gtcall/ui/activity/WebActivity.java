package kr.co.sketchlab.gtcall.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import kr.co.sketchlab.gtcall.model.obj.AuthObj;
import kr.co.sketchlab.gtcall.shlib.data.ShHashMap;
import kr.co.sketchlab.gtcall.shlib.data.StringUtil;
import kr.co.sketchlab.gtcall.shlib.ui.comp.SAlertDialog;

public class WebActivity extends GTCallActivity {
    WebView webView;

    public static void start(Activity fromActivity, String url, String title, boolean clear) {
        Intent intent = new Intent(fromActivity, WebActivity.class);
        if(clear) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        intent.putExtra("title", title);
        intent.putExtra("url", url);
        fromActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Intent data = getIntent();
        String title = data.getStringExtra("title");
        String url = data.getStringExtra("url");

        // 타이틀
        v(R.id.txtTitle).setText(title);


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

        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 250811
        webView.getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setAppCacheEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        // 웹뷰 디버깅 가능하게
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (BuildConfig.DEBUG) { // 디버그 모드에서만
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }


        // 백
        v(R.id.btnBack).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });

        webView.loadUrl(url);
    }

    boolean doAppCommand(String url) {
        if (url.startsWith("app://")) {
            String cmd = url.substring("app://".length());
            String[] fields = cmd.split("\\?");

            cmd = fields[0];
            if(cmd.equals("login_suc")) {
                ShHashMap params = StringUtil.paramFromUrl(url);
                String phone = params.getString("phone");
                String loginKey = params.getString("login_key");

                AuthObj authObj = new AuthObj(phone, loginKey);
                Pref.saveAuth(authObj);

                // 로그인 성공시, key 로 다시 로그인 시도
                Api.tryLogin(mActivity);
            } else if(cmd.equals("close")) {
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
                if(url.endsWith("register/phone_confirm")) {
                    String phone = Pref.getPhoneNumber();
                    if(phone != null) {
                        try {
                            url = url + "?phone=" + URLEncoder.encode(phone, "UTF-8");
                            Log.d("URL", url);
                            webView.loadUrl(url);
                            return true;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
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
