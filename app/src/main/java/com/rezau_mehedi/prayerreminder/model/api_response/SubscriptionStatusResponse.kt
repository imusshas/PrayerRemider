package com.rezau_mehedi.prayerreminder.model.api_response

data class SubscriptionStatusResponse(
    val statusCode: String,
    val statusDetail: String,
    val subscriptionStatus: String,
    val version: String
)