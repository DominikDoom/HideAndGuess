package de.thm.mobiletech.hideandguess

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import de.thm.mobiletech.hideandguess.databinding.FragmentMainMenuBinding
import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result
import de.thm.mobiletech.hideandguess.rest.services.create
import de.thm.mobiletech.hideandguess.stomp.StompClientHandler
import de.thm.mobiletech.hideandguess.stomp.services.join
import de.thm.mobiletech.hideandguess.util.DataBindingFragment
import de.thm.mobiletech.hideandguess.util.showError
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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
        val v: View = layoutInflater.inflate(R.layout.dialog_create_lobby, null)

        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Lobby erstellen")
            .setView(v)
            .setPositiveButton("Erstellen") { _, _ ->
                val id = v.findViewById<TextInputEditText>(R.id.et_lobby_max_players)
                    .text.toString().toInt()
                createLobby()
            }
            .setCancelable(true)
            .show()
    }

    fun showStats() {
        val action = MainMenuFragmentDirections.actionMainMenuFragmentToUserDetailFragment(args.user)
        navController.navigate(action)
    }

    fun createLobby() {
        lifecycleScope.launch {
            val defer = async { RestClient.create() }

            when (val result = defer.await()) {
                is Result.HttpCode -> {
                    when(result.code) {
                        200 -> {
                            val action = MainMenuFragmentDirections.actionMainMenuFragmentToLobbyFragment()
                            navController.navigate(action)
                        }
                        403 -> requireActivity().showError(TAG, "This User already has a lobby")
                        500 -> requireActivity().showError(TAG, "Internal Server Error")
                        else -> requireActivity().showError(TAG, "Unknown Error")
                    }
                }
                is Result.Error -> {
                    requireActivity().showError(TAG,"Lobby creation failed", result.exception)
                }
                else -> {
                    requireActivity().showError(TAG,"Lobby creation failed due to unknown reason")
                }
            }
        }
    }

    companion object {
        const val TAG = "MainMenuFragment"
    }

}