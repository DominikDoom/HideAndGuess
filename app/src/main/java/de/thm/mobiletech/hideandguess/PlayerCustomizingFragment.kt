package de.thm.mobiletech.hideandguess

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import de.thm.mobiletech.hideandguess.databinding.FragmentPlayerCustomizingBinding
import java.util.*
import kotlin.collections.ArrayList


class PlayerCustomizingFragment : Fragment() {

    private lateinit var binding: FragmentPlayerCustomizingBinding
    private lateinit var navController: NavController
    private var playerImages: ArrayList<Int> = ArrayList<Int>(Arrays.asList(R.drawable.amongus_player_blue, R.drawable.amongus_player_red, R.drawable.amongus_player_green, R.drawable.amongus_player_yellow))
    private var playerImagesCursor: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout using data binding
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_player_customizing, container, false
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

        binding.imageCustomizingPlayer.setImageResource(R.drawable.amongus_player_blue)
    }

    fun forwardPlayerColor() {
        if (playerImagesCursor < playerImages.size - 1)
            playerImagesCursor++
        else
            playerImagesCursor = 0
        binding.imageCustomizingPlayer.setImageResource(playerImages[playerImagesCursor])
    }

    fun backwardPlayerColor() {
        if (playerImagesCursor > 0)
            playerImagesCursor--
        else
            playerImagesCursor = playerImages.size - 1
        binding.imageCustomizingPlayer.setImageResource(playerImages[playerImagesCursor])
    }

    /* TODO: auf das Dokument zugreifen und die Farben ändern, statt die neue images files zu laden
    fun xml() {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document = builder.parse(resources.openRawResource(R.raw.amongus_player))

        val xpathExpression = "//path"
        val xpf: XPathFactory = XPathFactory.newInstance()
        val xpath: XPath = xpf.newXPath()
        val expression: XPathExpression = xpath.compile(xpathExpression)
        val svgPaths: NodeList = expression.evaluate(document, XPathConstants.NODESET) as NodeList

        svgPaths.item(0).attributes.getNamedItem("android:fillColor").nodeValue = "#AAAAA"
    }
    */

}