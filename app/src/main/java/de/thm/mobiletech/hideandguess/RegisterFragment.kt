package de.thm.mobiletech.hideandguess

import de.thm.mobiletech.hideandguess.databinding.FragmentRegisterBinding
import de.thm.mobiletech.hideandguess.util.DatabindingFragment

class RegisterFragment : DatabindingFragment<FragmentRegisterBinding>(R.layout.fragment_register) {

    override fun setBindingContext() {
        binding.context = this
    }

    fun register() {
        val username = binding.editTextRegisterUsername.text.toString()
        val password = binding.editTextRegisterPassword
        val passwordConfirm = binding.editTextRegisterPasswordConfirm
        if (password != passwordConfirm)
            binding.editTextRegisterPasswordConfirm.error = "Passwörter stimmen nicht überein!"
    }
}