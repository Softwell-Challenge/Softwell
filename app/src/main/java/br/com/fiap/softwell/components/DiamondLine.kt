package br.com.fiap.softwell.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.com.fiap.softwell.R

@Composable
fun DiamondLine(
    modifier: Modifier = Modifier,
    horizontalPadding: Int = 8
) {
    Image(
        modifier = modifier
            .fillMaxWidth()
            .height(12.dp)
            .padding(horizontal = horizontalPadding.dp),
        painter = painterResource(id = R.drawable.diamond_line),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}