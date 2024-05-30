package com.rezau_mehedi.prayerreminder.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rezau_mehedi.prayerreminder.model.SignUpUIEvent

@Composable
fun VerifyOTP(
    viewModel: PreferenceViewModel,
    navigateToPrayerTimeUI: () -> Unit
) {

    val otp = viewModel.otp.collectAsState().value

    val navigationState = viewModel.navigationState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
            textStyle = TextStyle(textAlign = TextAlign.Center)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.onSignUpUIEvent(SignUpUIEvent.SignUpButtonClicked)
                if (navigationState) {
                    navigateToPrayerTimeUI()
                }
            },
            shape = RoundedCornerShape(15)
        ) {
            Text(text = "Verify")
        }
    }

}