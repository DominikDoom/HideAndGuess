package de.thm.mobiletech.hideandguess

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.thm.mobiletech.hideandguess.databinding.FragmentImageSelectionBinding
import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result
import de.thm.mobiletech.hideandguess.rest.services.getImageOptions
import de.thm.mobiletech.hideandguess.rest.services.submitPaintingChoice
import de.thm.mobiletech.hideandguess.util.DataBindingFragment
import de.thm.mobiletech.hideandguess.util.hideProgressDialog
import de.thm.mobiletech.hideandguess.util.showError
import de.thm.mobiletech.hideandguess.util.showProgressDialog
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.reflect.Type

/**
 * A simple [Fragment] subclass.
 */
class ImageSelectionFragment :
    DataBindingFragment<FragmentImageSelectionBinding>(R.layout.fragment_image_selection) {

    private var imageOptions: Map<String, List<String>> = mutableMapOf()
    private var lastImages: Array<String>? = null

    private val args: ImageSelectionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        savedInstanceState?.let { bundle ->
            lastImages = bundle.getStringArray("lastImages")
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load images from pexels API
        Log.d("ImageSelectionFragment", "Loading images from pexels API")
        loadImages()
    }

    override fun setBindingContext() {
        binding.context = this
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //outState.putStringArray("lastImages", lastImages)
    }

    private fun loadImages() {
        lifecycleScope.launch {
            (requireActivity() as MainActivity).showProgressDialog()

            var imageUrls: List<String> = listOf()

            if (lastImages == null) {
                val defer = async { RestClient.getImageOptions() }
                when (val result = defer.await()) {
                    is Result.Success -> {
                        val imgMapType: Type =
                            object : TypeToken<Map<String, List<String>>>() {}.type
                        imageOptions = Gson().fromJson(result.data.toString(), imgMapType)

                        imageUrls = imageOptions.keys.toList()
                        val firstSynonym = imageOptions[imageOptions.keys.first()]!![1]
                        requireActivity().title = "Bitte Bild für \"${firstSynonym}\" auswählen"
                    }

                    else -> {
                        requireActivity().showError(
                            "LobbyFragment",
                            "Something went wrong while getting the lobby info"
                        )
                    }
                }
            } else {
                imageUrls = lastImages!!.toList();
            }

            Log.d("ImageSelectionFragment", "imageOptions: $imageOptions")

            lastImages = imageUrls.toTypedArray()

            Log.d("ImageSelectionFragment", "imageOptions: $imageUrls")

            val context = this@ImageSelectionFragment
            Glide.with(context).load(imageUrls[0]).into(binding.firstImg)
            Glide.with(context).load(imageUrls[1]).into(binding.secondImg)
            Glide.with(context).load(imageUrls[2]).into(binding.thirdImg)

            (requireActivity() as MainActivity).hideProgressDialog()
        }
    }

    override fun onPause() {
        requireActivity().title = "HideAndGuess"
        super.onPause()
    }

    fun selectImage(index: Int) {
        //val imageUrls = imageOptions.keys.toList()

        val selectedUrl: String = when (index) {
            0 -> lastImages!![0]
            1 -> lastImages!![1]
            2 -> lastImages!![2]
            else -> throw IllegalArgumentException("Index must be 0, 1 or 2")
        }

        Log.d("ImageSelectionFragment", "selectedUrl: $selectedUrl")

        submitPaintingChoice(selectedUrl)
    }

    private fun submitPaintingChoice(selectedUrl: String) {
        val selectedQuery = imageOptions[selectedUrl]!!.first()

        lifecycleScope.launch {
            val defer = async { RestClient.submitPaintingChoice(selectedQuery, args.lobbyId) }
            when (val result = defer.await()) {
                is Result.HttpCode -> {
                    when (result.code) {
                        200 -> {
                            val firstSynonym = imageOptions[selectedUrl]!![1]
                            val action =
                                ImageSelectionFragmentDirections.actionImageSelectionFragmentToDrawBlurFragment(selectedUrl, args.lobbyId, firstSynonym)
                            navController.navigate(action)
                        }
                        else -> {
                            requireActivity().showError(
                                "ImageSelectionFragment",
                                "Something went wrong while submitting the choice"
                            )
                        }
                    }
                }
                else -> {
                    requireActivity().showError(
                        "LobbyFragment",
                        "Something went wrong while submitting the choice"
                    )
                }
            }
        }
    }
}