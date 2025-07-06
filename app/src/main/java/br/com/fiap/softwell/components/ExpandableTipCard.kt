package br.com.fiap.softwell.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.softwell.R
import br.com.fiap.softwell.model.Tip
import br.com.fiap.softwell.ui.theme.Rubik
import br.com.fiap.softwell.ui.theme.Sora

@Composable
fun ExpandableTipCard(tip: Tip) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.bg_dark)
        )
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(8.dp)
        ) {
            Text(
                text = tip.title,
                color = colorResource(id = R.color.primary),
                fontWeight = FontWeight.Bold,
                fontFamily = Rubik,
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tip.summary,
                color = colorResource(id = R.color.primary),
                fontSize = 20.sp,
                fontFamily = Sora,
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (expanded) "Ver menos ▲" else "Ver mais ▼",
                color = colorResource(id = R.color.primary),
                fontFamily = Sora,
                modifier = Modifier.align(Alignment.End)
            )

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = tip.details,
                    fontFamily = Sora,
                    color = colorResource(id = R.color.primary)
                )
            }
        }
    }
}
