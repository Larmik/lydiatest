package com.lydiatest.randomuser.viewModel

import com.lydiatest.randomuser.extension.mock
import com.lydiatest.randomuser.extensions.parseToDate
import com.lydiatest.randomuser.mock.PreferencesRepositoryMock
import com.lydiatest.randomuser.mock.UserRepositoryMock
import com.lydiatest.randomuser.model.User
import com.lydiatest.randomuser.userList.UserListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
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
class UserListViewModelTest {
    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() = Dispatchers.setMain(dispatcher)

    @After
    fun tearDown() = Dispatchers.resetMain()


    @Test
    fun testOnUserList() = dispatcher.runBlockingTest {
       val viewModel = UserListViewModel(
           UserRepositoryMock(),
           PreferencesRepositoryMock()
       )
        val users = mutableListOf<User>()
        val job = launch {
            viewModel.sharedUsers.collect { users += it }
        }

        viewModel.bind(flowOf(1), flowOf())
        job.cancel()
        Assert.assertEquals(1, users.size)
        Assert.assertEquals("firstname_mock", users.first().name.first)
        Assert.assertEquals("gender_mock", users.first().gender)
        Assert.assertEquals("12 janvier 1970", users.first().dob.parseToDate())
    }

    @Test
    fun testOnLoading() = dispatcher.runBlockingTest {
       val viewModel = UserListViewModel(
           UserRepositoryMock(isLoading = true),
           PreferencesRepositoryMock()
       )
        val loading = mutableListOf<Boolean>()
        val job = launch {
            viewModel.sharedLoading.collect { loading += it }
        }

        viewModel.bind(flowOf(1), flowOf())
        job.cancel()
        Assert.assertTrue(loading.single())
    }

    @Test
    fun testOnError() = dispatcher.runBlockingTest {
       val viewModel = UserListViewModel(
           UserRepositoryMock(isWorking = false ),
           PreferencesRepositoryMock()
       )
        val error = mutableListOf<String>()
        val job = launch {
            viewModel.sharedError.collect { error += it }
        }

        viewModel.bind(flowOf(1), flowOf())
        job.cancel()
        Assert.assertEquals("mocked_error", error.single())
    }

    @Test
    fun testDisplayLastResultWhenError() = dispatcher.runBlockingTest {
        val repository =  PreferencesRepositoryMock(items = listOf(User.mock()))
        val viewModel = UserListViewModel(
           UserRepositoryMock(isWorking = false),
            repository
        )
        var lastItems = listOf<User>()
        val job = launch {
            viewModel.sharedError.collect { repository.items?.let { lastItems = it } }
        }

        viewModel.bind(flowOf(1), flowOf())
        job.cancel()
        Assert.assertEquals(1, lastItems.size)
        Assert.assertEquals("firstname_mock", lastItems.first().name.first)
        Assert.assertEquals("gender_mock", lastItems.first().gender)
        Assert.assertEquals("12 janvier 1970", lastItems.first().dob.parseToDate())

    }

    @Test
    fun testOnItemClick() = dispatcher.runBlockingTest {
        val viewModel = UserListViewModel(
            UserRepositoryMock(),
            PreferencesRepositoryMock()
        )
        val itemClicked = mutableListOf<User>()
        val job = launch {
            viewModel.sharedGoToInfo.collect { itemClicked += it }
        }
        val onItemClick = MutableSharedFlow<User>()

        viewModel.bind(flowOf(1), onItemClick)
        onItemClick.emit(User.mock())
        job.cancel()
        Assert.assertEquals("firstname_mock", itemClicked.single().name.first)
        Assert.assertEquals("gender_mock", itemClicked.single().gender)
        Assert.assertEquals("12 janvier 1970", itemClicked.single().dob.parseToDate())

    }

}