package de.thm.mobiletech.hideandguess

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import de.thm.mobiletech.hideandguess.databinding.FragmentDrawBlurBinding
import de.thm.mobiletech.hideandguess.util.GlideCallback

/**
 * A simple [Fragment] subclass using data binding.
 */
class DrawBlurFragment : Fragment() {

    private lateinit var binding: FragmentDrawBlurBinding
    private lateinit var navController: NavController
    private val args: DrawBlurFragmentArgs by navArgs() // Get SafeArgs from Navigation

    val username : ObservableField<String> = ObservableField()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using data binding
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_draw_blur, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.context = this // Set binding variable used in layout
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find and assign navController
        val navHostFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController

        // Add listener for multiplier display
        binding.blurDrawView.currentMultiplier.observe(viewLifecycleOwner) {
            val formattedMult = String.format("%.2f", it)
            binding.multiplierLabel.text = "Current multiplier: $formattedMult"
        }

        // Update username with args
        //username.set(args.username)

        // Load image from args
        Glide.with(this)
            .load(args.imageUrl)
            .listener(GlideCallback {
                binding.blurDrawView.setImageDrawable(it) // We need to set it ourselves to ensure it is ready
                binding.blurDrawView.initialize()
            })
            .into(binding.blurDrawView)

    }

    fun resetButtonClicked() {
        binding.blurDrawView.reset()
    }
}