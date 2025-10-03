package br.com.fiap.softwell.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import br.com.fiap.softwell.model.LoginRequest
import br.com.fiap.softwell.model.LoginResponse
import br.com.fiap.softwell.service.AuthTokenManager
import br.com.fiap.softwell.service.RetrofitFactory
import br.com.fiap.softwell.ui.theme.Rubik
import br.com.fiap.softwell.ui.theme.Sora
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen(navController: NavController) {
    var usernameText by remember { mutableStateOf("") }
    var senhaText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fundo_login),
            contentDescription = "Fundo da tela de Login",
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
                text = "Seu bem-estar importa",
                fontSize = 24.sp,
                fontFamily = Rubik,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.primary),
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .offset(y = (-10).dp)
                    .padding(end = 24.dp),
                painter = painterResource(id = R.drawable.triangle_arrow),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 24.dp)
            ) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                )
            }
            Button(
                onClick = { login(usernameText, senhaText, navController) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue))
            ) {
                Text(
                    text = "ACESSAR",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.primary)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = { navController.navigate("register") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    text = "REGISTRAR-SE",
                    fontSize = 14.sp,
                    fontFamily = Sora,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.primary),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .padding(top = 10.dp),
                painter = painterResource(id = R.drawable.diamond_line),
                contentDescription = "Linha divisória",
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


fun login(username: String, password: String, navController: NavController) {
    val authService = RetrofitFactory.getAuthService()
    val call = authService.login(LoginRequest(username, password))

    call.enqueue(object : Callback<LoginResponse> {
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            if (response.isSuccessful) {
                val token = response.body()?.token
                AuthTokenManager.saveToken(token)
                Log.i("LoginScreen", "Login bem-sucedido! Token salvo: $token")
                navController.navigate("dashboard")
            } else {
                Log.e("LoginScreen", "Erro no login: ${response.code()} - ${response.message()}")
            }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            Log.e("LoginScreen", "Falha na requisição de login: ${t.message}", t)
        }
    })
}