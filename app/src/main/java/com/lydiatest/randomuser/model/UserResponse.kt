package com.lydiatest.randomuser.model


sealed class UserResponse {
    data class Success(val users: List<User>): UserResponse()
    data class Error(val exception: Throwable): UserResponse()
    data class Loading(val isLoading: Boolean = true): UserResponse()
}