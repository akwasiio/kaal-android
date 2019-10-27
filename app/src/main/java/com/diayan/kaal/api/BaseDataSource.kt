package com.diayan.kaal.api

import android.util.Log
import com.diayan.kaal.data.Result
import com.google.gson.Gson
import retrofit2.Response
import timber.log.Timber

abstract class BaseDataSource {
    /**
     * Abstract Base Data source class with error handling
     */
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Result.success(body)

                Log.d("events response ", Gson().toJson(body) )

            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Result<T> {
        Timber.e(message)
        return Result.error("Network call has failed for a following reason: $message")
    }
}