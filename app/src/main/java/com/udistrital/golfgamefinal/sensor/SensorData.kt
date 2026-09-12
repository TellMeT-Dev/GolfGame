package com.udistrital.golfgamefinal.sensor

/**
 * Contenedor de datos unificados provenientes de los sensores del dispositivo móvil.
 *
 * @property ax Aceleración en el eje X en m/s².
 * @property ay Aceleración en el eje Y en m/s².
 * @property az Aceleración en el eje Z en m/s².
 * @property gx Velocidad angular en el eje X en rad/s.
 * @property gy Velocidad angular en el eje Y en rad/s.
 * @property gz Velocidad angular en el eje Z en rad/s.
 * @property timestamp Marca de tiempo en milisegundos de la captura.
 */
data class SensorData(
    val ax: Float,
    val ay: Float,
    val az: Float,
    val gx: Float = 0f,
    val gy: Float = 0f,
    val gz: Float = 0f,
    val timestamp: Long = System.currentTimeMillis()
)
