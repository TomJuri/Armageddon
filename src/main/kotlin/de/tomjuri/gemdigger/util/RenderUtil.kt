package de.tomjuri.gemdigger.util

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import org.lwjgl.opengl.GL11
import java.awt.Color
import javax.vecmath.Vector3d
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
        val worldRenderer = tessellator.worldRenderer

        val a: Float = color.getAlpha() / 255.0f
        val r: Float = color.getRed() / 255.0f
        val g: Float = color.getGreen() / 255.0f
        val b: Float = color.getBlue() / 255.0f

        GlStateManager.color(r, g, b, a)

        val vertices = arrayOf(
                // Bottom face
                Vector3d(aabb.minX, aabb.minY, aabb.minZ),
                Vector3d(aabb.maxX, aabb.minY, aabb.minZ),
                Vector3d(aabb.maxX, aabb.minY, aabb.maxZ),
                Vector3d(aabb.minX, aabb.minY, aabb.maxZ),
                // Top face
                Vector3d(aabb.minX, aabb.maxY, aabb.maxZ),
                Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ),
                Vector3d(aabb.maxX, aabb.maxY, aabb.minZ),
                Vector3d(aabb.minX, aabb.maxY, aabb.minZ)
        )

        val faceIndices = arrayOf(
                // Bottom face
                intArrayOf(0, 1, 2, 3),
                // Top face
                intArrayOf(4, 5, 6, 7),
                // Side faces
                intArrayOf(3, 2, 6, 7),
                intArrayOf(0, 1, 5, 4),
                intArrayOf(0, 3, 7, 4),
                intArrayOf(1, 2, 6, 5)
        )

        for (indices in faceIndices) {
            worldRenderer.begin(7, DefaultVertexFormats.POSITION)
            for (index in indices) {
                val vertex = vertices[index]
                worldRenderer.pos(vertex.x, vertex.y, vertex.z).endVertex()
            }
            tessellator.draw()
        }

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
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5)
        GlStateManager.rotate(-mc.renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(mc.renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        val scale: Double = 0.005 * sqrt(player.getDistanceSq(pos))
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

    fun drawLine(event: RenderWorldLastEvent, from: Vec3, to: Vec3, lineWidth: Float, color: Color) {
        val render = mc.renderViewEntity
        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.worldRenderer
        val realX: Double = render.lastTickPosX + (render.posX - render.lastTickPosX) * event.partialTicks
        val realY: Double = render.lastTickPosY + (render.posY - render.lastTickPosY) * event.partialTicks
        val realZ: Double = render.lastTickPosZ + (render.posZ - render.lastTickPosZ) * event.partialTicks
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
        bufferBuilder.pos(from.xCoord, from.yCoord, from.zCoord).color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f).endVertex();
        bufferBuilder.pos(to.xCoord, to.yCoord, to.zCoord).color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f).endVertex();
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
}
