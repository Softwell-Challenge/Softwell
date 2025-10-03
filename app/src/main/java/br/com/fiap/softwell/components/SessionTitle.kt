package br.com.fiap.softwell.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.softwell.ui.theme.Rubik

@Composable
fun SessionTitle(
    text: String,
    modifier: Modifier = Modifier.padding(horizontal = 8.dp),
    fontSize: Int = 24,
    fontFamily: FontFamily = Rubik,
    fontWeight: FontWeight = FontWeight.ExtraBold,
    color: Color = (MaterialTheme.colorScheme.primary)
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize.sp,
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        color = color
    )
}