package com.udistrital.golfgamefinal.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.udistrital.golfgamefinal.sensor.SwingDetector
import com.udistrital.golfgamefinal.engine.GameEngine
import com.udistrital.golfgamefinal.model.GameState
import com.udistrital.golfgamefinal.model.Shot
import com.udistrital.golfgamefinal.model.Vector2D
import com.udistrital.golfgamefinal.sensor.AndroidSensorManager
import com.udistrital.golfgamefinal.sensor.SensorData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class GameViewModel(
    application: Application
) : AndroidViewModel(application) {

    val gameEngine = GameEngine()

    private val sensorManager =
        AndroidSensorManager(application)

    private val swingDetector =
        SwingDetector()

    private val _gameState =
        MutableStateFlow(
            gameEngine.getCurrentState()
        )

    val gameState: StateFlow<GameState> =
        _gameState

    private val _currentAimDirection =
        MutableStateFlow(
            swingDetector.getAimDirection()
        )

    val currentAimDirection:
            StateFlow<Float> =
        _currentAimDirection

    private val _ballPosition =
        MutableStateFlow(Vector2D(0f, 0f))

    val ballPosition: StateFlow<Vector2D> =
        _ballPosition

    private val _holePosition =
        MutableStateFlow(Vector2D(0f, 0f))

    val holePosition: StateFlow<Vector2D> =
        _holePosition

    private val _currentHole = MutableStateFlow(1)

    val currentHole: StateFlow<Int> =
        _currentHole

    private val _az = MutableStateFlow(0f)

    private var velX = 0f
    private var velY = 0f
    private var screenWidth = 0f
    private var screenHeight = 0f
    private var density = 1f
    private var holeIndex = 0
    private var holePositions = listOf<Vector2D>()
    private val _golpes = MutableStateFlow(0)
    val golpes: StateFlow<Int> = _golpes

    fun initScreen(width: Float, height: Float, d: Float) {
        screenWidth = width
        screenHeight = height
        density = d
        holePositions = listOf(
            Vector2D(width / 2f + 200f / d, height / 6f + 200f / d),
            Vector2D(width * 0.25f, height * 0.45f),
            Vector2D(width / 2f, height /3f)
        )
        holeIndex = 0
        _ballPosition.value =
            Vector2D(width / 2f, height * 4f / 6f)
        _holePosition.value = holePositions[0]
    }

    fun startGameLoop() {
        viewModelScope.launch {
            while (isActive) {
                val az = _az.value
                val dir =
                    _currentAimDirection.value

                if (az > 14f) {
                    val force = (az - 14f) * 2.0f
                    velX = force * cos(dir)
                    velY = force * sin(dir)
                    _golpes.value = gameEngine.golpes
                }

                val pos = _ballPosition.value
                var newX = pos.x + velX
                var newY = pos.y + velY

                val rectLeft = screenWidth * 0.10f
                val rectRight = screenWidth * 0.90f
                val rectTop = screenHeight * 0.125f
                val rectBottom = screenHeight * 0.85f

                newX = newX.coerceIn(rectLeft, rectRight)
                newY = newY.coerceIn(rectTop, rectBottom)

                _ballPosition.value =
                    Vector2D(newX, newY)

                val hp = _holePosition.value
                val bp = _ballPosition.value
                val dist = kotlin.math.sqrt(
                    (bp.x - hp.x) * (bp.x - hp.x) +
                    (bp.y - hp.y) * (bp.y - hp.y)
                )
                if (dist < 80f / density) {
                    holeIndex = (holeIndex + 1) % holePositions.size
                    _currentHole.value ++
                    _holePosition.value = holePositions[holeIndex]
                    _ballPosition.value =
                        Vector2D(screenWidth / 2f, screenHeight * 4f / 6f)
                    velX = 0f
                    velY = 0f
                    _golpes.value = 0
                }

                velX *= 0.94f
                velY *= 0.94f
                if (abs(velX) < 0.01f) velX = 0f
                if (abs(velY) < 0.01f) velY = 0f

                delay(16L)
            }
        }
    }

    fun onStartSwingDetection() {
        if (!sensorManager.isSensorAvailable()) {
            return
        }
        swingDetector.resetDetector()
        sensorManager.startListening { data ->
            onSensorChanged(data)
        }
    }

    fun onStopSwingDetection() {
        sensorManager.stopListening()
    }

    fun onSensorChanged(data: SensorData) {
        _currentAimDirection.value =
            swingDetector.getAimDirection()

        _az.value = data.az

        val shot =
            swingDetector.processSensorData(data)

        if (shot != null) {
            gameEngine.processShot(shot)
            _golpes.value = gameEngine.golpes
            updateGameState()
        }
    }

    fun onResetClicked() {
        gameEngine.resetHole()
        _golpes.value = 0
        swingDetector.resetDetector()
        holeIndex = 0
        _currentHole.value = 1
        _holePosition.value = holePositions[0]
        _ballPosition.value =
            Vector2D(screenWidth / 2f, screenHeight * 4f / 6f)
        velX = 0f
        velY = 0f
        updateGameState()
    }

    fun onHoleSelected(holeIndex: Int) {
        gameEngine.loadHoleByIndex(holeIndex)
        updateGameState()
    }

    private fun updateGameState() {
        _gameState.value =
            gameEngine.getCurrentState()
    }

    override fun onCleared() {
        sensorManager.stopListening()
        super.onCleared()
    }
}
