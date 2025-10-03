package br.com.fiap.softwell.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.fiap.softwell.R
import br.com.fiap.softwell.components.DiamondLine
import br.com.fiap.softwell.components.SessionTitle
import br.com.fiap.softwell.model.ThematicAverages
import br.com.fiap.softwell.model.UserHumorResponse
import br.com.fiap.softwell.ui.theme.Sora
import br.com.fiap.softwell.viewmodel.HistoricState
import br.com.fiap.softwell.viewmodel.HistoricViewModel
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricScreen(
    navController: NavController,
    historicViewModel: HistoricViewModel = viewModel()
) {
    val diagonalGradient = Brush.linearGradient(
        colors = listOf(
            colorResource(id = R.color.bg_dark),
            colorResource(id = R.color.bg_middle),
            colorResource(id = R.color.bg_light)
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )

    val historicState by historicViewModel.historicState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
                            historicViewModel.fetchAveragesByDate(selectedDate)
                        }
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    Box(modifier = Modifier.fillMaxSize().background(diagonalGradient)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp).clickable { navController.popBackStack() },
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = colorResource(id = R.color.light_green)
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp)
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
                SessionTitle(
                    text = "Histórico de Respostas",
                    modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
                )

                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Text("Selecionar Data para Análise")
                }

                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .defaultMinSize(minHeight = 200.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    when (val state = historicState) {
                        is HistoricState.Idle -> {
                            Text("Por favor, selecione uma data para ver o histórico.", color = MaterialTheme.colorScheme.onSurface)
                        }
                        is HistoricState.Loading -> {
                            CircularProgressIndicator()
                        }
                        is HistoricState.Empty -> {
                            Text("Nenhum dado encontrado para a data selecionada.", color = MaterialTheme.colorScheme.onSurface)
                        }
                        is HistoricState.Error -> {
                            Text("Erro ao carregar dados: ${state.message}", color = MaterialTheme.colorScheme.error)
                        }
                        is HistoricState.Success -> {
                            Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                                state.averages?.let {
                                    AveragesDisplay(averages = it)
                                }
                                if (state.humorResponses.isNotEmpty()) {
                                    HumorHistoryDisplay(responses = state.humorResponses)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun AveragesDisplay(averages: ThematicAverages) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Médias do Questionário",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = colorResource(id = R.color.light_green),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        AverageItem("Carga de Trabalho", averages.workloadAverage)
        AverageItem("Sinais de Alerta", averages.warningSignsAverage)
        AverageItem("Clima e Relacionamento", averages.relationshipClimateAverage)
        AverageItem("Comunicação", averages.communicationAverage)
        AverageItem("Relação com Liderança", averages.leadershipRelationAverage)
    }
}

@Composable
fun AverageItem(label: String, value: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = String.format(Locale.getDefault(), "%.1f", value),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = colorResource(id = R.color.light_blue)
            )
        }
    }
}
@Composable
fun HumorHistoryDisplay(responses: List<UserHumorResponse>) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Respostas de Humor do Dia",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = colorResource(id = R.color.light_green),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        responses.forEach { response ->
            HumorHistoryItem(response = response)
        }
    }
}

@Composable
fun HumorHistoryItem(response: UserHumorResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji
            Text(text = response.emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = response.estadoDeHumor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
