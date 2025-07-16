package br.com.fiap.softwell.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.fiap.softwell.model.UserMood

@Dao
interface UserMoodDao {

    @Insert
    suspend fun insert(userMood: UserMood)

    @Query("SELECT * FROM user_mood ORDER BY timestamp ASC")
    suspend fun getAll(): List<UserMood>
}
