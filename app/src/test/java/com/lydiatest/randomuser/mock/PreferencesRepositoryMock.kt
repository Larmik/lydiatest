package com.lydiatest.randomuser.mock

import com.lydiatest.randomuser.model.User
import com.lydiatest.randomuser.repository.PreferencesRepositoryInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class PreferencesRepositoryMock(var items: List<User>? = listOf()) : PreferencesRepositoryInterface {

    override var lastItemsLoaded: List<User>?
        get() = items
        set(value) { this.items = value }
}