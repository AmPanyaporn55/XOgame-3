package com.example.xogame

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameHistoryDao {
    // การเรียกดูข้อมูล GameHistory จากฐานข้อมูล
    @Query("SELECT * FROM game_history")
    fun getAllGameHistories(): LiveData<List<GameHistory>>

    // เพิ่มข้อมูล GameHistory เข้าสู่ฐานข้อมูล
    @Insert
    suspend fun insertGameHistory(gameHistory: GameHistory)
}

