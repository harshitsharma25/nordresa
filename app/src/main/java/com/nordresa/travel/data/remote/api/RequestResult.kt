package com.nordresa.travel.data.remote.api

import retrofit2.HttpException
import java.io.IOException

sealed class RequestResult<out R> {
    data class Success<out T>(val data: T) : RequestResult<T>()
    data class Error(val e: Exception) : RequestResult<Nothing>()
    data class ApiError(val message:String, val code:Int): RequestResult<Nothing>()
    data class NoNetworkError(val e: Exception): RequestResult<Nothing>()
}


suspend fun <R: Any> safeApiCall(apiCall: suspend () -> R): RequestResult<R> {
    return try{
        RequestResult.Success(apiCall.invoke())
    } catch (e: Exception){
        if(e is HttpException){
            RequestResult.ApiError(e.message(), e.code())
        } else if (e is NoNetworkException){
            RequestResult.NoNetworkError(e)
        } else {
            RequestResult.Error(e)
        }
    }
}

suspend fun <T: Any> RequestResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): RequestResult<T> = apply {
    if (this is RequestResult.Success<T>) {
        executable(data)
    }
}

suspend fun <T : Any> RequestResult<T>.onApiError(
    executable: suspend (code: Int, message: String?) -> Unit
): RequestResult<T> = apply {
    if (this is RequestResult.ApiError) {
        executable(code, message)
    }
}

suspend fun <T : Any> RequestResult<T>.onException(
    executable: suspend (e: Throwable) -> Unit
): RequestResult<T> = apply {
    if (this is RequestResult.Error) {
        executable(e)
    } else if (this is RequestResult.NoNetworkError){
        executable(e)
    }
}

class NoNetworkException(message:String): IOException(message)