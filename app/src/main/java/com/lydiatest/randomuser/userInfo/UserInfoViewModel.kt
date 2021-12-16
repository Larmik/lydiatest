package com.lydiatest.randomuser.userInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lydiatest.randomuser.extensions.bind
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class UserInfoViewModel: ViewModel() {

    private val _sharedBack = MutableSharedFlow<Unit>()
    val sharedBack = _sharedBack.asSharedFlow()

    fun bind(onBack: Flow<Unit>) = onBack.bind(_sharedBack, viewModelScope)

}