package kr.co.sketchlab.gtcall.shlib.data;

import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class StringUtil {
    public static String toCommaNumber(int number) {
        return (new DecimalFormat("#,###,###,###")).format(number);
    }

    public static Spannable makeSpannableString(String str, String highlight, int color, int limit) {
        ArrayList<String> ar = new ArrayList<>();
        if(highlight != null && ! highlight.equals(""))
            ar.add(highlight);
        return makeSpannableString(str, ar, color, limit);
    }

    public static Spannable makeSpannableString(String str, ArrayList<String> highlight, int color, int limit) {
        Spannable spannable = new SpannableString(str);
        if(highlight != null && highlight.size() > 0) {
            for (String st : highlight) {
                if(st.equals(""))
                    continue;
                int s = str.indexOf(st, 0);
                while (s >= 0) {
                    if(limit > 0 && s >= limit)
                        break;
                    spannable.setSpan(new ForegroundColorSpan(color), s, s + st.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    s = str.indexOf(st, s + st.length());
                }
            }
        }
        return spannable;
    }

    /**
     * 전화번호 형식 포멧
     * @param phoneNumber
     * @return
     */
    public static String formatPhoneNumber(String phoneNumber) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return PhoneNumberUtils.formatNumber(phoneNumber, "KR");
        } else {
            return PhoneNumberUtils.formatNumber(phoneNumber);
        }
    }

    /**
     * 숫자가 아닌 문자 모두 제거
     * @param str
     * @return
     */
    public static String removeNonNumber(String str) {
        if(str == null) {
            return null;
        }
        return str.replaceAll("[^\\d.]", "");
    }

    /**
     * 전화번호에서 국가코드 삭제하고 번호만 남김
     * @param phoneNumber
     * @return
     */
    public static String toLocalPhoneNumber(String phoneNumber) {
        if(phoneNumber.startsWith("+82")) {
            phoneNumber = phoneNumber.substring(3);
            phoneNumber = "0" + phoneNumber;
            return phoneNumber;
        }
        return phoneNumber;
    }

    public static boolean isEmailAddress(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isNumber(String n) {
        String regexStr = "^[0-9]*$";
        return n.matches(regexStr);
    }

    public static String isEmpty(String str, String defaultStr) {
        return TextUtils.isEmpty(str) ? defaultStr : str;
    }

    /**
     * url 에서 parameter 분리
     * @param url url
     * @return parameters
     */
    public static ShHashMap paramFromUrl(String url) {
        ShHashMap ret = new ShHashMap();

        String[] fields = url.split("\\?");
        if(fields.length < 2) {
            return ret;
        }

        String strParam = fields[1];
        String[] segs = strParam.split("&");
        for(String seg : segs) {
            String[] kv = seg.split("=", 2);
            if(kv.length == 2) {
                try {
                    String key = URLDecoder.decode(kv[0], "utf-8");
                    String val = URLDecoder.decode(kv[1], "utf-8");
                    ret.put(key, val);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }
}
