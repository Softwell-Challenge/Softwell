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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.softwell.R
import br.com.fiap.softwell.components.DashboardCard
import br.com.fiap.softwell.components.DiamondLine
import br.com.fiap.softwell.components.MoodButton
import br.com.fiap.softwell.components.SessionTitle
import br.com.fiap.softwell.dao.AppDatabase
import br.com.fiap.softwell.model.MoodOption
import br.com.fiap.softwell.model.ThemeViewModel
import br.com.fiap.softwell.model.UserMood
import br.com.fiap.softwell.ui.theme.Sora
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(navController: NavController, themeViewModel: ThemeViewModel) {
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val userMoodDao = db.userMoodDao()

    val moodOptions = listOf(
        MoodOption("motivated", "MOTIVADO"),
        MoodOption("tired", "CANSADO"),
        MoodOption("worried", "PREOCUPADO"),
        MoodOption("stressed", "ESTRESSADO"),
        MoodOption("excited", "ANIMADO"),
        MoodOption("satisfied", "SATISFEITO")
    )
    val selectedMoodId = remember { mutableStateOf<String?>(null) }
    val emojis = listOf("🥱", "😁", "😮‍💨", "😰", "😱", "😡")
    val selectedEmoji = remember { mutableStateOf<String?>(null) }
    val diagonalGradient = Brush.linearGradient(
        colors = listOf(
            colorResource(id = R.color.bg_dark),
            colorResource(id = R.color.bg_middle),
            colorResource(id = R.color.bg_light)),
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
                    Switch(
                        checked = themeViewModel.isDarkTheme.value,
                        onCheckedChange = { themeViewModel.toggleTheme() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = colorResource(id = R.color.bg_light),
                            uncheckedThumbColor = colorResource(id = R.color.bg_dark)
                        )
                    )
                }
                DiamondLine()
                SessionTitle(
                    text = "Diário de Humor",
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally))
                Text(
                    text = "Como você está se sentindo hoje?",
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    fontFamily = Sora,
                    fontWeight = FontWeight.SemiBold
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        moodOptions.take(3).forEach { mood ->
                            MoodButton(
                                mood = mood,
                                isSelected = selectedMoodId.value == mood.id,
                                onClick = { selectedMoodId.value = it.id },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        moodOptions.drop(3).forEach { mood ->
                            MoodButton(
                                mood = mood,
                                isSelected = selectedMoodId.value == mood.id,
                                onClick = { selectedMoodId.value = it.id },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Escolha seu emoji de hoje!",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    fontFamily = Sora,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    emojis.forEach { emoji ->
                        Text(
                            text = emoji,
                            fontSize = 32.sp,
                            modifier = Modifier
                                .clickable { selectedEmoji.value = emoji }
                                .background(
                                    if (selectedEmoji.value == emoji) colorResource(id = R.color.light_blue) else Color.Transparent,
                                    shape = CircleShape
                                )
                                .padding(2.dp)
                        )
                    }
                }
                Button(
                    onClick = {
                        val moodId = selectedMoodId.value
                        val emoji = selectedEmoji.value

                        if (moodId != null && emoji != null) {
                            val userMood = UserMood(
                                moodId = moodId,
                                emoji = emoji,
                                timestamp = System.currentTimeMillis()
                            )

                            CoroutineScope(Dispatchers.IO).launch {
                                userMoodDao.insert(userMood)
                            }
                        }
                    },
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
                Spacer(modifier = Modifier.height(24.dp))
                DiamondLine()
                Spacer(modifier = Modifier.height(18.dp))
                DashboardCard("Avaliação Psicossocial", "psychosocial", navController)
                DashboardCard("Recursos de Apoio", "support", navController)
                DashboardCard("Gráficos Pessoais", "graphic", navController)
            }
        }
    }
}