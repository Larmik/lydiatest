package com.lydiatest.randomuser.model

import java.io.Serializable


data class User(
    val gender : String,
    val name: UserName,
    val location: UserLocation,
    val email: String,
    val login: UserLogin,
    val registered: Long,
    val dob: Long,
    val phone: String,
    val cell: String,
    val id: UserId,
    val picture: UserPicture,
    val nat: String
) : Serializable {
    companion object
}