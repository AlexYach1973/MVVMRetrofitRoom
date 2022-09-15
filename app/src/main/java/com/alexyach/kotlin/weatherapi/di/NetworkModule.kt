package com.alexyach.kotlin.weatherapi.di

import com.alexyach.kotlin.weatherapi.utils.OPENWEATHERMAP_BASE_URL
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(OPENWEATHERMAP_BASE_URL)
            addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().setLenient().create())
            )
            addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        }.build()
    }

}