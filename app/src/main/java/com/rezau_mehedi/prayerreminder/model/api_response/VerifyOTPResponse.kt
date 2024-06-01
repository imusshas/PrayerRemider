package com.rezau_mehedi.prayerreminder.model.api_response

data class VerifyOTPResponse(
    val statusCode: String,
    val statusDetail: String,
    val subscriberId: String,
    val subscriptionStatus: String,
    val version: String
)