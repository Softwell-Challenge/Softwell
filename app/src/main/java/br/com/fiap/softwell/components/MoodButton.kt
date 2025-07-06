package br.com.fiap.softwell.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.softwell.R
import br.com.fiap.softwell.model.MoodOption
import br.com.fiap.softwell.ui.theme.Sora

@Composable
fun MoodButton(
    mood: MoodOption,
    isSelected: Boolean,
    onClick: (MoodOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier
            .height(32.dp),
        onClick = { onClick(mood) },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected)
                colorResource(id = R.color.light_blue)
            else
                colorResource(id = R.color.bg_dark)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp,
            disabledElevation = 0.dp,
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 2.dp, vertical = 2.dp)
    ) {
        Text(
            text = mood.label,
            color = if (isSelected)
                colorResource(id = R.color.secondary)
            else
                colorResource(id = R.color.primary),
            fontSize = 12.sp,
            fontFamily = Sora,
            fontWeight = FontWeight.SemiBold
        )
    }
}
