package kr.co.sketchlab.gtcall;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import kr.co.sketchlab.gtcall.model.Api;
import kr.co.sketchlab.gtcall.shlib.net.SApi;

public class TheApp extends Application {
    static TheApp instance = null;

    public static TheApp instance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        Fabric.with(this, new Crashlytics());

        SApi.setBase(Api.API_BASE);
    }
}
