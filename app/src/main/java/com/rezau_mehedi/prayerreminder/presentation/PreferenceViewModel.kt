package com.rezau_mehedi.prayerreminder.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezau_mehedi.prayerreminder.core.Constants.DIVISIONS
import com.rezau_mehedi.prayerreminder.data.BdAppsApiRepository
import com.rezau_mehedi.prayerreminder.data.UserPref
import com.rezau_mehedi.prayerreminder.model.PrayerTimeUIEvent

import com.rezau_mehedi.prayerreminder.model.SignUpUIEvent
import com.rezau_mehedi.prayerreminder.model.UserModel
import com.rezau_mehedi.prayerreminder.model.UserValidator
import com.rezau_mehedi.prayerreminder.model.api_response.RequestOTPResponse
import com.rezau_mehedi.prayerreminder.model.api_response.VerifyOTPResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferenceViewModel @Inject constructor(
    private val userPref: UserPref,
    private val bdAppsApiRepository: BdAppsApiRepository
) : ViewModel() {

    private val _phoneNo = MutableStateFlow("")
    val phoneNo = _phoneNo.asStateFlow()

    private val _location = MutableStateFlow(DIVISIONS[0])
    val location = _location.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _otp = MutableStateFlow("")
    val otp = _otp.asStateFlow()

    private val _otpError = MutableStateFlow<String?>(null)
    val otpError = _otpError.asStateFlow()

    private val _requestOTPResponse = MutableStateFlow<RequestOTPResponse?>(null)
    private val requestOTPResponse = _requestOTPResponse.asStateFlow()

    private val _verifyOTPResponse = MutableStateFlow<VerifyOTPResponse?>(null)
    private val verifyOTPResponse = _verifyOTPResponse.asStateFlow()

    val user: StateFlow<UserModel> = userPref.getUserModel().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        UserModel()
    )




    private val _navigateToHomeFromLogin = MutableStateFlow(false)
    val navigateToHomeFromLogin = _navigateToHomeFromLogin.asStateFlow()

    private val _navigateToVerifyOTPFromSignUp = MutableStateFlow(false)
    val navigateToVerifyOTPFromSignUp = _navigateToVerifyOTPFromSignUp.asStateFlow()

    private val _navigateToHomeFromVerifyOTP = MutableStateFlow(false)
    val navigateToHomeFromVerifyOTP = _navigateToHomeFromVerifyOTP.asStateFlow()

    private val _locationDialogState = MutableStateFlow(false)
    val locationDialogState = _locationDialogState.asStateFlow()

    fun onSignUpUIEvent(event: SignUpUIEvent) {
        when (event) {

            is SignUpUIEvent.LocationChanged -> {
                _location.update { event.location }
            }

            SignUpUIEvent.LoginButtonClicked -> {
                if (validateAllStates()) {
                    login()
                }
            }

            SignUpUIEvent.LoginButtonClickedFromSignUp -> {
                _phoneNo.update { "" }
                _error.update { null }
            }

            is SignUpUIEvent.PhoneNoChanged -> {
                _phoneNo.update { event.phoneNo }
            }

            SignUpUIEvent.VerifyOTPButtonClicked -> {
                verifyOTP()
            }

            is SignUpUIEvent.OTPChanged -> {
                _otp.update { event.otp }
            }

            SignUpUIEvent.SignUpButtonClicked -> {
                if (validateAllStates()) {
                    signUp()
                }
            }


            SignUpUIEvent.SignUpButtonClickedFromLogin -> {
                _phoneNo.update { "" }
                _error.update { null }
            }
        }
    }


    fun onPrayerTimeUIEvent(event: PrayerTimeUIEvent) {
        when (event) {
            PrayerTimeUIEvent.ChangeButtonClicked -> {
                _location.update { user.value.location }
                _locationDialogState.update { true }
            }

            PrayerTimeUIEvent.ConfirmButtonClicked -> {
                updateLocation()
            }

            is PrayerTimeUIEvent.LocationChanged -> {
                _location.update { event.location }
            }

            PrayerTimeUIEvent.LogOutButtonClicked -> {
                logOut()
            }
        }
    }


    private fun saveUser(userModel: UserModel) = viewModelScope.launch(Dispatchers.IO) {
        if (validateAllStates()) {
            userPref.saveUserModel(userModel)
            _navigateToHomeFromLogin.update { true }
            _navigateToHomeFromVerifyOTP.update { true }
            _phoneNo.update { "" }
            _location.update { "" }
        }
    }


    private fun validateAllStates(): Boolean {
        val phoneNoResult = UserValidator.validatePhoneNo(phoneNo.value)

        _error.update { phoneNoResult }

        return phoneNoResult == null
    }


    private fun updateLocation() = viewModelScope.launch {
        userPref.saveUserModel(
            UserModel(
                phoneNo = user.value.phoneNo,
                location = location.value
            )
        )

        _location.update { "" }
        _locationDialogState.update { false }
    }


    private fun login() = viewModelScope.launch {
        val subscriberId = phoneNo.value
        Log.d(TAG, "requestOTP: number: $subscriberId")
        val response = bdAppsApiRepository.requestOTP(subscriberId = subscriberId)

        if (response.isSuccessful) {
            _requestOTPResponse.value = response.body()
            Log.d(TAG, "requestOTP: ${response.body()}")
            if (requestOTPResponse.value?.statusDetail == "Success") {
                _error.update { "User is not Registered" }
            }else if (requestOTPResponse.value?.statusDetail == "user already registered") {
                _navigateToHomeFromLogin.update { true }
                _locationDialogState.update { true }
                saveUser(UserModel(phoneNo = phoneNo.value))

            }
        } else {
            Log.d(TAG, "requestOTP: error: ${response.errorBody()}")
        }
    }

    private fun signUp() = viewModelScope.launch {
        val subscriberId = phoneNo.value
        Log.d(TAG, "requestOTP: number: $subscriberId")
        val response = bdAppsApiRepository.requestOTP(subscriberId = subscriberId)

        if (response.isSuccessful) {
            _requestOTPResponse.value = response.body()
            Log.d(TAG, "requestOTP: ${response.body()}")
            if (requestOTPResponse.value?.statusDetail == "Success") {
                _navigateToVerifyOTPFromSignUp.update { true }
            }else if (requestOTPResponse.value?.statusDetail == "user already registered") {
                _error.update { "User is Already Registered" }
            }
        } else {
            Log.d(TAG, "requestOTP: error: ${response.errorBody()}")
        }

    }


    private fun verifyOTP() = viewModelScope.launch {
        requestOTPResponse.value?.let {response ->

            val verifyResponse = bdAppsApiRepository.verifyOTP(
                referenceNo = response.referenceNo,
                otp = otp.value
            )

            if (verifyResponse.isSuccessful) {
                _verifyOTPResponse.value = verifyResponse.body()
                if (verifyOTPResponse.value?.statusDetail == "Success") {
                    saveUser(UserModel(phoneNo = phoneNo.value, location = location.value))
                    _navigateToHomeFromVerifyOTP.update { true }
                    _locationDialogState.update { true }
                } else {
                    _otpError.update { "Invalid OTP or Internet Connection" }
                }
            } else {
                Log.d(TAG, "verifyOTP: error: ${verifyResponse.errorBody()}")
            }
        }
    }



    private fun logOut() = viewModelScope.launch {
        userPref.saveUserModel(UserModel(phoneNo = "", location = user.value.location))

        _phoneNo.value = ""
        _location.value = user.value.location
        _error.value = null

        _otp.value = ""
        _otpError.value = null

        _requestOTPResponse.value = null
        _verifyOTPResponse.value = null

        _navigateToHomeFromLogin.value = false
        _navigateToVerifyOTPFromSignUp.value = false
        _navigateToHomeFromVerifyOTP.value = false
        _locationDialogState.value = false
    }
    companion object {
        const val TAG = "PreferenceViewModel"
    }
}