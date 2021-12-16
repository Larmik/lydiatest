package com.lydiatest.randomuser.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lydiatest.randomuser.model.User
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
interface PreferencesRepositoryInterface {
    var lastItemsLoaded: List<User>?
}

@FlowPreview
@ExperimentalCoroutinesApi
@Module
@InstallIn(ApplicationComponent::class)
interface PreferencesRepositoryModule {
    @Binds
    fun bindRepository(impl: PreferencesRepository): PreferencesRepositoryInterface
}

@FlowPreview
@ExperimentalCoroutinesApi
class PreferencesRepository @Inject constructor(
    @ApplicationContext var context: Context
) : PreferencesRepositoryInterface {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val type = object : TypeToken<List<User>>() {}.type

    override var lastItemsLoaded: List<User>?
        get() {
            val value = preferences.getString("lastItems", "")
            return if (!value.isNullOrEmpty()) Gson().fromJson(value, type) else null
        }
        set(value) {
            val lastJourney = Gson().toJson(value, type)
            preferences.edit().putString("lastItems", lastJourney).apply()
        }

}