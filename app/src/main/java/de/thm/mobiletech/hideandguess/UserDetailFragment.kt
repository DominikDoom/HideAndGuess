package de.thm.mobiletech.hideandguess

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.thm.mobiletech.hideandguess.databinding.FragmentUserDetailBinding
import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result
import de.thm.mobiletech.hideandguess.rest.services.Statistic
import de.thm.mobiletech.hideandguess.rest.services.Statistics
import de.thm.mobiletech.hideandguess.rest.services.getStatistics
import de.thm.mobiletech.hideandguess.util.DataBindingFragment
import de.thm.mobiletech.hideandguess.util.showError
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserDetailFragment : DataBindingFragment<FragmentUserDetailBinding>(R.layout.fragment_user_detail) {

    private val args: UserDetailFragmentArgs by navArgs()
    val user : ObservableField<User> = ObservableField()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user.set(args.user)
        setStatistics()
    }

    override fun setBindingContext() {
        binding.context = this
    }

    fun openUserEditFragment() {
        val action = UserDetailFragmentDirections.actionUserDetailFragmentToPlayerCustomizingFragment(user.get()!!)
        navController.navigate(action)
    }

    private fun setStatistics() {
        lifecycleScope.launch {
            val defer = async { RestClient.getStatistics() }

            when (val result = defer.await()) {
                is Result.Success -> {
                    val typeToken = object : TypeToken<List<Statistics>>() {}.type
                    val statistics = Gson().fromJson<List<Statistics>>(result.data, typeToken)
                    for (stat: Statistics in statistics) {
                        when (stat.type) {
                            Statistic.ROUNDS_WON -> binding.userStats1Num.text = stat.value.toString()
                            Statistic.ROUNDS_LOST -> binding.userStats2Num.text = stat.value.toString()
                            Statistic.GUESSED_RIGHT -> binding.userStats3Num.text = stat.value.toString()
                            Statistic.GUESSED_WRONG -> binding.userStats4Num.text = stat.value.toString()
                            Statistic.POINTS_EARNED -> binding.userStats5Num.text = stat.value.toString()
                            else -> {}
                        }
                    }
                }
                is Result.Error -> {
                    requireActivity().showError(MainMenuFragment.TAG,"Lobby creation failed", result.exception)
                }
                else -> {
                    requireActivity().showError(MainMenuFragment.TAG,"Lobby creation failed due to unknown reason")
                }
            }
        }
    }

}