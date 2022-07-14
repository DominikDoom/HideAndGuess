package de.thm.mobiletech.hideandguess

import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import de.thm.mobiletech.hideandguess.databinding.FragmentGuessBinding
import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result
import de.thm.mobiletech.hideandguess.rest.services.getSynonyms
import de.thm.mobiletech.hideandguess.util.DataBindingFragment
import de.thm.mobiletech.hideandguess.util.showError
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class GuessFragment : DataBindingFragment<FragmentGuessBinding>(R.layout.fragment_guess) {

    private val args: GuessFragmentArgs by navArgs()
    private lateinit var synonyms: Array<String>
    private lateinit var textInput: TextInputEditText

    override fun setBindingContext() {
        binding.context = this
        getSynonyms()
        textInput = binding.textInputGuess

        textInput.setOnEditorActionListener(TextView.OnEditorActionListener() { _, _, _ ->
            evaluate()
            true
        })
    }

    fun evaluate() {
        if (synonyms.contains(binding.textInputGuess.text.toString())) {
            Toast.makeText(requireContext(), "Richtig!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Falsch!", Toast.LENGTH_LONG).show()
        }
    }

    fun getSynonyms() {
        lifecycleScope.launch {
            val defer = async { RestClient.getSynonyms(args.lobbyId) }

            when (val result = defer.await()) {
                is Result.Success -> {
                    synonyms = Gson().fromJson(result.data, Array<String>::class.java)
                }
                is Result.Error -> {
                    requireActivity().showError(LoginFragment.TAG,"Login failed", result.exception)
                }
                else -> {
                    requireActivity().showError(LoginFragment.TAG,"Login failed due to unknown reason")
                }
            }
        }
    }

}