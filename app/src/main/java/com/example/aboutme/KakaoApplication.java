package com.example.aboutme;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class KakaoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        KakaoSdk.init(this, "14bbf6997b475309590efc7c9a7d9b11");
    }
}
