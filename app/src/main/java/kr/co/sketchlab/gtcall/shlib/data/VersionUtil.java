package kr.co.sketchlab.gtcall.shlib.data;

import android.content.Context;

import kr.co.sketchlab.gtcall.shlib.device.SystemUtil;

public class VersionUtil {
    /**
     * 버전 비교
     * @param versionFromServer
     * @return
     */
    public static boolean isNewerVersion(Context context, String versionFromServer) {
        String curVersion = SystemUtil.getAppVersionName(context);

        String[] cv = curVersion.split("\\.");
        String[] sv = versionFromServer.split("\\.");

        int cnt = cv.length;
        if(sv.length < cnt) {
            cnt = sv.length;
        }

        try {
            for(int i = 0; i < cnt; i++) {
                if(Integer.parseInt(sv[i]) > Integer.parseInt(cv[i])) {
                    return true; // 서버 버전이 더 최신
                } else if(Integer.parseInt(sv[i]) < Integer.parseInt(cv[i])) {
                    return false; // 로컬 버전이 더 최신
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            // 버전 스트링에 문제가 있으면, 현재버전을 최신버전으로 인식되게함
            return false;
        }

        // 있는 자릿수까지 숫자가 동일한 경우

        if(sv.length > cv.length) {
            return true; // 서버 버전이 자릿수가 많으면 서버버전이 더 최신
        } else if(sv.length < cv.length) {
            return false; // 로컬 버전의 자릿수가 많으면 로컬이 더 최신
        } else { // 완전히 동일하면
            return false;
        }
    }
}
