package de.tomjuri.armageddon.util

import de.tomjuri.macroframework.util.mc
import de.tomjuri.macroframework.util.player
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.Entity
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.sqrt

object RenderUtil {
    fun drawBlockBox(event: RenderWorldLastEvent, block: BlockPos, color: Color) {
        val x = block.x.toDouble()
        val y = block.y.toDouble()
        val z = block.z.toDouble()
        val frontTopLeft = Vec3(x, y + 1, z)
        val frontTopRight = Vec3(x, y + 1, z + 1)
        val frontBottomLeft = Vec3(x, y, z)
        val frontBottomRight = Vec3(x, y, z + 1)
        val backTopLeft = Vec3(x + 1, y + 1, z + 1)
        val backTopRight = Vec3(x + 1, y + 1, z)
        val backBottomLeft = Vec3(x + 1, y, z + 1)
        val backBottomRight = Vec3(x + 1, y, z)
        drawLine(event, frontTopLeft, frontTopRight, 2f, color)
        drawLine(event, frontTopRight, frontBottomRight, 2f, color)
        drawLine(event, frontBottomRight, frontBottomLeft, 2f, color)
        drawLine(event, frontBottomLeft, frontTopLeft, 2f, color)
        drawLine(event, backTopLeft, backTopRight, 2f, color)
        drawLine(event, backTopRight, backBottomRight, 2f, color)
        drawLine(event, backBottomRight, backBottomLeft, 2f, color)
        drawLine(event, backBottomLeft, backTopLeft, 2f, color)
        drawLine(event, frontTopLeft, backTopRight, 2f, color)
        drawLine(event, frontTopRight, backTopLeft, 2f, color)
        drawLine(event, frontBottomLeft, backBottomRight, 2f, color)
        drawLine(event, frontBottomRight, backBottomLeft, 2f, color)
    }

    fun drawLine(event: RenderWorldLastEvent, from: Vec3, to: Vec3, lineWidth: Float, color: Color) {
        val render: Entity = mc.renderViewEntity
        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.worldRenderer
        val realX = render.lastTickPosX + (render.posX - render.lastTickPosX) * event.partialTicks
        val realY = render.lastTickPosY + (render.posY - render.lastTickPosY) * event.partialTicks
        val realZ = render.lastTickPosZ + (render.posZ - render.lastTickPosZ) * event.partialTicks
        GlStateManager.pushMatrix()
        GlStateManager.translate(-realX, -realY, -realZ)
        GlStateManager.disableTexture2D()
        GlStateManager.disableLighting()
        GL11.glDisable(3553)
        GL11.glLineWidth(lineWidth)
        GlStateManager.enableBlend()
        GlStateManager.disableAlpha()
        GlStateManager.disableDepth()
        GlStateManager.depthMask(false)
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.color(1f, 1f, 1f, 1f)
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR)
        bufferBuilder.pos(from.xCoord, from.yCoord, from.zCoord)
            .color(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f).endVertex()
        bufferBuilder.pos(to.xCoord, to.yCoord, to.zCoord)
            .color(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f).endVertex()
        tessellator.draw()
        GlStateManager.translate(realX, realY, realZ)
        GlStateManager.disableBlend()
        GlStateManager.enableAlpha()
        GlStateManager.enableTexture2D()
        GlStateManager.enableDepth()
        GlStateManager.depthMask(true)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.popMatrix()
    }

    fun renderText(pos: BlockPos, text: String?) {
        val renderPosX: Double = mc.renderManager.viewerPosX
        val renderPosY: Double = mc.renderManager.viewerPosY
        val renderPosZ: Double = mc.renderManager.viewerPosZ
        val x = pos.x - renderPosX
        val y = pos.y - renderPosY
        val z = pos.z - renderPosZ
        GlStateManager.pushMatrix()
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5)
        GlStateManager.rotate(-mc.renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(mc.renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        val scale = 0.005 * sqrt(player.getDistanceSq(pos))
        GlStateManager.scale(-scale, -scale, scale)
        GL11.glNormal3f(0.0f, 1.0f, 0.0f)
        GlStateManager.disableLighting()
        GlStateManager.depthMask(false)
        GlStateManager.disableDepth()
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        mc.fontRendererObj.drawString(text, 0, 0, 0xFFFFFF)
        GlStateManager.enableDepth()
        GlStateManager.depthMask(true)
        GlStateManager.enableLighting()
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
    }
}
