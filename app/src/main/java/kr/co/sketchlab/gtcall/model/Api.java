package kr.co.sketchlab.gtcall.model;

import android.widget.Toast;

import org.json.JSONObject;

import kr.co.sketchlab.gtcall.model.obj.AuthObj;
import kr.co.sketchlab.gtcall.shlib.net.SApi;
import kr.co.sketchlab.gtcall.shlib.net.SApiCore;
import kr.co.sketchlab.gtcall.shlib.ui.activity.S3Activity;
import kr.co.sketchlab.gtcall.ui.activity.MainActivity;
import kr.co.sketchlab.gtcall.ui.activity.WebActivity;

public class Api {
    public static final String WEB_BASE = "http://gtcall.sketchlab.co.kr";
    public static final String API_BASE = WEB_BASE + "/api/";

    public static final String APP_BASE = WEB_BASE + "/app";



    // 로그인(phone, login_key)
    public static final String API_LOGIN = "member/login";


    public static void tryLogin(final S3Activity fromActivity) {
        AuthObj authObj = Pref.getAuth();

        if(authObj == null) {
            WebActivity.start(fromActivity, Api.APP_BASE, "회원가입", true);
            return;
        }

        // 로그인 시도
        SApi.with(fromActivity, Api.API_LOGIN)
                .param("phone", authObj.phone)
                .param("login_key", authObj.loginKey)
                .param("device_os", "Android")
                .call(true, new SApiCore.OnRequestComplete() {
                    @Override
                    public void onSucceeded(String str, JSONObject obj) throws Exception {
                        // 로그인 성공이면 메인으로, 실패면 로그인화면으로
                        if(obj.getInt("state") == 0) { // 로그인 성공
                            // 메인으로 이동
                            fromActivity.startActivityWithClear(MainActivity.class);
                        } else { // 로그인 실패
                            Toast.makeText(fromActivity, obj.getString("msg"), Toast.LENGTH_LONG).show();
                            WebActivity.start(fromActivity, Api.APP_BASE, "회원가입", true);
                        }
                    }

                    @Override
                    public void onFailed(String message) {

                    }

                    @Override
                    public void onError(String message) {

                    }
                });
    }
}
