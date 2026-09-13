package com.udistrital.golfgamefinal.engine

import com.udistrital.golfgamefinal.model.Ball
import com.udistrital.golfgamefinal.model.GameState
import com.udistrital.golfgamefinal.model.GameStatus
import com.udistrital.golfgamefinal.model.Hole
import com.udistrital.golfgamefinal.model.Shot
import com.udistrital.golfgamefinal.model.Vector2D

class GameEngine {

    companion object { // el transformar los val en const val aportacion de IA
        const val pared_izq = 0f
        const val pared_der = 1f
        const val pared_arri = 0f
        const val pared_baj = 1f
        const val radio_bola = 0.015f
        const val friccion = 0.96f //aportacion IA
        const val STOP_THRESHOLD = 0.0005f //aportacion IA
        const val MAX_STROKE_DISTANCE = 0.4f // aportacion IA
    }

    private var ball = Ball(
        posicion_i = Vector2D(0.5f, 0.8f),
        posicion = Vector2D(0.5f, 0.8f)
    )
    private val holes = listOf(
        Hole(
            posicion = Vector2D(0.5f, 0.2f)
        ),
        Hole(
            posicion = Vector2D(0.2f, 0.3f)
        ),
        Hole(
            posicion = Vector2D(0.8f, 0.25f)
        )
    )

    private var currentHoleIndex = 0

    private var hole = holes[currentHoleIndex]
    private var velocity = Vector2D(0f, 0f)
    private var golpes = 0
    private var gameStatus = GameStatus.READY
    private var isGameFinished = false


    // Agregacion de la seleccion de hoyos
    fun loadHoleByIndex(index: Int) {
        if (index !in holes.indices) return

        currentHoleIndex = index
        hole = holes[currentHoleIndex]

        ball = Ball(
            posicion_i = ball.posicion_i,
            posicion = ball.posicion_i
        )

        velocity = Vector2D(0f, 0f)
        golpes = 0
        gameStatus = GameStatus.READY
    }

    // carga del nuevo hoyo
    fun loadNextHole() {
        if (currentHoleIndex < holes.lastIndex) {
            currentHoleIndex++
            hole = holes[currentHoleIndex]

            ball = Ball(
                posicion_i = ball.posicion_i,
                posicion = ball.posicion_i
            )

            velocity = Vector2D(0f, 0f)
            golpes = 0
            gameStatus = GameStatus.READY
        } else {
            isGameFinished = true
            gameStatus = GameStatus.HOLE_COMPLETED
        }
    }


    fun getCurrentState(): GameState {
        return GameState(
            ball = ball,
            gameStatus = gameStatus,
            hole = hole,
            shot = Shot(fuerza = 0f, angulo = 0f, movimiento = Vector2D(0f, 0f)),
            strokesCount = golpes,
            isGameFinished = isGameFinished
        )
    }

    fun getStrokesCount(): Int = golpes
    fun getGameStatus(): GameStatus = gameStatus
    fun isGameFinished(): Boolean = isGameFinished

    fun processShot(shot: Shot) {
        val movement = shot.movimiento
        val magnitude = movement.magnitude()

        if (magnitude > MAX_STROKE_DISTANCE) {
            val scale = MAX_STROKE_DISTANCE / magnitude
            velocity = Vector2D(movement.x * scale, movement.y * scale)
        } else {
            velocity = movement
        }

        golpes++
        gameStatus = GameStatus.PROCESSING_SHOT
    }

    fun updatePhysics() { //aportacion IA
        if (gameStatus != GameStatus.PROCESSING_SHOT) return

        val newX = ball.posicion.x + velocity.x
        val newY = ball.posicion.y + velocity.y

        var bouncedX = newX
        var bouncedY = newY
        var newVelX = velocity.x
        var newVelY = velocity.y

        if (newX - radio_bola < pared_izq) {
            bouncedX = radio_bola
            newVelX = -velocity.x * 0.8f
        } else if (newX + radio_bola > pared_der) {
            bouncedX = pared_der - radio_bola
            newVelX = -velocity.x * 0.8f
        }

        if (newY - radio_bola < pared_arri) {
            bouncedY = radio_bola
            newVelY = -velocity.y * 0.8f
        } else if (newY + radio_bola > pared_baj) {
            bouncedY = pared_baj - radio_bola
            newVelY = -velocity.y * 0.8f
        }

        ball = Ball(posicion_i = ball.posicion_i, posicion = Vector2D(bouncedX, bouncedY))
        velocity = Vector2D(newVelX * friccion, newVelY * friccion)

        if (velocity.magnitude() < STOP_THRESHOLD) {
            velocity = Vector2D(0f, 0f)
            if (hole.containsBall(ball.posicion)) {
                gameStatus = GameStatus.HOLE_COMPLETED
            } else {
                gameStatus = GameStatus.BALL_STOPPED
            }
        }
    }

    fun checkHoleCompleted(): Boolean {
        return hole.containsBall(ball.posicion)
    }

    fun resetHole() {
        ball = Ball(
            posicion_i = ball.posicion_i,
            posicion = ball.posicion_i
        )
        velocity = Vector2D(0f, 0f)
        golpes = 0
        gameStatus = GameStatus.READY
    }

    fun restartEntireGame() {
        currentHoleIndex = 0
        hole = holes[currentHoleIndex]
        resetHole()
        isGameFinished = false
    }

    fun setGameStatus(status: GameStatus) {
        gameStatus = status
    }
}
