package br.com.fiap.softwell.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.fiap.softwell.model.UserMood

@Database(entities = [UserMood::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userMoodDao(): UserMoodDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "softwell_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
