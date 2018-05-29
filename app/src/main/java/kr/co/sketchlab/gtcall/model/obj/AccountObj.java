package kr.co.sketchlab.gtcall.model.obj;

import org.json.JSONObject;

import kr.co.sketchlab.gtcall.model.obj.base.JsonObj;

public class AccountObj extends JsonObj {
    public enum F {
        type,
        name,
        phone,
        bank_code,
        bank_account,
        bank_account_owner;
    }

    public AccountObj(JSONObject obj) {
        super(obj);
    }

    public String get(F name) {
        return super.get(name.name());
    }
}
