package de.tomjuri.gemdigger.util

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.sqrt


object RenderUtil {
    fun drawBlockBox(blockPos: BlockPos, color: Color) {
        val x = blockPos.x.toDouble()
        val y = blockPos.y.toDouble()
        val z = blockPos.z.toDouble()
        val x2 = x + 1
        val y2 = y + 1
        val z2 = z + 1
        val d0 = Minecraft.getMinecraft().renderManager.viewerPosX
        val d1 = Minecraft.getMinecraft().renderManager.viewerPosY
        val d2 = Minecraft.getMinecraft().renderManager.viewerPosZ
        drawBox(AxisAlignedBB(x, y, z, x2, y2, z2).offset(-d0, -d1, -d2), color)
    }

    fun drawBox(aabb: AxisAlignedBB, color: Color) {
        GlStateManager.pushMatrix()
        GlStateManager.enableBlend()
        GlStateManager.disableDepth()
        GlStateManager.disableLighting()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.disableTexture2D()
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        val a: Float = color.getAlpha() / 255.0f
        val r: Float = color.getRed() / 255.0f
        val g: Float = color.getGreen() / 255.0f
        val b: Float = color.getBlue() / 255.0f
        GlStateManager.color(r, g, b, a)
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        tessellator.draw()
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        tessellator.draw()
        GlStateManager.color(r, g, b, a)
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        tessellator.draw()
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        tessellator.draw()
        GlStateManager.color(r, g, b, a)
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        tessellator.draw()
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldrenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        tessellator.draw()
        GlStateManager.color(r, g, b, a)
        GL11.glLineWidth(2f)
        RenderGlobal.drawSelectionBoundingBox(aabb)
        GL11.glLineWidth(1.0f)
        GlStateManager.enableTexture2D()
        GlStateManager.enableDepth()
        GlStateManager.disableBlend()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.popMatrix()
    }

    fun renderText(pos: BlockPos, text: String) {
        val renderPosX = mc.renderManager.viewerPosX
        val renderPosY = mc.renderManager.viewerPosY
        val renderPosZ = mc.renderManager.viewerPosZ

        val x: Double = pos.x - renderPosX
        val y: Double = pos.y - renderPosY
        val z: Double = pos.z - renderPosZ

        GlStateManager.pushMatrix()
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5) // Adjust the offsets to position the text correctly.

        GlStateManager.rotate(-mc.renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(mc.renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        val scale: Double = 0.005 * sqrt(player.getDistanceSq(pos)) // Adjust the factor as needed.

        GlStateManager.scale(-scale, -scale, scale)

        GL11.glNormal3f(0.0f, 1.0f, 0.0f)
        GlStateManager.disableLighting()
        GlStateManager.depthMask(false)
        GlStateManager.disableDepth()
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        // Use fontRenderer.drawString to render your text.

        // Use fontRenderer.drawString to render your text.
        mc.fontRendererObj.drawString(text, 0, 0, 0xFFFFFF)

        GlStateManager.enableDepth()
        GlStateManager.depthMask(true)
        GlStateManager.enableLighting()
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
    }
}
