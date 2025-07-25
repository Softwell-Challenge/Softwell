package br.com.fiap.softwell.database.repository

import android.content.Context
import br.com.fiap.softwell.database.dao.AppDatabase
import br.com.fiap.softwell.model.PsychoSocial

class PsychoSocialRepository(context: Context) {

    private var db = AppDatabase.getDatabase(context).psychoSocialDao()

    fun salvar(psychoSocial: PsychoSocial) : Long{
        return db.insert(psychoSocial)
    }

    fun listPsychoSocial(): List<PsychoSocial> {
        return db.getAll()
    }
}