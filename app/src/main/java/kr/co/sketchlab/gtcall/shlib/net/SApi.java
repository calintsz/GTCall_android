package kr.co.sketchlab.gtcall.shlib.net;

import android.content.Context;
import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by shp on 2016. 11. 23..
 */

public class SApi {
    private static String API_BASE = null;

    private Context context;
    private SApiCore api;
    private ArrayList<Pair<String, String>> param = new ArrayList<>();
    private ArrayList<Pair<String, String>> headers = new ArrayList<>();

    private String url;

    public static void setBase(String baseUrl) {
        API_BASE = baseUrl;
    }

    public SApi(Context context) {
        this.context = context;
    }

    public static SApi with(Context context, String url) {
        SApi a = new SApi(context);
        a.api = SApiCore.get(context);
        a.url = url;
        return a;
    }

    public SApi header(String key, String val) {
        headers.add(new Pair<String,String>(key, val));
        return this;
    }

    public SApi param(String key, String val) {
        param.add(new Pair<String,String>(key, val));
        return this;
    }
    public SApi param(String key, double val) {
        param.add(new Pair<String,String>(key, Double.toString(val)));
        return this;
    }
    public SApi param(String key, int val) {
        return param(key, Integer.toString(val));
    }
    public SApi param(String key, long val) {
        return param(key, Long.toString(val));
    }

    /**
     * api 호출
     * @param showLoading
     * @param onRequestComplete
     */
    public void call(boolean showLoading, SApiCore.OnRequestComplete onRequestComplete) {
//        param("trsNo", Calendar.getInstance().getTimeInMillis());
//
//        header("User-Agent", "Android/SBike, iOS/SBike");
//        header("pkgName", context.getApplicationContext().getPackageName());
//        header("userId", Pref.getLoginHash(context));

        String finalUrl = url;
        if(! url.startsWith("http")) {
            finalUrl = API_BASE + url;
        }
        api.commonRequest(showLoading, finalUrl, param, headers, onRequestComplete);
    }

    /**
     * 웹페이지 요청
     * @param showLoading
     * @param onRequestComplete
     */
    public void loadWeb(boolean showLoading, SApiCore.OnRequestComplete onRequestComplete) {
        String finalUrl = url;
        if(! url.startsWith("http")) {
            finalUrl = API_BASE + url;
        }
        api.webRequest(showLoading, finalUrl, param, headers, onRequestComplete);
    }

    /**
     * 이미지 업로드용 api 호출
     * @param showLoading
     * @param filePartName
     * @param file
     * @param onRequestComplete
     */
    public void uploadPhoto(boolean showLoading, String filePartName, File file, SApiCore.OnRequestComplete onRequestComplete) {
        String finalUrl = url;
        if(! url.startsWith("http")) {
            finalUrl = API_BASE + url;
        }

//        param("trsNo", Calendar.getInstance().getTimeInMillis());

//        header("User-Agent", "Android/SBike, iOS/SBike");
//        header("pkgName", context.getApplicationContext().getPackageName());
//        header("userId", Pref.getLoginHash(context));

        api.uploadProfileImage(showLoading, finalUrl, param, headers, filePartName, file, onRequestComplete);
    }



    /**
     * 요청이 200떨어졌는지확인
     * @param obj
     * @return
     */
    public static boolean isSucceeded(JSONObject obj) {
        if(obj == null) {
            return false;
        }

        try {
            JSONObject result = obj.getJSONObject("result");
            if(result == null) {
                return false;
            }
            String code = result.getString("code");
            if(code != null && code.equals("200")) {
                return true;
            }
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 요청결과 메세지 출력
     * @param obj
     * @return
     */
    public static String getResultMessage(JSONObject obj) {
        if(obj == null) {
            return "";
        }

        try {
            JSONObject result = obj.getJSONObject("result");
            if(result == null) {
                return "";
            }
            return result.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static JSONObject getResultData(JSONObject obj) {
        if(obj == null) {
            return null;
        }

        try {
            JSONObject result = obj.getJSONObject("result");
            if(result == null) {
                return null;
            }
            return result.getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
