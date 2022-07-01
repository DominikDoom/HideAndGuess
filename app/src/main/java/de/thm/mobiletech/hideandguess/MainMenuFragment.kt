package de.thm.mobiletech.hideandguess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import de.thm.mobiletech.hideandguess.databinding.FragmentMainMenuBinding

/**
 * A simple [Fragment] subclass using data binding.
 */
class MainMenuFragment : Fragment() {

    private lateinit var binding: FragmentMainMenuBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout using data binding
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main_menu, container, false
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
    }

    fun openDrawFragment() {
        val username = binding.editTextUsername.text.toString()

        // Create action to navigate with SafeArgs
        val action = MainMenuFragmentDirections.actionOpenDrawBlurFragment(username)
        navController.navigate(action)
    }
}