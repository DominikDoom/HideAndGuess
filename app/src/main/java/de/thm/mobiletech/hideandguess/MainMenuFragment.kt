package de.thm.mobiletech.hideandguess

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import de.thm.mobiletech.hideandguess.databinding.FragmentMainMenuBinding
import de.thm.mobiletech.hideandguess.stomp.StompClientHandler
import de.thm.mobiletech.hideandguess.stomp.services.join
import de.thm.mobiletech.hideandguess.util.DataBindingFragment

/**
 * A simple [Fragment] subclass using data binding.
 */
class MainMenuFragment : DataBindingFragment<FragmentMainMenuBinding>(R.layout.fragment_main_menu) {

    private val args: MainMenuFragmentArgs by navArgs()

    override fun setBindingContext() {
        binding.context = this
    }

    fun joinGame() {
        val v: View = layoutInflater.inflate(R.layout.dialog_join_lobby, null)

        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Lobby-ID eingeben")
            .setView(v)
            .setPositiveButton("Beitreten") { _, _ ->
                val id = v.findViewById<TextInputEditText>(R.id.et_lobbyCode)
                    .text.toString().toInt()
                StompClientHandler.join(args.user.username, args.user.password, id)
            }
            .setCancelable(true)
            .show()
    }

    fun createGame() {

    }

    fun showStats() {
        val action = MainMenuFragmentDirections.actionMainMenuFragmentToUserDetailFragment(args.user)
        navController.navigate(action)
    }

}