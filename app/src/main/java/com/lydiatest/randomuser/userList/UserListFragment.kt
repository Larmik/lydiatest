package com.lydiatest.randomuser.userList

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.lydiatest.randomuser.R
import com.lydiatest.randomuser.databinding.FragmentUserListBinding
import com.lydiatest.randomuser.extensions.onPagination
import com.lydiatest.randomuser.extensions.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class UserListFragment : Fragment(R.layout.fragment_user_list) {

    private val binding: FragmentUserListBinding by viewBinding()
    private val viewModel: UserListViewModel by viewModels()
    lateinit var adapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = UserListAdapter(mutableListOf())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userRecycler.adapter = adapter
        viewModel.bind(binding.userRecycler.onPagination(), adapter.onItemClick)
        viewModel.sharedUsers.onEach { adapter.addData(it) }.launchIn(lifecycleScope)
        viewModel.sharedError.onEach { binding.root.snackbar(it) }.launchIn(lifecycleScope)
        viewModel.sharedLoading.onEach { binding.progress.isVisible = it }.launchIn(lifecycleScope)
        viewModel.sharedGoToInfo
            .filter { findNavController().currentDestination?.id == R.id.userlistFragment }
            .onEach { findNavController().navigate(UserListFragmentDirections.goToUserInfo(it)) }
            .launchIn(lifecycleScope)
    }

}