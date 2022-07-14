package de.thm.mobiletech.hideandguess

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import androidx.databinding.ObservableField
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import de.thm.mobiletech.hideandguess.databinding.FragmentPlayerCustomizingBinding
import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result
import de.thm.mobiletech.hideandguess.rest.services.Avatar
import de.thm.mobiletech.hideandguess.rest.services.createLobby
import de.thm.mobiletech.hideandguess.rest.services.postAvatar
import de.thm.mobiletech.hideandguess.util.DataBindingFragment
import de.thm.mobiletech.hideandguess.util.showError
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class PlayerCustomizingFragment : DataBindingFragment<FragmentPlayerCustomizingBinding>(R.layout.fragment_player_customizing) {

    private val args: PlayerCustomizingFragmentArgs by navArgs()
    val user : ObservableField<User> = ObservableField()
    private var clothes: ArrayList<Int> = ArrayList<Int>(listOf(R.drawable.clothes_1, R.drawable.clothes_2, R.drawable.clothes_3))
    private var hair: ArrayList<Int> = ArrayList<Int>(listOf(R.drawable.hair_1, R.drawable.hair_2, R.drawable.hair_3, R.drawable.hair_4, R.drawable.hair_5, R.drawable.hair_6))
    private var faces: ArrayList<Int> = ArrayList<Int>(listOf(R.drawable.face_1, R.drawable.face_2, R.drawable.face_3, R.drawable.face_4,  R.drawable.face_5))
    private var clothesImagesCursor: Int = 0
    private var facesImagesCursor: Int = 0
    private var hairImagesCursor: Int = 0

    override fun setBindingContext() {
        binding.context = this
        user.set(args.user)
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
        val bmClothes = BitmapFactory.decodeResource(resources, clothes[clothesImagesCursor])
        val bmFace = BitmapFactory.decodeResource(resources, faces[facesImagesCursor])
        val bmHair = BitmapFactory.decodeResource(resources, hair[hairImagesCursor])
        val bmOverlay = Bitmap.createBitmap(256, 256, bmClothes.config)
        val canvas = Canvas(bmOverlay)
        val rect = Rect(0, 0, 256, 256)
        canvas.drawBitmap(bmFace, rect, rect, null)
        canvas.drawBitmap(bmClothes, rect, rect, null)
        canvas.drawBitmap(bmHair, rect, rect, null)

        binding.imageCustomizingPlayer.setImageBitmap(bmOverlay)
    }

    fun save() {
        lifecycleScope.launch {
            val defer = async { RestClient.postAvatar(Avatar(facesImagesCursor, clothesImagesCursor, hairImagesCursor)) }

            when (val result = defer.await()) {
                is Result.HttpCode -> {
                    when(result.code) {
                        200 -> {
                            val action = PlayerCustomizingFragmentDirections.actionPlayerCustomizingFragmentToUserDetailFragment(args.user)
                            navController.navigate(action)
                        }
                        500 -> requireActivity().showError(MainMenuFragment.TAG, "Internal Server Error")
                        else -> requireActivity().showError(MainMenuFragment.TAG, "Unknown Error")
                    }
                }
                is Result.Error -> {
                    requireActivity().showError(MainMenuFragment.TAG,"Saving player avatar failed", result.exception)
                }
                else -> {
                    requireActivity().showError(MainMenuFragment.TAG,"Saving player avatar failed due to unknown reason")
                }
            }
        }


    }

}