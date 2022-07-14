package de.thm.mobiletech.hideandguess

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import de.thm.mobiletech.hideandguess.databinding.FragmentDrawBlurBinding
import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result
import de.thm.mobiletech.hideandguess.rest.services.getImageOptions
import de.thm.mobiletech.hideandguess.rest.services.submitPainting
import de.thm.mobiletech.hideandguess.rest.services.submitPaintingChoice
import de.thm.mobiletech.hideandguess.util.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass using data binding.
 */
class DrawBlurFragment : DataBindingFragment<FragmentDrawBlurBinding>(R.layout.fragment_draw_blur), TimerFinishedListener {

    private val args: DrawBlurFragmentArgs by navArgs() // Get SafeArgs from Navigation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).startTimer(30, this)

        // Add listener for multiplier display
        binding.blurDrawView.currentMultiplier.observe(viewLifecycleOwner) {
            val formattedMult = String.format("%.2f", it)
            binding.multiplierLabel.text = "Current multiplier: $formattedMult"
        }

        Glide.with(this)
            .load(args.chosenUrl)
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

    override fun onTimerFinished() {
        Log.d("DrawBlurFragment", "Timer finished")
        (requireActivity() as MainActivity).showProgressDialog()

        // submit the result
        val points = binding.blurDrawView.points


        lifecycleScope.launch {
            val defer = async { RestClient.submitPainting(args.lobbyId, points) }
            when (val result = defer.await()) {
                is Result.HttpCode -> {
                    when (result.code) {
                        200 -> {
                            val action =
                                DrawBlurFragmentDirections.actionDrawBlurFragmentToGuessFragment()
                            navController.navigate(action)
                        }
                        else -> {
                            requireActivity().showError(
                                "DrawBlurFragment",
                                "Something went wrong while submitting the painting"
                            )
                        }
                    }
                }
                else -> {
                    requireActivity().showError(
                        "DrawBlurFragment",
                        "Something went wrong while submitting the painting"
                    )
                }
            }
            (requireActivity() as MainActivity).hideProgressDialog()
        }
    }
}