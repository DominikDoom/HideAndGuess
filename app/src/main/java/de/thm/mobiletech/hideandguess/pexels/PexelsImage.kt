package de.thm.mobiletech.hideandguess.pexels

data class PexelsImage(
    val width: Int,
    val height: Int,
    val photographer: String,
    val photographerUrl: String,
    val avgColor: String,
    val description: String,
    val sources: SourceSet
) {
    data class SourceSet(
        val original: String, // Original size
        val large2x: String, // 940 x 650 @2x
        val large: String, // 940 x 650 @1x
        val medium: String, // Height 350
        val small: String, // Height 130
        val tiny: String // 280 x 200
    )
}
