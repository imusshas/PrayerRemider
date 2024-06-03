package com.rezau_mehedi.prayerreminder.domain.events

sealed interface PrayerTimeUIEvent {
    data class LocationChanged(val location: String): PrayerTimeUIEvent
    data object ChangeButtonClicked: PrayerTimeUIEvent

    data object ConfirmButtonClicked: PrayerTimeUIEvent
    data object LogOutButtonClicked: PrayerTimeUIEvent
}