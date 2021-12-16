package com.lydiatest.randomuser.userList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lydiatest.randomuser.databinding.CellUserBinding
import com.lydiatest.randomuser.extensions.clicks
import com.lydiatest.randomuser.model.User
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class UserListAdapter(private val list: MutableList<User>) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>(), CoroutineScope {

    private val _onItemClick = MutableSharedFlow<User>()
    val onItemClick = _onItemClick.asSharedFlow()

    class UserViewHolder(val binding: CellUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.userName.text = String.format("%s %s %s", user.name.title, user.name.first, user.name.last)
            binding.userEmail.text = user.email
            Picasso.get().load(user.picture.thumbnail).into(binding.userThumb)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserViewHolder(CellUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = list[position]
        holder.bind(user)
        holder.binding.root.clicks().onEach { _onItemClick.emit(user) }.launchIn(this)
    }

    override fun getItemCount() = list.size

    fun addData(data: List<User>) {
        val oldCount = list.size
        list.addAll(data)
        notifyItemRangeInserted(oldCount, list.size)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

}