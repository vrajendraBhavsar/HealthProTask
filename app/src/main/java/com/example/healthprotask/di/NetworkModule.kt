package com.example.retrofit_hilt.di

import android.content.Context
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.example.healthprotask.auth.nework.AuthApiService
import com.example.healthproclienttask.utility.NetworkUtility
import com.example.healthprotask.R
import com.example.healthprotask.utility.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.annotation.Nullable

// Singleton ..here we will provide all 3rd party library Obj..
@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun providesAuthorizationUrl(): String {
        return NetworkUtility.BASE_URL
    }

    @Provides
    fun providesGsonConverterFactory(): Converter.Factory { // better to use Parent than child "GsonConverterFactory"
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor { // Interceptor
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

//    @Provides
//    fun provideVrajendraIntercaptor(context: Context) : ResponseIntercaptor{
//        val intecaptor =  ResponseIntercaptor(context)
//        return intecaptor
//    }
//
//    class ResponseIntercaptor(okHttpClient: OkHttpClient, sessionManager: SessionManager) { // Interceptor Response
//        return okHttpClient.newBuilder().addInterceptor(object : Interceptor {
//            override fun intercept(chain: Interceptor.Chain): Response {
//                val newRequest: Request = chain.request().newBuilder()
//                    .addHeader("Authorization", "Bearer ${sessionManager.getBearerToken()}")
//                    .build()
//
//                val response = chain.proceed(newRequest)
//                Log.d("MyApp", "Code : " + response.code)
//                return response
//            }
//        })
//    }

    @Provides
    fun providesOkhttpClient(logger: HttpLoggingInterceptor): OkHttpClient { // Client
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor(logger) // to get log of each request & response
//        okHttpClient.addInterceptor(vra) // to get log of each request & response
        okHttpClient.callTimeout(60, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(60, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(60, TimeUnit.SECONDS)
        okHttpClient.readTimeout(60, TimeUnit.SECONDS)
        return okHttpClient.build()
    }

    @Provides
    fun provideRetrofit( // Retrofit
        baseUrl: String,
        converterFactory: Converter.Factory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides // for api call
    fun providesAuthApiService(retrofit: Retrofit): AuthApiService { // Interface available karavdavie chhie..
        return retrofit.create(AuthApiService::class.java)
    }
}
