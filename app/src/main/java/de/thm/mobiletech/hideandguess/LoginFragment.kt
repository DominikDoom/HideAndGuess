package de.thm.mobiletech.hideandguess

import de.thm.mobiletech.hideandguess.databinding.FragmentLoginBinding
import de.thm.mobiletech.hideandguess.util.DatabindingFragment

class LoginFragment : DatabindingFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    override fun setBindingContext() {
        binding.context = this
    }

    fun openRegisterFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        navController.navigate(action)
    }
}