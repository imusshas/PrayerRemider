package com.rezau_mehedi.prayerreminder.model

sealed class SignUpUIEvent {
    data class PhoneNoChanged(val phoneNo: String): SignUpUIEvent()
    data class LocationChanged(val location: String): SignUpUIEvent()

    data object SignUpButtonClicked: SignUpUIEvent()
}