package com.rezau_mehedi.prayerreminder.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.batoulapps.adhan2.CalculationMethod
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.Madhab
import com.batoulapps.adhan2.PrayerTimes
import com.batoulapps.adhan2.data.DateComponents
import com.rezau_mehedi.prayerreminder.core.Constants
import java.text.SimpleDateFormat

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.Locale
import java.util.TimeZone


@Composable
fun PrayerTimeUI(viewModel: PreferenceViewModel) {

    val user = viewModel.user.collectAsState().value

    val (fajr, dhuhr, asr, maghrib, isha) = getPrayerTimes(
        Constants.LATITUDE[Constants.DIVISIONS.indexOf(user.location)],
        Constants.LONGITUDE[Constants.DIVISIONS.indexOf(user.location)]
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 48.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Phone No: ${user.phoneNo}")
        Text(text = "Location: ${user.location}")
        Text(text = "Fajr $fajr")
        Text(text = "Dhuhr $dhuhr")
        Text(text = "Asr $asr")
        Text(text = "Maghrib $maghrib")
        Text(text = "Isha $isha")
    }

}


private fun getPrayerTimes(
    latitude: Double,
    longitude: Double
): List<String> {
    val coordinates = Coordinates(latitude, longitude)
    val today = LocalDate.now()

    val date = DateComponents(today.year, today.monthValue, today.dayOfMonth)
    val parameters = CalculationMethod.KARACHI.parameters.copy(madhab = Madhab.HANAFI)

    val prayerTimes = PrayerTimes(coordinates, date, parameters)

    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("Asia/Dhaka")

    val fajr = formatter.format(prayerTimes.fajr.toEpochMilliseconds())
    val dhuhr = formatter.format(prayerTimes.dhuhr.toEpochMilliseconds())
    val asr = formatter.format(prayerTimes.asr.toEpochMilliseconds())
    val maghrib = formatter.format(prayerTimes.maghrib.toEpochMilliseconds())
    val isha = formatter.format(prayerTimes.isha.toEpochMilliseconds())

    return listOf(fajr, dhuhr, asr, maghrib, isha)
}
