package com.rezau_mehedi.prayerreminder.data

import com.rezau_mehedi.prayerreminder.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserPref {
    suspend fun saveUserModel(userModel: UserModel)

    fun getUserModel(): Flow<UserModel>
}