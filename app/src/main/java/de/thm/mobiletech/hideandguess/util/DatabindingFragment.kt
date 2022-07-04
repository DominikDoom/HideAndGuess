package de.thm.mobiletech.hideandguess.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import de.thm.mobiletech.hideandguess.R

/**
 * Generic fragment with DataBinding.
 * Inflates the provided layout via [DataBindingUtil] and sets up
 * binding and navController variables.
 *
 * The binding reference is of type [BD], where BD is the concrete Binding class.
 *
 * **Example:**
 * ```
 * TestFragment : DatabindingFragment<FragmentTestBinding>(R.layout.fragment_test)
 * ```
 * @param layout The R.layout.layout_id reference to inflate.
 * */
abstract class DatabindingFragment<BD : ViewDataBinding>(val layout: Int) :
    Fragment(),
    IDatabindingContext {

    lateinit var binding: BD
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout using data binding
        binding = DataBindingUtil.inflate(
            inflater, layout, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner

        // This sets the layout data variable. It needs to be implemented by the concrete class.
        setBindingContext()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find and assign navController
        val navHostFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController
    }
}