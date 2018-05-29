package kr.co.sketchlab.gtcall.shlib.data;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class JsonUtil {
    public static Bundle JSONObjectToBundle(JSONObject obj) {
        Bundle b = new Bundle();
        Iterator<String> iter = obj.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                Object v= obj.get(key);
                if(v instanceof JSONObject) {
                    b.putBundle(key, JSONObjectToBundle((JSONObject) v));
                } else if(v instanceof Integer) {
                    b.putInt(key, (Integer)v);
                } else if(v instanceof Boolean) {
                    b.putBoolean(key, (Boolean)v);
                } else if(v instanceof Double) {
                    b.putDouble(key, (Double)v);
                } else if(v instanceof Float) {
                    b.putFloat(key, (Float)v);
                } else {
                    b.putString(key, v.toString());
                }
            } catch (JSONException e) {
            }
        }
        return b;
    }
    public static JSONObject bundleToJSONObject(Bundle b) {
        JSONObject json = new JSONObject();
        Set<String> keys = b.keySet();
        try {
            for (String key : keys) {
                json.put(key, b.get(key));
            }
        } catch(JSONException e) {
            return null;
        }
        return json;
    }

    /**
     * JSONArr 을 ArrayList로 만들어준다
     * @param arr
     * @return
     */
    public static ArrayList<JSONObject> toArrayList(JSONArray arr) {
        ArrayList<JSONObject> objs = new ArrayList<>();
        if (arr == null) {
            return objs;
        }
        try {
            for (int i = 0; i < arr.length(); i++) {
                objs.add(arr.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objs;
    }
}
