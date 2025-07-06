package br.com.fiap.softwell.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.softwell.R
import br.com.fiap.softwell.components.CustomDropdown
import br.com.fiap.softwell.components.DiamondLine
import br.com.fiap.softwell.components.NumberRatingBar
import br.com.fiap.softwell.components.Question
import br.com.fiap.softwell.components.RatingSlider
import br.com.fiap.softwell.components.SessionTitle
import br.com.fiap.softwell.model.UserMood
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PsychosocialScreen(navController: NavController) {
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

    // Estados para cada pergunta
    var workloadAssessment by remember { mutableStateOf("") }
    var qualityOfLifeImpact by remember { mutableStateOf("") }
    var extraHours by remember { mutableStateOf("") }
    var warningSigns by remember { mutableStateOf("") }
    var mentalHealthImpact by remember { mutableStateOf("") }

    var bossRating by remember { mutableStateOf(0) }
    var coworkerRating by remember { mutableStateOf(0) }
    var coworkerRespect by remember { mutableStateOf(0) }
    var teamRelationship by remember { mutableStateOf(0) }
    var freedomSpeech by remember { mutableStateOf(0) }
    var welcomedPart by remember { mutableStateOf(0) }
    var cooperationSpirit by remember { mutableStateOf(0) }

    var taskClarity by rememberSaveable { mutableStateOf(3f) }
    var openCommunication by rememberSaveable { mutableStateOf(3f) }
    var infoFlow by rememberSaveable { mutableStateOf(3f) }
    var goalClarity by rememberSaveable { mutableStateOf(3f) }

    var leaderCaresWellbeing by rememberSaveable { mutableStateOf(3f) }
    var leaderIsAvailable by rememberSaveable { mutableStateOf(3f) }
    var comfortableReportingIssues by rememberSaveable { mutableStateOf(3f) }
    var leaderRecognizesEfforts by rememberSaveable { mutableStateOf(3f) }
    var trustAndTransparency by rememberSaveable { mutableStateOf(3f) }

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
                .shadow(
                    elevation = 6.dp
                )
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
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
                SessionTitle(text = "Carga de Trabalho")
                Question(text = "Como você avalia sua carga de trabalho?")
                CustomDropdown(
                    options = listOf("Muito Leve", "Leve", "Média", "Alta", "Muito Alta"),
                    selectedOption = workloadAssessment,
                    onOptionSelected = { workloadAssessment = it },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Question(text = "Sua carga de trabalho afeta sua qualidade de vida?")
                CustomDropdown(
                    options = listOf("Não", "Raramente", "Às vezes", "Frequentemente", "Sempre"),
                    selectedOption = qualityOfLifeImpact,
                    onOptionSelected = { qualityOfLifeImpact = it },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Question(text = "Você trabalha além do seu horário regular?")
                CustomDropdown(
                    options = listOf("Não", "Raramente", "Às vezes", "Frequentemente", "Sempre"),
                    selectedOption = extraHours,
                    onOptionSelected = { extraHours = it },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                DiamondLine(modifier = Modifier.padding(vertical = 8.dp))
                SessionTitle(text = "Sinais de Alerta")
                Question(text = "Você tem apresentado sintomas como insônia, irritabilidade ou cansaço extremo?")
                CustomDropdown(
                    options = listOf("Nunca", "Raramente", "Às vezes", "Frequentemente", "Sempre"),
                    selectedOption = warningSigns,
                    onOptionSelected = { warningSigns = it },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Question(text = "Você sente que sua saúde mental prejudica sua produtividade no trabalho?")
                CustomDropdown(
                    options = listOf("Nunca", "Raramente", "Às vezes", "Frequentemente", "Sempre"),
                    selectedOption = mentalHealthImpact,
                    onOptionSelected = { mentalHealthImpact = it },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                DiamondLine(modifier = Modifier.padding(vertical = 8.dp))
                SessionTitle(text = "Clima - Relacionamento")
                SessionTitle(text = "(Sendo 01 - ruim e 05 - Ótimo)")
                Question(text = "Como está o seu relacionamento com seu chefe numa escala de 1 a 5?")
                NumberRatingBar(
                    currentRating = bossRating,
                    onRatingChanged = { newRating -> bossRating = newRating },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                Question(text = "Como está o seu relacionamento com seus colegas de trabalho numa escala de 1 a 5? ")
                NumberRatingBar(
                    currentRating = coworkerRating,
                    onRatingChanged = { newRating -> coworkerRating = newRating },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                Question(text = "Sinto que sou tratado(a) com respeito pelos meus colegas de trabalho.")
                NumberRatingBar(
                    currentRating = coworkerRespect,
                    onRatingChanged = { newRating -> coworkerRespect = newRating },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                Question(text = "Consigo me relacionar de forma saudável e colaborativa com minha equipe.")
                NumberRatingBar(
                    currentRating = teamRelationship,
                    onRatingChanged = { newRating -> teamRelationship = newRating },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                Question(text = "Tenho liberdade para expressar minhas opiniões sem medo de retaliações.")
                NumberRatingBar(
                    currentRating = freedomSpeech,
                    onRatingChanged = { newRating -> freedomSpeech = newRating },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                Question(text = "Me sinto acolhido(a) a parte do time onde trabalho.")
                NumberRatingBar(
                    currentRating = welcomedPart,
                    onRatingChanged = { newRating -> welcomedPart = newRating },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                Question(text = "Sinto que existe espírito de cooperação entre os colaboradores.")
                NumberRatingBar(
                    currentRating = cooperationSpirit,
                    onRatingChanged = { newRating -> cooperationSpirit = newRating },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                DiamondLine(modifier = Modifier.padding(vertical = 8.dp))
                SessionTitle(text = "Comunicação")
                SessionTitle(text = "(Sendo 01 - ruim e 05 - Ótimo)")
                Question(text = "Recebo orientações claras e objetivas sobre minhas atividades e responsabilidades.")
                RatingSlider(
                    value = taskClarity,
                    onValueChange = { taskClarity = it }
                )
                Question(text = "Sinto que posso me comunicar abertamente com minha liderança.")
                RatingSlider(
                    value = openCommunication,
                    onValueChange = { openCommunication = it }
                )
                Question(text = "As informações importantes circulam de forma eficiente dentro da empresa.")
                RatingSlider(
                    value = infoFlow,
                    onValueChange = { infoFlow = it }
                )
                Question(text = "Tenho clareza sobre as metas e os resultados esperados de mim.")
                RatingSlider(
                    value = goalClarity,
                    onValueChange = { goalClarity = it },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                DiamondLine(modifier = Modifier.padding(vertical = 8.dp))
                SessionTitle(text = "Relação com a Liderança")
                SessionTitle(text = "(Sendo 01 - ruim e 05 - Ótimo)")
                Question(text = "Minha liderança demonstra interesse pelo meu bem-estar no trabalho.")
                RatingSlider(
                    value = leaderCaresWellbeing,
                    onValueChange = { leaderCaresWellbeing = it }
                )
                Question(text = "Minha liderança está disponível para me ouvir quando necessário.")
                RatingSlider(
                    value = leaderIsAvailable,
                    onValueChange = { leaderIsAvailable = it }
                )
                Question(text = "Me sinto confortável para reportar problemas ou dificuldades ao meu líder.")
                RatingSlider(
                    value = comfortableReportingIssues,
                    onValueChange = { comfortableReportingIssues = it }
                )
                Question(text = "Minha liderança reconhece minhas entregas e esforços.")
                RatingSlider(
                    value = leaderRecognizesEfforts,
                    onValueChange = { leaderRecognizesEfforts = it }
                )
                Question(text = "Existe confiança e transparência na relação com minha liderança.")
                RatingSlider(
                    value = trustAndTransparency,
                    onValueChange = { trustAndTransparency = it },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue))
                ) {
                    Text(
                        "ENVIAR",
                        color = colorResource(id = R.color.primary),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
