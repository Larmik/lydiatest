package com.lydiatest.randomuser.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
fun View.clicks(): Flow<Unit> = callbackFlow {
    this@clicks.setOnClickListener { safeOffer(Unit) }
    awaitClose { }
}

fun View.snackbar(message: String) = Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
