package com.rezau_mehedi.prayerreminder.di

import android.content.Context
import com.rezau_mehedi.prayerreminder.data.UserPref
import com.rezau_mehedi.prayerreminder.data.UserPrefImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PreferenceModule {

    @Singleton
    @Provides
    fun providesUserPreferences(
        @ApplicationContext context: Context
    ): UserPref = UserPrefImpl(context)
}