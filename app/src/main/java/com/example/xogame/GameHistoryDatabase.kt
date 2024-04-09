package com.example.xogame

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GameHistory::class], version = 1, exportSchema = false)
abstract class GameHistoryDatabase : RoomDatabase() {

    abstract fun gameHistoryDao(): GameHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: GameHistoryDatabase? = null

        fun getDatabase(context: Context): GameHistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameHistoryDatabase::class.java,
                    "game_history_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

