package de.thm.mobiletech.hideandguess.rest

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data class HttpCode(val code: Int) : Result<Int>()
}