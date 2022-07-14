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
    private var lastQuery: String? = null

    private val args: ImageSelectionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        savedInstanceState?.let { bundle ->
            lastQuery = bundle.getString("lastQuery")
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load images from pexels API
        Log.d("ImageSelectionFragment", "Loading images from pexels API")
        loadImages()

        object : CountDownTimer(30000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                Log.d("ImageSelectionFragment", "onTick: $millisUntilFinished")
            }

            override fun onFinish() {
                Log.d("ImageSelectionFragment", "choosing random image")
            }
        }.start()
    }

    override fun setBindingContext() {
        binding.context = this
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("lastQuery", lastQuery)
    }

    private fun loadImages() {
        lifecycleScope.launch {
            (requireActivity() as MainActivity).showProgressDialog()

            val defer = async { RestClient.getImageOptions() }
            when (val result = defer.await()) {
                is Result.Success -> {
                    val imgMapType: Type = object : TypeToken<Map<String, List<String>>>() {}.type
                    imageOptions = Gson().fromJson(result.data.toString(), imgMapType)
                }

                else -> {
                    requireActivity().showError(
                        "LobbyFragment",
                        "Something went wrong while getting the lobby info"
                    )
                }
            }

            Log.d("ImageSelectionFragment", "imageOptions: $imageOptions")

            val imageUrls = imageOptions.keys.toList()
            Log.d("ImageSelectionFragment", "imageOptions: $imageUrls")

            val context = this@ImageSelectionFragment
            Glide.with(context).load(imageUrls[0]).into(binding.firstImg)
            Glide.with(context).load(imageUrls[1]).into(binding.secondImg)
            Glide.with(context).load(imageUrls[2]).into(binding.thirdImg)

            (requireActivity() as MainActivity).hideProgressDialog()
        }
    }

    fun selectImage(index: Int) {
        val imageUrls = imageOptions.keys.toList()

        val selectedUrl: String = when (index) {
            0 -> imageUrls[0]
            1 -> imageUrls[1]
            2 -> imageUrls[2]
            else -> throw IllegalArgumentException("Index must be 0, 1 or 2")
        }

        Log.d("ImageSelectionFragment", "selectedUrl: $selectedUrl")

        submitPaintingChoice(selectedUrl)
    }

    private fun submitPaintingChoice(selectedUrl: String) {
        lifecycleScope.launch {
            val defer = async { RestClient.submitPaintingChoice(selectedUrl, args.lobbyId) }
            when (defer.await()) {
                is Result.Success -> {
                    val action =
                        ImageSelectionFragmentDirections.actionImageSelectionFragmentToDrawBlurFragment(selectedUrl)
                    navController.navigate(action)
                }
                else -> {
                    requireActivity().showError(
                        "LobbyFragment",
                        "Something went wrong while getting the lobby info"
                    )
                }
            }
        }
    }
}