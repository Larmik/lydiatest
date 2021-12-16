package com.lydiatest.randomuser.model

data class UserLogin(
    val username: String,
    val password: String,
    val salt: String,
    val md5: String,
    val sha1: String,
    val sha256: String
)