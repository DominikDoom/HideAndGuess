package de.thm.mobiletech.hideandguess

import androidx.lifecycle.lifecycleScope
import de.thm.mobiletech.hideandguess.databinding.FragmentRegisterBinding
import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result
import de.thm.mobiletech.hideandguess.rest.services.register
import de.thm.mobiletech.hideandguess.util.DataBindingFragment
import de.thm.mobiletech.hideandguess.util.showError
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RegisterFragment : DataBindingFragment<FragmentRegisterBinding>(R.layout.fragment_register) {

    override fun setBindingContext() {
        binding.context = this
    }

    fun register() {
        if (!validate()) return

        val username = binding.etRegisterUsername.text.toString()
        val password = binding.etRegisterPass.text.toString()

        binding.registerButton.isEnabled = false

        lifecycleScope.launch {
            val defer = async { RestClient.register(username, password) }

            when (val result = defer.await()) {
                is Result.HttpCode -> {
                    when (result.code) {
                        201 -> {
                            val user = User(username)
                            val action = RegisterFragmentDirections.actionRegisterFragmentToMainMenuFragment(user)
                            navController.navigate(action)
                        }
                        409 -> requireActivity().showError(TAG, "Username already exists")
                        else -> requireActivity().showError(TAG, "Unknown error")
                    }
                }
                is Result.Error -> {
                    requireActivity().showError(TAG,"Registration failed", result.exception)
                }
                else -> {
                    requireActivity().showError(TAG,"Registration failed due to unknown reason")
                }
            }

            binding.registerButton.isEnabled = true
        }
    }

    private fun validate(): Boolean {
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

    companion object {
        const val TAG = "RegisterFragment"
    }
}