package kr.co.sketchlab.gtcall.ui.activity;

import android.os.Bundle;

import kr.co.sketchlab.gtcall.GTCallActivity;
import kr.co.sketchlab.gtcall.R;
import kr.co.sketchlab.gtcall.model.Api;

public class SplashActivity extends GTCallActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Api.tryLogin(mActivity);
    }
}
