package de.thm.mobiletech.hideandguess

import de.thm.mobiletech.hideandguess.databinding.FragmentGuessBinding
import de.thm.mobiletech.hideandguess.util.DataBindingFragment

class GuessFragment : DataBindingFragment<FragmentGuessBinding>(R.layout.fragment_guess) {

    override fun setBindingContext() {
        binding.context = this
    }

}