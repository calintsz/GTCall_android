package kr.co.sketchlab.gtcall.model.obj.base;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonObj {
    protected JSONObject obj;

    public JsonObj(JSONObject obj) {
        this.obj = obj;
    }

    protected String get(String name) {
        try {
            return obj.getString(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
