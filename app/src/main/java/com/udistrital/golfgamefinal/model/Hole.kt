class Hole (val posicion: Vector2D){
    fun containsBall(ballPos: Vector2D): Boolean{
    val hoyo = 0.5
        var distancia = posicion.distanceTo(ballPos)
        return distancia <= hoyo
    }
}