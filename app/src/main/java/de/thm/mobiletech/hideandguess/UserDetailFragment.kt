package de.thm.mobiletech.hideandguess

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.navigation.fragment.navArgs
import de.thm.mobiletech.hideandguess.databinding.FragmentUserDetailBinding
import de.thm.mobiletech.hideandguess.util.DataBindingFragment

class UserDetailFragment : DataBindingFragment<FragmentUserDetailBinding>(R.layout.fragment_user_detail) {

    private val args: UserDetailFragmentArgs by navArgs()
    val user : ObservableField<User> = ObservableField()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user.set(args.user)
    }

    override fun setBindingContext() {
        binding.context = this
    }

    fun openUserEditFragment() {
        val action = UserDetailFragmentDirections.actionOpenUserEditFragment(user.get()!!)
        navController.navigate(action)
    }

}