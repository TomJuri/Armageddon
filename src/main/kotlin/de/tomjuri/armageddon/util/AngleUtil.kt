package de.tomjuri.armageddon.util

import net.minecraft.init.Blocks
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.MathHelper
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.Vec3
import org.apache.commons.lang3.RandomUtils
import kotlin.math.atan2
import kotlin.math.sqrt


object AngleUtil {

    fun getRotationForBlock(bp: BlockPos): RotationUtil.Rotation? {
        for (point in getPoints(bp)) {
            val deltaX = point.xCoord - player.posX
            val deltaY = point.yCoord - player.posY - player.getEyeHeight()
            val deltaZ = point.zCoord - player.posZ
            val dist = sqrt(deltaX * deltaX + deltaZ * deltaZ)
            var pitch = (-atan2(dist, deltaY)).toFloat()
            var yaw = atan2(deltaZ, deltaX).toFloat()
            pitch = MathHelper.wrapAngleTo180_double((pitch * 180f / Math.PI + 90) * -1).toFloat()
            yaw = MathHelper.wrapAngleTo180_double(yaw * 180 / Math.PI - 90).toFloat()
            return RotationUtil.Rotation(yaw, pitch)
        }
        return null
    }

    private fun getPoints(bp: BlockPos): MutableList<Vec3> {
        if(world.getBlockState(bp).block == Blocks.air) return mutableListOf(Vec3(bp.x + 0.5, bp.y + 0.5, bp.z + 0.5))
        val points = mutableListOf<Vec3>()
        val pointsOnBlock = getPointsOnBlock(bp)
        val playerPosition = Vec3(player.posX, player.posY + 1.54, player.posZ)
        for (point in pointsOnBlock) {
            val mop = player.worldObj.rayTraceBlocks(playerPosition, point)
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                if (mop.blockPos == bp) {
                    points.add(point)
                }
            }
        }
        return points
    }

    private fun getPointsOnBlock(bp: BlockPos): List<Vec3> {
        val points = mutableListOf<Vec3>()
        val blockSides = mapOf(
            EnumFacing.UP to floatArrayOf(0.5f, 0.99f, 0.5f),
            EnumFacing.DOWN to floatArrayOf(0.5f, 0.01f, 0.5f),
            EnumFacing.NORTH to floatArrayOf(0.5f, 0.5f, 0.01f),
            EnumFacing.EAST to floatArrayOf(0.99f, 0.5f, 0.5f),
            EnumFacing.SOUTH to floatArrayOf(0.5f, 0.5f, 0.99f),
            EnumFacing.WEST to floatArrayOf(0.01f, 0.5f, 0.5f)
        )
        for (side in blockSides.values) {
            for (i in 0..19) {
                var x = side[0]
                var y = side[1]
                var z = side[2]
                if (x.toDouble() == 0.5) x = RandomUtils.nextFloat(0.1f, 0.9f)
                if (y.toDouble() == 0.5) y = RandomUtils.nextFloat(0.1f, 0.9f)
                if (z.toDouble() == 0.5) z = RandomUtils.nextFloat(0.1f, 0.9f)
                points.add(Vec3(bp).addVector(x.toDouble(), y.toDouble(), z.toDouble()))
            }
        }
        return points
    }
}