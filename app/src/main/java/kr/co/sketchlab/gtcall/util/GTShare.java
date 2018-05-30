package kr.co.sketchlab.gtcall.util;

import android.content.Context;

import kr.co.sketchlab.gtcall.shlib.data.ShareUtil;

public class GTShare {
    /**
     * 공유하기
     */
    public static void share(Context context, String shareMsg) {
        ShareUtil.shareText(context, "GT대리운전 추천하기", "GT대리운전", shareMsg);
    }
}
