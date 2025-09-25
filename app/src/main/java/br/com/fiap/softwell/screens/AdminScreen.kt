package br.com.fiap.softwell.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.fiap.softwell.R
import br.com.fiap.softwell.components.DiamondLine
import br.com.fiap.softwell.components.SessionTitle
import br.com.fiap.softwell.model.HumorData
import br.com.fiap.softwell.ui.theme.Sora
import br.com.fiap.softwell.viewmodel.HumorDataState
import br.com.fiap.softwell.viewmodel.HumorViewModel
import android.util.Log // Importado para logs de debug

// Usei AdminScreenType para manter o padrão que você definiu
enum class AdminScreenType {
    Humor,
    Apoio
}

@Composable
fun AdminScreen(navController: NavController, humorViewModel: HumorViewModel = viewModel()) {
    var moodText by remember { mutableStateOf("") }
    var emojiText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val selectedScreen = remember { mutableStateOf(AdminScreenType.Humor) }

    // Obtenha o estado reativo do ViewModel
    val humorDataState by humorViewModel.humorDataState.collectAsState()

    // Dispara a busca de dados assim que a tela é composta
    LaunchedEffect(Unit) {
        humorViewModel.fetchHumorData()
    }

    val diagonalGradient = Brush.linearGradient(
        colors = listOf(
            colorResource(id = R.color.bg_dark),
            colorResource(id = R.color.bg_middle),
            colorResource(id = R.color.bg_light)
        ),
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
                .shadow(elevation = 6.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState) // Esta rolagem irá funcionar para o conteúdo inteiro
            ) {
                // ... (Cabeçalho, botões Humor/Apoio, e DiamondLine, sem alterações) ...
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = colorResource(id = R.color.light_green),
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { navController.popBackStack() }
                    )
                    Row(
                        modifier = Modifier
                            .background(
                                color = colorResource(id = R.color.bg_dark),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        SessionTitle(
                            text = "Humor",
                            fontWeight = FontWeight.Bold,
                            fontFamily = Sora,
                            color = colorResource(id = R.color.primary),
                            modifier = Modifier
                                .clickable { selectedScreen.value = AdminScreenType.Humor }
                                .background(
                                    color = if (selectedScreen.value == AdminScreenType.Humor)
                                        colorResource(id = R.color.bg_middle)
                                    else
                                        Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        SessionTitle(
                            text = "Apoio",
                            fontWeight = FontWeight.Bold,
                            fontFamily = Sora,
                            color = colorResource(id = R.color.primary),
                            modifier = Modifier
                                .clickable { selectedScreen.value = AdminScreenType.Apoio }
                                .background(
                                    color = if (selectedScreen.value == AdminScreenType.Apoio)
                                        colorResource(id = R.color.bg_middle)
                                    else
                                        Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                DiamondLine(modifier = Modifier.padding(bottom = 8.dp))

                if (selectedScreen.value == AdminScreenType.Humor) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // ... (Formulário Adicionar Novo Humor, sem alterações) ...
                        SessionTitle(
                            text = "Adicionar Novo Humor",
                            fontWeight = FontWeight.Bold,
                            fontFamily = Sora,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Preencha os campos para adicionar um novo estado de humor e emoji ao sistema.",
                            fontSize = 18.sp,
                            fontFamily = Sora,
                            fontWeight = FontWeight.Normal,
                            color = colorResource(id = R.color.light_blue),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                        TextField(
                            value = moodText,
                            onValueChange = { moodText = it },
                            label = { Text("Estado de Humor", color = colorResource(id = R.color.light_blue)) },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedLabelColor = colorResource(id = R.color.light_blue),
                                focusedLabelColor = colorResource(id = R.color.primary),
                                cursorColor = colorResource(id = R.color.primary),
                                focusedIndicatorColor = colorResource(id = R.color.primary)
                            ),
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextField(
                            value = emojiText,
                            onValueChange = { emojiText = it },
                            label = { Text("Emoji", color = colorResource(id = R.color.light_blue)) },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedLabelColor = colorResource(id = R.color.light_blue),
                                focusedLabelColor = colorResource(id = R.color.primary),
                                cursorColor = colorResource(id = R.color.primary),
                                focusedIndicatorColor = colorResource(id = R.color.primary)
                            ),
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                if (moodText.isNotBlank() && emojiText.isNotBlank()) {
                                    humorViewModel.addHumor(moodText, emojiText)
                                    moodText = ""
                                    emojiText = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue))
                        ) {
                            Text(
                                text = "SALVAR NOVO HUMOR",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = colorResource(id = R.color.primary)
                            )
                        }

                        // --- Lista de Humores ---
                        SessionTitle(
                            text = "Humores Existentes",
                            fontWeight = FontWeight.Bold,
                            fontFamily = Sora,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 24.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Renderiza a lista com base no estado do ViewModel
                        when (val state = humorDataState) {
                            is HumorDataState.Loading -> {
                                Text(text = "Carregando humores...", color = colorResource(id = R.color.primary))
                            }
                            is HumorDataState.Success -> {
                                // CORREÇÃO AQUI: Substituímos o LazyColumn com altura fixa por um Column normal
                                // para que a lista se expanda e se integre à rolagem principal da tela.
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    state.data.forEach { humor ->
                                        HumorListItem(humor = humor, onDelete = { id ->
                                            Log.d("AdminScreen", "Tentando excluir humor com ID: $id")
                                            humorViewModel.deleteHumor(id)
                                        })
                                    }
                                }
                            }
                            is HumorDataState.Error -> {
                                Text(text = "Erro ao buscar humores: ${state.message}", color = Color.Red)
                            }
                        }
                    }
                } else {
                    // ... (Conteúdo da tela de Apoio, sem alterações) ...
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SessionTitle(
                            text = "Apoio",
                            fontWeight = FontWeight.Bold,
                            fontFamily = Sora,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Este é o conteúdo da tela de Apoio.",
                            modifier = Modifier.padding(top = 16.dp),
                            color = colorResource(id = R.color.primary)
                        )
                    }
                }
            }
        }
    }
}

// O HumorListItem está perfeito e não precisa de alteração.
@Composable
fun HumorListItem(humor: HumorData, onDelete: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(colorResource(id = R.color.bg_dark), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = humor.emoji,
                fontSize = 24.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(
                text = humor.estadoDeHumor,
                color = colorResource(id = R.color.primary),
                fontWeight = FontWeight.Bold
            )
        }
        IconButton(onClick = {
            // Lógica de exclusão segura.
            humor.id?.let { onDelete(it) }
        }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Excluir",
                tint = colorResource(id = R.color.light_blue)
            )
        }
    }
}