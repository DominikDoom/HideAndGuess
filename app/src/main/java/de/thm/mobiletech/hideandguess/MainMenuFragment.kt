package de.thm.mobiletech.hideandguess

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import de.thm.mobiletech.hideandguess.databinding.FragmentMainMenuBinding
import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result
import de.thm.mobiletech.hideandguess.rest.services.create
import de.thm.mobiletech.hideandguess.util.DataBindingFragment
import de.thm.mobiletech.hideandguess.util.hideProgressDialog
import de.thm.mobiletech.hideandguess.util.showError
import de.thm.mobiletech.hideandguess.util.showProgressDialog
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
                lifecycleScope.launch {
                    (requireActivity() as MainActivity).showProgressDialog()

                    val defer = async { RestClient.postRequest("join/$id") }

                    when (val result = defer.await()) {
                        is Result.HttpCode -> {
                            when(result.code) {
                                200 -> {
                                    val action = MainMenuFragmentDirections.actionMainMenuFragmentToLobbyFragment(args.user, id)
                                    navController.navigate(action)
                                }
                                401 -> {
                                    requireActivity().showError(TAG, "Du hast keine Berechtigung, dieser Lobby zu betreten.")
                                }
                                403 -> {
                                    // requireActivity().showError(LoginFragment.TAG, "You are already in a lobby")
                                    // show dialog to leave lobby
                                    MaterialAlertDialogBuilder(requireContext())
                                        .setTitle("Du bist bereits in einer Lobby")
                                        .setMessage("Soll die Lobby wirklich verlassen werden?")
                                        .setPositiveButton("Ja") { _, _ ->
                                            lifecycleScope.launch {
                                                val defer1 = async { RestClient.postRequest("leave") }
                                                when (val result1 = defer1.await()) {
                                                    is Result.HttpCode -> {
                                                        when(result1.code) {
                                                            200 -> {
                                                                Toast.makeText(requireContext(), "Lobby verlassen", Toast.LENGTH_SHORT).show()
                                                            }
                                                            404 -> requireActivity().showError(TAG, "Lobby wurde nicht gefunden")
                                                            else -> {
                                                                requireActivity().showError(TAG, "Verlassen der Lobby fehlgeschlagen")
                                                            }
                                                        }
                                                    }
                                                    is Result.Error -> {
                                                        requireActivity().showError(TAG, "Verlassen der Lobby fehlgeschlagen")
                                                    }
                                                    else -> {
                                                        requireActivity().showError(TAG, "Verlassen der Lobby fehlgeschlagen")
                                                    }
                                                }
                                            }
                                        }
                                        .setNegativeButton("Nein") { _, _ ->
                                            requireActivity().showError(TAG, "Verlassen der Lobby abgebrochen")
                                        }
                                        .setCancelable(true)
                                        .show()
                                }
                                404 -> requireActivity().showError(LoginFragment.TAG, "Lobby wurde nicht gefunden")
                                409 -> requireActivity().showError(TAG, "Du kannst dieser Lobby nicht beitreten")
                                else -> requireActivity().showError(TAG, "Unbekannter Fehler")
                            }
                        }
                        is Result.Error -> {
                            requireActivity().showError(TAG,"Lobby beitreten fehlgeschlagen", result.exception)
                        }
                        else -> {
                            requireActivity().showError(TAG,"Lobby beitreten fehlgeschlagen aufgrund eines unbekannten Fehlers")
                        }
                    }

                    (requireActivity() as MainActivity).hideProgressDialog()
                }

            }
            .setCancelable(true)
            .show()
    }

    fun createGame() {
        createLobby()
    }

    fun showStats() {
        val action = MainMenuFragmentDirections.actionMainMenuFragmentToUserDetailFragment(args.user, true)
        navController.navigate(action)
    }

    private fun createLobby() {
        lifecycleScope.launch {
            (requireActivity() as MainActivity).showProgressDialog()
            val defer = async { RestClient.create() }

            when (val result = defer.await()) {
                is Result.Success -> {
                    val lobbyId = result.data?.toInt()
                    val action = MainMenuFragmentDirections.actionMainMenuFragmentToLobbyFragment(args.user, lobbyId!!)
                    navController.navigate(action)
                }
                is Result.Error -> {
                    requireActivity().showError(TAG,"Lobby Erstellung fehlgeschlagen", result.exception)
                }
                else -> {
                    requireActivity().showError(TAG,"Lobby Erstellung fehlgeschlagen aufgrund eines unbekannten Fehlers")
                }
            }

            (requireActivity() as MainActivity).hideProgressDialog()
        }
    }

    companion object {
        const val TAG = "MainMenuFragment"
    }

}