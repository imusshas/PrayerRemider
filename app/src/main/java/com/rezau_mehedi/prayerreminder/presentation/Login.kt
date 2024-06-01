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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rezau_mehedi.prayerreminder.model.SignUpUIEvent

@Composable
fun Login(
    viewModel: PreferenceViewModel,
    navigateToHomeScreen: () -> Unit,
    navigateToSignUp: () -> Unit
) {
    val phoneNo = viewModel.phoneNo.collectAsState().value
    val error = viewModel.error.collectAsState().value
    val navigationState = viewModel.navigateToHomeFromLogin.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 48.dp, horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Prayer Reminder", fontWeight = FontWeight.Thin, fontSize = 32.sp)
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.onSignUpUIEvent(SignUpUIEvent.LoginButtonClicked)
                    if (navigationState) {
                        navigateToHomeScreen()
                    }
                },
                shape = RoundedCornerShape(15)
            ) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.height(48.dp))
            Text(text = "Not a User?")
            Spacer(modifier = Modifier.height(4.dp))
            Button(onClick = {
                viewModel.onSignUpUIEvent(SignUpUIEvent.SignUpButtonClickedFromLogin)
                navigateToSignUp()
            }, shape = RoundedCornerShape(15)) {
                Text(text = "Sign Up")
            }
        }
    }
}