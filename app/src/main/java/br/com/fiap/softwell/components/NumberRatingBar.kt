package br.com.fiap.softwell.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.softwell.R
import br.com.fiap.softwell.ui.theme.Sora

@Composable
fun NumberRatingBar(
    currentRating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color = colorResource(id = R.color.light_blue),
    inactiveColor: Color = colorResource(id = R.color.bg_dark),
    numberSize: Int = 36
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            Box(
                modifier = Modifier
                    .clickable { onRatingChanged(i) }
                    .padding(4.dp)
                    .background(
                        color = if (i <= currentRating) activeColor else inactiveColor,
                        shape = CircleShape
                    )
                    .size(numberSize.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = i.toString(),
                    fontSize = 16.sp,
                    color =
                        if (i <= currentRating) colorResource(id = R.color.secondary)
                        else colorResource(id = R.color.primary),
                    fontFamily = Sora,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}