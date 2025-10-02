package br.com.fiap.softwell.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.softwell.R
import br.com.fiap.softwell.components.DiamondLine
import br.com.fiap.softwell.components.SessionTitle
import br.com.fiap.softwell.ui.theme.Sora

// Modelo de dados de exemplo (Substitua isso pelo seu ViewModel e modelos reais)
data class HistoricData(
    val date: String,
    val userName: String,
    val mood: String, // Ex: "Alegre", "Triste"
    val score: Int // Ex: Pontuação da avaliação psicossocial
)

@Composable
fun HistoricScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    // 🚀 NOVO: Definição do gradiente de fundo (igual à Dashboard)
    val diagonalGradient = Brush.linearGradient(
        colors = listOf(
            colorResource(id = R.color.bg_dark),
            colorResource(id = R.color.bg_middle),
            colorResource(id = R.color.bg_light)
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )

    // Dados de exemplo (Substitua pela chamada ao seu ViewModel)
    val historicDataList = listOf(
        HistoricData("20/Set", "Alice S.", "Alegre", 85),
        HistoricData("21/Set", "Bob M.", "Neutro", 72),
        HistoricData("22/Set", "Carlos T.", "Triste", 55),
        HistoricData("23/Set", "Alice S.", "Calmo", 90),
        HistoricData("24/Set", "Daniel A.", "Ansioso", 40),
        // Adicione mais dados conforme necessário
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(diagonalGradient)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(0.dp))
                .shadow(elevation = 0.dp)
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                // === CABEÇALHO (SETA DE VOLTAR) ===
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Start, // Seta fica na esquerda
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { navController.popBackStack() },
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = colorResource(id = R.color.light_green)
                    )
                }

                // === TÍTULO SOFTWELL ===
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = "SOFTWELL",
                        fontFamily = Sora,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp,
                        color = colorResource(id = R.color.light_blue),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                DiamondLine()

                // === 1. HISTÓRICO DE HUMOR ===
                SessionTitle(
                    text = "Histórico de Humor (Geral)",
                    modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
                )

                HistoricList(dataList = historicDataList)

                Spacer(modifier = Modifier.height(24.dp))
                DiamondLine()

                // === 2. HISTÓRICO PSICOSSOCIAL ===
                SessionTitle(
                    text = "Pontuação Psicossocial",
                    modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
                )

                // Aqui você listaria as avaliações psicossociais, usando o mesmo componente ou um adaptado
                HistoricList(dataList = historicDataList.map { it.copy(mood = "Score", score = it.score) })

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun HistoricList(dataList: List<HistoricData>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        dataList.forEach { data ->
            HistoricItemCard(data)
        }
    }
}

@Composable
fun HistoricItemCard(data: HistoricData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Data e Usuário
            Column {
                Text(
                    text = data.userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Data: ${data.date}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            // Humor / Score
            Text(
                text = if (data.mood == "Score") "Score: ${data.score}" else data.mood,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = if (data.mood == "Triste" || data.score < 60) Color.Red else colorResource(id = R.color.light_green)
            )
        }
    }
}