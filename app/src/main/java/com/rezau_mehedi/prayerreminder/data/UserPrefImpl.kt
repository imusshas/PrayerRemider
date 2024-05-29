package com.rezau_mehedi.prayerreminder.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rezau_mehedi.prayerreminder.core.Constants.DIVISIONS
import com.rezau_mehedi.prayerreminder.core.Constants.datastore
import com.rezau_mehedi.prayerreminder.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPrefImpl (private val context: Context) : UserPref {

    companion object {
        val PHONE_NO = stringPreferencesKey("PHONE_NO")
        val LOCATION = stringPreferencesKey("LOCATION")

    }

    override suspend fun saveUserModel(userModel: UserModel) {
        context.datastore.edit { users ->
            users[PHONE_NO] = userModel.phoneNo
            users[LOCATION] = userModel.location
        }
    }

    override fun getUserModel(): Flow<UserModel> = context.datastore.data.map { user ->
        UserModel(
            phoneNo = user[PHONE_NO] ?: "",
            location = user[LOCATION] ?: DIVISIONS[0]
        )
    }
}