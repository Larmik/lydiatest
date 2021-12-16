package com.lydiatest.randomuser.service

import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.lydiatest.randomuser.MainApplication
import org.json.JSONObject

const val RANDOM_USER_URL = "https://randomuser.me/api/1.0/?seed=lydia&results=10&page="

abstract class NetworkService {

    fun createRequest(page: Int, success: Response.Listener<JSONObject>, error: Response.ErrorListener) {
        val url = "$RANDOM_USER_URL$page"
        val jsObjRequest = JsonObjectRequest(url, success, error)
        jsObjRequest.setShouldCache(false)
        jsObjRequest.retryPolicy = DefaultRetryPolicy(
            15000,
            0,
            0f
        )
        MainApplication.mRequestQueue?.add(jsObjRequest)
    }
}