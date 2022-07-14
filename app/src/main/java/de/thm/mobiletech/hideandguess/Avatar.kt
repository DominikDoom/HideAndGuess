package de.thm.mobiletech.hideandguess

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.widget.ImageView

class Avatar {

    var indexHair: Int
    var indexFace: Int
    var indexClothes: Int

    constructor(indexHair: Int, indexFace: Int, indexClothes: Int,) {
        this.indexHair = indexHair
        this.indexFace = indexFace
        this.indexClothes = indexClothes
    }

    companion object {
        var clothes: ArrayList<Int> = ArrayList<Int>(listOf(R.drawable.clothes_1, R.drawable.clothes_2, R.drawable.clothes_3, R.drawable.clothes_4, R.drawable.clothes_5, R.drawable.clothes_6))
        var hair: ArrayList<Int> = ArrayList<Int>(listOf(R.drawable.hair_1, R.drawable.hair_2, R.drawable.hair_3, R.drawable.hair_4, R.drawable.hair_5, R.drawable.hair_6))
        var faces: ArrayList<Int> = ArrayList<Int>(listOf(R.drawable.face_1, R.drawable.face_2, R.drawable.face_3, R.drawable.face_4,  R.drawable.face_5))

        fun drawPlayerImage(imageView: ImageView, resources: Resources, indexHair: Int, indexFace: Int, indexClothes: Int) {
            val bmClothes = BitmapFactory.decodeResource(resources, clothes[indexClothes])
            val bmFace = BitmapFactory.decodeResource(resources, faces[indexFace])
            val bmHair = BitmapFactory.decodeResource(resources, hair[indexHair])
            val bmOverlay = Bitmap.createBitmap(bmClothes.width, bmClothes.height, bmClothes.config)
            val canvas = Canvas(bmOverlay)
            val rect = Rect(0, 0, bmClothes.width, bmClothes.height)
            canvas.drawBitmap(bmFace, rect, rect, null)
            canvas.drawBitmap(bmClothes, rect, rect, null)
            canvas.drawBitmap(bmHair, rect, rect, null)

            imageView.setImageBitmap(bmOverlay)
        }
    }
}