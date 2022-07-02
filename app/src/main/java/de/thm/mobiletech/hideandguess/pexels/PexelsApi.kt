package de.thm.mobiletech.hideandguess.pexels

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class PexelsApi(private val secret: String) {

    private val url : String = "https://api.pexels.com/v1/search?locale=de-DE&size=medium"

    // This warning is a bug in the current version of Android Studio. withContext assures that coroutines are used.
    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun getResponse(uriString: String) : Result<String> {
        return withContext(Dispatchers.IO) {
            val url = URL(uriString)
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                setRequestProperty("Authorization", secret)

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
                    429 -> return@withContext Result.Error(PexelsApiException("Rate limit exceeded"))
                    else -> return@withContext Result.Error(PexelsApiException("Unexpected response code: $responseCode"))
                }
            }
        }
    }

    suspend fun search(query: String, perPage: Int) : PexelsResult {
        val uriString = Uri.parse(url)
            .buildUpon()
            .appendQueryParameter("query", query)
            .appendQueryParameter("per_page", perPage.toString())
            .build().toString()
        val result = try {
            getResponse(uriString)
        } catch(e: Exception) {
            Result.Error(e)
        }

        return when (result) {
            is Result.Success -> PexelsResult.fromJson(result.data)
            else -> throw (result as Result.Error).exception
        }
    }
}