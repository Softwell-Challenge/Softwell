package br.com.fiap.softwell.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.fiap.softwell.R
import br.com.fiap.softwell.components.DiamondLine
import br.com.fiap.softwell.components.SessionTitle
import br.com.fiap.softwell.model.ActivityVoteReportDTO
import br.com.fiap.softwell.model.HumorData
import br.com.fiap.softwell.service.ActivityApiService
import br.com.fiap.softwell.ui.theme.Sora
import br.com.fiap.softwell.viewmodel.ActivityDataState
import br.com.fiap.softwell.viewmodel.ActivityViewModel
import br.com.fiap.softwell.viewmodel.ActivityViewModelFactory
import br.com.fiap.softwell.viewmodel.HumorDataState
import br.com.fiap.softwell.viewmodel.HumorViewModel
import br.com.fiap.softwell.service.RetrofitFactory

enum class AdminScreenType {
    Humor,
    Apoio
}

@Composable
fun AdminScreen(
    navController: NavController,
    humorViewModel: HumorViewModel = viewModel()
) {
    val activityApiService: ActivityApiService = remember {
        RetrofitFactory.getActivityService()
    }
    val activityViewModel: ActivityViewModel = viewModel(
        factory = ActivityViewModelFactory(activityApiService)
    )

    var moodText by remember { mutableStateOf("") }
    var emojiText by remember { mutableStateOf("") }
    var newActivityText by remember { mutableStateOf("") }
    var showReport by remember { mutableStateOf(true) }
    var showLimitMessage by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val selectedScreen = remember { mutableStateOf(AdminScreenType.Humor) }

    val humorDataState by humorViewModel.humorDataState.collectAsState()
    val activityDataState by activityViewModel.activityDataState.collectAsState()

    LaunchedEffect(Unit) {
        humorViewModel.fetchHumorData()
        activityViewModel.fetchData()
    }

    LaunchedEffect(selectedScreen.value) {
        if (selectedScreen.value == AdminScreenType.Apoio) {
            activityViewModel.fetchData()
        }
    }

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
            .background(diagonalGradient)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(0.dp))
                .shadow(elevation = 0.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = colorResource(id = R.color.light_green),
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { navController.popBackStack() }
                    )
                    Row(
                        modifier = Modifier
                            .background(
                                color = colorResource(id = R.color.bg_dark),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        SessionTitle(
                            text = "Humor",
                            fontWeight = FontWeight.Bold,
                            fontFamily = Sora,
                            color = colorResource(id = R.color.primary),
                            modifier = Modifier
                                .clickable { selectedScreen.value = AdminScreenType.Humor }
                                .background(
                                    color = if (selectedScreen.value == AdminScreenType.Humor)
                                        colorResource(id = R.color.bg_middle)
                                    else
                                        Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        SessionTitle(
                            text = "Apoio",
                            fontWeight = FontWeight.Bold,
                            fontFamily = Sora,
                            color = colorResource(id = R.color.primary),
                            modifier = Modifier
                                .clickable { selectedScreen.value = AdminScreenType.Apoio }
                                .background(
                                    color = if (selectedScreen.value == AdminScreenType.Apoio)
                                        colorResource(id = R.color.bg_middle)
                                    else
                                        Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                DiamondLine(modifier = Modifier.padding(bottom = 8.dp))

                if (selectedScreen.value == AdminScreenType.Humor) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SessionTitle(
                            text = "Adicionar Novo Humor",
                            fontWeight = FontWeight.Bold,
                            fontFamily = Sora,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Preencha os campos para adicionar um novo estado de humor e emoji ao sistema.",
                            fontSize = 18.sp,
                            fontFamily = Sora,
                            fontWeight = FontWeight.Normal,
                            color = colorResource(id = R.color.light_blue),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                        TextField(
                            value = moodText,
                            onValueChange = { moodText = it },
                            label = { Text("Estado de Humor", color = colorResource(id = R.color.light_blue)) },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedLabelColor = colorResource(id = R.color.light_blue),
                                focusedLabelColor = colorResource(id = R.color.primary),
                                cursorColor = colorResource(id = R.color.primary),
                                focusedIndicatorColor = colorResource(id = R.color.primary)
                            ),
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextField(
                            value = emojiText,
                            onValueChange = { emojiText = it },
                            label = { Text("Emoji", color = colorResource(id = R.color.light_blue)) },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedLabelColor = colorResource(id = R.color.light_blue),
                                focusedLabelColor = colorResource(id = R.color.primary),
                                cursorColor = colorResource(id = R.color.primary),
                                focusedIndicatorColor = colorResource(id = R.color.primary)
                            ),
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                if (moodText.isNotBlank() && emojiText.isNotBlank()) {
                                    humorViewModel.addHumor(
                                        estadoDeHumor = moodText,
                                        emoji = emojiText,
                                        onLimitReached = { showLimitMessage = true }
                                    )
                                    if (!showLimitMessage) {
                                        moodText = ""
                                        emojiText = ""
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue))
                        ) {
                            Text(
                                text = "SALVAR NOVO HUMOR",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = colorResource(id = R.color.primary)
                            )
                        }
                        SessionTitle(
                            text = "Humores Existentes (${(humorDataState as? HumorDataState.Success)?.data?.size ?: 0}/9)",
                            fontWeight = FontWeight.Bold,
                            fontFamily = Sora,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 24.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        when (val state = humorDataState) {
                            is HumorDataState.Loading -> {
                                Text(text = "Carregando humores...", color = colorResource(id = R.color.primary))
                            }
                            is HumorDataState.Success -> {
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    state.data.forEach { humor ->
                                        HumorListItem(humor = humor, onDelete = { id ->
                                            Log.d("AdminScreen", "Tentando excluir humor com ID: $id")
                                            humorViewModel.deleteHumor(id)
                                        })
                                    }
                                }
                            }
                            is HumorDataState.Error -> {
                                Text(text = "Erro ao buscar humores: ${state.message}", color = Color.Red)
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(colorResource(id = R.color.bg_dark))
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { showReport = true },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (showReport) colorResource(id = R.color.primary) else Color.Transparent
                                )
                            ) {
                                Text(
                                    text = "VOTOS",
                                    color = if (showReport) colorResource(id = R.color.bg_dark) else colorResource(id = R.color.primary)
                                )
                            }
                            Button(
                                onClick = { showReport = false },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (!showReport) colorResource(id = R.color.primary) else Color.Transparent
                                )
                            ) {
                                Text(
                                    text = "ADICIONAR",
                                    color = if (!showReport) colorResource(id = R.color.bg_dark) else colorResource(id = R.color.primary)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        if (showReport) {
                            SessionTitle(
                                text = "Resultado da Votação de Apoio",
                                fontWeight = FontWeight.Bold,
                                fontFamily = Sora,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            when (val state = activityDataState) {
                                is ActivityDataState.Loading -> {
                                    Text(text = "Carregando relatório...", color = colorResource(id = R.color.primary))
                                }
                                is ActivityDataState.Success -> {
                                    if (state.report.isEmpty()) {
                                        Text(text = "Nenhuma atividade ou voto encontrado.", color = colorResource(id = R.color.light_blue))
                                    } else {
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            state.report.sortedByDescending { it.voteCount }.forEach { reportItem ->
                                                VoteReportListItem(
                                                    reportItem = reportItem,
                                                    showVoteCount = true,
                                                    onDelete = { activityId ->
                                                        activityViewModel.deleteActivity(activityId)
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                                is ActivityDataState.Error -> {
                                    Text(text = "Erro ao buscar relatório: ${state.message}", color = Color.Red)
                                }
                            }

                        } else {
                            SessionTitle(
                                text = "Adicionar Nova Opção de Apoio",
                                fontWeight = FontWeight.Bold,
                                fontFamily = Sora,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Preencha o campo para adicionar uma nova opção de atividade para votação.",
                                fontSize = 18.sp,
                                fontFamily = Sora,
                                fontWeight = FontWeight.Normal,
                                color = colorResource(id = R.color.light_blue),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 24.dp)
                            )
                            TextField(
                                value = newActivityText,
                                onValueChange = { newActivityText = it },
                                label = { Text("Opção de Atividade", color = colorResource(id = R.color.light_blue)) },
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedLabelColor = colorResource(id = R.color.light_blue),
                                    focusedLabelColor = colorResource(id = R.color.primary),
                                    cursorColor = colorResource(id = R.color.primary),
                                    focusedIndicatorColor = colorResource(id = R.color.primary)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    if (newActivityText.isNotBlank()) {
                                        activityViewModel.addActivity(newActivityText)
                                        newActivityText = ""
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue))
                            ) {
                                Text(
                                    text = "ADICIONAR ATIVIDADE",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = colorResource(id = R.color.primary)
                                )
                            }
                            SessionTitle(
                                text = "Opções Existentes",
                                fontWeight = FontWeight.Bold,
                                fontFamily = Sora,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 24.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            when (val state = activityDataState) {
                                is ActivityDataState.Success -> {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        state.activities.forEach { activity ->
                                            VoteReportListItem(
                                                reportItem = ActivityVoteReportDTO(activity.id ?: "", activity.activity, 0),
                                                showVoteCount = false,
                                                onDelete = { activityId ->
                                                    activityViewModel.deleteActivity(activityId)
                                                }
                                            )
                                        }
                                    }
                                }
                                else -> Unit
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    if (showLimitMessage) {
        AlertDialog(
            onDismissRequest = { showLimitMessage = false },
            title = { Text("Limite de Humores Atingido") },
            text = { Text("Você atingiu o limite máximo de 9 humores para exibição. Por favor, exclua um humor existente antes de adicionar um novo.") },
            confirmButton = {
                Button(onClick = { showLimitMessage = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun HumorListItem(humor: HumorData, onDelete: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(colorResource(id = R.color.bg_dark), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = humor.emoji,
                fontSize = 24.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(
                text = humor.estadoDeHumor,
                color = colorResource(id = R.color.primary),
                fontWeight = FontWeight.Bold
            )
        }
        IconButton(onClick = {
            humor.id?.let { onDelete(it) }
        }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Excluir",
                tint = colorResource(id = R.color.light_blue)
            )
        }
    }
}

@Composable
fun VoteReportListItem(reportItem: ActivityVoteReportDTO, showVoteCount: Boolean, onDelete: (String) -> Unit) {
    val showDialog = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(colorResource(id = R.color.bg_dark), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = reportItem.activityName,
            color = colorResource(id = R.color.primary),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        if (showVoteCount) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "(${reportItem.voteCount} votos)",
                color = colorResource(id = R.color.light_green),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        }
        IconButton(onClick = {
            if (!reportItem.activityId.isNullOrBlank()) {
                showDialog.value = true
            }
        }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Excluir",
                tint = colorResource(id = R.color.light_blue)
            )
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Confirmar Exclusão") },
            text = { Text("Tem certeza que deseja excluir a opção: ${reportItem.activityName}? Isso é irreversível e pode afetar a contagem de votos existentes.") },
            confirmButton = {
                Button(onClick = {
                    onDelete(reportItem.activityId)
                    showDialog.value = false
                }) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}