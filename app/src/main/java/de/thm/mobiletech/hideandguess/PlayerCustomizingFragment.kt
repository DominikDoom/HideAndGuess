package de.thm.mobiletech.hideandguess

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import de.thm.mobiletech.hideandguess.Avatar.Companion.drawPlayerImage
import de.thm.mobiletech.hideandguess.databinding.FragmentPlayerCustomizingBinding
import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result
import de.thm.mobiletech.hideandguess.rest.services.getAvatar
import de.thm.mobiletech.hideandguess.rest.services.postAvatar
import de.thm.mobiletech.hideandguess.util.DataBindingFragment
import de.thm.mobiletech.hideandguess.util.showError
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class PlayerCustomizingFragment : DataBindingFragment<FragmentPlayerCustomizingBinding>(R.layout.fragment_player_customizing) {

    private val args: PlayerCustomizingFragmentArgs by navArgs()
    val user : ObservableField<User> = ObservableField()
    private var clothesImagesCursor: Int = 0
    private var facesImagesCursor: Int = 0
    private var hairImagesCursor: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAvatar()
    }

    override fun setBindingContext() {
        binding.context = this
        user.set(args.user)
    }

    fun forwardFaces() {
        if (facesImagesCursor < Avatar.faces.size - 1)
            facesImagesCursor++
        else
            facesImagesCursor = 0
        drawPlayerImage()
    }

    fun backwardFaces() {
        if (facesImagesCursor > 0)
            facesImagesCursor--
        else
            facesImagesCursor = Avatar.faces.size - 1
        drawPlayerImage()
    }

    fun forwardClothes() {
        if (clothesImagesCursor < Avatar.clothes.size - 1)
            clothesImagesCursor++
        else
            clothesImagesCursor = 0
        drawPlayerImage()
    }

    fun backwardClothes() {
        if (clothesImagesCursor > 0)
            clothesImagesCursor--
        else
            clothesImagesCursor = Avatar.clothes.size - 1
        drawPlayerImage()
    }

    fun forwardHair() {
        if (hairImagesCursor < Avatar.hair.size - 1)
            hairImagesCursor++
        else
            hairImagesCursor = 0
        drawPlayerImage()
    }

    fun backwardHair() {
        if (hairImagesCursor > 0)
            hairImagesCursor--
        else
            hairImagesCursor = Avatar.hair.size - 1
        drawPlayerImage()
    }

    private fun drawPlayerImage() {
        drawPlayerImage(binding.imageCustomizingPlayer, resources, hairImagesCursor, clothesImagesCursor, facesImagesCursor)
    }

    fun save() {
        lifecycleScope.launch {
            val defer = async { RestClient.postAvatar(Avatar(hairImagesCursor, clothesImagesCursor, facesImagesCursor)) }

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

    private fun getAvatar() {
        lifecycleScope.launch {
            val defer = async { RestClient.getAvatar() }

            when (val result = defer.await()) {
                is Result.Success -> {
                    var avatar = Gson().fromJson(result.data, Avatar::class.java)
                    clothesImagesCursor = avatar.indexClothes
                    facesImagesCursor = avatar.indexFace
                    hairImagesCursor = avatar.indexHair
                    drawPlayerImage()
                }
                is Result.Error -> {
                    requireActivity().showError(MainMenuFragment.TAG,"Fetching Avatar failed", result.exception)
                }
                else -> {
                    requireActivity().showError(MainMenuFragment.TAG,"Fetching Avatar failed due to unknown reason")
                }
            }
        }
    }

}