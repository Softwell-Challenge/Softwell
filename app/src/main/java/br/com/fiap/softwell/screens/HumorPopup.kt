package br.com.fiap.softwell.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onSend: (estadoDeHumor: String, emoji: String) -> Unit,
    isSendButtonEnabled: Boolean,
    buttonText: String
) {
    var selectedMoodText by remember { mutableStateOf<String?>(null) }
    var selectedEmoji by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    val limitedHumors = humorDataList.take(9)
    val humorRows = limitedHumors.chunked(3)

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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                humorRows.forEach { rowOfHumors ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowOfHumors.forEach { humor ->
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

                        if (rowOfHumors.size < 3) {
                            repeat(3 - rowOfHumors.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (selectedMoodText != null && selectedEmoji != null) {
                        onSend(selectedMoodText!!, selectedEmoji!!)
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                enabled = selectedMoodText != null && isSendButtonEnabled
            ) {
                Text(buttonText)
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
                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = emoji, fontSize = 32.sp)
        Text(text = moodText, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
    }
}