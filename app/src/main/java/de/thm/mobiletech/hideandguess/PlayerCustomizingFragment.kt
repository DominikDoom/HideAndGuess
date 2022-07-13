package de.thm.mobiletech.hideandguess

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import de.thm.mobiletech.hideandguess.databinding.FragmentPlayerCustomizingBinding
import de.thm.mobiletech.hideandguess.util.DataBindingFragment
import java.util.*


class PlayerCustomizingFragment : DataBindingFragment<FragmentPlayerCustomizingBinding>(R.layout.fragment_player_customizing) {

    private var clothes: ArrayList<Int> = ArrayList<Int>(Arrays.asList(R.drawable.clothes_1, R.drawable.clothes_2, R.drawable.clothes_3))
    private var hair: ArrayList<Int> = ArrayList<Int>(Arrays.asList(R.drawable.hair_1, R.drawable.hair_2, R.drawable.hair_3, R.drawable.hair_4, R.drawable.hair_5, R.drawable.hair_6))
    private var faces: ArrayList<Int> = ArrayList<Int>(Arrays.asList(R.drawable.face_1, R.drawable.face_2, R.drawable.face_3, R.drawable.face_4,  R.drawable.face_5))
    private var clothesImagesCursor: Int = 0
    private var facesImagesCursor: Int = 0
    private var hairImagesCursor: Int = 0

    override fun setBindingContext() {
        binding.context = this
    }

    fun forwardFaces() {
        if (facesImagesCursor < faces.size - 1)
            facesImagesCursor++
        else
            facesImagesCursor = 0
        drawPlayerImage()
    }

    fun backwardFaces() {
        if (facesImagesCursor > 0)
            facesImagesCursor--
        else
            facesImagesCursor = faces.size - 1
        drawPlayerImage()
    }

    fun forwardClothes() {
        if (clothesImagesCursor < clothes.size - 1)
            clothesImagesCursor++
        else
            clothesImagesCursor = 0
        drawPlayerImage()
    }

    fun backwardClothes() {
        if (clothesImagesCursor > 0)
            clothesImagesCursor--
        else
            clothesImagesCursor = clothes.size - 1
        drawPlayerImage()
    }

    fun forwardHair() {
        if (hairImagesCursor < hair.size - 1)
            hairImagesCursor++
        else
            hairImagesCursor = 0
        drawPlayerImage()
    }

    fun backwardHair() {
        if (hairImagesCursor > 0)
            hairImagesCursor--
        else
            hairImagesCursor = hair.size - 1
        drawPlayerImage()
    }

    fun drawPlayerImage() {
        val bm_clothes = BitmapFactory.decodeResource(resources, clothes[clothesImagesCursor])
        val bm_face = BitmapFactory.decodeResource(resources, faces[facesImagesCursor])
        val bm_hair = BitmapFactory.decodeResource(resources, hair[hairImagesCursor])
        val bmOverlay = Bitmap.createBitmap(256, 256, bm_clothes.config)
        val canvas = Canvas(bmOverlay)
        val rect = Rect(0, 0, 256, 256)
        canvas.drawBitmap(bm_face, rect, rect, null)
        canvas.drawBitmap(bm_clothes, rect, rect, null)
        canvas.drawBitmap(bm_hair, rect, rect, null)

        binding.imageCustomizingPlayer.setImageBitmap(bmOverlay)
    }

}