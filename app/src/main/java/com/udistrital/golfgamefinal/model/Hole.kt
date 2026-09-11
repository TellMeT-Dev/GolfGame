class Hole (val posicion: Vector2D){
    fun containsBall(ballPos: Vector2D): Boolean{
    return ballPos == posicion
    }
}