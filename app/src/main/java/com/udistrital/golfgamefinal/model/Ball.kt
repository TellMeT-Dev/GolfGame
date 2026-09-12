package com.udistrital.golfgamefinal.model

data class Ball (val posicion_i: Vector2D,
                 var posicion: Vector2D
){
    fun reset(): Vector2D {
        return posicion_i
    }

    fun moveTo(newPosition: Vector2D): Vector2D {
        posicion = newPosition
        return posicion
    }

}