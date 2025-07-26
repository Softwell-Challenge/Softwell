package br.com.fiap.softwell.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.softwell.R
import br.com.fiap.softwell.components.DiamondLine
import br.com.fiap.softwell.components.ExpandableTipCard
import br.com.fiap.softwell.components.SessionTitle
import br.com.fiap.softwell.database.dao.AppDatabase
import br.com.fiap.softwell.database.repository.SupportRepository
import br.com.fiap.softwell.model.Support
import br.com.fiap.softwell.model.Tip
import br.com.fiap.softwell.model.TipsData
import br.com.fiap.softwell.ui.theme.Sora
import java.sql.Date

enum class SupportScreenType {
    Care,
    Guidelines
}

@Composable
fun SupportScreen(navController: NavController) {
    val selectedScreen = remember { mutableStateOf(SupportScreenType.Care) }

    val context = LocalContext.current
    val supportRepository = SupportRepository(context)
    val db = remember { AppDatabase.getDatabase(context) }
    val scrollState = rememberScrollState()

    val diagonalGradient = Brush.linearGradient(
        colors = listOf(
            colorResource(id = R.color.bg_dark),
            colorResource(id = R.color.bg_middle),
            colorResource(id = R.color.bg_light)
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )

    val options = listOf(
        "Jogo de xadrez pós-almoço",
        "Aula de yoga pela tarde",
        "30 min de música relaxante (meditação)",
        "Massagens oferecidas aos colaboradores",
        "Musicoterapia",
        "Rodas de conversa com psicólogos",
        "Sessões de alongamento guiado"
    )

    val selectedOption = remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(diagonalGradient)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .clip(RoundedCornerShape(6.dp))
                .shadow(elevation = 6.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = colorResource(id = R.color.light_green),
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { navController.popBackStack() }
                    )
                    Spacer(modifier = Modifier
                        .width(8.dp)
                        .weight(1f))
                    SessionTitle(
                        text = if (selectedScreen.value == SupportScreenType.Care) "Orientações" else "Atividade de cuidado",
                        fontWeight = FontWeight.Bold,
                        fontFamily = Sora,
                        color = colorResource(id = R.color.primary),
                        modifier = Modifier
                            .clickable {
                                selectedScreen.value = when (selectedScreen.value) {
                                    SupportScreenType.Care -> SupportScreenType.Guidelines
                                    SupportScreenType.Guidelines -> SupportScreenType.Care
                                }
                            }
                            .background(
                                color = colorResource(id = R.color.bg_dark),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                DiamondLine(modifier = Modifier.padding(bottom = 8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SessionTitle(
                        text = if (selectedScreen.value == SupportScreenType.Care)
                            "Atividade de cuidado"
                        else
                            "Orientações",
                        fontWeight = FontWeight.Bold,
                        fontFamily = Sora,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (selectedScreen.value == SupportScreenType.Care)
                            "Qual atividade você gostaria que a empresa promovesse?"
                        else
                            "Invista em você mesmo - práticas para uma vida mais equilibrada",
                        fontSize = 18.sp,
                        fontFamily = Sora,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.light_blue),
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                if (selectedScreen.value == SupportScreenType.Care) {
                    options.forEach { option ->
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth()
                                .clickable { selectedOption.value = option }
                                .border(
                                    width = if (selectedOption.value == option) 2.dp else 1.dp,
                                    color = if (selectedOption.value == option) colorResource(id = R.color.light_blue)
                                    else colorResource(id = R.color.primary),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = colorResource(id = R.color.bg_dark)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Text(
                                text = option,
                                modifier = Modifier.padding(16.dp),
                                fontSize = 16.sp,
                                fontWeight = if (selectedOption.value == option) FontWeight.Bold else FontWeight.Normal,
                                color = colorResource(id = R.color.primary)
                            )
                        }
                    }
                    Button(
                        onClick = {
                            val support = Support(
                                id = 0,
                                selectedOption.value.toString(),
                                System.currentTimeMillis()
                            )
                            supportRepository.salvar(support)
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = selectedOption.value != null,
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue))
                    ) {
                        Text(
                            text = "VOTAR",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = colorResource(id = R.color.primary)
                        )
                    }
                    Text(
                        text = "Sua votação é anônima.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(colorResource(id = R.color.bg_light))
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Atenção",
                                tint = colorResource(id = R.color.primary),
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(end = 8.dp)
                            )
                            Text(
                                text = "Se você estiver passando por um momento difícil, procure ajuda. Ligue gratuitamente para o CVV – 188. O atendimento é sigiloso e funciona 24h.",
                                color = colorResource(id = R.color.primary),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        TipsData.tips.forEach { tip ->
                            ExpandableTipCard(tip = tip)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}
