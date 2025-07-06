package br.com.fiap.softwell.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import br.com.fiap.softwell.R
import br.com.fiap.softwell.ui.theme.Rubik
import br.com.fiap.softwell.ui.theme.Sora

@Composable
fun LoginScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fundo_login),
            contentDescription = "Fundo da tela de Login",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
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
            Spacer(modifier = Modifier.height(100.dp))
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
            Text(
                text = "Todas as respostas são 100% confidenciais.",
                fontSize = 18.sp,
                fontFamily = Sora,
                fontWeight = FontWeight.ExtraLight,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.primary),
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { navController.navigate("dashboard") },
                modifier = Modifier
                    .padding(16.dp)
                    .height(60.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.blue)
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "COMEÇAR",
                        fontSize = 32.sp,
                        color = colorResource(id = R.color.primary),
                        fontWeight = FontWeight.Black,
                        fontFamily = Rubik,

                        )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.incognito),
                        contentDescription = "Anônimo",
                        tint = colorResource(R.color.primary),
                        modifier = Modifier
                            .size(36.dp)
                            .padding(end = 8.dp)
                    )
                }
            }
        }
        Image(
            modifier = Modifier
                .width(187.dp)
                .height(187.dp)
                .offset(x = 205.dp, y = 262.dp)
                .zIndex(1f),
            painter = painterResource(id = R.drawable.reverse_triangle),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}






