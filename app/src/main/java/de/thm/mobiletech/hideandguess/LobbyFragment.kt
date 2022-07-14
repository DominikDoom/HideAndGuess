package de.thm.mobiletech.hideandguess

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import de.thm.mobiletech.hideandguess.databinding.FragmentLobbyBinding
import de.thm.mobiletech.hideandguess.rest.LobbyState
import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result
import de.thm.mobiletech.hideandguess.rest.services.lobbyInfo
import de.thm.mobiletech.hideandguess.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class LobbyFragment : DataBindingFragment<FragmentLobbyBinding>(R.layout.fragment_lobby) {

    private val args: LobbyFragmentArgs by navArgs()

    private var lobbyId by Delegates.notNull<Int>()

    private var userList = mutableListOf<User>()

    private var lobbyOwner by Delegates.notNull<User>()

    private lateinit var mainHandler: Handler

    private var round: Int = 1

    override fun setBindingContext() {
        binding.context = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.extendedFab.isEnabled = false

        lobbyId = args.lobbyId

        binding.recyclerViewUser.layoutManager = GridLayoutManager(context, 2)

        binding.recyclerViewUser.adapter = UserAdapter(userList, onClick = {
            val action = LobbyFragmentDirections.actionOpenUserDetailFragment(it)
            navController.navigate(action)
        }, resources)

        binding.extendedFab.setOnClickListener {
            startGame()
        }

        requireActivity().title = "Lobby $lobbyId"

        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                Log.d("LobbyFragment", "Refreshing lobby")
                updateLobbyList()
                mainHandler.postDelayed(this, 1000)
            }
        })
    }

    override fun onPause() {
        requireActivity().title = "HideAndGuess"
        mainHandler.removeCallbacksAndMessages(null)

        super.onPause()
    }

    private fun startGame() {
        (requireActivity() as MainActivity).showProgressDialog()
        lifecycleScope.launch {
            val result = withContext(Dispatchers.Default) {
                RestClient.postRequest("start/$lobbyId")
            }
            if (result is Result.HttpCode) {
                when (result.code) {
                    200 -> {
                        requestPainter()
                    }
                    else -> {
                        requireActivity().showError(
                            "LobbyFragment",
                            "Das Spiel konnte nicht gestartet werden"
                        )
                        (requireActivity() as MainActivity).hideProgressDialog()
                    }
                }
            } else {
                requireActivity().showError("LobbyFragment", "Spiel konnte nicht gestartet werden")
            }
        }
    }

    private suspend fun requestPainter() {
        val lobbyInfo = getLobbyInfo()
        val painter = lobbyInfo!!.lobbyPlayers[round % lobbyInfo.lobbyPlayers.size]
        (requireActivity() as MainActivity).hideProgressDialog()

        if (painter.username == args.user.username) {
            navController.navigate(R.id.action_lobbyFragment_to_imageSelectionFragment)
        } else {
            // val action = LobbyFragmentDirections.actionLobbyFragmentToGuessFragment()
            // navController.navigate(action)
            (requireActivity() as MainActivity).showOtherPaintingDialog(painter.username)
        }
    }

    private suspend fun getLobbyInfo(): LobbyInfo? {
        return when (val result = RestClient.lobbyInfo(lobbyId)) {
            is Result.Success -> {
                println(Gson().fromJson(result.data.toString(), LobbyInfo::class.java).lobbyPlayers.indices)
                Gson().fromJson(result.data.toString(), LobbyInfo::class.java)
            }

            else -> {
                if (activity == null) {
                    return null
                }
                requireActivity().showError(
                    "LobbyFragment",
                    "Something went wrong while getting the lobby info"
                )
                null
            }
        }
    }

    private fun updateLobbyList() {
        lifecycleScope.launch {
            val defer = async {
                getLobbyInfo()
            }

            val result = defer.await()

            val oldList = userList.toMutableList()

            userList.clear()
            userList.addAll(result!!.lobbyPlayers)

            if (oldList != userList) {
                binding.recyclerViewUser.adapter?.notifyDataSetChanged()
            }

            lobbyOwner = result.lobbyOwner

            binding.extendedFab.isEnabled = result.lobbyOwner.username == args.user.username
                    && result.lobbyPlayers.size >= 2

            evaluateLobbyState(result.lobbyState)
        }
    }

    private var lastLobbyState: LobbyState? = null

    private suspend fun evaluateLobbyState(lobbyState: LobbyState) {
        if ((lastLobbyState == null || lastLobbyState == LobbyState.NOT_IN_GAME) && lobbyState == LobbyState.PAINTING_CHOOSE) {
            requestPainter()
        } else if (lastLobbyState == lobbyState) {
            // do nothing
        } else if (lastLobbyState == LobbyState.PAINTING_CHOOSE && lobbyState == LobbyState.PAINTING) {
            // do nothing
        } else if (lastLobbyState == LobbyState.PAINTING && lobbyState == LobbyState.GUESSING) {
            val action = LobbyFragmentDirections.actionLobbyFragmentToGuessFragment()
            navController.navigate(action)
        }

        lastLobbyState = lobbyState
    }

    data class LobbyInfo(
        val lobbyId: Long,
        val maxPlayers: Int,
        val lobbyOwner: User,
        val lobbyPlayers: MutableList<User>,
        val lobbyState: LobbyState
    )
}