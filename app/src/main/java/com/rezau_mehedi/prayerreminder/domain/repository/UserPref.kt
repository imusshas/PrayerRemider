package com.rezau_mehedi.prayerreminder.domain.repository

import com.rezau_mehedi.prayerreminder.data.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserPref {
    suspend fun saveUserModel(userModel: UserModel)

    fun getUserModel(): Flow<UserModel>
}