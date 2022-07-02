package de.thm.mobiletech.hideandguess.pexels

import org.json.JSONObject
import kotlin.properties.Delegates

class PexelsResult {

    var totalResults by Delegates.notNull<Int>()
    var page by Delegates.notNull<Int>()
    var perPage by Delegates.notNull<Int>()

    val images: ArrayList<PexelsImage> = ArrayList()

    companion object {
        /**
         * Maps the given JSON string to a PexelsResult object.
         * This object contains request metadata and a list of PexelsImage objects.
         * @param json The JSON string to map.
         * @return A PexelsResult object containing metadata and a list of [PexelsImage] objects for the current page.
         * */
        fun fromJson(json: String): PexelsResult {
            val jObj = JSONObject(json)

            val result = PexelsResult().apply {
                totalResults = jObj.getInt("total_results")
                page = jObj.getInt("page")
                perPage = jObj.getInt("per_page")

                val imageArray = jObj.getJSONArray("photos")
                for (i in 0 until imageArray.length()) {
                    val imageJson = imageArray.getJSONObject(i)
                    val sourceSetJson = imageJson.getJSONObject("src")

                    val image = PexelsImage(
                        width = imageJson.getInt("width"),
                        height = imageJson.getInt("height"),
                        photographer = imageJson.getString("photographer"),
                        photographerUrl = imageJson.getString("photographer_url"),
                        avgColor = imageJson.getString("avg_color"),
                        description = imageJson.getString("alt"),

                        sources = PexelsImage.SourceSet(
                            original = sourceSetJson.getString("original"),
                            large2x = sourceSetJson.getString("large2x"),
                            large = sourceSetJson.getString("large"),
                            medium = sourceSetJson.getString("medium"),
                            small = sourceSetJson.getString("small"),
                            tiny = sourceSetJson.getString("tiny")
                        )
                    )
                    images.add(image)
                }
            }

            return result
        }
    }
}
