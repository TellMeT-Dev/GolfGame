package com.udistrital.golfgamefinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.udistrital.golfgamefinal.enums.TypeScreen
import com.udistrital.golfgamefinal.ui.GameScreen
import com.udistrital.golfgamefinal.ui.HomeScreen
import com.udistrital.golfgamefinal.ui.theme.GolfGameFinalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GolfGameFinalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var currentScreen by remember { mutableStateOf(TypeScreen.HOME) }

                    if(currentScreen == TypeScreen.HOME){
                        HomeScreen(innerPadding = innerPadding) {
                             currentScreen = TypeScreen.GAME
                        }
                    }else if(currentScreen == TypeScreen.GAME){
                        GameScreen(innerPadding = innerPadding){
                            currentScreen = TypeScreen.HOME
                        }
                    }
                }
            }
        }
    }
}