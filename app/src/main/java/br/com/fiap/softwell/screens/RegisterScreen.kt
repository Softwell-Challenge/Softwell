package br.com.fiap.softwell.screens

// ✅ 1. IMPORTAÇÕES NECESSÁRIAS ADICIONADAS
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext // ✅ Para o Toast
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.softwell.R
import br.com.fiap.softwell.model.RegisterRequest // ✅ Modelo de dados
import br.com.fiap.softwell.service.RetrofitFactory // ✅ Fábrica do Retrofit
import br.com.fiap.softwell.ui.theme.Rubik
import br.com.fiap.softwell.ui.theme.Sora
import retrofit2.Call // ✅ Classes do Retrofit
import retrofit2.Callback
import retrofit2.Response

@Composable
fun RegisterScreen(navController: NavController) {
    // ESTADOS
    var usernameText by remember { mutableStateOf("") }
    var cpfText by remember { mutableStateOf("") }
    var senhaText by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val context = LocalContext.current // ✅ 2. OBTÉM O CONTEXTO CORRETAMENTE

    // ✅ 3. LÓGICA DE REGISTRO MOVIDA PARA DENTRO DO COMPOSABLE
    fun handleRegister() {
        // Validação simples para não enviar dados vazios
        if (usernameText.isBlank() || cpfText.isBlank() || senhaText.isBlank()) {
            Toast.makeText(context, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        val roles = if (isAdmin) listOf("ADMIN", "USER") else listOf("USER")
        val registerRequest = RegisterRequest(usernameText, cpfText, senhaText, roles)

        RetrofitFactory.getAuthService().register(registerRequest).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Usuário registrado com sucesso!", Toast.LENGTH_LONG).show()
                    navController.popBackStack() // Volta para a tela de login
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Erro ao registrar."
                    Toast.makeText(context, "Erro: $errorMsg", Toast.LENGTH_LONG).show()
                    Log.e("RegisterScreen", "Erro no registro: ${response.code()} - $errorMsg")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(context, "Falha na conexão: ${t.message}", Toast.LENGTH_LONG).show()
                Log.e("RegisterScreen", "Falha de rede", t)
            }
        })
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fundo_login),
            contentDescription = "Fundo da tela de Registro",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .width(400.dp)
                    .height(400.dp)
                    .offset(y = (-27).dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Softwell Logo",
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(0.dp))

            Text(
                text = "Crie sua Conta",
                fontSize = 24.sp,
                fontFamily = Rubik,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.primary),
            )
            Text(
                text = "É rápido e simples.",
                fontSize = 18.sp,
                fontFamily = Sora,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.primary),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- INPUTS ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Input USERNAME
                TextField(
                    value = usernameText,
                    onValueChange = { usernameText = it },
                    label = { Text("Nome de Usuário", color = colorResource(id = R.color.light_blue)) },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedLabelColor = colorResource(id = R.color.light_blue),
                        focusedLabelColor = colorResource(id = R.color.primary),
                        cursorColor = colorResource(id = R.color.primary),
                        focusedIndicatorColor = colorResource(id = R.color.primary),
                        unfocusedIndicatorColor = colorResource(id = R.color.light_blue),
                        focusedTextColor = colorResource(id = R.color.primary),
                        unfocusedTextColor = colorResource(id = R.color.primary)
                    ),
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Input CPF
                TextField(
                    value = cpfText,
                    onValueChange = { cpfText = it },
                    label = { Text("CPF", color = colorResource(id = R.color.light_blue)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedLabelColor = colorResource(id = R.color.light_blue),
                        focusedLabelColor = colorResource(id = R.color.primary),
                        cursorColor = colorResource(id = R.color.primary),
                        focusedIndicatorColor = colorResource(id = R.color.primary),
                        unfocusedIndicatorColor = colorResource(id = R.color.light_blue),
                        focusedTextColor = colorResource(id = R.color.primary),
                        unfocusedTextColor = colorResource(id = R.color.primary)
                    ),
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Input Senha
                TextField(
                    value = senhaText,
                    onValueChange = { senhaText = it },
                    label = { Text("Senha", color = colorResource(id = R.color.light_blue)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedLabelColor = colorResource(id = R.color.light_blue),
                        focusedLabelColor = colorResource(id = R.color.primary),
                        cursorColor = colorResource(id = R.color.primary),
                        focusedIndicatorColor = colorResource(id = R.color.primary),
                        unfocusedIndicatorColor = colorResource(id = R.color.light_blue),
                        focusedTextColor = colorResource(id = R.color.primary),
                        unfocusedTextColor = colorResource(id = R.color.primary)
                    ),
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(24.dp))

                // --- SELETOR DE ADMIN (CHECKBOX) ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isAdmin = !isAdmin } // Torna a linha clicável
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Checkbox(
                        checked = isAdmin,
                        onCheckedChange = { isAdmin = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = colorResource(id = R.color.blue),
                            uncheckedColor = colorResource(id = R.color.light_blue)
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sou Administrador",
                        color = colorResource(id = R.color.primary),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
            // --- FIM DOS INPUTS ---

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { handleRegister() }, // ✅ 4. BOTÃO CONECTADO À FUNÇÃO
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue))
            ) {
                Text(
                    text = "CRIAR CONTA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.primary)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botão para voltar à tela de Login
            Button(
                onClick = { navController.popBackStack() }, // Volta para a tela anterior (Login)
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = "Fazer login",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.primary)
                )
            }
        }
    }
}



//package br.com.fiap.softwell.screens
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Checkbox
//import androidx.compose.material3.CheckboxDefaults
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import br.com.fiap.softwell.R
//import br.com.fiap.softwell.ui.theme.Rubik
//import br.com.fiap.softwell.ui.theme.Sora
//
//@Composable
//fun RegisterScreen(navController: NavController) {
//    // ESTADOS
//    var usernameText by remember { mutableStateOf("") }
//    var cpfText by remember { mutableStateOf("") }
//    var senhaText by remember { mutableStateOf("") }
//    var isAdmin by remember { mutableStateOf(false) }
//
//    val scrollState = rememberScrollState()
//
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.fundo_login),
//            contentDescription = "Fundo da tela de Registro",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize()
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(scrollState),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Image(
//                modifier = Modifier
//                    .width(400.dp)
//                    .height(400.dp)
//                    .offset(y = (-27).dp),
//                painter = painterResource(id = R.drawable.logo),
//                contentDescription = "Softwell Logo",
//                contentScale = ContentScale.Crop
//            )
//            Spacer(modifier = Modifier.height(0.dp))
//
//            Text(
//                text = "Crie sua Conta",
//                fontSize = 24.sp,
//                fontFamily = Rubik,
//                fontWeight = FontWeight.ExtraBold,
//                textAlign = TextAlign.Center,
//                color = colorResource(id = R.color.primary),
//            )
//            Text(
//                text = "É rápido e simples.",
//                fontSize = 18.sp,
//                fontFamily = Sora,
//                fontWeight = FontWeight.Normal,
//                textAlign = TextAlign.Center,
//                color = colorResource(id = R.color.primary),
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // --- INPUTS ---
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 32.dp),
//                horizontalAlignment = Alignment.Start
//            ) {
//                // Input USERNAME
//                TextField(
//                    value = usernameText,
//                    onValueChange = { usernameText = it },
//                    label = { Text("Nome de Usuário", color = colorResource(id = R.color.light_blue)) },
//                    colors = TextFieldDefaults.colors(
//                        unfocusedContainerColor = Color.Transparent,
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedLabelColor = colorResource(id = R.color.light_blue),
//                        focusedLabelColor = colorResource(id = R.color.primary),
//                        cursorColor = colorResource(id = R.color.primary),
//                        focusedIndicatorColor = colorResource(id = R.color.primary),
//                        unfocusedIndicatorColor = colorResource(id = R.color.light_blue),
//                        focusedTextColor = colorResource(id = R.color.primary),
//                        unfocusedTextColor = colorResource(id = R.color.primary)
//                    ),
//                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Input CPF
//                TextField(
//                    value = cpfText,
//                    onValueChange = { cpfText = it },
//                    label = { Text("CPF", color = colorResource(id = R.color.light_blue)) },
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                    colors = TextFieldDefaults.colors(
//                        unfocusedContainerColor = Color.Transparent,
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedLabelColor = colorResource(id = R.color.light_blue),
//                        focusedLabelColor = colorResource(id = R.color.primary),
//                        cursorColor = colorResource(id = R.color.primary),
//                        focusedIndicatorColor = colorResource(id = R.color.primary),
//                        unfocusedIndicatorColor = colorResource(id = R.color.light_blue),
//                        focusedTextColor = colorResource(id = R.color.primary),
//                        unfocusedTextColor = colorResource(id = R.color.primary)
//                    ),
//                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Input Senha
//                TextField(
//                    value = senhaText,
//                    onValueChange = { senhaText = it },
//                    label = { Text("Senha", color = colorResource(id = R.color.light_blue)) },
//                    visualTransformation = PasswordVisualTransformation(),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                    colors = TextFieldDefaults.colors(
//                        unfocusedContainerColor = Color.Transparent,
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedLabelColor = colorResource(id = R.color.light_blue),
//                        focusedLabelColor = colorResource(id = R.color.primary),
//                        cursorColor = colorResource(id = R.color.primary),
//                        focusedIndicatorColor = colorResource(id = R.color.primary),
//                        unfocusedIndicatorColor = colorResource(id = R.color.light_blue),
//                        focusedTextColor = colorResource(id = R.color.primary),
//                        unfocusedTextColor = colorResource(id = R.color.primary)
//                    ),
//                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
//                )
//                Spacer(modifier = Modifier.height(24.dp))
//
//                // --- SELETOR DE ADMIN (CHECKBOX) ---
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { isAdmin = !isAdmin } // Torna a linha clicável
//                        .padding(vertical = 4.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Start
//                ) {
//                    Checkbox(
//                        checked = isAdmin,
//                        onCheckedChange = { isAdmin = it },
//                        colors = CheckboxDefaults.colors(
//                            checkedColor = colorResource(id = R.color.blue),
//                            uncheckedColor = colorResource(id = R.color.light_blue)
//                        )
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        text = "Sou Administrador",
//                        color = colorResource(id = R.color.primary),
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Normal
//                    )
//                }
//            }
//            // --- FIM DOS INPUTS ---
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Button(
//                // TODO: Defina a rota de sucesso após o registro, ex: navController.navigate("home_screen")
//                onClick = { /* Lógica de registro */ },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp)
//                    .padding(horizontal = 32.dp),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue))
//            ) {
//                Text(
//                    text = "CRIAR CONTA",
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 18.sp,
//                    color = colorResource(id = R.color.primary)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Botão para voltar à tela de Login
//            Button(
//                onClick = { navController.popBackStack() }, // Volta para a tela anterior (Login)
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp)
//                    .padding(horizontal = 32.dp),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
//            ) {
//                Text(
//                    text = "Fazer login",
//                    fontSize = 14.sp,
//                    fontFamily = Sora,
//                    fontWeight = FontWeight.Bold,
//                    textAlign = TextAlign.Center,
//                    color = colorResource(id = R.color.primary),
//                )
//            }
//
//            // Ajusta o offset para evitar que a linha "caia" na rolagem
//            Image(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(10.dp)
//                    .padding(top = 10.dp),
//                painter = painterResource(id = R.drawable.diamond_line),
//                contentDescription = "Linha divisória",
//                contentScale = ContentScale.FillWidth
//            )
//
//            // Adiciona um espaço extra no final para que o último conteúdo não fique colado na borda
//            Spacer(modifier = Modifier.height(32.dp))
//        }
//    }
//}