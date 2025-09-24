package br.com.fiap.softwell.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.fiap.softwell.model.HumorData
import br.com.fiap.softwell.ui.theme.Sora

@Composable
fun HumorPopup(
    humorDataList: List<HumorData>,
    onDismiss: () -> Unit,
    onSend: (estadoDeHumor: String, emoji: String) -> Unit
) {
    var selectedMoodText by remember { mutableStateOf<String?>(null) }
    var selectedEmoji by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Como você está se sentindo hoje?",
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                fontFamily = Sora,
                fontWeight = FontWeight.SemiBold
            )

            // Mood Buttons e Emojis
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
                    humorDataList.take(3).forEach { humor ->
                        MoodButton(
                            moodText = humor.estadoDeHumor,
                            emoji = humor.emoji,
                            isSelected = selectedMoodText == humor.estadoDeHumor,
                            onClick = {
                                selectedMoodText = humor.estadoDeHumor
                                selectedEmoji = humor.emoji
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    humorDataList.drop(3).forEach { humor ->
                        MoodButton(
                            moodText = humor.estadoDeHumor,
                            emoji = humor.emoji,
                            isSelected = selectedMoodText == humor.estadoDeHumor,
                            onClick = {
                                selectedMoodText = humor.estadoDeHumor
                                selectedEmoji = humor.emoji
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botão ENVIAR
            Button(
                onClick = {
                    if (selectedMoodText != null && selectedEmoji != null) {
                        onSend(selectedMoodText!!, selectedEmoji!!)
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("ENVIAR")
            }
        }
    }
}

@Composable
fun MoodButton(
    moodText: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = if (isSelected) Color.LightGray.copy(alpha = 0.5f) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = emoji, fontSize = 32.sp)
        Text(text = moodText, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}