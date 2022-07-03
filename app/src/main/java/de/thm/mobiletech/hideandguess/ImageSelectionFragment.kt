package de.thm.mobiletech.hideandguess

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import de.thm.mobiletech.hideandguess.databinding.FragmentImageSelectionBinding
import de.thm.mobiletech.hideandguess.pexels.PexelsApi
import de.thm.mobiletech.hideandguess.pexels.PexelsResult
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class ImageSelectionFragment : Fragment() {

    private lateinit var binding: FragmentImageSelectionBinding
    private lateinit var navController: NavController

    private var pexelsResult: PexelsResult? = null

    private var lastQuery: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using data binding
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_image_selection, container, false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.context = this // Set binding variable used in layout

        savedInstanceState?.let { bundle ->
            lastQuery = bundle.getString("lastQuery")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find and assign navController
        val navHostFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController

        // Load images from pexels API
        loadImages()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("lastQuery", lastQuery)
    }

    private fun loadImages() {
        val api = PexelsApi(BuildConfig.PexelsApiKey)

        val queries = resources.getStringArray(R.array.searchQueries)

        if (lastQuery == null)
            lastQuery = queries.random()

        lifecycleScope.launch {
            val defer = async { api.searchRandomPage(lastQuery!!, 3) }

            pexelsResult = defer.await()
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