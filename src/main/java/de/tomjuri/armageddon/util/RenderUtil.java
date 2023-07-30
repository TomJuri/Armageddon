package de.tomjuri.armageddon.util;

import de.tomjuri.armageddon.Armageddon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderUtil {

    public static void drawBlockBox(RenderWorldLastEvent event, BlockPos block, Color color) {
        double x = block.getX();
        double y = block.getY();
        double z = block.getZ();
        Vec3 frontTopLeft = new Vec3(x, y + 1, z);
        Vec3 frontTopRight = new Vec3(x, y + 1, z + 1);
        Vec3 frontBottomLeft = new Vec3(x, y, z);
        Vec3 frontBottomRight = new Vec3(x, y, z + 1);
        Vec3 backTopLeft = new Vec3(x + 1, y + 1, z + 1);
        Vec3 backTopRight = new Vec3(x + 1, y + 1, z);
        Vec3 backBottomLeft = new Vec3(x + 1, y, z + 1);
        Vec3 backBottomRight = new Vec3(x + 1, y, z);
        drawLine(event, frontTopLeft, frontTopRight, 2f, color);
        drawLine(event, frontTopRight, frontBottomRight, 2f, color);
        drawLine(event, frontBottomRight, frontBottomLeft, 2f, color);
        drawLine(event, frontBottomLeft, frontTopLeft, 2f, color);
        drawLine(event, backTopLeft, backTopRight, 2f, color);
        drawLine(event, backTopRight, backBottomRight, 2f, color);
        drawLine(event, backBottomRight, backBottomLeft, 2f, color);
        drawLine(event, backBottomLeft, backTopLeft, 2f, color);
        drawLine(event, frontTopLeft, backTopRight, 2f, color);
        drawLine(event, frontTopRight, backTopLeft, 2f, color);
        drawLine(event, frontBottomLeft, backBottomRight, 2f, color);
        drawLine(event, frontBottomRight, backBottomLeft, 2f, color);
    }

    public void drawLine(RenderWorldLastEvent event, Vec3 from, Vec3 to, float lineWidth, Color color) {
        Entity render = Armageddon.INSTANCE.getMinecraft().getRenderViewEntity();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer bufferBuilder = tessellator.getWorldRenderer();
        double realX = render.lastTickPosX + (render.posX - render.lastTickPosX) * event.partialTicks;
        double realY = render.lastTickPosY + (render.posY - render.lastTickPosY) * event.partialTicks;
        double realZ = render.lastTickPosZ + (render.posZ - render.lastTickPosZ) * event.partialTicks;
        GlStateManager.pushMatrix();
        GlStateManager.translate(-realX, -realY, -realZ);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GL11.glDisable(3553);
        GL11.glLineWidth(lineWidth);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1f, 1f, 1f, 1f);
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(from.xCoord, from.yCoord, from.zCoord).color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f).endVertex();
        bufferBuilder.pos(to.xCoord, to.yCoord, to.zCoord).color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f).endVertex();
        tessellator.draw();
        GlStateManager.translate(realX, realY, realZ);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    public static void renderText(BlockPos pos, String text) {
        double renderPosX = Armageddon.INSTANCE.getMinecraft().getRenderManager().viewerPosX;
        double renderPosY = Armageddon.INSTANCE.getMinecraft().getRenderManager().viewerPosY;
        double renderPosZ = Armageddon.INSTANCE.getMinecraft().getRenderManager().viewerPosZ;
        double x = pos.getX() - renderPosX;
        double y = pos.getY() - renderPosY;
        double z = pos.getZ() - renderPosZ;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);
        GlStateManager.rotate(-Armageddon.INSTANCE.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(Armageddon.INSTANCE.getMinecraft().getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
        double scale = 0.005 * Math.sqrt(Armageddon.INSTANCE.getPlayer().getDistanceSq(pos));
        GlStateManager.scale(-scale, -scale, scale);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Armageddon.INSTANCE.getMinecraft().fontRendererObj.drawString(text, 0, 0, 0xFFFFFF);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
