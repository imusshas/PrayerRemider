package com.rezau_mehedi.prayerreminder.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezau_mehedi.prayerreminder.core.Constants.DIVISIONS
import com.rezau_mehedi.prayerreminder.data.UserPref
import com.rezau_mehedi.prayerreminder.model.PrayerTimeUIEvent

import com.rezau_mehedi.prayerreminder.model.SignUpUIEvent
import com.rezau_mehedi.prayerreminder.model.UserModel
import com.rezau_mehedi.prayerreminder.model.UserValidator
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
    private val userPref: UserPref
): ViewModel() {

    private val _phoneNo = MutableStateFlow("")
    val phoneNo = _phoneNo.asStateFlow()

    private val _location = MutableStateFlow(DIVISIONS[0])
    val location = _location.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    val user: StateFlow<UserModel> = userPref.getUserModel().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        UserModel()
    )


    private val _navigationState = MutableStateFlow(false)
    val navigationState = _navigationState.asStateFlow()


    private val _locationDialogState = MutableStateFlow(false)
    val locationDialogState = _locationDialogState.asStateFlow()


    fun onSignUpUIEvent(event: SignUpUIEvent) {
        when(event) {
            is SignUpUIEvent.LocationChanged -> {
                _location.update { event.location }
            }
            is SignUpUIEvent.PhoneNoChanged -> {
                validateAllStates()
                _phoneNo.update { event.phoneNo }
            }
            SignUpUIEvent.SignUpButtonClicked -> {
                saveUser(UserModel(phoneNo = phoneNo.value, location = location.value))

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
        }
    }


    private fun saveUser(userModel: UserModel) = viewModelScope.launch(Dispatchers.IO) {
            if (validateAllStates()) {
                userPref.saveUserModel(userModel)
                _navigationState.update { true }
                _phoneNo.update { "" }
                _location.update { "" }
            }
    }


    private fun validateAllStates(): Boolean {
        Log.d(TAG, "validateAllStates: phoneNo: ${phoneNo.value}")
        Log.d(TAG, "validateAllStates: location: ${location.value}")
        Log.d(TAG, "validateAllStates: user: ${user.value}")
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


    fun resetUser() = viewModelScope.launch {
        userPref.saveUserModel(UserModel())
    }

    companion object {
        const val TAG = "PreferenceViewModel"
    }
}