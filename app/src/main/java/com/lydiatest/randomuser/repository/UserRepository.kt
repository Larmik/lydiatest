package com.lydiatest.randomuser.repository

import com.lydiatest.randomuser.datasource.UserDataSourceInterface
import com.lydiatest.randomuser.model.UserResponse
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserRepositoryInterface {
    fun getUsers(page: Int): Flow<UserResponse>
}

@FlowPreview
@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
interface UserRepositoryModule {
    @Binds
    fun bindUserRepository(impl: UserRepository): UserRepositoryInterface
}

@FlowPreview
@ExperimentalCoroutinesApi
class UserRepository @Inject constructor(private val userDataSourceInterface: UserDataSourceInterface) : UserRepositoryInterface {
    override fun getUsers(page: Int) = userDataSourceInterface.getUsers(page)
}