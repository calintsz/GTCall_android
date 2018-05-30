package kr.co.sketchlab.gtcall.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import kr.co.sketchlab.gtcall.GTCallActivity;
import kr.co.sketchlab.gtcall.R;
import kr.co.sketchlab.gtcall.model.Api;
import kr.co.sketchlab.gtcall.model.Pref;
import kr.co.sketchlab.gtcall.shlib.data.StringUtil;

public class SplashActivity extends GTCallActivity {
    private final static int PERMISSIONS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) { // 권한없음
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CODE);
            return;
        } else { // 권한있음
            savePhoneNumberAndTryLogin();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) { // 권한 없음
                Api.tryLogin(mActivity);
            } else { // 권한 가짐
                savePhoneNumberAndTryLogin();
            }
        }

    }

    /**
     * 전화번호를 읽어서 저장하고, 로그인 시도
     */
    private void savePhoneNumberAndTryLogin() {
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (tMgr != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Api.tryLogin(mActivity);
                return;
            }
            String phoneNumber = tMgr.getLine1Number();
            // 국가코드 삭제
            phoneNumber = StringUtil.toLocalPhoneNumber(phoneNumber);
            // 번호 저장
            Pref.savePhoneNumber(phoneNumber);
        }

        Api.tryLogin(mActivity);
    }
}
