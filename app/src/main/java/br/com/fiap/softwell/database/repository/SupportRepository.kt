package br.com.fiap.softwell.database.repository

import android.content.Context
import br.com.fiap.softwell.database.dao.AppDatabase
import br.com.fiap.softwell.model.Support

class SupportRepository(context: Context) {
    private var db = AppDatabase.getDatabase(context).supportDao()

    fun salvar(support: Support): Long{
        return db.insert(support)
    }

    fun listarSupport(): List<Support>{
        return db.getAll()
    }
}