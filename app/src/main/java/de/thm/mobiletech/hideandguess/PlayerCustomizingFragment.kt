package de.thm.mobiletech.hideandguess

import android.os.Bundle
import android.view.View
import de.thm.mobiletech.hideandguess.databinding.FragmentPlayerCustomizingBinding
import de.thm.mobiletech.hideandguess.util.DataBindingFragment
import java.util.*


class PlayerCustomizingFragment : DataBindingFragment<FragmentPlayerCustomizingBinding>(R.layout.fragment_player_customizing) {

    private var playerImages: ArrayList<Int> = ArrayList<Int>(Arrays.asList(R.drawable.amongus_player_blue, R.drawable.amongus_player_red, R.drawable.amongus_player_green, R.drawable.amongus_player_yellow))
    private var playerImagesCursor: Int = 0

    override fun setBindingContext() {
        binding.context = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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