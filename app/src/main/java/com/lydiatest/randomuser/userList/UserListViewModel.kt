package com.lydiatest.randomuser.userList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lydiatest.randomuser.extensions.bind
import com.lydiatest.randomuser.model.User
import com.lydiatest.randomuser.model.UserResponse
import com.lydiatest.randomuser.repository.PreferencesRepositoryInterface
import com.lydiatest.randomuser.repository.UserRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepositoryInterface,
    private val preferencesRepository: PreferencesRepositoryInterface
) : ViewModel() {

    private val _sharedUsers = MutableSharedFlow<List<User>>()
    private val _sharedGoToInfo = MutableSharedFlow<User>()
    private val _sharedError = MutableSharedFlow<String>()
    private val _sharedLoading = MutableSharedFlow<Boolean>()
    private val _sharedPageChange = MutableStateFlow(1)

    val sharedUsers = _sharedUsers.asSharedFlow()
    val sharedGoToInfo = _sharedGoToInfo.asSharedFlow()
    val sharedError = _sharedError.asSharedFlow()
    val sharedLoading = _sharedLoading.asSharedFlow()

    private var hasEmitted = false

    fun bind(onPageChange: Flow<Int>, onItemClick: Flow<User>) {
        onPageChange.bind(_sharedPageChange, viewModelScope)
        _sharedPageChange.onEach {
            load(it)
        }.launchIn(viewModelScope)
        onItemClick.bind(_sharedGoToInfo, viewModelScope)
    }

    private fun load(page: Int) {
        val response = userRepository.getUsers(page)
            .shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)

        response.mapNotNull { (it as? UserResponse.Success)?.users }
            .onEach { preferencesRepository.lastItemsLoaded = it }
            .bind(_sharedUsers, viewModelScope)

        response.mapNotNull { (it as? UserResponse.Loading)?.isLoading }
            .bind(_sharedLoading, viewModelScope)

        response.mapNotNull { (it as? UserResponse.Error)?.exception?.message }
            .onEach {
                preferencesRepository.lastItemsLoaded?.let {
                    if (!hasEmitted) {
                        _sharedUsers.emit(it)
                        hasEmitted = true
                    }
                } }
            .bind(_sharedError, viewModelScope)
    }

}