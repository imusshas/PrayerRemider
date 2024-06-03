package com.rezau_mehedi.prayerreminder.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rezau_mehedi.prayerreminder.R
import com.rezau_mehedi.prayerreminder.domain.events.SignUpUIEvent

@Composable
fun VerifyOTP(
    viewModel: PreferenceViewModel,
    navigateToPrayerTimeUI: () -> Unit
) {

    val otp = viewModel.otp.collectAsState().value
    val otpError = viewModel.otpError.collectAsState().value

    val navigationState = viewModel.navigateToHomeFromVerifyOTP.collectAsState().value

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.mosque_screen),
            contentDescription = "Mosque Screen",
            modifier = Modifier.fillMaxSize().alpha(0.4f),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Prayer Reminder",
                    fontWeight = FontWeight.Black, fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    value = otp,
                    onValueChange = { currentOTP ->
                        viewModel.onSignUpUIEvent(SignUpUIEvent.OTPChanged(currentOTP))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 0.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    label = {
                        Text(
                            text = "OTP",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    placeholder = {
                        Text(
                            text = "* * * * * *",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    isError = otpError == null
                )

                if (otpError != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = otpError, color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.onSignUpUIEvent(SignUpUIEvent.VerifyOTPButtonClicked)
                        if (navigationState) {
                            navigateToPrayerTimeUI()
                        }
                        navigateToPrayerTimeUI()
                    },
                    shape = RoundedCornerShape(15)
                ) {
                    Text(text = "Verify")
                }
            }
        }
    }



}