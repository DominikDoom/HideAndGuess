package de.thm.mobiletech.hideandguess.rest

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

// This warning is a bug in the current version of Android Studio. withContext assures that coroutines are used.
@Suppress("BlockingMethodInNonBlockingContext")
object RestClient {

    // private const val URL = "http://192.168.178.42:8080/"
    private const val URL = "http://raspberrypi.tq2o4aj1y6ubht9d.myfritz.net:8080/"

    private lateinit var auth: UserAuth

    fun setAuth(auth: UserAuth) {
        this.auth = auth
    }

    suspend fun getRequest(endpoint: String): Result<String> {
        return withContext(Dispatchers.IO) {
            val url = URL(URL + endpoint)
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                setRequestProperty("Authorization", auth.authToken)

                connect()
                when (responseCode) {
                    200 -> {
                        inputStream.bufferedReader().use {
                            val response = StringBuffer()
                            it.lines().forEach { line ->
                                response.append(line)
                            }
                            return@withContext Result.Success(response.toString())
                        }
                    }
                    500 -> return@withContext Result.Error(Exception("Internal server error"))
                    else -> return@withContext Result.Error(Exception("Unexpected response code: $responseCode"))
                }
            }
        }
    }

    suspend fun postRequest(endpoint: String, body: String? = null): Result<Int> {
        return withContext(Dispatchers.IO) {
            val url = URL(URL + endpoint)
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "POST"
                setRequestProperty("Authorization", auth.authToken)
                doOutput = !body.isNullOrEmpty()

                if (doOutput) {
                    setRequestProperty("Content-Type", "application/json")
                    outputStream.bufferedWriter().use {
                        it.write(body)
                    }
                }

                connect()
                when (responseCode) {
                    200, 401, 403, 404, 409 -> return@withContext Result.HttpCode(responseCode)
                    else -> return@withContext Result.Error(Exception("Unexpected response code: $responseCode"))
                }
            }
        }
    }

    suspend fun postRequestWithReturn(endpoint: String, body : String? = null) : Result<String?> {
        return withContext(Dispatchers.IO) {
            val url = URL(URL + endpoint)
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "POST"
                setRequestProperty("Authorization", auth.authToken)
                doOutput = !body.isNullOrEmpty()

                if (doOutput) {
                    setRequestProperty("Content-Type", "application/json")
                    outputStream.bufferedWriter().use {
                        it.write(body!!.escape())
                    }
                }

                connect()
                when (responseCode) {
                    200 -> return@withContext Result.Success(inputStream.bufferedReader().use {
                        it.readText()
                    })
                    else -> return@withContext Result.Error(Exception("Unexpected response code: $responseCode"))
                }
            }
        }
    }

    suspend fun putRequestWithReturn(endpoint: String, body : String? = null) : Result<String?> {
        return withContext(Dispatchers.IO) {
            val url = URL(URL + endpoint)
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "PUT"
                setRequestProperty("Authorization", auth.authToken)
                doOutput = !body.isNullOrEmpty()

                if (doOutput) {
                    setRequestProperty("Content-Type", "application/json")
                    outputStream.bufferedWriter().use {
                        it.write(body!!.escape())
                    }
                }

                connect()
                when (responseCode) {
                    200 -> return@withContext Result.Success(inputStream.bufferedReader().use {
                        it.readText()
                    })
                    403, 500 -> return@withContext Result.Error(Exception("Error: $responseCode"))
                    else -> return@withContext Result.Error(Exception("Unexpected response code: $responseCode"))
                }
            }
        }
    }

    suspend fun putRequest(endpoint: String, body: String? = null): Result<Int> {
        return withContext(Dispatchers.IO) {
            val url = URL(URL + endpoint)
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "PUT"

                if (endpoint != "register")
                    setRequestProperty("Authorization", auth.authToken)

                doOutput = !body.isNullOrEmpty()

                if (doOutput) {
                    addRequestProperty("Content-Type", "application/json")
                    outputStream.bufferedWriter().use {
                        Log.d("RestClient", body!!)
                        it.write(body)
                    }
                }

                connect()
                when (responseCode) {
                    200, 201, 400, 403, 409, 500 -> return@withContext Result.HttpCode(responseCode)
                    else -> return@withContext Result.Error(Exception("Unexpected response code: $responseCode"))
                }
            }
        }
    }
}