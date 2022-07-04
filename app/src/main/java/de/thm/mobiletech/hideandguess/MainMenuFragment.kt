package de.thm.mobiletech.hideandguess

import androidx.fragment.app.Fragment
import de.thm.mobiletech.hideandguess.databinding.FragmentMainMenuBinding
import de.thm.mobiletech.hideandguess.util.DataBindingFragment

/**
 * A simple [Fragment] subclass using data binding.
 */
class MainMenuFragment : DataBindingFragment<FragmentMainMenuBinding>(R.layout.fragment_main_menu) {

    override fun setBindingContext() {
        binding.context = this
    }

    fun openDrawFragment() {
        val username = binding.editTextUsername.text.toString()

        // Create action to navigate with SafeArgs
        val action = MainMenuFragmentDirections.actionOpenDrawBlurFragment(username)
        navController.navigate(action)
    }
}