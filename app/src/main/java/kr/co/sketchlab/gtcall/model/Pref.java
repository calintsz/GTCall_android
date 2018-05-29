package kr.co.sketchlab.gtcall.model;

import kr.co.sketchlab.gtcall.TheApp;
import kr.co.sketchlab.gtcall.model.obj.AuthObj;
import kr.co.sketchlab.gtcall.shlib.data.ShPref;

public class Pref {
    public static final String PREF_NAME = "gtcall";

    private static AuthObj authObjCache = null;
    /**
     * 로그인 인증정보 저장
     * @param obj
     */
    public static void saveAuth(AuthObj obj) {
        ShPref.setPreference(TheApp.instance(), PREF_NAME, "auth_phone", obj.phone);
        ShPref.setPreference(TheApp.instance(), PREF_NAME, "auth_login_key", obj.loginKey);

        authObjCache = new AuthObj(obj.phone, obj.loginKey);
    }

    /**
     * 로그인 인증정보 꺼내오기
     * @return
     */
    public static AuthObj getAuth() {
        if(authObjCache == null) {
            String phone = ShPref.getPreference(TheApp.instance(), PREF_NAME, "auth_phone", null);
            String loginKey = ShPref.getPreference(TheApp.instance(), PREF_NAME, "auth_login_key", null);
            if(phone == null && loginKey == null) {
                return null;
            }

            authObjCache = new AuthObj(phone, loginKey);
        }

        return authObjCache;
    }
}
