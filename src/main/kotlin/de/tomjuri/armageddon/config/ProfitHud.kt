package de.tomjuri.armageddon.config

import cc.polyfrost.oneconfig.config.core.OneColor
import cc.polyfrost.oneconfig.hud.BasicHud
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import cc.polyfrost.oneconfig.platform.Platform
import cc.polyfrost.oneconfig.renderer.NanoVGHelper
import cc.polyfrost.oneconfig.renderer.asset.Image
import cc.polyfrost.oneconfig.renderer.font.Fonts
import de.tomjuri.armageddon.util.armageddon
import de.tomjuri.armageddon.util.macro
import de.tomjuri.armageddon.util.tracker
import net.minecraft.util.Tuple
import java.awt.Color
import java.util.*

class ProfitHud : BasicHud(true, 1f, 1f, 3.5f, true, true, 1F, 0F, 0F, OneColor(0, 0, 0, 150), false, 2F, OneColor(0, 0, 0, 240)) {

    private var lines: ArrayList<Tuple<String, String>>? = ArrayList()
    private var width: Float = 0f
    private var color = OneColor(0, 0, 0, 255)

    init { addLines() }

    override fun draw(matrices: UMatrixStack, x: Float, y: Float, scale: Float, example: Boolean) {
        if (lines!!.isEmpty()) return
        val chroma = Color.WHITE
        color.setFromOneColor(OneColor(chroma.red, chroma.green, chroma.blue, 255))
        NanoVGHelper.INSTANCE.setupAndDraw(true) { vg ->
            val textX = position.x + 1 * scale
            var textY = position.y + 1 * scale
            addLines()
            for (line in lines!!) {
                drawLine(vg, line.first, line.second, textX, textY, scale)
                textY += 5 * scale
            }
        }
    }

    override fun shouldShow() = macro.isEnabled()

    private fun drawLine(vg: Long, text: String?, iconPath: String?, x: Float, y: Float, scale: Float) {
        val iconWidth = 4 * scale
        val iconHeight = 4 * scale
        iconPath?.let { NanoVGHelper.INSTANCE.drawImage(vg, Image(it), x + scale - 0.9f * scale, y, iconWidth, iconHeight) }
        text?.let { NanoVGHelper.INSTANCE.drawText(vg, it, (x + iconWidth + 2 * scale - 0.9f * scale), (y + (iconHeight / 2)),  -1, 2 * scale, Fonts.SEMIBOLD) }
    }

    private fun getLineWidth(): Float {
        var longestLine = ""
        var maxLength = 0
        for (entry in lines!!) {
            val lineText = entry.first ?: return 0f
            if (lineText.length > maxLength) {
                longestLine = lineText
                maxLength = lineText.length
            }
        }
        return Platform.getGLPlatform().getStringWidth(longestLine).toFloat()
    }

    override fun getWidth(scale: Float, example: Boolean): Float {
        if (lines == null || lines!!.isEmpty()) return 0f
        val currentWidth = (getLineWidth() / 2 * (scale / 1.5f)) + (scale * lines!!.size) + paddingX / 2
        return width.coerceAtLeast(currentWidth)
    }

    override fun getHeight(scale: Float, example: Boolean): Float {
        if (lines == null || lines!!.isEmpty()) return 0f
        return (((6f - (lines!!.size * 0.1f)) * scale) * lines!!.size - scale + paddingY / 2)
    }

    private fun addLines() {
        if(!armageddon.isTrackerInitialized()) return
        lines!!.clear()
        lines!!.add(Tuple(tracker.getTotalProfit(), "/assets/armageddon/profit.png"))
        lines!!.add(Tuple(tracker.getProfitPerHour(), "/assets/armageddon/profithr.png"))
    }
}
