package br.com.fiap.softwell.database.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.fiap.softwell.model.PsychoSocial
import br.com.fiap.softwell.model.Support
import br.com.fiap.softwell.model.UserMood

@Database(entities = [UserMood::class, PsychoSocial::class, Support::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userMoodDao(): UserMoodDao
    abstract fun psychoSocialDao(): PsychoSocialDao

    abstract fun supportDao(): SupportDao
    companion object {
        //@Volatile private var INSTANCE: AppDatabase? = null
        private lateinit var instance: AppDatabase

        fun getDatabase(context: Context): AppDatabase {
            if(!::instance.isInitialized){
                instance = Room
                    .databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "user_mood_room_db"
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()

            }
            return instance
        }
    }
}
