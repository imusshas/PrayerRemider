package com.rezau_mehedi.prayerreminder.di

import com.rezau_mehedi.prayerreminder.api.BdAppsApi
import com.rezau_mehedi.prayerreminder.data.BdAppsApiRepository
import com.rezau_mehedi.prayerreminder.data.BdAppsApiRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {

    @Singleton
    @Provides
    fun providesRetrofitApi(): BdAppsApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
        .create(BdAppsApi::class.java)

    @Singleton
    @Provides
    fun providesBdPassApiRepository(api: BdAppsApi): BdAppsApiRepository = BdAppsApiRepositoryImpl(api)

    private const val BASE_URL = "http://20.197.50.116:3000/"
}