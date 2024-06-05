package com.rezau_mehedi.prayerreminder.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.batoulapps.adhan2.CalculationMethod
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.Madhab
import com.batoulapps.adhan2.PrayerTimes
import com.batoulapps.adhan2.data.DateComponents
import com.rezau_mehedi.prayerreminder.R
import com.rezau_mehedi.prayerreminder.core.Constants
import com.rezau_mehedi.prayerreminder.domain.events.PrayerTimeUIEvent
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Locale
import java.util.TimeZone


@Composable
fun PrayerTimeUI(
    viewModel: PreferenceViewModel,
    navigateToSignUpScreen: () -> Unit
) {

    val user = viewModel.user.collectAsState().value
    val locationDialogState = viewModel.locationDialogState.collectAsState().value

    val prayerTimes = getPrayerTimes(
        Constants.LATITUDE[Constants.DIVISIONS.indexOf(user.location)],
        Constants.LONGITUDE[Constants.DIVISIONS.indexOf(user.location)]
    )

    val fajr = prayerTimes[0]
    val dhuhr = prayerTimes[2]
    val asr = prayerTimes[3]
    val maghrib = prayerTimes[4]
    val isha = prayerTimes[5]
    val sunrise = prayerTimes[1]

    val fajrShift = fajr.substring(6, 8)
    val sunriseShift = sunrise.substring(6, 8)
    val dhuhrShift = dhuhr.substring(6, 8)
    val asrShift = asr.substring(6, 8)
    val maghribShift = maghrib.substring(6, 8)

    var fajrHour = fajr.substring(0, 2).toInt()
    if (fajrHour < 12 && fajrShift == "PM") {
        fajrHour += 12
    } else if (fajrHour == 12 && fajrShift == "AM") {
        fajrHour = 0
    }
    val fajrMinute = fajr.substring(3, 5).toInt()

    var dhuhrHour = dhuhr.substring(0, 2).toInt()
    if (dhuhrHour < 12 && dhuhrShift == "PM") {
        dhuhrHour += 12
    } else if (dhuhrHour == 12 && dhuhrShift == "AM") {
        dhuhrHour = 0
    }
    val dhuhrMinute = dhuhr.substring(3, 5).toInt()

    var asrHour = asr.substring(0, 2).toInt()
    if (asrHour < 12 && asrShift == "PM") {
        asrHour += 12
    } else if (asrHour == 12 && asrShift == "AM") {
        asrHour = 0
    }
    val asrMinute = asr.substring(3, 5).toInt()

    var maghribHour = maghrib.substring(0, 2).toInt()
    if (maghribHour < 12 && maghribShift == "PM") {
        maghribHour += 12
    } else if (maghribHour == 12 && maghribShift == "AM") {
        maghribHour = 0
    }
    val maghribMinute = maghrib.substring(3, 5).toInt()


    var sunriseHour = sunrise.substring(0, 2).toInt()
    if (sunriseHour < 12 && sunriseShift == "PM") {
        sunriseHour += 12
    } else if (sunriseHour == 12 && sunriseShift == "AM") {
        sunriseHour = 0
    }
    val sunriseMinute = sunrise.substring(3, 5).toInt()


    val today = LocalDateTime.now()


    val now =
        if ((today.hour == fajrHour && today.minute >= fajrMinute)
            || (today.hour in (fajrHour + 1)..sunriseHour && today.minute < sunriseMinute)) {
            "Fajr"
        } else if ((today.hour == sunriseHour && today.minute >= sunriseMinute)
            || (today.hour in (sunriseHour + 1)..dhuhrHour && today.minute < dhuhrMinute)) {
            "Sunrise"
        } else if ((today.hour == dhuhrHour && today.minute >= dhuhrMinute)
            || (today.hour in (dhuhrHour + 1)..asrHour && today.minute < asrMinute)) {
            "Dhuhr"
        } else if ((today.hour == asrHour && today.minute >= asrMinute)
            || (today.hour in (asrHour + 1)..maghribHour) && today.minute < maghribMinute) {
            "Asr"
        } else if (today.hour == maghribHour && today.minute >= maghribMinute && today.minute < maghribMinute + 15) {
            "Maghrib"
        } else {
            "Isha"
        }

    val next = when (now) {
        "Fajr" -> "Sunrise"
        "Sunrise" -> "Dhuhr"
        "Dhuhr" -> "Asr"
        "Asr" -> "Maghrib"
        "Maghrib" -> "Isha"
        else -> "Fajr"
    }

    val nextTime = when (next) {
        "Fajr" -> fajr.substring(0, 5)
        "Sunrise" -> sunrise.substring(0, 5)
        "Dhuhr" -> dhuhr.substring(0, 5)
        "Asr" -> asr.substring(0, 5)
        "Maghrib" -> maghrib.substring(0, 5)
        else -> isha.substring(0, 5)
    }

    val nextShift = when (next) {
        "Fajr" -> fajr.substring(6, 8)
        "Sunrise" -> sunrise.substring(6, 8)
        "Dhuhr" -> dhuhr.substring(6, 8)
        "Asr" -> asr.substring(6, 8)
        "Maghrib" -> maghrib.substring(6, 8)
        else -> isha.substring(6, 8)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Top Card
        TopCard(
            next = next,
            now = now,
            nextTime = nextTime,
            nextShift = nextShift,
            location = user.location,
            locationDialogStateChange = { viewModel.onPrayerTimeUIEvent(PrayerTimeUIEvent.ChangeButtonClicked) }
        )

        Spacer(modifier = Modifier.height(48.dp))


        // Prayer Times
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val dhuhrOrJumma = if (LocalDateTime.now().dayOfWeek.name.uppercase() == "FRIDAY") "Jumma" else "Dhuhr"
            // Fajr
            PrayerTimeCard(prayerName = "Fajr", prayerTime = fajr, selected = now == "Fajr")
            // Dhuhr
            PrayerTimeCard(prayerName = dhuhrOrJumma, prayerTime = dhuhr, selected = now == "Dhuhr")
            // Asr
            PrayerTimeCard(prayerName = "Asr", prayerTime = asr, selected = now == "Asr")
            // Maghrib
            PrayerTimeCard(prayerName = "Maghrib", prayerTime = maghrib, selected = now == "Maghrib")
            // Isha
            PrayerTimeCard(prayerName = "Isha", prayerTime = isha, selected = now == "Isha")

        }

        Button(
            onClick = {
                viewModel.onPrayerTimeUIEvent(PrayerTimeUIEvent.LogOutButtonClicked)
                    navigateToSignUpScreen()
            },
            shape = RoundedCornerShape(15)
        ) {
            Text(text = "Log Out")
        }
    }

    if (locationDialogState) {
        LocationDialog(viewModel = viewModel)
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
    val sunrise = formatter.format(prayerTimes.sunrise.toEpochMilliseconds())
    val dhuhr = formatter.format(prayerTimes.dhuhr.toEpochMilliseconds())
    val asr = formatter.format(prayerTimes.asr.toEpochMilliseconds())
    val maghrib = formatter.format(prayerTimes.maghrib.toEpochMilliseconds())
    val isha = formatter.format(prayerTimes.isha.toEpochMilliseconds())

    return listOf(fajr, sunrise, dhuhr, asr, maghrib, isha)
}


@Composable
private fun LocationDialog(
    viewModel: PreferenceViewModel
) {

    val location = viewModel.location.collectAsState().value

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = false,
            decorFitsSystemWindows = false
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Constants.DIVISIONS.forEach { division ->
                if (division.isNotBlank()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = location == division,
                            onClick = {
                                viewModel.onPrayerTimeUIEvent(
                                    PrayerTimeUIEvent.LocationChanged(
                                        division
                                    )
                                )
                            }
                        )
                        Text(
                            text = division,
                            modifier = Modifier.clickable {
                                viewModel.onPrayerTimeUIEvent(
                                    PrayerTimeUIEvent.LocationChanged(
                                        division
                                    )
                                )
                            })
                    }
                }
            }

            Button(
                onClick = { viewModel.onPrayerTimeUIEvent(PrayerTimeUIEvent.ConfirmButtonClicked) },
                shape = RoundedCornerShape(15)
            ) {
                Text(text = "Confirm")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TopCard(
    next: String,
    now: String,
    nextTime: String,
    nextShift: String,
    location: String,
    locationDialogStateChange: () -> Unit
) {

    val today = LocalDateTime.now()
    val weekDay = today.dayOfWeek.toString().lowercase()
    val month = today.month.toString().lowercase()
    val intHour = today.hour
    val amOrPm = if (intHour > 11) "PM" else "AM"
    val hour = if (intHour > 12) {
        "${intHour - 12}"
    } else if (intHour == 0) {
        "12"
    } else {
        intHour.toString()
    }
    var minute = today.minute.toString()
    if (minute.length == 1) {
        minute = "0$minute"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(10),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.Black
        )
    ) {
        Box(modifier = Modifier.fillMaxSize().alpha(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.mosque_background),
                contentDescription = "Mosque background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f),
                ) {
                    Text(
                        text = "${weekDay[0].uppercase()}${weekDay.substring(1)}, ${today.dayOfMonth} ${month[0].uppercase()}${
                            month.substring(1)
                        }",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Next", fontSize = 12.sp)
                    Text(text = next, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = nextTime, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                        Text(text = nextShift, modifier = Modifier.padding(top = 10.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "Now:")
                        Text(text = now, fontWeight = FontWeight.SemiBold)
                    }


                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(text = "Time Now", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "$hour:$minute", fontSize = 64.sp, fontWeight = FontWeight.Bold)
                        Text(text = amOrPm, modifier = Modifier.padding(top = 10.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.clickable { locationDialogStateChange() },
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            modifier = Modifier
                                .size(36.dp),
                            tint = Color.Red
                        )
                        Text(text = location, fontSize = 20.sp, color = Color.Red)
                    }
                }
            }
        }

    }


}


@Composable
fun PrayerTimeCard(
    prayerName: String,
    prayerTime: String,
    selected: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary else Color(0xFFf2f5eb)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = prayerName, fontSize = 24.sp)
            Text(text = prayerTime, fontSize = 24.sp)
        }
    }
}