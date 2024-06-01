package com.rezau_mehedi.prayerreminder.model

sealed interface SignUpUIEvent {
    data class PhoneNoChanged(val phoneNo: String): SignUpUIEvent
    data class LocationChanged(val location: String): SignUpUIEvent

    data object SignUpButtonClicked: SignUpUIEvent
    data object SignUpButtonClickedFromLogin: SignUpUIEvent
    data object LoginButtonClicked: SignUpUIEvent
    data object LoginButtonClickedFromSignUp: SignUpUIEvent
    data object VerifyOTPButtonClicked: SignUpUIEvent

    data class OTPChanged(val otp: String): SignUpUIEvent
}