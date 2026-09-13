package com.udistrital.golfgamefinal.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.udistrital.golfgamefinal.sensor.SwingDetector
import com.udistrital.golfgamefinal.engine.GameEngine
import com.udistrital.golfgamefinal.model.GameState
import com.udistrital.golfgamefinal.sensor.AndroidSensorManager
import com.udistrital.golfgamefinal.sensor.SensorData
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

    fun getAimDirection(): Float {
        return swingDetector.getAimDirection()
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