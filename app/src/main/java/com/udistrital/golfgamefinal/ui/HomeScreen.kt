package com.udistrital.golfgamefinal.ui

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

@Composable
fun HomeScreen (
    innerPadding: PaddingValues,
    onClickGame: () -> Unit
){
    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()){
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Bienvenido al prototipo de Golf")
            Text(text = "Tu mejor puntaje es:")
            Button(
                onClick = onClickGame
            ){
                Text("Iniciar")
            }
        }
    }
}