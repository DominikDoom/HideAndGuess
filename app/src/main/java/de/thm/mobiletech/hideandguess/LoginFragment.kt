package de.thm.mobiletech.hideandguess

import de.thm.mobiletech.hideandguess.databinding.FragmentLoginBinding
import de.thm.mobiletech.hideandguess.util.DataBindingFragment

class LoginFragment : DataBindingFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    override fun setBindingContext() {
        binding.context = this
    }

    fun openRegisterFragment() {
        navController.navigate(R.id.action_loginFragment_to_registerFragment)
    }
}