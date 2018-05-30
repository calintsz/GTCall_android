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

    public JSONObject getRawObj() {
        return obj;
    }

    public String get(String name) {
        try {
            return obj.getString(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JsonObj getObj(String name) {
        try {
            JSONObject jo = obj.getJSONObject(name);
            if(jo == null) {
                return null;
            }
            return new JsonObj(jo);
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
