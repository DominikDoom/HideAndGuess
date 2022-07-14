package de.thm.mobiletech.hideandguess

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.navigation.fragment.navArgs
import de.thm.mobiletech.hideandguess.databinding.FragmentUserEditBinding
import de.thm.mobiletech.hideandguess.util.DataBindingFragment

class UserEditFragment : DataBindingFragment<FragmentUserEditBinding>(R.layout.fragment_user_edit) {

    private val args: PlayerCustomizingFragmentArgs by navArgs()
    val user : ObservableField<User> = ObservableField()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user.set(args.user)
        println(user.get()?.username)
    }

    override fun setBindingContext() {
        binding.context = this
    }
}