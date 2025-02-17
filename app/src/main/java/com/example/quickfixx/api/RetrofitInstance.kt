package com.example.quickfixx.api

import com.example.quickfixx.util.constants.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitInstance {

    @Provides
    @Singleton
    fun electricianApi(): ElectricianApi {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ElectricianApi::class.java)
        return retrofitBuilder
    }

    @Provides
    @Singleton
    fun TutorsApi(): TutorsApi {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TutorsApi::class.java)
        return retrofitBuilder
    }

    @Provides
    @Singleton
    fun userApi(): UserAPI{
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(Constants.USER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserAPI::class.java)
        return retrofitBuilder

    }


}