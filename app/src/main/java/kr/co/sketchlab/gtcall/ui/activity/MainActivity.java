package kr.co.sketchlab.gtcall.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import kr.co.sketchlab.gtcall.GTCallActivity;
import kr.co.sketchlab.gtcall.R;
import kr.co.sketchlab.gtcall.model.Api;
import kr.co.sketchlab.gtcall.model.Conf;
import kr.co.sketchlab.gtcall.model.Pref;
import kr.co.sketchlab.gtcall.model.obj.AccountObj;
import kr.co.sketchlab.gtcall.shlib.data.StringUtil;
import kr.co.sketchlab.gtcall.shlib.location.MyLocationManager;

public class MainActivity extends GTCallActivity {
    private final static int PERMISSIONS_REQUEST_CODE = 100;
    DrawerLayout drawer;

    AccountObj accountObj = null;

    boolean finishReady = false;

    double curLat = 0;
    double curLng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) v(R.id.drawer).v();
        v(R.id.btnDrawer).click(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawer.isDrawerOpen(GravityCompat.END)) {
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
                
            }
        });
        // 친구초청
        v(R.id.btnDrawerInviteFriend).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        // 일반전화번호등록
        v(R.id.btnDrawerSubPhone).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        // 이용약관
        v(R.id.btnDrawerTerms).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        // 개인정보 처리방침
        v(R.id.btnDrawerPrivacyPolicy).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // 캐시백 문의
        v(R.id.btnDrawerCashBackQuestion).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + Conf.CONTACT_NUMBER));
                startActivity(intent);
            }
        });
        // 일반문의
        v(R.id.btnDrawerGeneralQuestion).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + Conf.CONTACT_NUMBER));
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

            }
        });

        /**
         * 지도보기
         */
        v(R.id.btnMap).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /**
         * 친구만들기
         */
        v(R.id.btnAddFriend).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        ///////////////////////////////////////////////////////////////

        // 현재위치 찾기

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);
            return;
        } else {

        }
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

                    // 주소로 변환
                }
            }
        });
    }
}
