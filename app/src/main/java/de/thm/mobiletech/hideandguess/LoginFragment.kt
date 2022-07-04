package de.thm.mobiletech.hideandguess

import de.thm.mobiletech.hideandguess.databinding.FragmentLoginBinding
import de.thm.mobiletech.hideandguess.util.DataBindingFragment

class LoginFragment : DataBindingFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    override fun setBindingContext() {
        binding.context = this
    }

    fun openRegisterFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        navController.navigate(action)
    }
}