package com.rezau_mehedi.prayerreminder.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezau_mehedi.prayerreminder.core.Constants.DIVISIONS
import com.rezau_mehedi.prayerreminder.data.UserPref

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


    fun onEvent(event: SignUpUIEvent) {
        when(event) {
            is SignUpUIEvent.LocationChanged -> {
                _location.update { event.location }
            }
            is SignUpUIEvent.PhoneNoChanged -> {
                _phoneNo.update { event.phoneNo }
            }
            SignUpUIEvent.SignUpButtonClicked -> {
                if (validateAllStates()) {
                    saveUser(UserModel(phoneNo = phoneNo.value, location = location.value))
                }
            }
        }
    }


    private fun saveUser(userModel: UserModel) = viewModelScope.launch(Dispatchers.IO) {
            userPref.saveUserModel(userModel)
    }


    private fun validateAllStates(): Boolean {
        Log.d(TAG, "validateAllStates: phoneNo: ${phoneNo.value}")
        Log.d(TAG, "validateAllStates: location: ${location.value}")
        Log.d(TAG, "validateAllStates: user: ${user.value}")
        val phoneNoResult = UserValidator.validatePhoneNo(phoneNo.value)

        _error.update { phoneNoResult }

        return phoneNoResult == null
    }

    companion object {
        const val TAG = "PreferenceViewModel"
    }
}