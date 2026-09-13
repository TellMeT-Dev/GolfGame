package com.udistrital.golfgamefinal.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.udistrital.golfgamefinal.R

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen (
    innerPadding: PaddingValues,
    onClickGame: () -> Unit
){
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val textMeasurer = rememberTextMeasurer()
    val labelGolf = textMeasurer.measure(
        AnnotatedString("Golf Game"),
        TextStyle(
            fontSize = 45.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    )
    Canvas(modifier = Modifier.fillMaxSize()){
        drawImage(
            image = BitmapFactory.decodeResource(context.resources ,R.drawable.portada).asImageBitmap(),
            dstSize = IntSize(config.screenWidthDp*4,config.screenHeightDp*4)
        )
    }
    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()){
        Canvas(modifier = Modifier.padding(innerPadding).fillMaxSize()) {

            drawRoundRect(
                color = Color(39, 131, 37, 255),
                topLeft = Offset(
                    x= size.width/6,
                    y= size.height*2/8
                ),
                size = Size(size.width/1.5f, size.height/8),
                cornerRadius = CornerRadius(40f, 40f)
            )

            drawText(
                textLayoutResult = labelGolf,
                topLeft = Offset(
                    (size.width /4),
                    (size.height /3.5f)
                ),
                color = Color(255,255,255),

                )
        }
    }
    Column(
        modifier = Modifier.padding(innerPadding).fillMaxSize()
    ) {
        Button(
            onClick = onClickGame,
            shape = RoundedCornerShape(
                topStart = 10.dp,
                topEnd = 10.dp,
                bottomStart = 10.dp,
                bottomEnd = 10.dp,
            ),
            modifier = Modifier.offset(
                x = (LocalConfiguration.current.screenWidthDp/4).dp,
                y = (LocalConfiguration.current.screenHeightDp*2.2/4).dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0, 46, 91, 255),
                contentColor = Color.White
            ),

        ) {
            Text(
                text = "Iniciar Juego",
                fontSize = 25.sp
            )
        }
    }
}