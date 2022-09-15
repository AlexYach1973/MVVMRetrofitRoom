package com.alexyach.kotlin.weatherapi.di

import com.alexyach.kotlin.weatherapi.repository.IRepositoryByCityName
import com.alexyach.kotlin.weatherapi.repository.IRepositoryByLocation
import com.alexyach.kotlin.weatherapi.repository.local.RepositoryRoomImpl
import com.alexyach.kotlin.weatherapi.repository.remote.retrofit.RepositoryRetrofitImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SourcesModule {

    // Дві різні імплементації одного інтерфейсу
    @Qualifier
    annotation class RetrofitImpl

    @Qualifier
    annotation class RoomImpl

    @Binds
    @RetrofitImpl // перша
    @Singleton // або вказати в RepositoryRetrofitImpl
    abstract fun bindIRepositoryByCityNameRetrofit(
        impl: RepositoryRetrofitImpl
    ): IRepositoryByCityName


    @Binds
    @RoomImpl // друга
    @Singleton // або вказати в RepositoryRoomImpl
    abstract fun bindIRepositoryByCityNameRoom(
        impl: RepositoryRoomImpl
    ): IRepositoryByCityName



    @Binds
    @Singleton
    abstract fun bindRepositoryByLocation(
        impl: RepositoryRetrofitImpl
    ): IRepositoryByLocation


}