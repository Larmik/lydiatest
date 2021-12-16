package com.lydiatest.randomuser.datasource

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lydiatest.randomuser.extensions.safeOffer
import com.lydiatest.randomuser.model.User
import com.lydiatest.randomuser.model.UserResponse
import com.lydiatest.randomuser.service.NetworkService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import javax.inject.Inject

interface UserDataSourceInterface {
    fun getUsers(page: Int): Flow<UserResponse>
}

@FlowPreview
@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
interface UserDataSourceModule {
    @Binds
    fun bindUserDataSource(impl: UserDataSource): UserDataSourceInterface
}

@FlowPreview
@ExperimentalCoroutinesApi
class UserDataSource @Inject constructor() : UserDataSourceInterface {

    private val networkService = object : NetworkService(){}

    override fun getUsers(page: Int) = callbackFlow {
        safeOffer(UserResponse.Loading())
        networkService.createRequest(
            page = page,
            { response ->
                async {
                    val userParsed = parseJson(response)
                    safeOffer(UserResponse.Success(userParsed))
                }
            },
            { volleyError ->
                async {
                    safeOffer(UserResponse.Error(volleyError.cause ?: Throwable(volleyError.message)))
                }
            }
        )
        awaitClose { }
    }.flowOn(Dispatchers.IO)

    private fun parseJson(vararg params: JSONObject?): List<User> {
        val finalList = ArrayList<User>()
        try {
            val result = params[0]
            val users = Gson().fromJson<List<User>>(result?.get("results").toString(), object : TypeToken<List<User>>(){}.type)
            Log.d("TAG", "getUsers: $users")
            users?.let {
                finalList.addAll(it)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return finalList
    }
}