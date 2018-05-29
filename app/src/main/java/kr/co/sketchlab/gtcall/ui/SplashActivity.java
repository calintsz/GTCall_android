package kr.co.sketchlab.gtcall.ui;

import android.os.Bundle;

import kr.co.sketchlab.gtcall.R;
import kr.co.sketchlab.gtcall.shlib.ui.activity.S3Activity;

public class SplashActivity extends S3Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
}
