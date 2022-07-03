package de.thm.mobiletech.hideandguess.pexels

import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.random.Random

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
                        // Get request limit information from headers
                        val remaining = getHeaderField("X-Ratelimit-Remaining")
                        val reset = getHeaderField("X-Ratelimit-Reset")
                        val dateReadable = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            .withZone(ZoneId.systemDefault())
                            .format(Instant.ofEpochSecond(reset.toLong()))
                        // Log request limit information
                        Log.d("PexelsApi", "Request successful")
                        Log.d("PexelsApi", "Remaining: $remaining, Resets at $dateReadable")

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

    private suspend fun search(query: String, perPage: Int, page: Int = 1): PexelsResult {
        val uriString = Uri.parse(url)
            .buildUpon()
            .appendQueryParameter("query", query)
            .appendQueryParameter("per_page", perPage.toString())
            .appendQueryParameter("page", page.toString())
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

    suspend fun searchRandomPage(query: String, perPage: Int): PexelsResult {
        // Get first page with total results/pages information
        val firstResult = search(query, perPage)
        val totalResults = firstResult.totalResults
        val totalPages = totalResults / perPage

        // Check for incomplete last page
        val lastPage = if (totalResults % perPage == 0) totalPages else totalPages - 1

        val randomPage = (1..lastPage).random(Random(System.currentTimeMillis()))

        return if (randomPage == 1) {
            firstResult
        } else {
            search(query, perPage, randomPage)
        }
    }
}