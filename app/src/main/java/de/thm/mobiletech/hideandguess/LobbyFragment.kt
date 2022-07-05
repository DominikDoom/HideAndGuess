package de.thm.mobiletech.hideandguess

import de.thm.mobiletech.hideandguess.databinding.FragmentLobbyBinding
import de.thm.mobiletech.hideandguess.util.DataBindingFragment

class LobbyFragment : DataBindingFragment<FragmentLobbyBinding>(R.layout.fragment_lobby) {
    override fun setBindingContext() {
        binding.context = this
    }

    fun addPlayer(user: User) {

    }
}