package kr.co.sketchlab.gtcall.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.sketchlab.gtcall.GTCallActivity;
import kr.co.sketchlab.gtcall.R;
import kr.co.sketchlab.gtcall.model.Api;
import kr.co.sketchlab.gtcall.model.Pref;
import kr.co.sketchlab.gtcall.model.obj.AccountObj;
import kr.co.sketchlab.gtcall.model.obj.AddressObj;
import kr.co.sketchlab.gtcall.shlib.data.JsonUtil;
import kr.co.sketchlab.gtcall.shlib.data.ShDateTime;
import kr.co.sketchlab.gtcall.shlib.data.StringUtil;
import kr.co.sketchlab.gtcall.shlib.location.MyLocationManager;
import kr.co.sketchlab.gtcall.shlib.net.SApi;
import kr.co.sketchlab.gtcall.shlib.net.SApiCore;
import kr.co.sketchlab.gtcall.shlib.ui.comp.SAlertDialog;
import kr.co.sketchlab.gtcall.util.GTShare;

public class MainActivity extends GTCallActivity {
    private final static int PERMISSIONS_REQUEST_CODE = 100;
    DrawerLayout drawer;

    AccountObj accountObj = null;

    boolean finishReady = false;

    double curLat = 0;
    double curLng = 0;

    AddressObj addressObj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) v(R.id.drawer).v();
        v(R.id.btnDrawer).click(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });

        updateViewWithAccountObj();

        ///////////////////////////////////////////////////////////
        /// drawer 관련
        ///////////////////////////////////////////////////////////
        // 메뉴 닫기
        v(R.id.btnCloseDrawer).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.END);
            }
        });
        // 설정
        v(R.id.btnSetting).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.start(mActivity, Api.PAGE_SETTING + Pref.getAccount().get(AccountObj.F.login_key), "내 정보", false);
            }
        });
        // 공지사항
        v(R.id.btnDrawerNotice).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.start(mActivity, Api.PAGE_NOTICE, "공지사항", false);
            }
        });
        // 나의 캐시백
        v(R.id.btnDrawerMyCashBack).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.start(mActivity, Api.PAGE_CASHBACK + Pref.getAccount().get(AccountObj.F.login_key), "나의 캐시백", false);
            }
        });
        // 친구초청
        v(R.id.btnDrawerInviteFriend).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 공유하기
                GTShare.share(mActivity, Pref.getAccount().get(AccountObj.F.share_msg));
            }
        });
        // 일반전화번호등록
        v(R.id.btnDrawerSubPhone).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.start(mActivity, Api.PAGE_SUBPHONE + Pref.getAccount().get(AccountObj.F.login_key), "일반 전화번호 등록", false);
            }
        });
        // 이용약관
        v(R.id.btnDrawerTerms).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.start(mActivity, Api.PAGE_TERMS, "이용약관", false);
            }
        });
        // 개인정보 처리방침
        v(R.id.btnDrawerPrivacyPolicy).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.start(mActivity, Api.PAGE_PRIVACY, "개인정보처리방침", false);
            }
        });
        // 위치정보 처리방침
        v(R.id.btnDrawerLocationPolicy).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.start(mActivity, Api.PAGE_LOCATION, "위치정보처리방침", false);
            }
        });
        // 캐시백 문의
        v(R.id.btnDrawerCashBackQuestion).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + accountObj.get(AccountObj.F.contact_cashback)));
                startActivity(intent);
            }
        });
        // 일반문의
        v(R.id.btnDrawerGeneralQuestion).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + accountObj.get(AccountObj.F.contact_general)));
                startActivity(intent);
            }
        });


        ///////////////////////////////////////////////////////////
        /// 메인화면 관련
        ///////////////////////////////////////////////////////////
        /**
         * 전화걸기
         */
        v(R.id.btnCall).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressObj != null) {
                    String address = addressObj.getAddr();
                    String callNumber = addressObj.getCallNumber();
                    if(callNumber == null) {
                        try {
                            selectAreaAndCall();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return;
                    }

                    // 전화걸기 기록 추가
                    Api.addCallHistory(mActivity, address, addressObj.getArea(), callNumber);

                    if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + callNumber));
                        startActivity(intent);
                        return;
                    }
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + callNumber));
                    startActivity(intent);
                } else {
                    try {
                        selectAreaAndCall();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        /**
         * 지도보기
         */
        v(R.id.btnMap).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addressObj == null) {
                    showToast("현재 위치를 알 수 없습니다.");
                    return;
                }
                Intent intent = new Intent(mActivity, MapActivity.class);
                intent.putExtra("lat", curLat);
                intent.putExtra("lng", curLng);
                intent.putExtra("addr", addressObj.getAddr());
                startActivity(intent);
            }
        });

        /**
         * 친구만들기
         */
        v(R.id.btnAddFriend).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 공유하기
                GTShare.share(mActivity, Pref.getAccount().get(AccountObj.F.share_msg));
            }
        });



        ///////////////////////////////////////////////////////////////

        // 현재위치 찾기

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);
            return;
        } else {

        }


        // 새로운 공지 확인
        AccountObj accountObj = Pref.getAccount();
        String prevLogin = accountObj.get(AccountObj.F.prev_login);
        String lastNotice = accountObj.get(AccountObj.F.last_notice_time);
        if(ShDateTime.parseDateTimeString(prevLogin).getTimeInMillis() < ShDateTime.parseDateTimeString(lastNotice).getTimeInMillis()) { // 공지팝업
            // 공지 alert 띄우기
            SAlertDialog.show(mActivity, "새로운 공지가 있습니다.\n지금 확인하시겠습니까?", "아니요", "예", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 취소
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 확인
                    WebActivity.start(mActivity, Api.PAGE_NOTICE, "공지사항", false);
                }
            });
        } else {
            if(accountObj.getInt(AccountObj.F.bank_account_check_remain) >= 0 && accountObj.get(AccountObj.F.bank_account) == null) {
                SAlertDialog.show(mActivity, accountObj.get(AccountObj.F.bank_account_reg_msg), "다음에 하기", "계좌 등록", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 취소
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 확인
                        WebActivity.start(mActivity, Api.PAGE_SETTING + Pref.getAccount().get(AccountObj.F.login_key), "내 정보", false);
                    }
                });
            }
        }
    }

    private void selectAreaAndCall() throws JSONException {
        SApi.with(mActivity, Api.API_SERVICE_AREA)
                .call(true, new SApiCore.OnRequestComplete() {
                    @Override
                    public void onSucceeded(String str, JSONObject obj) throws Exception {
                        if(obj.getInt("state") == 0) { // 조회 성공
                            JSONArray objs = obj.getJSONArray("data");
                            final ArrayList<JSONObject> list = JsonUtil.toArrayList(objs);
                            // 목록에서 선택하게함
                            ArrayList<String> areaNames = new ArrayList<>();
                            for(JSONObject item : list) {
                                String areaName = item.getString("area_name");
                                areaNames.add(areaName);
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                            builder.setTitle("지역을 선택해주세요.");
                            builder.setItems(areaNames.toArray(new String[areaNames.size()]), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    JSONObject selItem = list.get(which);
                                    try {
                                        String callNumber = selItem.getString("call_number");
                                        String areaName = selItem.getString("area_name");

                                        // 전화걸기 기록 추가
                                        Api.addCallHistory(mActivity, "", areaName, callNumber);


                                        // 선택된 콜센터로 전화걸기
                                        Intent intent = new Intent(Intent.ACTION_CALL);
                                        intent.setData(Uri.parse("tel:" + callNumber));
                                        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            return;
                                        }
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                            builder.show();
                        }
                    }

                    @Override
                    public void onFailed(String message) {

                    }

                    @Override
                    public void onError(String message) {

                    }
                });
    }

    /**
     * accountObj 데이터 가져와서 화면 데이터 업데이트
     */
    private void updateViewWithAccountObj() {
        accountObj = Pref.getAccount();
        if(accountObj == null) {
            showToast("오류");
            finish();
        }

        // 친구 숫자
        v(R.id.txtFollowerCount).setText(accountObj.get(AccountObj.F.follower_cnt));
        // 하단 멘트
        v(R.id.txtMonthlyReward).setText(accountObj.get(AccountObj.F.bottom_notice));

        // drawer 내용 채우기
        v(R.id.txtName).setText(accountObj.get(AccountObj.F.name));
        v(R.id.txtPhone).setText(StringUtil.formatPhoneNumber(accountObj.get(AccountObj.F.phone)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { // 권한 없음
                // 권한이 없음
            } else { // 권한 가짐
                requestLocation();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if(finishReady) {
            super.onBackPressed();
        } else {
            showToast("'뒤로' 버튼을 한번 더 눌러 종료합니다.");
            finishReady = true;
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishReady = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 정보 업데이트되었을수 있으니, 정보 갱신
        Api.updateMemberInfo(mActivity, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateViewWithAccountObj();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { // 권한 없음
            // 권한이 없음
        } else { // 권한 가짐
            requestLocation();
        }
    }



    /**
     * 현재위치 조회
     */
    private void requestLocation() {
        MyLocationManager.getSingleLocation(mActivity, 5000, new MyLocationManager.OnSingleLocationReceivedListener() {
            @Override
            public void onSingleLocationReceived(double lat, double lng) {
                if(lat == 0 || lng == 0) { // 위치 가져올 수 없음
                    if(isActivityResumed()) {
                        requestLocation(); // 다시 시도
                    }
                } else { // 위치 가져옴
                    curLat = lat;
                    curLng = lng;
                    // 37.525879, 128.030375 회성
//                    curLat = 37.525879;
//                    curLng = 128.030375;

//                    curLat = 35.548088;
//                    curLng = 129.2810711;

                    // 주소로 변환
                    SApi.with(mActivity, Api.API_GPS2ADDR)
                            .param("login_key", Pref.getAccount().get(AccountObj.F.login_key))
                            .param("lat", curLat)
                            .param("lng", curLng)
                            .call(false, new SApiCore.OnRequestComplete() {
                                @Override
                                public void onSucceeded(String str, JSONObject obj) throws Exception {
                                    if(obj.getInt("state") == 0) { // 요청 성공
                                        JSONObject data = obj.getJSONObject("data");
                                        if(data == null) {
                                            return;
                                        }

                                        addressObj = new AddressObj(data);

                                        // 주소 표시
                                        v(R.id.txtAddress).setText(addressObj.getAddr());
                                    }
                                }

                                @Override
                                public void onFailed(String message) {

                                }

                                @Override
                                public void onError(String message) {

                                }
                            });
                }
            }
        });
    }
}
