package kr.co.sketchlab.gtcall.model;

import org.json.JSONException;

import kr.co.sketchlab.gtcall.TheApp;
import kr.co.sketchlab.gtcall.model.obj.AccountObj;
import kr.co.sketchlab.gtcall.model.obj.AuthObj;
import kr.co.sketchlab.gtcall.shlib.data.ShPref;

public class Pref {
    public static final String PREF_NAME = "gtcall";

    private static AuthObj authObjCache = null;

    private static void save(String key, String val) {
        ShPref.setPreference(TheApp.instance(), PREF_NAME, key, val);
    }
    private static String get(String key) {
        return ShPref.getPreference(TheApp.instance(), PREF_NAME, key, null);
    }

    /**
     * 로그인 인증정보 저장
     * @param obj
     */
    public static void saveAuth(AuthObj obj) {
        save("auth_phone", obj.phone);
        save("auth_login_key", obj.loginKey);

        authObjCache = new AuthObj(obj.phone, obj.loginKey);
    }

    /**
     * 로그인 인증정보 꺼내오기
     * @return
     */
    public static AuthObj getAuth() {
        if(authObjCache == null) {
            String phone = get("auth_phone");
            String loginKey = get("auth_login_key");
            if(phone == null && loginKey == null) {
                return null;
            }

            authObjCache = new AuthObj(phone, loginKey);
        }

        return authObjCache;
    }

    /**
     * 계정정보 저장
     * @param obj
     */
    public static void saveAccount(AccountObj obj) {
        String strObj = obj.toString();
        save("accountObj", strObj);
    }

    /**
     * 저장된 계정정보 조회
     * @return
     */
    public static AccountObj getAccount() {
        String strObj = get("accountObj");
        if(strObj == null) {
            return null;
        }

        try {
            return new AccountObj(strObj);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
