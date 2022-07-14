package de.thm.mobiletech.hideandguess

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import de.thm.mobiletech.hideandguess.databinding.FragmentLobbyBinding
import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result
import de.thm.mobiletech.hideandguess.rest.services.lobbyInfo
import de.thm.mobiletech.hideandguess.util.DataBindingFragment
import de.thm.mobiletech.hideandguess.util.showError
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class LobbyFragment : DataBindingFragment<FragmentLobbyBinding>(R.layout.fragment_lobby) {

    private val args: LobbyFragmentArgs by navArgs()

    private var lobbyId by Delegates.notNull<Int>()

    private var userList = mutableListOf<User>()

    override fun setBindingContext() {
        binding.context = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lobbyId = args.lobbyId

        binding.recyclerViewUser.layoutManager = GridLayoutManager(context, 2)

        binding.recyclerViewUser.adapter = UserAdapter(userList, onClick = {
            val action = LobbyFragmentDirections.actionOpenUserDetailFragment(it)
            navController.navigate(action)
        })

        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                Log.d("LobbyFragment", "Refreshing lobby")
                updateLobbyList()
                mainHandler.postDelayed(this, 1000)
            }
        })

    }

    private suspend fun getLobbyInfo(): LobbyInfo? {
        return when (val result = RestClient.lobbyInfo(lobbyId)) {
            is Result.Success -> {
                Gson().fromJson(result.data.toString(), LobbyInfo::class.java)
            }

            else -> {
                requireActivity().showError(
                    "LobbyFragment",
                    "Something went wrong while getting the lobby info"
                )
                null
            }
        }
    }

    private fun updateLobbyList() {
        lifecycleScope.launch{
            val defer = async {
                getLobbyInfo()
            }

            val result = defer.await()

            val oldList = userList

            userList.clear()
            userList.addAll(result!!.lobbyPlayers)

            if (oldList == userList)
                return@launch

            binding.recyclerViewUser.adapter?.notifyDataSetChanged()
        }
    }

    data class LobbyInfo(
        val lobbyId: Long,
        val maxPlayers: Int,
        val lobbyOwner: User,
        val lobbyPlayers: MutableList<User>
    )
}