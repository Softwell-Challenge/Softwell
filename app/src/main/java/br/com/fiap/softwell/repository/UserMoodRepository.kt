package br.com.fiap.softwell.repository

import br.com.fiap.softwell.dao.UserMoodDao
import br.com.fiap.softwell.model.UserMood

class UserMoodRepository(private val userMoodDao: UserMoodDao) {

    // Insere um novo estado emocional
    suspend fun insertUserMood(userMood: UserMood) {
        userMoodDao.insert(userMood)
    }

    // Busca todos os estados emocionais
    suspend fun getAllUserMoods(): List<UserMood> {
        return userMoodDao.getAll()
    }
}
