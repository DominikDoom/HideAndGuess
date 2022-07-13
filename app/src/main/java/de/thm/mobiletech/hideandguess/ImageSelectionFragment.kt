package de.thm.mobiletech.hideandguess

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import de.thm.mobiletech.hideandguess.databinding.FragmentImageSelectionBinding
import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.services.getImageOptions
import de.thm.mobiletech.hideandguess.util.DataBindingFragment
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class ImageSelectionFragment : DataBindingFragment<FragmentImageSelectionBinding>(R.layout.fragment_image_selection) {

    private val imageOptions: Map<String, List<String>> = mutableMapOf()
    private var lastQuery: String? = null

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
        // TODO loadImages()
    }

    override fun setBindingContext() {
        binding.context = this
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("lastQuery", lastQuery)
    }

    private fun loadImages() {
        // TODO call our rest backend request for the images

        lifecycleScope.launch {
            val defer = async { RestClient.getImageOptions() }

            val result = defer.await()
            // TODO Parse JSON result to the Map<Query, Urls>
            //imageOptions.plus()

            val imageUrls = imageOptions.entries.flatMap { it.value }
            val context = this@ImageSelectionFragment
            Glide.with(context).load(imageUrls[0]).into(binding.firstImg)
            Glide.with(context).load(imageUrls[1]).into(binding.secondImg)
            Glide.with(context).load(imageUrls[2]).into(binding.thirdImg)
        }
    }

    fun selectImage(index: Int) {
        val imageUrls = imageOptions.entries.flatMap { it.value }

        val selectedUrl: String = when (index) {
            0 -> imageUrls[0]
            1 -> imageUrls[1]
            2 -> imageUrls[2]
            else -> throw IllegalArgumentException("Index must be 0, 1 or 2")
        }

        val action = ImageSelectionFragmentDirections.actionImageSelectionFragmentToDrawBlurFragment(selectedUrl)
        navController.navigate(action)
    }
}