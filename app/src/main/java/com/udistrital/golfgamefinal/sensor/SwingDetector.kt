package com.udistrital.golfgame.sensor

import Shot
import Vector2D
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

class SwingDetector(
    val accelerationThreshold: Float = 14.0f,
    val noiseFilterFactor: Float = 0.92f,
    val cooldownMs: Long = 850L
) {
    private var gravityX = 0f
    private var gravityY = 0f
    private var gravityZ = 9.81f

    private var isGravityInitialized = false
    private var lastShotTimestamp: Long = 0L

    private var lastGyroX = 0f
    private var lastGyroY = 0f
    private var lastGyroZ = 0f
    private var hasGyroscope = false

    private var accumulatedAngleZ = -Math.PI.toFloat() / 2f
    private var lastGyroTimestamp: Long = 0L

    var isSwingActive: Boolean = false
        private set

    fun processSensorData(data: SensorData): Shot? {
        val now = data.timestamp
        if (now - lastShotTimestamp < cooldownMs) {
            return null
        }

        if (data.gx != 0f || data.gy != 0f || data.gz != 0f) {
            lastGyroX = data.gx
            lastGyroY = data.gy
            lastGyroZ = data.gz
            hasGyroscope = true
            updateAccumulatedAngle(data.gz, data.timestamp)
        }

        val filteredData = filterNoise(data)
        val dynamicMagnitude = calculateMagnitude(filteredData.ax, filteredData.ay, filteredData.az)

        if (dynamicMagnitude >= accelerationThreshold) {
            isSwingActive = true
            lastShotTimestamp = now

            val force = calculateForce(dynamicMagnitude)
            val directionAngle = if (hasGyroscope) {
                accumulatedAngleZ
            } else {
                calculateDirectionFromAccelerometer(filteredData.ax, filteredData.ay)
            }

            val deltaX = force * cos(directionAngle)
            val deltaY = force * sin(directionAngle)

            isSwingActive = false

            return Shot(
                fuerza = force,
                angulo= directionAngle,
                movimiento = Vector2D(deltaX, deltaY)
            )
        }

        return null
    }

    private fun updateAccumulatedAngle(gz: Float, timestamp: Long) {
        if (lastGyroTimestamp == 0L) {
            lastGyroTimestamp = timestamp
            return
        }

        val timeDeltaSeconds = (timestamp - lastGyroTimestamp) / 1000f
        lastGyroTimestamp = timestamp

        if (timeDeltaSeconds > 0f && timeDeltaSeconds < 0.5f) {
            accumulatedAngleZ += gz * timeDeltaSeconds
        }
    }

    fun filterNoise(data: SensorData): SensorData {
        if (!isGravityInitialized) {
            gravityX = data.ax
            gravityY = data.ay
            gravityZ = data.az
            isGravityInitialized = true
        } else {
            gravityX = noiseFilterFactor * gravityX + (1f - noiseFilterFactor) * data.ax
            gravityY = noiseFilterFactor * gravityY + (1f - noiseFilterFactor) * data.ay
            gravityZ = noiseFilterFactor * gravityZ + (1f - noiseFilterFactor) * data.az
        }

        val linearAx = data.ax - gravityX
        val linearAy = data.ay - gravityY
        val linearAz = data.az - gravityZ

        return SensorData(
            ax = linearAx,
            ay = linearAy,
            az = linearAz,
            gx = data.gx,
            gy = data.gy,
            gz = data.gz,
            timestamp = data.timestamp
        )
    }

    fun calculateMagnitude(ax: Float, ay: Float, az: Float): Float {
        return sqrt(ax * ax + ay * ay + az * az)
    }

    fun calculateForce(magnitude: Float): Float {
        val excess = max(0f, magnitude - accelerationThreshold)
        val scaledForce = 20f + (excess * 3.2f)
        return min(100f, max(0f, scaledForce))
    }

    fun getAimDirection(): Float {
        return accumulatedAngleZ
    }

    fun calculateDirectionFromAccelerometer(ax: Float, ay: Float): Float {
        val horizontalDeflection = ax
        val forwardThrust = if (ay != 0f) -kotlin.math.abs(ay) else -1f

        if (kotlin.math.abs(horizontalDeflection) < 0.8f) {
            return -Math.PI.toFloat() / 2f
        }

        return atan2(forwardThrust, horizontalDeflection)
    }

    fun resetDetector() {
        isSwingActive = false
        isGravityInitialized = false
        gravityX = 0f
        gravityY = 0f
        gravityZ = 9.81f
        lastShotTimestamp = 0L
        lastGyroX = 0f
        lastGyroY = 0f
        lastGyroZ = 0f
        hasGyroscope = false
        accumulatedAngleZ = -Math.PI.toFloat() / 2f
        lastGyroTimestamp = 0L
    }
}
