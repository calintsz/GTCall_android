package kr.co.sketchlab.gtcall.shlib.net;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.m_hogaeng.shlib.Progress;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shp on 2016. 10. 11..
 */

public class SApiCore {
    public static String COOKIE;

    private String TAG = this.getClass().getSimpleName();
    private static SApiCore thisObj = null;
    private RequestQueue requestQueue = null;

    Context mContext;

    private final int TIMEOUT_MS = 60 * 1000;

    public static SApiCore get(Context context) {
        if(thisObj == null) {
            thisObj = new SApiCore(context);
        }
        thisObj.mContext = context;
        return thisObj;
    }
    private SApiCore(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        mContext = context;
    }

    public void cancelRequest() {
        if(requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }


    /**
     * 사진업로드용
     * @param url
     * @param params
     * @param file
     * @param onRequestComplete
     */
    public void uploadProfileImage(final boolean showLoading, final String url, final ArrayList<Pair<String,String>> params, final ArrayList<Pair<String,String>> headers, String filePartName, File file, final OnRequestComplete onRequestComplete) {
        if(showLoading) {
            Progress.showProgress(mContext, false);
        }
        PhotoMultipartRequest imageUploadReq = new PhotoMultipartRequest(url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(showLoading) {
                    Progress.hideProgress();
                }
                String paramStr = "";
                for(Pair<String,String> entry:params) {
                    paramStr += entry.first + "=" + entry.second + "&";
                }
                Log.e(TAG, error.toString() + " url: " + url + "?" + paramStr);
//                Toast.makeText(mContext, "서버통신 오류: " + error.networkResponse.statusCode, Toast.LENGTH_LONG).show();
                onRequestComplete.onError(error.getMessage());
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(showLoading) {
                    Progress.hideProgress();
                }
                String paramStr = "";
                for(Pair<String,String> entry:params) {
                    paramStr += entry.first + "=" + entry.second + "&";
                }
                Log.i(TAG, url + "?" + paramStr + " => " + response);

                try {
                    JSONObject obj = new JSONObject(response);
                    onRequestComplete.onSucceeded(response, obj);
                } catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(mContext, "JSON형식 오류", Toast.LENGTH_LONG).show();
                    onRequestComplete.onFailed(response);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(mContext, "JSON해석 오류", Toast.LENGTH_LONG).show();
                    onRequestComplete.onFailed(response);
                }
            }
        }, params, headers, filePartName, file);
        imageUploadReq.setRetryPolicy(
                new DefaultRetryPolicy(TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(imageUploadReq);

    }

    /**
     * 일반 api 호출용
     * @param url
     * @param params
     * @param onRequestComplete
     */
    public void commonRequest(final boolean showLoading, final String url, final ArrayList<Pair<String,String>> params, ArrayList<Pair<String,String>> headers, final OnRequestComplete onRequestComplete) {
        if(showLoading) {
            Progress.showProgress(mContext, false);
        }
        ShRequest request = new ShRequest(Request.Method.POST, url, params, headers, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(showLoading) {
                    Progress.hideProgress();
                }
                String paramStr = "";
                for(Pair<String,String> entry:params) {
                    paramStr += entry.first + "=" + entry.second + "&";
                }
                Log.i(TAG, url + "?" + paramStr + " => " + response);

                try {
                    JSONObject obj = new JSONObject(response);
                    onRequestComplete.onSucceeded(response, obj);
                } catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(mContext, "JSON형식 오류", Toast.LENGTH_LONG).show();
                    onRequestComplete.onFailed(response);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(mContext, "JSON해석 오류", Toast.LENGTH_LONG).show();
                    onRequestComplete.onFailed(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(showLoading) {
                    Progress.hideProgress();
                }
                String paramStr = "";
                for(Pair<String,String> entry:params) {
                    paramStr += entry.first + "=" + entry.second + "&";
                }

                String urlWithParam = url + "?" + paramStr;
                Log.e(TAG, error.toString() + " url: " + url + "?" + paramStr);

//                String msg = new String(error.networkResponse.data, Charset.forName("UTF8"));
//                msg = "서버통신 오류: " + error.networkResponse.statusCode + "\n" + urlWithParam + "\n => \n" + msg;
//                try {
//                    ErrorLogActivity.start((Activity) mContext, msg);
//                } catch(Exception e) {
//                    Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
//                }
                onRequestComplete.onError(error.getMessage());
            }
        });
        request.setRetryPolicy(
                new DefaultRetryPolicy(TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(request);
    }


    /**
     * 웹페이지 요청, commonRequest와 같지만 JSON parsing 만 안한다.
     * @param showLoading
     * @param url
     * @param params
     * @param onRequestComplete
     */
    public void webRequest(final boolean showLoading, final String url, final ArrayList<Pair<String,String>> params, ArrayList<Pair<String,String>> headers, final OnRequestComplete onRequestComplete) {
        if(showLoading) {
            Progress.showProgress(mContext, false);
        }
        ShRequest request = new ShRequest(Request.Method.POST, url, params, headers, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(showLoading) {
                    Progress.hideProgress();
                }
                String paramStr = "";
                for(Pair<String,String> entry:params) {
                    paramStr += entry.first + "=" + entry.second + "&";
                }
                Log.i(TAG, url + "?" + paramStr + " => " + response);

                try {
                    onRequestComplete.onSucceeded(response, null);
                } catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(mContext, "JSON형식 오류", Toast.LENGTH_LONG).show();
                    onRequestComplete.onFailed(response);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(mContext, "JSON해석 오류", Toast.LENGTH_LONG).show();
                    onRequestComplete.onFailed(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(showLoading) {
                    Progress.hideProgress();
                }
                String paramStr = "";
                for(Pair<String,String> entry:params) {
                    paramStr += entry.first + "=" + entry.second + "&";
                }
                Log.e(TAG, error.toString() + " url: " + url + "?" + paramStr);
//                Toast.makeText(mContext, "서버통신 오류", Toast.LENGTH_LONG).show();
                onRequestComplete.onError(error.getMessage());
            }
        });
        request.setRetryPolicy(
                new DefaultRetryPolicy(TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(request);
    }

    /**
     * 요청결과 인터페이스
     */
    public interface OnRequestComplete {
        void onSucceeded(String str, JSONObject obj) throws Exception;
        void onFailed(String message);
        void onError(String message);
    }


    /**
     * 커스텀 요청
     */
    private class ShRequest extends Request<String> {
        private final Response.Listener mListener;
        private ArrayList<Pair<String, String>> mParams;
        private ArrayList<Pair<String, String>> mHeaders;

        public ShRequest(int method, String url, ArrayList<Pair<String,String>> params, ArrayList<Pair<String,String>> headers, Response.Listener mListener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            this.mListener = mListener;
            mParams = params;
            mHeaders = headers;
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            String result = null;
            String str = "";
            try {
                str = new String(response.data, "UTF-8");
                result = str;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        protected void deliverResponse(String response) {
            mListener.onResponse(response);
        }

//        @Override
//        public Map<String, String> getParams() {
//            return mParams;
//        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            if (mParams != null && mParams.size() > 0) {
                return encodeParameters(mParams, getParamsEncoding());
            }
            return null;
        }
        /**
         * Converts <code>params</code> into an application/x-www-form-urlencoded encoded string.
         */
        private byte[] encodeParameters(ArrayList<Pair<String, String>> params, String paramsEncoding) {
            StringBuilder encodedParams = new StringBuilder();
            try {
                for(Pair<String,String> entry : params) {
                    if(entry.first == null)
                        continue;

                    String second = entry.second;
                    if(second == null) {
                        second = "";
                    }

                    encodedParams.append(URLEncoder.encode(entry.first, paramsEncoding));
                    encodedParams.append('=');
                    encodedParams.append(URLEncoder.encode(second, paramsEncoding));
                    encodedParams.append('&');
                }
                return encodedParams.toString().getBytes(paramsEncoding);
            } catch (UnsupportedEncodingException uee) {
                throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
            }
        }


        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<>();
            headers.put("Cookie", COOKIE);
            for(Pair<String,String> h : mHeaders) {
                headers.put(h.first, h.second);
            }
            return headers;
        }
    }

    /**
     * 사진업로드용 request
     */
    public class PhotoMultipartRequest extends Request<String> {
        private MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
        private final Response.Listener<String> mListener;
        private final File mImageFile;
        protected Map<String, String> headers;
        private ArrayList<Pair<String, String>> mParams;
        private ArrayList<Pair<String, String>> mHeaders;

        private String filePartName;

        public PhotoMultipartRequest(String url, Response.ErrorListener errorListener, Response.Listener<String> listener, ArrayList<Pair<String,String>> params, ArrayList<Pair<String,String>> headers, String filePartName, File imageFile){
            super(Method.POST, url, errorListener);

            mListener = listener;
            mImageFile = imageFile;
            mParams = params;
            mHeaders = headers;
            this.filePartName = filePartName;

            buildMultipartEntity();
        }

//        @Override
//        public Map<String, String> getParams() {
//            return mParams;
//        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = super.getHeaders();

            if (headers == null || headers.equals(Collections.emptyMap())) {
                headers = new HashMap<String, String>();
            }

            headers.put("Accept", "application/json");

            for(Pair<String,String> h : mHeaders) {
                headers.put(h.first, h.second);
            }

            return headers;
        }

        private void buildMultipartEntity(){
            for(Pair<String,String> entry:mParams) {
                mBuilder.addTextBody(entry.first, entry.second, ContentType.create("text/plain", "utf-8"));
            }

            mBuilder.addBinaryBody(filePartName, mImageFile, ContentType.create("image/jpeg"), mImageFile.getName());
            mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            mBuilder.setLaxMode().setBoundary("-------ZZKE39dje0f3Jie0Lie-BOUNDARY-------").setCharset(Charset.forName("UTF-8"));
        }

        @Override
        public String getBodyContentType(){
            String contentTypeHeader = mBuilder.build().getContentType().getValue();
            return contentTypeHeader;
        }

        @Override
        public byte[] getBody() throws AuthFailureError{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                mBuilder.build().writeTo(bos);
            } catch (IOException e) {
                VolleyLog.e("IOException writing to ByteArrayOutputStream bos, building the multipart request.");
            }

            return bos.toByteArray();
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            String result = null;
            String str = "";
            try {
                str = new String(response.data, "UTF-8");
                result = str;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        protected void deliverResponse(String response) {
            mListener.onResponse(response);
        }
    }
}
