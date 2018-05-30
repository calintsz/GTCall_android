package kr.co.sketchlab.gtcall.model.obj;

import org.json.JSONException;
import org.json.JSONObject;

import kr.co.sketchlab.gtcall.shlib.data.JsonObj;

/*
{
  "state": 0,
  "msg": null,
  "data": {
    "address": "울산광역시 북구 가대동 849",
    "addrdetail": {
      "country": "대한민국",
      "sido": "울산광역시",
      "sigugun": "북구",
      "dongmyun": "가대동",
      "ri": "",
      "rest": "849"
    },
    "isAdmAddress": false,
    "isRoadAddress": false,
    "point": {
      "x": 129.2994781,
      "y": 35.5950731
    },
    "service_area": {
      "idx": "2",
      "area_name": "울산",
      "call_number": "1577-1234",
      "area_code": "052",
      "sido_match": "\/^(울산광역시)$\/",
      "reward_percent": null,
      "reward_amount": "1000",
      "use_yn": "Y",
      "reg_date": "2018-05-21 23:29:29"
    }
  }
}
 */
public class AddressObj extends JsonObj {
    JsonObj addrDetail = null;

    public AddressObj(JSONObject obj) {
        super(obj);

        init();
    }

    public AddressObj(String strObj) throws JSONException {
        super(strObj);

        init();
    }

    private void init() {
        addrDetail = getObj("addrdetail");
    }

    /**
     * 전체주소
     * @return
     */
    public String getAddr() {
        String sido = getSido();
        String addr = get("address");
        if(sido != null && addr != null) {
            addr = addr.replace(sido, "");
        }
        return addr.trim();
    }

    /**
     * 국가
     * @return
     */
    public String getCountry() {
        return addrDetail.get("country");
    }

    /**
     * 시도
     * @return
     */
    public String getSido() {
        return addrDetail.get("sido");
    }

    /**
     * 시구군
     * @return
     */
    public String getSigugun() {
        return addrDetail.get("sigugun");
    }

    /**
     * 콜센터 전화번호
     * @return
     */
    public String getCallNumber() {
        JsonObj serviceArea = getObj("service_area");
        if(serviceArea == null) {
            return null;
        }

        return serviceArea.get("call_number");
    }
}
