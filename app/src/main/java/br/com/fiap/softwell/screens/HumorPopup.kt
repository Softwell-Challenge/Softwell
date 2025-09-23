package br.com.fiap.softwell.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.fiap.softwell.components.MoodButton
import br.com.fiap.softwell.model.HumorData
import br.com.fiap.softwell.model.MoodOption
import br.com.fiap.softwell.ui.theme.Sora

@Composable
fun HumorPopup(
    humorData: HumorData,
    onDismiss: () -> Unit,
    onSend: (moodId: String, emoji: String) -> Unit
) {
    var selectedMoodId by remember { mutableStateOf<String?>(null) }
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
                text = humorData.questionText,
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                fontFamily = Sora,
                fontWeight = FontWeight.SemiBold
            )

            // Mood Buttons
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
                    humorData.moodOptions.take(3).forEach { mood ->
                        MoodButton(
                            mood = mood,
                            isSelected = selectedMoodId == mood.id,
                            onClick = { selectedMoodId = it.id },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    humorData.moodOptions.drop(3).forEach { mood ->
                        MoodButton(
                            mood = mood,
                            isSelected = selectedMoodId == mood.id,
                            onClick = { selectedMoodId = it.id },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Escolha seu emoji de hoje!",
                modifier = Modifier.padding(horizontal = 8.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                fontFamily = Sora,
                fontWeight = FontWeight.SemiBold
            )

            // Emoji Selection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                humorData.emojis.forEach { emoji ->
                    Text(
                        text = emoji,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .clickable { selectedEmoji = emoji }
                            .background(
                                color = if (selectedEmoji == emoji) Color.LightGray.copy(alpha = 0.5f) else Color.Transparent,
                                shape = CircleShape
                            )
                            .padding(2.dp)
                    )
                }
            }

            // Send Button
            Button(
                onClick = {
                    if (selectedMoodId != null && selectedEmoji != null) {
                        onSend(selectedMoodId!!, selectedEmoji!!)
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("ENVIAR")
            }
        }
    }
}