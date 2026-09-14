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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udistrital.golfgamefinal.R
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GameScreen(
    innerPadding: PaddingValues,
    onClickGame: () -> Unit
) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val density = LocalDensity.current.density
    val screenWidth = config.screenWidthDp.toFloat()
    val screenHeight = config.screenHeightDp.toFloat()

    val gameViewModel = remember {
        GameViewModel(context.applicationContext as Application)
    }

    val ballPos by gameViewModel.ballPosition.collectAsState()
    val aimDir by gameViewModel.currentAimDirection.collectAsState()
    val currentHole by gameViewModel.currentHole.collectAsState()
    val holePos by gameViewModel.holePosition.collectAsState()

    LaunchedEffect(Unit) {
        gameViewModel.initScreen(screenWidth, screenHeight, density)
        gameViewModel.onStartSwingDetection()
        gameViewModel.startGameLoop()
    }

    val gameBitmap = remember {
        BitmapFactory.decodeResource(
            context.resources,
            R.drawable.game
        ).asImageBitmap()
    }

    val textMeasurer = rememberTextMeasurer()

    val labelHole = remember {
        textMeasurer.measure(
            AnnotatedString("Hoyo"),
            TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }

    val labelTime = remember {
        textMeasurer.measure(
            AnnotatedString("Tiempo"),
            TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }

    val textTime = remember {
        textMeasurer.measure(
            AnnotatedString("1:00"),
            TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }

    val textHole = remember(currentHole) {
        textMeasurer.measure(
            AnnotatedString(currentHole.toString()),
            TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }

    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        Canvas(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            drawImage(
                image = gameBitmap,
                dstSize = IntSize(
                    config.screenWidthDp * 4,
                    config.screenHeightDp * 4
                )
            )

            // Panel Hoyo
            drawRoundRect(
                color = Color(0, 101, 5, 255),
                topLeft = Offset(size.width / 16, size.height / 70),
                size = Size(size.width / 4, 200f),
                cornerRadius = CornerRadius(20f, 20f)
            )
            drawText(
                textLayoutResult = labelHole,
                topLeft = Offset(size.width / 9, size.height / 70),
                color = Color(101, 255, 122, 255)
            )
            drawText(
                textLayoutResult = textHole,
                topLeft = Offset(size.width / 6, size.height / 18),
                color = Color(210, 255, 212, 255)
            )

            // Panel Tiempo
            drawRoundRect(
                color = Color(0, 46, 91, 255),
                topLeft = Offset(
                    size.width / 2 - size.width / 8,
                    size.height / 70
                ),
                size = Size(size.width / 4, 200f),
                cornerRadius = CornerRadius(20f, 20f)
            )
            drawText(
                textLayoutResult = labelTime,
                topLeft = Offset(
                    size.width / 2 - size.width / 10,
                    size.height / 70
                ),
                color = Color(101, 172, 255, 255)
            )
            drawText(
                textLayoutResult = textTime,
                topLeft = Offset(
                    size.width / 2 - size.width / 14,
                    size.height / 18
                ),
                color = Color(210, 227, 255, 255)
            )

            // Campo de juego (rectángulo verde)
            drawRoundRect(
                color = Color(70, 148, 67, 255),
                topLeft = Offset(
                    (size.width - size.width * 0.80f) / 2f,
                    (size.height - size.height * 0.75f) / 2f
                ),
                size = Size(size.width * 0.80f, size.height * 0.9f),
                cornerRadius = CornerRadius(80f, 80f)
            )
            drawRoundRect(
                color = Color(9, 117, 0, 255),
                topLeft = Offset(
                    (size.width - size.width * 0.80f) / 2f,
                    (size.height - size.height * 0.75f) / 2f
                ),
                size = Size(size.width * 0.80f, size.height * 0.9f),
                cornerRadius = CornerRadius(80f, 80f),
                Stroke(width = 2.dp.toPx())
            )

            // Flecha amarilla
            val ballScreenX = ballPos.x * density
            val ballScreenY = ballPos.y * density
            drawLine(
                color = Color(255, 242, 0, 174),
                start = Offset(ballScreenX, ballScreenY),
                end = Offset(
                    ballScreenX + cos(aimDir) * 300f,
                    ballScreenY + sin(aimDir) * 300f
                ),
                strokeWidth = 20f
            )

            // Bola blanca
            drawCircle(
                color = Color(255, 255, 255),
                radius = 30f,
                center = Offset(ballScreenX, ballScreenY)
            )

            // Hoyo
            drawCircle(
                color = Color.Black,
                radius = 50f,
                center = Offset(
                    holePos.x * density,
                    holePos.y * density
                )
            )
            drawCircle(
                color = Color(118, 204, 128, 255),
                radius = 50f,
                center = Offset(
                    holePos.x * density,
                    holePos.y * density
                ),
                style = Stroke(width = 2.dp.toPx())
            )
        }

        // Botón Volver
        Button(
            onClick = onClickGame,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(
                    x = (config.screenWidthDp * 12f / 17f).dp,
                    y = (config.screenHeightDp / 35f).dp
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0, 46, 91, 255),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(text = "Volver")
        }

        // Botón Reiniciar
        Button(
            onClick = {
                gameViewModel.onResetClicked()
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(
                    x = (config.screenWidthDp * 13f / 19f).dp,
                    y = (config.screenHeightDp / 12f).dp
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0, 46, 91, 255),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(text = "Reiniciar")
        }
    }
}
