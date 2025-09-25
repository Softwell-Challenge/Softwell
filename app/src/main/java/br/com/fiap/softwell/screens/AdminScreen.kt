package br.com.fiap.softwell.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
                    .verticalScroll(scrollState)
            ) {
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
                                    // AQUI! Passe os dois parâmetros.
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
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                ) {
                                    items(state.data) { humor ->
                                        HumorListItem(humor = humor, onDelete = { id ->
                                            humorViewModel.deleteHumor(id)
                                        })
                                    }
                                }
                            }
                            is HumorDataState.Error -> {
                                Text(text = "Erro: ${state.message}", color = Color.Red)
                            }
                        }
                    }
                } else {
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
        IconButton(onClick = { humor.id?.let { onDelete(it) } }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Excluir",
                tint = colorResource(id = R.color.light_blue)
            )
        }
    }
}


//package br.com.fiap.softwell.screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import br.com.fiap.softwell.R
//import br.com.fiap.softwell.components.DiamondLine
//import br.com.fiap.softwell.components.SessionTitle
//import br.com.fiap.softwell.model.HumorData
//import br.com.fiap.softwell.ui.theme.Sora
//import br.com.fiap.softwell.viewmodel.HumorDataState
//import br.com.fiap.softwell.viewmodel.HumorViewModel
//
//enum class AdminScreenType {
//    Humor,
//    Apoio
//}
//
//@Composable
//fun AdminScreen(navController: NavController, humorViewModel: HumorViewModel = viewModel()) {
//    var moodText by remember { mutableStateOf("") }
//    var emojiText by remember { mutableStateOf("") }
//    val scrollState = rememberScrollState()
//    val selectedScreen = remember { mutableStateOf(AdminScreenType.Humor) }
//
//    val diagonalGradient = Brush.linearGradient(
//        colors = listOf(
//            colorResource(id = R.color.bg_dark),
//            colorResource(id = R.color.bg_middle),
//            colorResource(id = R.color.bg_light)
//        ),
//        start = Offset(0f, 0f),
//        end = Offset(1000f, 1000f)
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(diagonalGradient)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(8.dp)
//                .clip(RoundedCornerShape(6.dp))
//                .shadow(elevation = 6.dp)
//                .background(MaterialTheme.colorScheme.background)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .verticalScroll(scrollState)
//            ) {
//                // Cabeçalho com o botão de voltar e os dois títulos clicáveis
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 8.dp, vertical = 12.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Voltar",
//                        tint = colorResource(id = R.color.light_green),
//                        modifier = Modifier
//                            .size(32.dp)
//                            .clickable { navController.popBackStack() }
//                    )
//                    Row(
//                        modifier = Modifier
//                            .background(
//                                color = colorResource(id = R.color.bg_dark),
//                                shape = RoundedCornerShape(12.dp)
//                            )
//                            .padding(horizontal = 8.dp, vertical = 4.dp)
//                    ) {
//                        SessionTitle(
//                            text = "Humor",
//                            fontWeight = FontWeight.Bold,
//                            fontFamily = Sora,
//                            color = colorResource(id = R.color.primary),
//                            modifier = Modifier
//                                .clickable { selectedScreen.value = AdminScreenType.Humor }
//                                .background(
//                                    color = if (selectedScreen.value == AdminScreenType.Humor)
//                                        colorResource(id = R.color.bg_middle)
//                                    else
//                                        Color.Transparent,
//                                    shape = RoundedCornerShape(12.dp)
//                                )
//                                .padding(horizontal = 8.dp, vertical = 4.dp)
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        SessionTitle(
//                            text = "Apoio",
//                            fontWeight = FontWeight.Bold,
//                            fontFamily = Sora,
//                            color = colorResource(id = R.color.primary),
//                            modifier = Modifier
//                                .clickable { selectedScreen.value = AdminScreenType.Apoio }
//                                .background(
//                                    color = if (selectedScreen.value == AdminScreenType.Apoio)
//                                        colorResource(id = R.color.bg_middle)
//                                    else
//                                        Color.Transparent,
//                                    shape = RoundedCornerShape(12.dp)
//                                )
//                                .padding(horizontal = 8.dp, vertical = 4.dp)
//                        )
//                    }
//                }
//                DiamondLine(modifier = Modifier.padding(bottom = 8.dp))
//
//                // Conteúdo da tela, baseado no estado atual
//                if (selectedScreen.value == AdminScreenType.Humor) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        SessionTitle(
//                            text = "Adicionar Novo Humor",
//                            fontWeight = FontWeight.Bold,
//                            fontFamily = Sora,
//                            color = MaterialTheme.colorScheme.primary
//                        )
//                        Text(
//                            text = "Adicione um novo estado de humor e emoji ao sistema.",
//                            fontSize = 18.sp,
//                            fontFamily = Sora,
//                            fontWeight = FontWeight.Normal,
//                            color = colorResource(id = R.color.light_blue),
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier.padding(bottom = 24.dp)
//                        )
//                        TextField(
//                            value = moodText,
//                            onValueChange = { moodText = it },
//                            label = { Text("Estado de Humor", color = colorResource(id = R.color.light_blue)) },
//                            colors = TextFieldDefaults.colors(
//                                unfocusedContainerColor = Color.Transparent,
//                                focusedContainerColor = Color.Transparent,
//                                unfocusedLabelColor = colorResource(id = R.color.light_blue),
//                                focusedLabelColor = colorResource(id = R.color.primary),
//                                cursorColor = colorResource(id = R.color.primary),
//                                focusedIndicatorColor = colorResource(id = R.color.primary)
//                            ),
//                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
//                        )
//                        Spacer(modifier = Modifier.height(16.dp))
//                        TextField(
//                            value = emojiText,
//                            onValueChange = { emojiText = it },
//                            label = { Text("Emoji", color = colorResource(id = R.color.light_blue)) },
//                            colors = TextFieldDefaults.colors(
//                                unfocusedContainerColor = Color.Transparent,
//                                focusedContainerColor = Color.Transparent,
//                                unfocusedLabelColor = colorResource(id = R.color.light_blue),
//                                focusedLabelColor = colorResource(id = R.color.primary),
//                                cursorColor = colorResource(id = R.color.primary),
//                                focusedIndicatorColor = colorResource(id = R.color.primary)
//                            ),
//                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
//                        )
//                        Spacer(modifier = Modifier.height(24.dp))
//                        Button(
//                            onClick = {
//                                if (moodText.isNotBlank() && emojiText.isNotBlank()) {
//                                    humorViewModel.addHumor(moodText, emojiText)
//                                    moodText = ""
//                                    emojiText = ""
//                                }
//                            },
//                            modifier = Modifier.fillMaxWidth().height(50.dp),
//                            shape = RoundedCornerShape(12.dp),
//                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue))
//                        ) {
//                            Text(
//                                text = "SALVAR NOVO HUMOR",
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 18.sp,
//                                color = colorResource(id = R.color.primary)
//                            )
//                        }
//                        SessionTitle(
//                            text = "Humores Existentes",
//                            fontWeight = FontWeight.Bold,
//                            fontFamily = Sora,
//                            color = MaterialTheme.colorScheme.primary,
//                            modifier = Modifier.padding(top = 24.dp)
//                        )
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        val humorDataState by humorViewModel.humorDataState.collectAsState()
//                        LaunchedEffect(Unit) {
//                            humorViewModel.fetchHumorData()
//                        }
//
//                        when (val state = humorDataState) {
//                            is HumorDataState.Loading -> {
//                                // Indicador de progresso
//                            }
//                            is HumorDataState.Success -> {
//                                LazyColumn(
//                                    modifier = Modifier.fillMaxWidth().height(200.dp)
//                                ) {
//                                    items(state.data) { humor ->
//                                        HumorListItem(humor = humor, onDelete = {
//                                            humorViewModel.deleteHumor(it)
//                                        })
//                                    }
//                                }
//                            }
//                            is HumorDataState.Error -> {
//                                Text(text = "Erro: ${state.message}", color = Color.Red)
//                            }
//                        }
//                    }
//                } else {
//                    // Aqui você colocará o conteúdo da tela de Apoio
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        SessionTitle(
//                            text = "Apoio",
//                            fontWeight = FontWeight.Bold,
//                            fontFamily = Sora,
//                            color = MaterialTheme.colorScheme.primary
//                        )
//                        Text(
//                            text = "Este é o conteúdo da tela de Apoio.",
//                            modifier = Modifier.padding(top = 16.dp)
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun HumorListItem(humor: HumorData, onDelete: (String) -> Unit) {
//    // ...
//    IconButton(onClick = {
//        // Verifica se o ID não é nulo antes de chamar a função de exclusão
//        humor.id?.let { onDelete(it) }
//    }) {}
//}
//
////package br.com.fiap.softwell.screens
////
////import androidx.compose.foundation.background
////import androidx.compose.foundation.clickable
////import androidx.compose.foundation.layout.*
////import androidx.compose.foundation.rememberScrollState
////import androidx.compose.foundation.shape.RoundedCornerShape
////import androidx.compose.foundation.verticalScroll
////import androidx.compose.material.icons.Icons
////import androidx.compose.material.icons.filled.ArrowBack
////import androidx.compose.material3.Button
////import androidx.compose.material3.ButtonDefaults
////import androidx.compose.material3.Icon
////import androidx.compose.material3.MaterialTheme
////import androidx.compose.material3.Text
////import androidx.compose.material3.TextField
////import androidx.compose.material3.TextFieldDefaults
////import androidx.compose.runtime.*
////import androidx.compose.ui.Alignment
////import androidx.compose.ui.Modifier
////import androidx.compose.ui.draw.clip
////import androidx.compose.ui.draw.shadow
////import androidx.compose.ui.geometry.Offset
////import androidx.compose.ui.graphics.Brush
////import androidx.compose.ui.graphics.Color
////import androidx.compose.ui.res.colorResource
////import androidx.compose.ui.text.font.FontWeight
////import androidx.compose.ui.unit.dp
////import androidx.compose.ui.unit.sp
////import androidx.lifecycle.viewmodel.compose.viewModel
////import androidx.navigation.NavController
////import br.com.fiap.softwell.R
////import br.com.fiap.softwell.components.DiamondLine
////import br.com.fiap.softwell.components.SessionTitle
////import br.com.fiap.softwell.ui.theme.Sora
////import br.com.fiap.softwell.viewmodel.HumorViewModel
////
////@Composable
////fun AdminHumorScreen(navController: NavController, humorViewModel: HumorViewModel = viewModel()) {
////    var moodText by remember { mutableStateOf("") }
////    var emojiText by remember { mutableStateOf("") }
////    val scrollState = rememberScrollState()
////
////    val diagonalGradient = Brush.linearGradient(
////        colors = listOf(
////            colorResource(id = R.color.bg_dark),
////            colorResource(id = R.color.bg_middle),
////            colorResource(id = R.color.bg_light)
////        ),
////        start = Offset(0f, 0f),
////        end = Offset(1000f, 1000f)
////    )
////
////    Box(
////        modifier = Modifier
////            .fillMaxSize()
////            .background(diagonalGradient)
////    ) {
////        Box(
////            modifier = Modifier
////                .fillMaxSize()
////                .padding(8.dp)
////                .clip(RoundedCornerShape(6.dp))
////                .shadow(elevation = 6.dp)
////                .background(MaterialTheme.colorScheme.background)
////        ) {
////            Column(
////                modifier = Modifier
////                    .fillMaxSize()
////                    .verticalScroll(scrollState)
////            ) {
////                // Cabeçalho com o botão de voltar
////                Row(
////                    modifier = Modifier
////                        .fillMaxWidth()
////                        .padding(horizontal = 8.dp, vertical = 12.dp),
////                    verticalAlignment = Alignment.CenterVertically,
////                    horizontalArrangement = Arrangement.SpaceBetween // Adicione esta linha
////                ) {
////                    Icon(
////                        imageVector = Icons.Default.ArrowBack,
////                        contentDescription = "Voltar",
////                        tint = colorResource(id = R.color.light_green),
////                        modifier = Modifier
////                            .size(32.dp)
////                            .clickable { navController.popBackStack() }
////                    )
////                    SessionTitle(
////                        text = "Admin",
////                        fontWeight = FontWeight.Bold,
////                        fontFamily = Sora,
////                        color = colorResource(id = R.color.primary),
////                        modifier = Modifier
////                            .background(
////                                color = colorResource(id = R.color.bg_dark),
////                                shape = RoundedCornerShape(12.dp)
////                            )
////                            .padding(horizontal = 8.dp, vertical = 4.dp)
////                    )
////                }
////                DiamondLine(modifier = Modifier.padding(bottom = 8.dp))
////
////                // Formulário de administração
////                Column(
////                    modifier = Modifier
////                        .fillMaxWidth()
////                        .padding(16.dp),
////                    horizontalAlignment = Alignment.CenterHorizontally
////                ) {
////                    SessionTitle(
////                        text = "Humor",
////                        fontWeight = FontWeight.Bold,
////                        fontFamily = Sora,
////                        color = MaterialTheme.colorScheme.primary
////                    )
////                    Text(
////                        text = "Preencha os campos para adicionar um novo estado de humor e emoji ao sistema.",
////                        fontSize = 18.sp,
////                        fontFamily = Sora,
////                        fontWeight = FontWeight.Normal,
////                        color = colorResource(id = R.color.light_blue),
////                        modifier = Modifier.padding(bottom = 24.dp)
////                    )
////
////                    TextField(
////                        value = moodText,
////                        onValueChange = { moodText = it },
////                        label = { Text("Estado de Humor", color = colorResource(id = R.color.light_blue)) },
////                        colors = TextFieldDefaults.colors(
////                            unfocusedContainerColor = Color.Transparent,
////                            focusedContainerColor = Color.Transparent,
////                            unfocusedLabelColor = colorResource(id = R.color.light_blue),
////                            focusedLabelColor = colorResource(id = R.color.primary),
////                            cursorColor = colorResource(id = R.color.primary),
////                            focusedIndicatorColor = colorResource(id = R.color.primary)
////                        ),
////                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
////                    )
////
////                    Spacer(modifier = Modifier.height(16.dp))
////
////                    TextField(
////                        value = emojiText,
////                        onValueChange = { emojiText = it },
////                        label = { Text("Emoji", color = colorResource(id = R.color.light_blue)) },
////                        colors = TextFieldDefaults.colors(
////                            unfocusedContainerColor = Color.Transparent,
////                            focusedContainerColor = Color.Transparent,
////                            unfocusedLabelColor = colorResource(id = R.color.light_blue),
////                            focusedLabelColor = colorResource(id = R.color.primary),
////                            cursorColor = colorResource(id = R.color.primary),
////                            focusedIndicatorColor = colorResource(id = R.color.primary)
////                        ),
////                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
////                    )
////
////                    Spacer(modifier = Modifier.height(24.dp))
////
////                    Button(
////                        onClick = {
////                            if (moodText.isNotBlank() && emojiText.isNotBlank()) {
////                                humorViewModel.addHumor(moodText, emojiText)
////                                moodText = ""
////                                emojiText = ""
////                            }
////                        },
////                        modifier = Modifier.fillMaxWidth().height(50.dp),
////                        shape = RoundedCornerShape(12.dp),
////                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue))
////                    ) {
////                        Text(
////                            text = "SALVAR NOVO HUMOR",
////                            fontWeight = FontWeight.Bold,
////                            fontSize = 18.sp,
////                            color = colorResource(id = R.color.primary)
////                        )
////                    }
////                }
////            }
////        }
////    }
////}
