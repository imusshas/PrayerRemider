package com.rezau_mehedi.prayerreminder.data

import com.rezau_mehedi.prayerreminder.api.BdAppsApi
import com.rezau_mehedi.prayerreminder.model.api_response.RequestOTPResponse
import com.rezau_mehedi.prayerreminder.model.api_response.VerifyOTPResponse
import retrofit2.Response
import javax.inject.Inject

class BdAppsApiRepositoryImpl @Inject constructor(
    private val api: BdAppsApi
) : BdAppsApiRepository {
    override suspend fun requestOTP(subscriberId: String): Response<RequestOTPResponse> {
        return api.requestOTP(subscriberId)
    }

    override suspend fun verifyOTP(referenceNo: String, otp: String): Response<VerifyOTPResponse> {
        return api.verifyOTP(referenceNo, otp)
    }


}