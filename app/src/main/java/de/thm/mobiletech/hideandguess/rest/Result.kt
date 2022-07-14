package de.thm.mobiletech.hideandguess.rest

import de.thm.mobiletech.hideandguess.rest.services.Statistic

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data class HttpCode(val code: Int) : Result<Int>()
    data class Statistics(val type: Statistic, val value: Int) : Result<Statistics>()
}