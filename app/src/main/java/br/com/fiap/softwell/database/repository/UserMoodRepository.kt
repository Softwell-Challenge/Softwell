package br.com.fiap.softwell.database.repository

import android.content.Context
import br.com.fiap.softwell.database.dao.AppDatabase
import br.com.fiap.softwell.database.dao.UserMoodDao
import br.com.fiap.softwell.model.UserMood

//class UserMoodRepository(private val userMoodDao: UserMoodDao) {
class UserMoodRepository(context: Context) {

    private var db = AppDatabase.getDatabase(context).userMoodDao()

    fun salvar(userMood: UserMood) : Long{
        return db.insert(userMood)
    }

    fun listUserMood(): List<UserMood> {
        return db.getAll()
    }


//    // Insere um novo estado emocional
//    suspend fun insertUserMood(userMood: UserMood) {
//        userMoodDao.insert(userMood)
//    }
//
//    // Busca todos os estados emocionais
//    suspend fun getAllUserMoods(): List<UserMood> {
//        return userMoodDao.getAll()
//    }
}
