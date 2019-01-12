package kr.co.sketchlab.gtcall.model;

import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import kr.co.sketchlab.gtcall.model.obj.AccountObj;
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

    // 공지 페이지
    public static final String PAGE_NOTICE = APP_BASE + "/notice";
    // 설정
    public static final String PAGE_SETTING = APP_BASE + "/profile/edit?login_key=";
    // 나의 캐시백
    public static final String PAGE_CASHBACK = APP_BASE + "/cashback/manage?login_key=";
    // 일반전화번호 등록
    public static final String PAGE_SUBPHONE = APP_BASE + "/profile/subphone?login_key=";

    // 현재위치 지도보기
    public static final String PAGE_MAP = APP_BASE + "/map?login_key=";

    // 이용약관
    public static final String PAGE_TERMS = APP_BASE + "/agreement/terms";
    // 개인정보 처리방침
    public static final String PAGE_PRIVACY = APP_BASE + "/agreement/privacy";
    // 위치정보 처리방침
    public static final String PAGE_LOCATION = APP_BASE + "/agreement/location";


    // 로그인(phone, login_key)
    public static final String API_LOGIN = "member/login";
    // 회원정보 요청
    public static final String API_GET_INFO = "member/getInfo";

    // gps 좌표를 주소로 변환(login_key, lat, lng)
    public static final String API_GPS2ADDR = "location/gps2addr";

    // 전화걸기 기록 추가
    public static final String API_ADD_CALL_HISTORY = "location/addCallHistory";

    // 서비스 지역 목로 조회
    public static final String API_SERVICE_AREA = "service_area/getList";

    /**
     * 업데이트된 회원정보 조회
     * @param activity
     * @param onInfoUpdate
     */
    public static void updateMemberInfo(S3Activity activity, final View.OnClickListener onInfoUpdate) {
        SApi.with(activity, Api.API_GET_INFO)
                .param("login_key", Pref.getAccount().get(AccountObj.F.login_key))
                .call(true, new SApiCore.OnRequestComplete() {
                    @Override
                    public void onSucceeded(String str, JSONObject obj) throws Exception {
                        if(obj.getInt("state") == 0) { // 로그인 성공
                            // 계정정보
                            JSONObject accountData = obj.getJSONObject("data");
                            // 계정정보 저장
                            AccountObj accountObj = new AccountObj(accountData);
                            Pref.saveAccount(accountObj);

                            if(onInfoUpdate != null) {
                                onInfoUpdate.onClick(null);
                            }
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


    public static void tryLogin(final S3Activity fromActivity) {
        final AuthObj authObj = Pref.getAuth();

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
                            // 계정정보
                            JSONObject accountData = obj.getJSONObject("data");
                            // 계정정보 저장
                            AccountObj accountObj = new AccountObj(accountData);
                            Pref.saveAccount(accountObj);

                            // 바뀐 로그인 키 저장
                            authObj.loginKey = accountObj.get(AccountObj.F.login_key);
                            Pref.saveAuth(authObj);

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


    /**
     * 전화통화 기록 추가
     * @param activity
     * @param addr
     * @param area
     * @param callCenterPhone
     */
    public static void addCallHistory(S3Activity activity, String addr, String area, String callCenterPhone) {
        SApi.with(activity, Api.API_ADD_CALL_HISTORY)
                .param("login_key", Pref.getAccount().get(AccountObj.F.login_key))
                .param("addr", addr)
                .param("area", area)
                .param("call_center_phone", callCenterPhone)
                .call(false, new SApiCore.OnRequestComplete() {
                    @Override
                    public void onSucceeded(String str, JSONObject obj) throws Exception {

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
