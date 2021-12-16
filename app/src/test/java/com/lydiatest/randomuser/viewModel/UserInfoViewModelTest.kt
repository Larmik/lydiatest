package com.lydiatest.randomuser.viewModel

import com.lydiatest.randomuser.userInfo.UserInfoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class UserInfoViewModelTest {

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() = Dispatchers.setMain(dispatcher)

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun testOnBack() = dispatcher.runBlockingTest {
        val viewModel = UserInfoViewModel()
        val onBack = mutableListOf<Unit>()
        val onBackClick = MutableSharedFlow<Unit>()
        val job = launch {
            viewModel.sharedBack.collect { onBack += it }
        }

        viewModel.bind(onBackClick)
        onBackClick.emit(Unit)
        job.cancel()
        Assert.assertEquals(1, onBack.size)
    }

}