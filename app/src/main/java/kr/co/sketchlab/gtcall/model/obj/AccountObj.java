package kr.co.sketchlab.gtcall.model.obj;

import org.json.JSONException;
import org.json.JSONObject;

import kr.co.sketchlab.gtcall.shlib.data.JsonObj;

public class AccountObj extends JsonObj {
    public enum F {
        type,
        name,
        phone,
        bank_code,
        bank_account,
        bank_account_owner,
        bank_account_check_remain, // 계좌등록 알림팝업 띄울지 여부, 0 보다 크거나 같을때 띄움
        bank_account_reg_msg, // 계좌등록 안내 멘트
        login_key,
        follower_cnt,
        bottom_notice,
        prev_login, // 이전 로그인시간
        last_notice_time, // 마지막 공지 시간
        contact_cashback, // 캐시백 문의 전화버호
        contact_general, // 일반문의 전화번호
        share_msg, // 공유에 사용할 메세지
        ;
    }

    public AccountObj(JSONObject obj) {
        super(obj);
    }

    public AccountObj(String strObj) throws JSONException {
        super(strObj);
    }

    public String get(F name) {
        return super.get(name.name());
    }

    public int getInt(F name) {
        return super.getInt(name.name());
    }
}
