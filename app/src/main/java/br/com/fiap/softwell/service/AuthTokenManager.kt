package br.com.fiap.softwell.service

import android.util.Log
import com.auth0.android.jwt.JWT

object AuthTokenManager {

    private var authToken: String? = null

    fun saveToken(token: String?) {
        authToken = token
    }

    fun getToken(): String? {
        return authToken
    }

    fun getUserIdFromToken(): String? {
        return try {
            // ✅ ATENÇÃO: Corrigido um bug residual aqui também.
            // O token já está puro, não precisa mais do removePrefix.
            val jwt = JWT(authToken ?: return null)
            jwt.subject
        } catch (e: Exception) {
            null
        }
    }

    // ✅✅✅ FUNÇÃO CORRIGIDA PARA LER LISTA DE ROLES ✅✅✅
    fun isUserAdmin(): Boolean {
        // 1. Pega o token puro. Se não existir, o usuário não pode ser admin.
        val token = authToken ?: return false

        try {
            val jwt = JWT(token)

            // 2. Tenta extrair a claim "roles" como uma lista de Strings.
            val rolesList = jwt.getClaim("roles").asList(String::class.java)

            // 3. LOG para depuração: veja o que foi lido.
            Log.d("ADMIN_CHECK", "Roles lidas do token: $rolesList")

            // 4. LÓGICA DE VERIFICAÇÃO SEGURA:
            //    - 'rolesList' não pode ser nulo.
            //    - Pelo menos um item ('any') na lista deve ser "ADMIN" (ignorando maiúsculas/minúsculas).
            val isAdmin = rolesList?.any { role -> role.equals("ADMIN", ignoreCase = true) } ?: false

            Log.d("ADMIN_CHECK", "O usuário é admin? -> $isAdmin")

            // 5. Retorna o resultado booleano final.
            return isAdmin

        } catch (e: Exception) {
            // Se ocorrer qualquer erro na decodificação (claim não existe, tipo errado, etc.),
            // assume que o usuário não é admin por segurança.
            Log.e("ADMIN_CHECK", "Falha ao processar a claim 'roles'. Assumindo 'false'.", e)
            return false
        }
    }

    fun clearToken() {
        authToken = null
    }
}
