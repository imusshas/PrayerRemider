package com.rezau_mehedi.prayerreminder.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.batoulapps.adhan2.CalculationMethod
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.Madhab
import com.batoulapps.adhan2.PrayerTimes
import com.batoulapps.adhan2.data.DateComponents
import com.rezau_mehedi.prayerreminder.core.Constants
import com.rezau_mehedi.prayerreminder.model.PrayerTimeUIEvent
import com.rezau_mehedi.prayerreminder.model.SignUpUIEvent
import java.text.SimpleDateFormat

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.Locale
import java.util.TimeZone


@Composable
fun PrayerTimeUI(
    viewModel: PreferenceViewModel,
    navigateToSignUpScreen: () -> Unit
) {

    val user = viewModel.user.collectAsState().value
    val locationDialogState = viewModel.locationDialogState.collectAsState().value

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
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(text = "Location: ${user.location}")
            Button(onClick = {
                viewModel.onPrayerTimeUIEvent(PrayerTimeUIEvent.ChangeButtonClicked)
            }) {
                Text(text = "Change")
            }
        }
        Text(text = "Fajr $fajr")
        Text(text = "Dhuhr $dhuhr")
        Text(text = "Asr $asr")
        Text(text = "Maghrib $maghrib")
        Text(text = "Isha $isha")

        ElevatedButton(
            onClick = {
                viewModel.resetUser()
                navigateToSignUpScreen()
            },
            shape = RoundedCornerShape(15)
        ) {
            Text(text = "Reset")
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
    val dhuhr = formatter.format(prayerTimes.dhuhr.toEpochMilliseconds())
    val asr = formatter.format(prayerTimes.asr.toEpochMilliseconds())
    val maghrib = formatter.format(prayerTimes.maghrib.toEpochMilliseconds())
    val isha = formatter.format(prayerTimes.isha.toEpochMilliseconds())

    return listOf(fajr, dhuhr, asr, maghrib, isha)
}


@Composable
private fun LocationDialog(
    viewModel: PreferenceViewModel
) {
    
    val location = viewModel.location.collectAsState().value
    
    Dialog(
        onDismissRequest = {  },
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
                                viewModel.onPrayerTimeUIEvent(PrayerTimeUIEvent.LocationChanged(division))
                            }
                        )
                        Text(
                            text = division,
                            modifier = Modifier.clickable {
                                viewModel.onPrayerTimeUIEvent(PrayerTimeUIEvent.LocationChanged(division))
                            })
                    }
                }
            }
            
            ElevatedButton(onClick = { viewModel.onPrayerTimeUIEvent(PrayerTimeUIEvent.ConfirmButtonClicked) }) {
                Text(text = "Confirm")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

