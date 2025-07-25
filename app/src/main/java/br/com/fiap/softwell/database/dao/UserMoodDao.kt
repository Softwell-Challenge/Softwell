package br.com.fiap.softwell.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.fiap.softwell.model.UserMood

@Dao
interface UserMoodDao {

    @Insert
     fun insert(userMood: UserMood): Long

    @Query("SELECT * FROM user_mood ORDER BY timestamp ASC")
    fun getAll(): List<UserMood>
}
