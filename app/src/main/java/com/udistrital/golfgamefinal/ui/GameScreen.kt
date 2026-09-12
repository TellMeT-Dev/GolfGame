package com.udistrital.golfgamefinal.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.unit.dp

@Composable
fun GameScreen (
    innerPadding: PaddingValues, onClickGame: () -> Unit
){
    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()){
        Canvas(modifier = Modifier.padding(innerPadding).fillMaxSize() ){
            drawRect(
                color = Color(5, 124, 0, 255),
                topLeft = Offset(
                    (size.width - size.width*0.80f) / 2f,
                    (size.height - size.height*0.75f) / 2f
                ),
                size = Size(size.width*0.80f, size.height*0.9f)
            )

            drawLine(
                color = Color(255, 244, 39, 101),
                start = Offset(
                    size.width /2f,
                    (size.height/6f)*5f
                ),
                end = Offset(
                    (size.width /2f)+200f,
                    (size.height /6f)*5 -200f
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
                radius = 30f,
                center = Offset(
                    (size.width /2f)+200f,
                    (size.height/6f)+ 200f
                )
            )


        }
    }
}