package de.thm.mobiletech.hideandguess

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import de.thm.mobiletech.hideandguess.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout using data binding
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_register, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.context = this // Set binding variable used in layout
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find and assign navController
        val navHostFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController
    }

    fun register() {
        val username = binding.editTextRegisterUsername.text.toString()
        val password = binding.editTextRegisterPassword
        val passwordConfirm = binding.editTextRegisterPasswordConfirm
        if (password != passwordConfirm)
            binding.editTextRegisterPasswordConfirm.error = "Passwörter stimmen nicht überein!"
    }

}