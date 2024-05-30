package com.rezau_mehedi.prayerreminder.presentation

import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rezau_mehedi.prayerreminder.core.Constants
import com.rezau_mehedi.prayerreminder.core.Constants.APP_NAME
import com.rezau_mehedi.prayerreminder.model.SignUpUIEvent


@Composable
fun SignUp(
    viewModel: PreferenceViewModel,
    navigateVerifyOTP: () -> Unit
) {

    val phoneNo = viewModel.phoneNo.collectAsState().value
    val location = viewModel.location.collectAsState().value
    val error = viewModel.error.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 48.dp, horizontal = 24.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Prayer Reminder", fontWeight = FontWeight.Thin, fontSize = 32.sp)
        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = phoneNo,
                onValueChange = { phone ->
                    viewModel.onSignUpUIEvent(SignUpUIEvent.PhoneNoChanged(phone))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                label = { Text(text = "Phone Number") },
                placeholder = { Text(text = "Enter your phone no") },
                isError = error != null
            )

            if (error != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = error, color = Color.Red, fontSize = 12.sp)
            }
        }

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
                            viewModel.onSignUpUIEvent(SignUpUIEvent.LocationChanged(division))
                        }
                    )
                    Text(
                        text = division,
                        modifier = Modifier.clickable { 
                            viewModel.onSignUpUIEvent(SignUpUIEvent.LocationChanged(division))
                        })
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            Button(
                onClick = navigateVerifyOTP,
                shape = RoundedCornerShape(15)
            ) {
                Text(text = "Sign Up")
            }
        }
    }
}