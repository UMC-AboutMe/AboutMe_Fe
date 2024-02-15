plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.aboutme"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.aboutme"
        minSdk = 29
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    dataBinding{
        enable = true
    }
    viewBinding{
        enable = true
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    //프래그먼트
    implementation("androidx.fragment:fragment-ktx:1.3.6")

    //뷰페이저2
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    //리사이클러뷰
    implementation("androidx.recyclerview:recyclerview:1.1.0")

    //glide
    implementation ("com.github.bumptech.glide:glide:4.13.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.0")

    //둥근 이미지뷰
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //바텀시트
    implementation ("com.google.android.material:material:1.5.0-alpha02")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2")

    implementation ("com.kakao.sdk:v2-all:2.19.0") // 전체 모듈 설치, 2.11.0 버전부터 지원
    implementation ("com.kakao.sdk:v2-user:2.19.0") // 카카오 로그인
    implementation ("com.kakao.sdk:v2-talk:2.19.0") // 친구, 메시지(카카오톡)
    implementation ("com.kakao.sdk:v2-share:2.19.0") // 메시지(카카오톡 공유)
    implementation ("com.kakao.sdk:v2-friend:2.19.0") // 카카오톡 소셜 피커, 리소스 번들 파일 포함
    implementation ("com.kakao.sdk:v2-navi:2.19.0") // 카카오내비
    implementation ("com.kakao.sdk:v2-cert:2.19.0") // 카카오 인증서비스

    // Normal
    implementation ("io.github.ParkSangGwon:tedpermission-normal:3.3.0")

    //구글 로그인
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    //구글 로그인 - 파이어베이스
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation("com.google.firebase:firebase-analytics")

    //네비게이션
    implementation ("androidx.navigation:navigation-fragment-ktx:2.4.2")
    implementation ("androidx.navigation:navigation-ui-ktx:2.4.2")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // google gson
    implementation ("com.google.code.gson:gson:2.8.8")

    // http3
    implementation ("com.squareup.okhttp3:logging-interceptor:4.8.1")

    //탭레이아웃
    implementation("com.google.android.material:material:1.5.0-alpha02")

    // 리사이클러뷰 아이템 shimmer effect
    implementation ("com.facebook.shimmer:shimmer:0.5.0")

    // swipe refresh
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation ("com.squareup.okhttp3:okhttp:4.9.3")

    // lottie 애니메이션
    implementation ("com.airbnb.android:lottie:6.3.0")

    // 날씨 애니메이션
    implementation ("com.github.MatteoBattilana:WeatherView:3.0.0")
}