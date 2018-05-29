package kr.co.sketchlab.gtcall.ui.activity;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import kr.co.sketchlab.gtcall.GTCallActivity;
import kr.co.sketchlab.gtcall.R;
import kr.co.sketchlab.gtcall.model.Pref;
import kr.co.sketchlab.gtcall.model.obj.AccountObj;
import kr.co.sketchlab.gtcall.shlib.data.StringUtil;

public class MainActivity extends GTCallActivity {
    DrawerLayout drawer;

    AccountObj accountObj = null;


    boolean finishReady = false;

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

        accountObj = Pref.getAccount();
        if(accountObj == null) {
            showToast("오류");
            finish();
        }

        // 친구 숫자
        v(R.id.txtFollowerCount).setText(accountObj.get(AccountObj.F.follower_cnt));

        // drawer 내용 채우기
        v(R.id.txtName).setText(accountObj.get(AccountObj.F.name));
        v(R.id.txtPhone).setText(StringUtil.formatPhoneNumber(accountObj.get(AccountObj.F.phone)));

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

            }
        });
        // 공지사항
        v(R.id.btnDrawerNotice).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
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

            }
        });
        // 일반문의
        v(R.id.btnDrawerGeneralQuestion).click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
