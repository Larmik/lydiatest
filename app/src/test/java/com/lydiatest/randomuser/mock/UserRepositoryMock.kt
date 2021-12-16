package com.lydiatest.randomuser.mock

import com.lydiatest.randomuser.extension.mock
import com.lydiatest.randomuser.model.User
import com.lydiatest.randomuser.model.UserResponse
import com.lydiatest.randomuser.repository.UserRepositoryInterface
import kotlinx.coroutines.flow.flow

class UserRepositoryMock(
    val isWorking: Boolean = true,
    val isLoading: Boolean = false,
    val list: List<User> = listOf(User.mock())
) : UserRepositoryInterface {

    override fun getUsers(page: Int) = flow {
        emit(when {
            !isWorking -> UserResponse.Error(Throwable("mocked_error"))
            isLoading -> UserResponse.Loading()
            else -> UserResponse.Success(list)
        })
    }

}