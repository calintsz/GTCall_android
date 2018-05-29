package kr.co.sketchlab.gtcall.model.obj;

import org.json.JSONException;
import org.json.JSONObject;

import kr.co.sketchlab.gtcall.shlib.data.JsonObj;

public class AccountObj extends JsonObj {
    public enum F {
        type,
        name,
        phone,
        bank_code,
        bank_account,
        bank_account_owner,
        login_key,
        follower_cnt;
    }

    public AccountObj(JSONObject obj) {
        super(obj);
    }

    public AccountObj(String strObj) throws JSONException {
        super(strObj);
    }

    public String get(F name) {
        return super.get(name.name());
    }


}
