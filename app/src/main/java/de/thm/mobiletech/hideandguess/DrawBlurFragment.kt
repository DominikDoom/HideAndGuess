package de.thm.mobiletech.hideandguess

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import de.thm.mobiletech.hideandguess.databinding.FragmentDrawBlurBinding
import de.thm.mobiletech.hideandguess.util.DatabindingFragment

/**
 * A simple [Fragment] subclass using data binding.
 */
class DrawBlurFragment : DatabindingFragment<FragmentDrawBlurBinding>(R.layout.fragment_draw_blur) {

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
        username.set(args.username)
    }

    override fun setBindingContext() {
        binding.context = this
    }

    fun resetButtonClicked() {
        binding.blurDrawView.reset()
    }
}