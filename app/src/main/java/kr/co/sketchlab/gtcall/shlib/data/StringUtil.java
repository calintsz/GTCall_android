package kr.co.sketchlab.gtcall.shlib.data;

import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;

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

    public static String formatPhoneNumber(String phoneNumber, String countryCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return PhoneNumberUtils.formatNumber(phoneNumber, countryCode);
        } else {
            return PhoneNumberUtils.formatNumber(phoneNumber);
        }
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
}
