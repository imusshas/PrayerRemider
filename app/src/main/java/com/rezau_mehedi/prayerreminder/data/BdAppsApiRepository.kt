package com.rezau_mehedi.prayerreminder.data

import com.rezau_mehedi.prayerreminder.model.api_response.RequestOTPResponse
import com.rezau_mehedi.prayerreminder.model.api_response.SubscriptionStatusResponse
import com.rezau_mehedi.prayerreminder.model.api_response.VerifyOTPResponse
import org.json.JSONObject
import retrofit2.Response

interface BdAppsApiRepository {
    suspend fun requestOTP(subscriberId: String): Response<RequestOTPResponse>

    suspend fun verifyOTP(
        referenceNo: String,
        otp: String
    ): Response<VerifyOTPResponse>
}