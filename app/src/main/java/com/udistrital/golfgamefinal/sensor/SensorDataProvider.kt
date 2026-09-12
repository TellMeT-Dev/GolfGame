package com.udistrital.golfgamefinal.sensor
//
///**
// * Interfaz de abstracción para la captura de lecturas de sensores de movimiento.
// * Permite desacoplar el hardware nativo de Android de la lógica de detección,
// * facilitando pruebas unitarias mediante mocks o inyección de datos simulados.
// */
interface SensorDataProvider {
    /**
     * Inicia la captura continua de eventos de sensores y despacha los datos al listener proporcionado.
     *
     * @param listener Callback invocado en cada nueva lectura de movimiento.
     */
    fun startListening(listener: (SensorData) -> Unit)

    /**
     * Detiene la captura y libera los listeners para ahorrar batería y evitar fugas de memoria.
     */
    fun stopListening()

    /**
     * Comprueba si el hardware del dispositivo cuenta con los sensores requeridos.
     */
    fun isSensorAvailable(): Boolean
}
