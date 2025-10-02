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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.fiap.softwell.R
import br.com.fiap.softwell.database.dao.AppDatabase
import br.com.fiap.softwell.model.Mood
import br.com.fiap.softwell.model.UserMood
import br.com.fiap.softwell.viewmodel.MoodViewModel
import br.com.fiap.softwell.viewmodel.GraphicViewModel
import br.com.fiap.softwell.viewmodel.ThematicAverages

// --- Composables Auxiliares (mantidos) ---

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(id = R.color.light_green),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun ThematicAverageItem(label: String, value: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 16.sp, color = colorResource(id = R.color.blue))
        Text(
            text = "%.2f".format(value),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.light_green)
        )
    }
}

@Composable
fun ApiMoodList(moods: List<Mood>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if(moods.isEmpty()){
            Text("Nenhum dado da API para exibir. Tente buscar na tela anterior.")
        } else {
            for (mood in moods) {
                CardApiMood(mood = mood)
            }
        }
    }
}

@Composable
fun CardApiMood(mood: Mood) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.light_blue))
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mood.moodId,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "(ID: ${mood.id})",
                fontSize = 18.sp
            )
        }
    }
}


@Composable
fun moodList(listarMood: MutableState<List<UserMood>>) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ){
        for (mood in listarMood.value){
            cardMood(mood)
            Spacer(modifier = Modifier.height(4.dp))
        }

    }
}

@Composable
fun cardMood(mood: UserMood) {
    Card (
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Column (
                modifier = Modifier
                    .padding(8.dp)
                    .weight(2f)
            ){
                Text(
                    text = mood.moodId,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = mood.emoji,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// --- Tela Principal ---

@Composable
fun GraphicScreen(
    navController: NavController,
    moodViewModel: MoodViewModel,
    graphicViewModel: GraphicViewModel = viewModel()
) {

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // Dados do MoodViewModel
    val apiMoods by moodViewModel.moods.collectAsStateWithLifecycle()
    val isLoadingMoods by moodViewModel.isLoading.collectAsStateWithLifecycle()
    val errorMoods by moodViewModel.error.collectAsStateWithLifecycle()

    // DADOS DO GRAPHIC VIEWMODEL (Médias Psicossociais)
    val averages by graphicViewModel.averages.collectAsStateWithLifecycle()
    val isLoadingAverages by graphicViewModel.isLoading.collectAsStateWithLifecycle()
    val errorAverages by graphicViewModel.error.collectAsStateWithLifecycle()

    // 🚀 NOVO: Definição do gradiente de fundo
    val diagonalGradient = Brush.linearGradient(
        colors = listOf(
            colorResource(id = R.color.bg_dark),
            colorResource(id = R.color.bg_middle),
            colorResource(id = R.color.bg_light)
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            // 🚀 NOVO: Aplicando o gradiente de fundo
            .background(diagonalGradient)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                // ✅ AJUSTE: Aplicando o padding horizontal padrão
                .padding(horizontal = 8.dp)
                // ✅ AJUSTE: Removendo arredondamento
                .clip(RoundedCornerShape(0.dp))
                // ✅ AJUSTE: Removendo sombra
                .shadow(elevation = 0.dp)
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // 🚀 NOVO: Espaçamento de 30dp para a Status Bar
                Spacer(modifier = Modifier.height(30.dp))

                // Botão Voltar (Ajustado o padding vertical para ser consistente)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { navController.popBackStack() },
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = colorResource(id = R.color.light_green),
                    )
                }

                // ✅ AJUSTE: O padding da Column original foi movido para o conteúdo abaixo da Row do botão.
                // O padding horizontal de 16.dp é mantido para o conteúdo principal, mas não para a Column inteira.

                // Conteúdo da Tela (A partir daqui, o padding horizontal será interno)
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                    Spacer(modifier = Modifier.height(16.dp))

                    // -----------------------------------------------------------
                    // SEÇÃO 1: MÉDIAS PSICOSSOCIAIS
                    // -----------------------------------------------------------
                    Text("Análise Psicossocial (Médias)", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (isLoadingAverages) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color = colorResource(id = R.color.light_green))
                        Text("Calculando médias...", modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally))
                    } else if(errorAverages != null && averages == null){
                        // Exibe a mensagem de erro/status
                        Text(text = "Erro/Status: $errorAverages", color = Color.Red.copy(alpha = 0.8f), modifier = Modifier.padding(vertical = 8.dp))
                    } else {
                        averages?.let { avg ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.bg_middle).copy(alpha = 0.2f)),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    SectionTitle(text = "Métricas Temáticas (1.0 a 5.0)")
                                    Divider(color = colorResource(id = R.color.light_green), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                                    ThematicAverageItem(label = "Carga de Trabalho", value = avg["avgWorkload"] ?: 0.0)
                                    ThematicAverageItem(label = "Sinais de Alerta", value = avg["avgWarningSigns"] ?: 0.0)
                                    ThematicAverageItem(label = "Clima/Relacionamento", value = avg["avgRelationshipClimate"] ?: 0.0)
                                    ThematicAverageItem(label = "Comunicação", value = avg["avgCommunication"] ?: 0.0)
                                    ThematicAverageItem(label = "Relação com Liderança", value = avg["avgLeadershipRelation"] ?: 0.0)
                                }
                            }
                        } ?: Text("Nenhuma média psicossocial encontrada. Responda o questionário!", modifier = Modifier.padding(top = 8.dp))
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // -----------------------------------------------------------
                    // SEÇÃO 2: HUMORES DA API
                    // -----------------------------------------------------------
                    Text("Humores da API", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (isLoadingMoods) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color = colorResource(id = R.color.light_green))
                    } else if(errorMoods != null){
                        Text(text = "Erro ao carregar humores: $errorMoods", color = Color.Red.copy(alpha = 0.8f), modifier = Modifier.padding(vertical = 8.dp))
                    } else {
                        ApiMoodList(moods = apiMoods)
                    }
                }
                // Fim do Column com padding horizontal 16.dp

                // ✅ AJUSTE: Manter o espaçamento inferior grande (32dp)
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// O código auxiliar 'SectionTitle', 'ThematicAverageItem', 'ApiMoodList', 'CardApiMood', 'moodList' e 'cardMood' permanece inalterado.