package com.lydiatest.randomuser.userInfo

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.lydiatest.randomuser.R
import com.lydiatest.randomuser.databinding.FragmentUserInfoBinding
import com.lydiatest.randomuser.extensions.clicks
import com.lydiatest.randomuser.extensions.parseToDate
import com.lydiatest.randomuser.model.User
import com.squareup.picasso.Picasso
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class UserInfoFragment : Fragment(R.layout.fragment_user_info) {

    private val binding: FragmentUserInfoBinding by viewBinding()
    private val viewModel: UserInfoViewModel by viewModels()

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = arguments?.get("user") as? User
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user?.let {
            Picasso.get().load(it.picture.large).into(binding.imageView)
            binding.headerUserName.text = String.format("%s %s %s", it.name.title, it.name.first, it.name.last)
            binding.firstnameTv.text = it.name.first
            binding.lastnameTv.text = it.name.last
            binding.genderTv.text = it.gender
            binding.birthdateTv.text = it.dob.parseToDate()
            binding.natTv.text = it.nat
            binding.addressTv.text = it.location.street
            binding.cityTv.text = String.format("%s - %s", it.location.city, it.location.state)
            binding.emailTv.text = it.email
            binding.phoneTv.text = it.phone
            binding.cellTv.text = it.cell
            binding.usernameTv.text = it.login.username
            binding.registerTv.text = it.registered.parseToDate()
            it.id.value?.let { value ->
                binding.idLabel.isVisible = true
                binding.idTv.isVisible = true
                binding.idTv.text = value
            }
        }
        viewModel.bind(onBack = binding.backBtn.clicks())
        viewModel.sharedBack
            .filter { findNavController().currentDestination?.id == R.id.userinfoFragment }
            .onEach { findNavController().popBackStack() }
            .launchIn(lifecycleScope)
    }
}