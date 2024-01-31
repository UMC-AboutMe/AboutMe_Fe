import com.example.aboutme.MainProfile
import com.google.firebase.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://aboutme-prod-env.eba-3cw2pgyk.ap-northeast-2.elasticbeanstalk.com/"

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }.build())
        .build()

    val mainProfile: MainProfile by lazy {
        retrofit.create(MainProfile::class.java)
    }
}
