package com.alexyach.kotlin.weatherapi.di

import android.content.Context
import androidx.room.Room
import com.alexyach.kotlin.weatherapi.room.IWeatherDAO
import com.alexyach.kotlin.weatherapi.room.WeatherDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DBRoomModule {

    @Provides
    fun provideDBRoom(db: WeatherDataBase): IWeatherDAO {
        return db.weatherDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context) : WeatherDataBase {
        return Room.databaseBuilder(
            appContext,
            WeatherDataBase::class.java,
            "RoomDataBase"
        ).build()
    }

}