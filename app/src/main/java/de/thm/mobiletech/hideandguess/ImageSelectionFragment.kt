package de.thm.mobiletech.hideandguess

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import de.thm.mobiletech.hideandguess.databinding.FragmentImageSelectionBinding
import de.thm.mobiletech.hideandguess.pexels.PexelsApi
import de.thm.mobiletech.hideandguess.pexels.PexelsResult
import de.thm.mobiletech.hideandguess.util.DataBindingFragment
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * A simple [Fragment] subclass.
 */
class ImageSelectionFragment : DataBindingFragment<FragmentImageSelectionBinding>(R.layout.fragment_image_selection) {

    private var pexelsResult: PexelsResult? = null
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
        loadImages()
    }

    override fun setBindingContext() {
        binding.context = this
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("lastQuery", lastQuery)
    }

    private fun loadImages() {
        val api = PexelsApi(BuildConfig.PexelsApiKey)

        val queries = resources.getStringArray(R.array.searchQueries)

        if (lastQuery == null)
            lastQuery = queries.random(Random(System.currentTimeMillis()))

        lifecycleScope.launch {
            // To break up sequential images, we request more per page than we need
            val defer = async { api.searchRandomPage(lastQuery!!, 15) }

            pexelsResult = defer.await()
            // Shuffle the result list to select random elements from the page
            pexelsResult!!.images.shuffle(Random(System.currentTimeMillis()))
            val images = pexelsResult!!.images.map { it.sources.large }

            val context = this@ImageSelectionFragment
            Glide.with(context).load(images[0]).into(binding.firstImg)
            Glide.with(context).load(images[1]).into(binding.secondImg)
            Glide.with(context).load(images[2]).into(binding.thirdImg)
        }
    }

    fun selectImage(index: Int) {
        val images = pexelsResult!!.images.map { it.sources.large2x }
        val selectedUrl: String = when (index) {
            0 -> images[0]
            1 -> images[1]
            2 -> images[2]
            else -> throw IllegalArgumentException("Index must be 0, 1 or 2")
        }

        val action = ImageSelectionFragmentDirections.actionOpenDrawBlurFragment(selectedUrl)
        navController.navigate(action)
    }
}