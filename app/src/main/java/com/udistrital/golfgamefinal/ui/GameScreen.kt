package com.udistrital.golfgamefinal.ui

import android.app.Application
import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udistrital.golfgamefinal.model.Ball
import com.udistrital.golfgamefinal.model.Vector2D
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GameScreen (
    innerPadding: PaddingValues, onClickGame: () -> Unit
){



    val context = LocalContext.current
    val config = LocalConfiguration.current
    var actualHole by remember { mutableStateOf(0) }


    val gameViewModel = remember{ GameViewModel(context.applicationContext as Application) }

    LaunchedEffect(Unit) {
        gameViewModel.onStartSwingDetection()
    }

    val zAxis by gameViewModel.currentAimDirection.collectAsState()

    var calibrationOffset by remember { mutableStateOf(0f) }
    var calibrationSamples by remember { mutableStateOf(0) }
    var isCalibrated by remember { mutableStateOf(false) }
    var calibrationSum by remember { mutableStateOf(0f) }
    val calibrationTarget = 10

    val rawAngle = zAxis
    val calibratedAngle = if (isCalibrated) rawAngle - calibrationOffset else rawAngle
    val zDegree = Math.toDegrees(calibratedAngle.toDouble()).toFloat()

    if (!isCalibrated) {
        val magnitude = gameViewModel.currentAccelerationMagnitude.collectAsState().value
        val isStationary = magnitude in 9.0f..10.6f
                if (isStationary) {
                    calibrationSum += zAxis
                            calibrationSamples++
                            if (calibrationSamples >= calibrationTarget) {
                                calibrationOffset = calibrationSum / calibrationTarget
                                isCalibrated = true
                            }
                } else {
                    calibrationSamples = 0
                    calibrationSum = 0f
                }
    }

    val ball: Ball(
        Vector2D(

        ),
        Vector2D(

        )
    )

    val vectorBall = ball.reset()


    Canvas(Modifier.fillMaxSize()) {
        drawImage(
            image = BitmapFactory.decodeResource(context.resources , com.udistrital.golfgamefinal.R.drawable.game).asImageBitmap(),
            dstSize = IntSize(config.screenWidthDp*4,config.screenHeightDp*4)
        )
    }
    val textMeasurer = rememberTextMeasurer()
    val labelHole = textMeasurer.measure(
        AnnotatedString("Hoyo"),
        TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    )

    val textHole = textMeasurer.measure(
        AnnotatedString(actualHole.toString()),
        TextStyle(
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    )

    val labelTime = textMeasurer.measure(
        AnnotatedString("Tiempo"),
        TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    )

    val textTime = textMeasurer.measure(
        AnnotatedString("1:00"),
        TextStyle(
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    )

    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()){
        Canvas(modifier = Modifier.padding(innerPadding).fillMaxSize() ){

            drawRoundRect(
                color = Color(0, 101, 5, 255),
                topLeft = Offset(
                    (size.width /16),
                    (size.height /70)
                ),
                size = Size((size.width/4), 200f),
                cornerRadius = CornerRadius(20f,20f)
            )

            drawText(
                textLayoutResult = labelHole,
                topLeft = Offset(
                    (size.width /9),
                    (size.height /70)
                ),
                color = Color(101, 255, 122, 255),

            )

            drawText(
                textLayoutResult = textHole,
                topLeft = Offset(
                    (size.width /6),
                    (size.height /18)
                ),
                color = Color(210, 255, 212, 255),

            )

            drawRoundRect(
                color = Color(0, 46, 91, 255),
                topLeft = Offset(
                    (size.width /2)- (size.width/8),
                    (size.height /70)
                ),
                size = Size((size.width/4), 200f),
                cornerRadius = CornerRadius(20f,20f)
            )

            drawText(
                textLayoutResult = labelTime,
                topLeft = Offset(
                    (size.width /2)-(size.width/10),
                    (size.height /70)
                ),
                color = Color(101, 172, 255, 255),

            )

            drawText(
                textLayoutResult = textTime,
                topLeft = Offset(
                    (size.width /2)-(size.width/14),
                    (size.height /18)
                ),
                color = Color(210, 227, 255, 255),

            )

            drawRoundRect(
                color = Color(70, 148, 67, 255),
                topLeft = Offset(
                    (size.width - size.width*0.80f) / 2f,
                    (size.height - size.height*0.75f) / 2f
                ),
                size = Size(size.width*0.80f, size.height*0.9f),
                cornerRadius = CornerRadius(80f,80f)
            )

            drawRoundRect(
                color = Color(9, 117, 0, 255),
                topLeft = Offset(
                    (size.width - size.width*0.80f) / 2f,
                    (size.height - size.height*0.75f) / 2f
                ),
                size = Size(size.width*0.80f, size.height*0.9f),
                cornerRadius = CornerRadius(80f,80f),
                Stroke(width = 2.dp.toPx())
            )

            drawLine(
                color = Color(255, 242, 0, 174),
                start = Offset(
                    size.width /2f,
                    (size.height/6f)*5f
                ),
                end = Offset(
                    (size.width /2f) + (cos(calibratedAngle) * 300f),
                    ((size.height/6f)*5f) + (sin(calibratedAngle) * 300f)
                ),
                strokeWidth = 20f
            )

            drawCircle(
                color = Color.White,
                radius = 30f,
                center = Offset(
                    size.width /2f,
                    (size.height/6f)*5f
                )
            )

            drawCircle(
                color = Color.Black,
                radius = 50f,
                center = Offset(
                    (size.width /2f)+200f,
                    (size.height/6f)+ 200f
                )
            )

            drawCircle(
                color = Color(118, 204, 128, 255),
                radius = 50f,
                center = Offset(
                    (size.width /2f)+200f,
                    (size.height/6f)+ 200f
                ),
                style = Stroke(width = 2.dp.toPx())
            )


        }

        Button(
            onClick = (onClickGame),
            modifier = Modifier.align(Alignment.TopStart)
                .offset(
                    x = (LocalConfiguration.current.screenWidthDp * 12f / 17f).dp,
                    y = (LocalConfiguration.current.screenHeightDp / 35f).dp
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0, 46, 91, 255),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(
                topStart = 10.dp,
                topEnd = 10.dp,
                bottomStart = 10.dp,
                bottomEnd = 10.dp,
            )

        ) {
            Text(text = "Volver")
        }

        Button(
            onClick = {
                actualHole += 1
            },
            modifier = Modifier.align(Alignment.TopStart)
                .offset(
                    x = (LocalConfiguration.current.screenWidthDp * 13f / 19f).dp,
                    y = (LocalConfiguration.current.screenHeightDp / 12f).dp
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0, 46, 91, 255),
                contentColor = Color.White

            ),
            shape = RoundedCornerShape(
                topStart = 10.dp,
                topEnd = 10.dp,
                bottomStart = 10.dp,
                bottomEnd = 10.dp,
            )

        ) {
            Text(text = "Reiniciar")
        }
    }
}