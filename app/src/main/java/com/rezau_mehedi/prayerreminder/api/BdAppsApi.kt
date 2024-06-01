package com.rezau_mehedi.prayerreminder.api

import com.rezau_mehedi.prayerreminder.core.Constants.OTP
import com.rezau_mehedi.prayerreminder.core.Constants.REFERENCE_NO
import com.rezau_mehedi.prayerreminder.core.Constants.SUBSCRIBER_ID
import com.rezau_mehedi.prayerreminder.model.api_response.RequestOTPResponse
import com.rezau_mehedi.prayerreminder.model.api_response.VerifyOTPResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface BdAppsApi {


    @POST(REQUEST_OTP_END_POINT)
    suspend fun requestOTP(@Query(SUBSCRIBER_ID) number: String): Response<RequestOTPResponse>

    @POST(VERIFY_OTP_END_POINT)
    suspend fun verifyOTP(
        @Query(value = REFERENCE_NO) referenceNo: String,
        @Query(value = OTP) otp: String
    ): Response<VerifyOTPResponse>



    companion object {
        private const val REQUEST_OTP_END_POINT = "request_otp"
        private const val VERIFY_OTP_END_POINT = "verify_otp"
    }
}