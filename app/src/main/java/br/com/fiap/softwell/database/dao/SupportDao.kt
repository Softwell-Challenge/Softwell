package br.com.fiap.softwell.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.fiap.softwell.model.Support

@Dao
interface SupportDao {

    @Insert
    fun insert(support: Support): Long


    @Query("SELECT * FROM support ORDER BY id")
    fun getAll(): List<Support>
}