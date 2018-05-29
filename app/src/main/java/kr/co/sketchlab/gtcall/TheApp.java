package kr.co.sketchlab.gtcall;

import android.app.Application;

import kr.co.sketchlab.gtcall.model.Api;
import kr.co.sketchlab.gtcall.shlib.net.SApi;

public class TheApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SApi.setBase(Api.API_BASE);
    }
}
