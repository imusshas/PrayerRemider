package com.rezau_mehedi.prayerreminder.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

object Constants {

    val DIVISIONS = listOf(
        "Dhaka", "Chattogram", "Khulna", "Rajshahi", "Sylhet", "Barishal", "Rangpur", "Mymensingh", ""
    )

    val LATITUDE = listOf(
        23.8103, 22.3569, 22.8456, 24.3745, 24.9045, 22.7010, 25.7439, 24.7471, 23.8103
    )

    val LONGITUDE = listOf(
        90.4125, 91.7832, 89.5403, 88.6042, 91.8611, 90.3535, 89.2752, 90.4203, 90.4125
    )

    private const val DATASTORE_NAME = "USER"

    val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

    const val BASE_URL = "http://20.197.50.116:3000/"  // TODO(): Change the port no
    const val REFERENCE_NO = "referenceNo"
    const val OTP = "otp"
    const val SUBSCRIBER_ID = "subscriberId"
}












