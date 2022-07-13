package de.thm.mobiletech.hideandguess

import de.thm.mobiletech.hideandguess.databinding.FragmentRegisterBinding
import de.thm.mobiletech.hideandguess.util.DataBindingFragment

class RegisterFragment : DataBindingFragment<FragmentRegisterBinding>(R.layout.fragment_register) {

    override fun setBindingContext() {
        binding.context = this
    }

    fun register() {
        if (!validate()) return
    }

    fun validate(): Boolean {
        // ALLOWED CHARACTERS: A-Za-z0-9_.-
        val username = binding.etRegisterUsername
        val password = binding.etRegisterPass
        val passwordConfirm = binding.etRegisterPassConfirm
        val reg = Regex("^[a-zA-Z0-9_.-]*\$")
        val passLength = (6..200)

        if (!reg.matches(username.text.toString())) {
            username.error = "Nicht erlaubte Zeichen!"
            return false
        }

        if (!passLength.contains(password.text.toString().length)) {
            password.error = "Passwort muss zwischen 6 und 200 Zeichen lang sein!"
            return false
        }

        if (password.text.toString() != passwordConfirm.text.toString()) {
            binding.etRegisterPassConfirm.error = "Passwörter stimmen nicht überein!"
            return false
        }

        return true
    }
}