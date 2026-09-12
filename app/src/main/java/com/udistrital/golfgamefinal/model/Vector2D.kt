package com.udistrital.golfgamefinal.model

import kotlin.math.sqrt

data class Vector2D(val x: Float, val y: Float){
    fun distanceTo(other: Vector2D): Float{
        val dx =other.x - x
        val dy = other.y - y
        val oper = sqrt(dx * dx + dy * dy)
        return oper
    }

    fun plus(deltaX: Float, deltaY: Float): Vector2D{
        val xt = deltaX + x
        val yt = deltaY + y
        return Vector2D(xt, yt)
    }

    fun magnitude(): Float{
        val oper = sqrt(x * x + y * y)
        return oper
    }

}