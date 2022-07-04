package de.thm.mobiletech.hideandguess.util

/**
 * This interface enforces that all classes extending [DataBindingFragment]
 * have to implement a method to set their data binding layout variable.
 * Content of said method should be along the line of
 *
 * `binding.<contextVariable> = this`
 * */
sealed interface IDatabindingContext {
    /**
     * This method is called in [DataBindingFragment.onCreateView].
     * It should set the data binding variable of the layout to the fragment instance.
     *
     * e.g. `binding.context = this`
     * */
    fun setBindingContext()
}