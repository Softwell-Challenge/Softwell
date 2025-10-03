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
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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

    // --- Date Picker Dialog ---
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

    // --- Screen Layout ---
    Box(modifier = Modifier.fillMaxSize().background(diagonalGradient)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                Spacer(modifier = Modifier.height(30.dp))

                // --- Header ---
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

                // --- Content ---
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

                // --- Dynamic Data Display Area ---
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
                                // Mostra as médias do questionário SE existirem dados.
                                state.averages?.let {
                                    AveragesDisplay(averages = it)
                                }
                                // Mostra a lista de respostas de humor SE existirem dados.
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

// --- Componentes para Médias do Questionário (sem alteração) ---
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

// --- Componentes para Histórico de Humor (novos e corrigidos) ---
@Composable
fun HumorHistoryDisplay(responses: List<UserHumorResponse>) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // O título é exibido aqui, pois já verificamos que a lista não está vazia.
        Text(
            text = "Respostas de Humor do Dia",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = colorResource(id = R.color.light_green),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Itera sobre a lista e cria um Card para cada resposta
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
            // Coluna com o estado de humor e informações do usuário/hora
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = response.estadoDeHumor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // Formata a data e hora para exibição amigável
                val parsedDateTime = LocalDateTime.parse(response.dataResposta)
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                Text(
                    text = "Enviado por: ${response.userId} às ${parsedDateTime.format(formatter)}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}



//package br.com.fiap.softwell.screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import br.com.fiap.softwell.R
//import br.com.fiap.softwell.components.DiamondLine
//import br.com.fiap.softwell.components.SessionTitle
//import br.com.fiap.softwell.model.ThematicAverages
//import br.com.fiap.softwell.model.UserHumorResponse // ✅ Importa o modelo de humor
//import br.com.fiap.softwell.ui.theme.Sora
//import br.com.fiap.softwell.viewmodel.HistoricState
//import br.com.fiap.softwell.viewmodel.HistoricViewModel
//import java.time.Instant
//import java.time.LocalDateTime // ✅ Importa LocalDateTime
//import java.time.ZoneId
//import java.time.format.DateTimeFormatter // ✅ Importa DateTimeFormatter
//import java.util.Locale
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HistoricScreen(
//    navController: NavController,
//    historicViewModel: HistoricViewModel = viewModel()
//) {
//    // ... (Código do DatePicker e do layout principal, sem alterações) ...
//    val historicState by historicViewModel.historicState.collectAsState()
//    var showDatePicker by remember { mutableStateOf(false) }
//    val datePickerState = rememberDatePickerState()
//
//    if (showDatePicker) {
//        DatePickerDialog(
//            onDismissRequest = { showDatePicker = false },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        showDatePicker = false
//                        datePickerState.selectedDateMillis?.let { millis ->
//                            val selectedDate = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
//                            historicViewModel.fetchAveragesByDate(selectedDate)
//                        }
//                    }
//                ) { Text("OK") }
//            },
//            dismissButton = {
//                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
//            }
//        ) {
//            DatePicker(state = datePickerState)
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Box(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)) {
//            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
//                // ... (Cabeçalho, Título, DiamondLine e botão "Selecionar Data", sem alterações) ...
//                Spacer(modifier = Modifier.height(30.dp))
//                // ...
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp)
//                        .defaultMinSize(minHeight = 200.dp),
//                    contentAlignment = Alignment.TopCenter // Alinha o conteúdo ao topo
//                ) {
//                    when (val state = historicState) {
//                        is HistoricState.Idle -> {
//                            Text("Por favor, selecione uma data para ver o histórico.", color = MaterialTheme.colorScheme.onSurface)
//                        }
//                        is HistoricState.Loading -> {
//                            CircularProgressIndicator()
//                        }
//                        is HistoricState.Empty -> {
//                            Text("Nenhum dado encontrado para a data selecionada.", color = MaterialTheme.colorScheme.onSurface)
//                        }
//                        is HistoricState.Error -> {
//                            Text("Erro ao carregar dados: ${state.message}", color = MaterialTheme.colorScheme.error)
//                        }
//                        // ✅✅✅ ATUALIZAÇÃO PRINCIPAL AQUI ✅✅✅
//                        is HistoricState.Success -> {
//                            Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
//                                // 1. Mostra as médias do questionário apenas se existirem dados.
//                                if (state.averages != null) {
//                                    AveragesDisplay(averages = state.averages)
//                                }
//                                // 2. Mostra a lista de humores do dia.
//                                HumorHistoryDisplay(responses = state.humorResponses)
//                            }
//                        }
//                    }
//                }
//                Spacer(modifier = Modifier.height(32.dp))
//            }
//        }
//    }
//}
//
//// ✅✅✅ NOVO COMPONENTE PARA EXIBIR A LISTA DE HUMORES ✅✅✅
//@Composable
//fun HumorHistoryDisplay(responses: List<UserHumorResponse>) {
//    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
//        // Título para a seção de humor
//        Text(
//            text = "Respostas de Humor do Dia",
//            fontWeight = FontWeight.Bold,
//            fontSize = 20.sp,
//            color = colorResource(id = R.color.light_green),
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//
//        // Mensagem caso a lista de humores esteja vazia
//        if (responses.isEmpty()) {
//            Text("Nenhuma resposta de humor encontrada para esta data.", color = MaterialTheme.colorScheme.onSurface)
//        } else {
//            // Itera sobre a lista e cria um Card para cada resposta
//            responses.forEach { response ->
//                HumorHistoryItem(response = response)
//            }
//        }
//    }
//}
//
//// ✅✅✅ NOVO COMPONENTE PARA CADA ITEM DA LISTA DE HUMORES ✅✅✅
//@Composable
//fun HumorHistoryItem(response: UserHumorResponse) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(8.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth().padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // Emoji
//            Text(text = response.emoji, fontSize = 28.sp)
//            Spacer(modifier = Modifier.width(16.dp))
//            // Coluna com o estado de humor e informações do usuário/hora
//            Column(modifier = Modifier.weight(1f)) {
//                Text(
//                    text = response.estadoDeHumor,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 16.sp,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//                // Formata a data e hora para exibição amigável
//                val parsedDateTime = LocalDateTime.parse(response.dataResposta)
//                val formatter = DateTimeFormatter.ofPattern("HH:mm")
//                Text(
//                    text = "Enviado por: ${response.userId} às ${parsedDateTime.format(formatter)}",
//                    fontSize = 12.sp,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun AveragesDisplay(averages: ThematicAverages) {
//    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
//        Text(
//            text = "Médias Consolidadas do Dia",
//            fontWeight = FontWeight.Bold,
//            fontSize = 20.sp,
//            color = colorResource(id = R.color.light_green),
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//        AverageItem("Carga de Trabalho", averages.workloadAverage)
//        AverageItem("Sinais de Alerta", averages.warningSignsAverage)
//        AverageItem("Clima e Relacionamento", averages.relationshipClimateAverage)
//        AverageItem("Comunicação", averages.communicationAverage)
//        AverageItem("Relação com Liderança", averages.leadershipRelationAverage)
//    }
//}
//
//@Composable
//fun AverageItem(label: String, value: Double) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(8.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth().padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = label,
//                fontSize = 16.sp,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//            Text(
//                text = String.format(Locale.getDefault(), "%.1f", value),
//                fontWeight = FontWeight.ExtraBold,
//                fontSize = 18.sp,
//                color = colorResource(id = R.color.light_blue)
//            )
//        }
//    }
//}





//
//package br.com.fiap.softwell.screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import br.com.fiap.softwell.R
//import br.com.fiap.softwell.components.DiamondLine
//import br.com.fiap.softwell.components.SessionTitle
//import br.com.fiap.softwell.model.ThematicAverages
//import br.com.fiap.softwell.ui.theme.Sora
//import br.com.fiap.softwell.viewmodel.HistoricState
//import br.com.fiap.softwell.viewmodel.HistoricViewModel // ✅ Verifique se esta importação não está mais vermelha
//import java.time.Instant
//import java.time.ZoneId
//import java.util.Locale
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HistoricScreen(
//    navController: NavController,
//    historicViewModel: HistoricViewModel = viewModel() // Injeta o ViewModel
//) {
//    val diagonalGradient = Brush.linearGradient(
//        colors = listOf(
//            colorResource(id = R.color.bg_dark),
//            colorResource(id = R.color.bg_middle),
//            colorResource(id = R.color.bg_light)
//        ),
//        start = Offset(0f, 0f),
//        end = Offset(1000f, 1000f)
//    )
//
//    val historicState by historicViewModel.historicState.collectAsState()
//    var showDatePicker by remember { mutableStateOf(false) }
//    val datePickerState = rememberDatePickerState()
//
//    if (showDatePicker) {
//        DatePickerDialog(
//            onDismissRequest = { showDatePicker = false },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        showDatePicker = false
//                        datePickerState.selectedDateMillis?.let { millis ->
//                            val selectedDate = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
//                            historicViewModel.fetchAveragesByDate(selectedDate)
//                        }
//                    }
//                ) { Text("OK") }
//            },
//            dismissButton = {
//                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
//            }
//        ) {
//            DatePicker(state = datePickerState)
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize().background(diagonalGradient)) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 8.dp)
//                .background(MaterialTheme.colorScheme.background),
//        ) {
//            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
//                Spacer(modifier = Modifier.height(30.dp))
//
//                Row(
//                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
//                    horizontalArrangement = Arrangement.Start,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        modifier = Modifier.size(32.dp).clickable { navController.popBackStack() },
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Voltar",
//                        tint = colorResource(id = R.color.light_green)
//                    )
//                }
//                Box(
//                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp)
//                ) {
//                    Text(
//                        text = "SOFTWELL",
//                        fontFamily = Sora,
//                        fontWeight = FontWeight.ExtraBold,
//                        fontSize = 32.sp,
//                        color = colorResource(id = R.color.light_blue),
//                        modifier = Modifier.align(Alignment.Center)
//                    )
//                }
//                DiamondLine()
//
//                SessionTitle(
//                    text = "Histórico Psicossocial",
//                    modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
//                )
//
//                Button(
//                    onClick = { showDatePicker = true },
//                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
//                ) {
//                    Text("Selecionar Data para Análise")
//                }
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp)
//                        .defaultMinSize(minHeight = 200.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    when (val state = historicState) {
//                        is HistoricState.Idle -> {
//                            Text("Por favor, selecione uma data para ver as médias.", color = MaterialTheme.colorScheme.onSurface)
//                        }
//                        is HistoricState.Loading -> {
//                            CircularProgressIndicator()
//                        }
//                        is HistoricState.Empty -> {
//                            Text("Nenhum dado de questionário encontrado para a data selecionada.", color = MaterialTheme.colorScheme.onSurface)
//                        }
//                        is HistoricState.Error -> {
//                            Text("Erro ao carregar dados: ${state.message}", color = Color.Red)
//                        }
//                        is HistoricState.Success -> {
//                            AveragesDisplay(averages = state.averages)
//                        }
//                    }
//                }
//                Spacer(modifier = Modifier.height(32.dp))
//            }
//        }
//    }
//}
//
//@Composable
//fun AveragesDisplay(averages: ThematicAverages) {
//    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
//        Text(
//            text = "Médias Consolidadas do Dia",
//            fontWeight = FontWeight.Bold,
//            fontSize = 20.sp,
//            color = colorResource(id = R.color.light_green),
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//        AverageItem("Carga de Trabalho", averages.workloadAverage)
//        AverageItem("Sinais de Alerta", averages.warningSignsAverage)
//        AverageItem("Clima e Relacionamento", averages.relationshipClimateAverage)
//        AverageItem("Comunicação", averages.communicationAverage)
//        AverageItem("Relação com Liderança", averages.leadershipRelationAverage)
//    }
//}
//
//@Composable
//fun AverageItem(label: String, value: Double) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(8.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth().padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = label,
//                fontSize = 16.sp,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//            Text(
//                text = String.format(Locale.getDefault(), "%.1f", value),
//                fontWeight = FontWeight.ExtraBold,
//                fontSize = 18.sp,
//                color = colorResource(id = R.color.light_blue)
//            )
//        }
//    }
//}
