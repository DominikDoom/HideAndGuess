package de.thm.mobiletech.hideandguess

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import de.thm.mobiletech.hideandguess.databinding.FragmentLobbyBinding
import de.thm.mobiletech.hideandguess.util.DataBindingFragment

class LobbyFragment : DataBindingFragment<FragmentLobbyBinding>(R.layout.fragment_lobby) {
    override fun setBindingContext() {
        binding.context = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // addPlayer(yourself)
    }

    fun addPlayer(user: User) {
        val grid = binding.gridlayoutPlayers
        val view = layoutInflater.inflate(R.layout.cardview_user, grid, false)
        val text = view.findViewById<TextView>(R.id.textViewUser)
        val image = view.findViewById<ImageView>(R.id.imageViewUser)
        text.text = user.username
        image.setImageResource(R.drawable.example_user_image1)
        grid.addView(view)
    }
}