package com.udistrital.golfgamefinal.model

data class GameState (val ball: Ball, val gameStatus: GameStatus,
                      val hole: Hole, val shot: Shot,
                      val strokesCount: Int, val isGameFinished: Boolean
){

}