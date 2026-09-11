enum class GameStatus {
    READY, //listo para jugar
    WAITING_FOR_SWING, // Esperando que muevas el telefono
    DETECTING_SWING, // Detectando el movimiento
    PROCESSING_SHOT, // Calculando donde va la pelota
    BALL_STOPPED, // La pelota se detuvo
    HOLE_COMPLETED, // La pelota entro al hoyo
   RESETTING // Reiniciando
}