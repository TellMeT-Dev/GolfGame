package com.udistrital.golfgamefinal.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.udistrital.golfgamefinal.engine.GameEngine
import com.udistrital.golfgamefinal.model.GameState
import com.udistrital.golfgamefinal.model.GameStatus
import com.udistrital.golfgamefinal.model.Shot
import com.udistrital.golfgamefinal.model.Vector2D
import com.udistrital.golfgamefinal.sensor.AndroidSensorManager
import com.udistrital.golfgamefinal.sensor.SensorData
import com.udistrital.golfgamefinal.sensor.SwingDetector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val gameEngine = GameEngine()

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

    private val _currentAccelerationMagnitude =
        MutableStateFlow(0f)

    val currentAccelerationMagnitude:
            StateFlow<Float> =
        _currentAccelerationMagnitude

    private val _currentAimDirection =
        MutableStateFlow(
            swingDetector.getAimDirection()
        )

    val currentAimDirection:
            StateFlow<Float> =
        _currentAimDirection

    private val _showHomeScreen =
        MutableStateFlow(true)

    val showHomeScreen: StateFlow<Boolean> =
        _showHomeScreen


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

        val shot =
            swingDetector.processSensorData(data)

        if (shot != null) {

            gameEngine.processShot(shot)

            updateGameState()
        }
    }


    fun onResetClicked() {

        gameEngine.resetHole()

        swingDetector.resetDetector()

        updateGameState()
    }


    fun onBackToHome() {

        onStopSwingDetection()

        _showHomeScreen.value = true
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