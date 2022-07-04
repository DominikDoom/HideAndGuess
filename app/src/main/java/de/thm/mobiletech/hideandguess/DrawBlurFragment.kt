package de.thm.mobiletech.hideandguess

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import de.thm.mobiletech.hideandguess.databinding.FragmentDrawBlurBinding
import de.thm.mobiletech.hideandguess.util.GlideCallback
import de.thm.mobiletech.hideandguess.util.DataBindingFragment

/**
 * A simple [Fragment] subclass using data binding.
 */
class DrawBlurFragment : DataBindingFragment<FragmentDrawBlurBinding>(R.layout.fragment_draw_blur) {

    private val args: DrawBlurFragmentArgs by navArgs() // Get SafeArgs from Navigation
    val username : ObservableField<String> = ObservableField()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    override fun setBindingContext() {
        binding.context = this
    }

    fun resetButtonClicked() {
        binding.blurDrawView.reset()
    }
}