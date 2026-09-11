data class Vector2D(val x: Float, val y: Float){
    fun distanceTo(other: Vector2D): Float{
        val dx =other.x - x
        val dy = other.y - y
        val oper = kotlin.math.sqrt(dx * dx + dy * dy)
        return oper
    }

    fun plus(deltaX: Float, deltaY: Float): Vector2D{
        val xt = deltaX + x
        val yt = deltaY + y
        return Vector2D(xt, yt)
    }

    fun magnitude(): Float{
        val oper = kotlin.math.sqrt(x * x + y * y)
        return oper
    }

}