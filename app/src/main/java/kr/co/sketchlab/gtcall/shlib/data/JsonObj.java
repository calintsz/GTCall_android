package kr.co.sketchlab.gtcall.shlib.data;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonObj {
    protected JSONObject obj;

    public JsonObj(JSONObject obj) {
        this.obj = obj;
    }
    public JsonObj(String strObj) throws JSONException {
        this.obj = new JSONObject(strObj);
    }

    protected String get(String name) {
        try {
            return obj.getString(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return obj.toString();
    }
}
