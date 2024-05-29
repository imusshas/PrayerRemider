package com.rezau_mehedi.prayerreminder.model

sealed class PrayerTimeUIEvent {
    data class LocationChanged(val location: String): PrayerTimeUIEvent()
    data object ChangeButtonClicked: PrayerTimeUIEvent()

    data object ConfirmButtonClicked: PrayerTimeUIEvent()
}