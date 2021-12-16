package com.lydiatest.randomuser.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Long.parseToDate(): String  = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date(this))
