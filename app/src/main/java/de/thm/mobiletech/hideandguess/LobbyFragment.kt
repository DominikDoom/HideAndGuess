package de.thm.mobiletech.hideandguess

import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import de.thm.mobiletech.hideandguess.databinding.FragmentLobbyBinding
import de.thm.mobiletech.hideandguess.util.DataBindingFragment
import java.util.*

class LobbyFragment : DataBindingFragment<FragmentLobbyBinding>(R.layout.fragment_lobby) {
    override fun setBindingContext() {
        binding.context = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewUser.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerViewUser.adapter = UserAdapter(
            listOf(User("sedoox"),
                User("hallo"),
                User("hasf"),
                User("sdfsdffqwwfwqef"))
        )
    }
}