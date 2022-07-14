package de.thm.mobiletech.hideandguess

import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import de.thm.mobiletech.hideandguess.databinding.FragmentLoginBinding
import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result
import de.thm.mobiletech.hideandguess.rest.services.getSynonyms
import de.thm.mobiletech.hideandguess.rest.services.login
import de.thm.mobiletech.hideandguess.util.DataBindingFragment
import de.thm.mobiletech.hideandguess.util.showError
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginFragment : DataBindingFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    override fun setBindingContext() {
        binding.context = this
    }

    fun login() {
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()

        binding.loginButton.isEnabled = false

        lifecycleScope.launch {
            val defer = async { RestClient.login(username, password) }

            when (val result = defer.await()) {
                is Result.HttpCode -> {
                    when(result.code) {
                        200 -> {
                            val user = User(username, password)
                            val action = LoginFragmentDirections.actionLoginFragmentToMainMenuFragment(user)
                            navController.navigate(action)
                        }
                        401 -> requireActivity().showError(TAG, "Wrong username or password")
                        else -> requireActivity().showError(TAG, "Login failed: Http ${result.code}")
                    }

                }
                is Result.Error -> {
                    requireActivity().showError(TAG,"Login failed", result.exception)
                }
                else -> {
                    requireActivity().showError(TAG,"Login failed due to unknown reason")
                }
            }

            binding.loginButton.isEnabled = true
        }
    }

    fun openRegisterFragment() {
        navController.navigate(R.id.action_loginFragment_to_registerFragment)
    }

    companion object {
        const val TAG = "LoginFragment"
    }
}