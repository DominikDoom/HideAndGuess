package de.thm.mobiletech.hideandguess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import de.thm.mobiletech.hideandguess.databinding.FragmentDrawBlurBinding

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
        username.set(args.username)
    }

    fun resetButtonClicked() {
        binding.blurDrawView.reset()
    }
}