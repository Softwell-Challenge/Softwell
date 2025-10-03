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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.fiap.softwell.R
import br.com.fiap.softwell.components.DiamondLine
import br.com.fiap.softwell.components.ExpandableTipCard
import br.com.fiap.softwell.components.SessionTitle
import br.com.fiap.softwell.model.TipsData
import br.com.fiap.softwell.service.ActivityApiService
import br.com.fiap.softwell.ui.theme.Sora
import br.com.fiap.softwell.viewmodel.UserActivityState
import br.com.fiap.softwell.viewmodel.UserActivityViewModel
import br.com.fiap.softwell.viewmodel.UserActivityViewModelFactory
import br.com.fiap.softwell.viewmodel.VoteFeedbackState

enum class SupportScreenType {
    Care,
    Guidelines
}

@Composable
fun SupportScreen(
    navController: NavController,
    apiService: ActivityApiService
) {
    val viewModel: UserActivityViewModel = viewModel(
        factory = UserActivityViewModelFactory(apiService)
    )
    val state = viewModel.activityState.collectAsState().value
    val voteFeedbackState by viewModel.voteFeedbackState.collectAsState()

    val selectedScreen = remember { mutableStateOf(SupportScreenType.Care) }
    val selectedActivityId = remember { mutableStateOf<String?>(null) }
    val showVoteFeedbackDialog = remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.fetchActivities()
    }

    LaunchedEffect(voteFeedbackState) {
        when (voteFeedbackState) {
            is VoteFeedbackState.VotedSuccessfully,
            is VoteFeedbackState.VoteLimitReached,
            is VoteFeedbackState.Error -> {
                showVoteFeedbackDialog.value = true
            }
            else -> Unit
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
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
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
                    when (state) {
                        is UserActivityState.Loading -> {
                            Text("Carregando opções de atividades...", modifier = Modifier.padding(16.dp))
                        }
                        is UserActivityState.Error -> {
                            Text("Falha ao carregar as atividades. Verifique o servidor.", color = colorResource(id = R.color.black), modifier = Modifier.padding(16.dp))
                        }
                        is UserActivityState.Success -> {
                            state.activities.forEach { activity ->
                                Card(
                                    modifier = Modifier
                                        .padding(vertical = 4.dp, horizontal = 16.dp)
                                        .fillMaxWidth()
                                        .clickable { selectedActivityId.value = activity.id }
                                        .border(
                                            width = if (selectedActivityId.value == activity.id) 2.dp else 1.dp,
                                            color = if (selectedActivityId.value == activity.id) colorResource(id = R.color.light_blue)
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
                                        text = activity.activity,
                                        modifier = Modifier.padding(16.dp),
                                        fontSize = 16.sp,
                                        fontWeight = if (selectedActivityId.value == activity.id) FontWeight.Bold else FontWeight.Normal,
                                        color = colorResource(id = R.color.primary)
                                    )
                                }
                            }

                            if (state.activities.isEmpty()) {
                                Text("Nenhuma atividade disponível para votação.", modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                    Button(
                        onClick = {
                            selectedActivityId.value?.let { id ->
                                viewModel.registrarVoto(id)
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = selectedActivityId.value != null && state !is UserActivityState.Loading,
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue))
                    ) {
                        Text(
                            text = if (state is UserActivityState.Loading) "ENVIANDO..." else "VOTAR",
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
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    if (showVoteFeedbackDialog.value) {
        val feedback = voteFeedbackState
        val title: String
        val message: String

        when (feedback) {
            is VoteFeedbackState.VotedSuccessfully -> {
                title = "Voto Registrado!"
                message = "Obrigado por votar! Seu voto foi contabilizado com sucesso."
            }
            is VoteFeedbackState.VoteLimitReached -> {
                title = "Limite de Votação Atingido"
                message = feedback.message
            }
            is VoteFeedbackState.Error -> {
                title = "Erro"
                message = (feedback as VoteFeedbackState.Error).message
            }
            else -> {
                showVoteFeedbackDialog.value = false
                return
            }
        }

        AlertDialog(
            onDismissRequest = {
                showVoteFeedbackDialog.value = false
                viewModel.resetVoteState()
            },
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = {
                    showVoteFeedbackDialog.value = false
                    viewModel.resetVoteState()
                }) {
                    Text("OK")
                }
            }
        )
    }
}