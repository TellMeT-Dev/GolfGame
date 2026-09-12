package com.udistrital.golfgamefinal.sensor
//
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
//
///**
// * Implementación de SensorDataProvider basada en el framework oficial de sensores de Android.
// * Gestiona el registro y desregistro seguro de los listeners para el Acelerómetro y Giroscopio.
// *
// * @param context Contexto de la aplicación Android para acceder al SensorManager del sistema.
// */
class AndroidSensorManager(
    context: Context
) : SensorDataProvider {

    companion object {
        private const val TAG = "AndroidSensorManager"
    }

    private val sensorManager: SensorManager? =
        context.applicationContext.getSystemService(Context.SENSOR_SERVICE) as? SensorManager

    private val accelerometer: Sensor? =
        sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val gyroscope: Sensor? =
        sensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    private var sensorListener: SensorEventListener? = null
    private var dataCallback: ((SensorData) -> Unit)? = null

    // Últimos valores almacenados para combinar lecturas sincronizadas
    private var lastAx = 0f
    private var lastAy = 0f
    private var lastAz = 9.81f
    private var lastGx = 0f
    private var lastGy = 0f
    private var lastGz = 0f

    @Volatile
    private var isListening = false

    override fun startListening(listener: (SensorData) -> Unit) {
        if (isListening) {
            stopListening()
        }

        val sm = sensorManager
        if (sm == null || accelerometer == null) {
            Log.w(TAG, "El dispositivo no cuenta con SensorManager o Acelerómetro disponible.")
            return
        }

        dataCallback = listener
        isListening = true

        val eventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null || !isListening) return

                val timestampMs = System.currentTimeMillis()

                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        lastAx = event.values.getOrElse(0) { 0f }
                        lastAy = event.values.getOrElse(1) { 0f }
                        lastAz = event.values.getOrElse(2) { 9.81f }

                        val data = SensorData(
                            ax = lastAx,
                            ay = lastAy,
                            az = lastAz,
                            gx = lastGx,
                            gy = lastGy,
                            gz = lastGz,
                            timestamp = timestampMs
                        )
                        dataCallback?.invoke(data)
                    }

                    Sensor.TYPE_GYROSCOPE -> {
                        lastGx = event.values.getOrElse(0) { 0f }
                        lastGy = event.values.getOrElse(1) { 0f }
                        lastGz = event.values.getOrElse(2) { 0f }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // No se requiere ajuste para cambios de precisión en este contexto
            }
        }

        this.sensorListener = eventListener

        // Registrar acelerómetro con tasa de muestreo adecuada para juegos
        sm.registerListener(eventListener, accelerometer, SensorManager.SENSOR_DELAY_GAME)

        // Registrar giroscopio si está presente
        gyroscope?.let { gyro ->
            sm.registerListener(eventListener, gyro, SensorManager.SENSOR_DELAY_GAME)
        }

        Log.d(TAG, "Sensor listeners registrados con éxito.")
    }

    override fun stopListening() {
        if (!isListening) return

        isListening = false
        sensorListener?.let { listener ->
            try {
                sensorManager?.unregisterListener(listener)
                Log.d(TAG, "Sensor listeners desregistrados correctamente.")
            } catch (e: Exception) {
                Log.e(TAG, "Error al desregistrar sensor listeners", e)
            }
        }
        sensorListener = null
        dataCallback = null
    }

    override fun isSensorAvailable(): Boolean {
        return sensorManager != null && accelerometer != null
    }
}
