package de.thm.mobiletech.hideandguess

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import de.thm.mobiletech.hideandguess.databinding.FragmentMainMenuBinding
import de.thm.mobiletech.hideandguess.util.DataBindingFragment

/**
 * A simple [Fragment] subclass using data binding.
 */
class MainMenuFragment : DataBindingFragment<FragmentMainMenuBinding>(R.layout.fragment_main_menu) {

    private val args: MainMenuFragmentArgs by navArgs()

    override fun setBindingContext() {
        binding.context = this
    }

    fun joinGame() {

    }

    fun createGame() {

    }

    fun showStats() {
        val action = MainMenuFragmentDirections.actionMainMenuFragmentToUserDetailFragment(args.user)
        navController.navigate(action)
    }

}