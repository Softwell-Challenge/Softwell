package br.com.fiap.softwell.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.fiap.softwell.model.PsychoSocial

@Dao
interface PsychoSocialDao {
    @Insert
    fun insert(psychoSocial: PsychoSocial): Long

    @Query("SELECT * FROM psycho_social ORDER BY id")
    fun getAll(): List<PsychoSocial>
}