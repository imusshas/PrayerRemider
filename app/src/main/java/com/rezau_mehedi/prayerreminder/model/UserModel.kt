package com.rezau_mehedi.prayerreminder.model

import com.rezau_mehedi.prayerreminder.core.Constants.DIVISIONS

data class UserModel(
    val phoneNo: String = "",
    val location: String = DIVISIONS[0]
)
