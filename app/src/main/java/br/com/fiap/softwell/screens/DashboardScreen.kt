package br.com.fiap.softwell.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.fiap.softwell.R
import br.com.fiap.softwell.components.DashboardCard
import br.com.fiap.softwell.components.DiamondLine
import br.com.fiap.softwell.components.SessionTitle
import br.com.fiap.softwell.database.dao.AppDatabase
import br.com.fiap.softwell.database.repository.UserMoodRepository
import br.com.fiap.softwell.model.HumorData
import br.com.fiap.softwell.model.ThemeViewModel
import br.com.fiap.softwell.ui.theme.Sora
import br.com.fiap.softwell.viewmodel.HumorDataState
import br.com.fiap.softwell.viewmodel.HumorViewModel

@Composable
fun DashboardScreen(navController: NavController, themeViewModel: ThemeViewModel) {
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val moodOptionRepository = UserMoodRepository(context)
    val db = remember { AppDatabase.getDatabase(context) }
    val userMoodDao = db.userMoodDao()

    val humorViewModel: HumorViewModel = viewModel()
    val humorDataState by humorViewModel.humorDataState.collectAsState()
    val showMoodPopup = remember { mutableStateOf(false) }

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
                .shadow(
                    elevation = 6.dp
                )
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { navController.popBackStack() },
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = colorResource(id = R.color.light_green)
                    )
                    Switch(
                        checked = themeViewModel.isDarkTheme.value,
                        onCheckedChange = { themeViewModel.toggleTheme() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = colorResource(id = R.color.bg_light),
                            uncheckedThumbColor = colorResource(id = R.color.bg_dark)
                        )
                    )
                }
                DiamondLine()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(top = 8.dp)
                ) {
                    when (val state = humorDataState) {
                        is HumorDataState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )
                        }
                        is HumorDataState.Success -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                SessionTitle(
                                    text = "Diário de Humor",
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                )
                                Button(
                                    onClick = { showMoodPopup.value = true },
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue))
                                ) {
                                    Text(text = "Abrir Diário de Humor")
                                }
                                // Novo botão para o administrador
                                Button(
                                    onClick = { navController.navigate("adminHumorScreen") }, // Rota para a tela de admin
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.light_green))
                                ) {
                                    Text(text = "Painel de Admin")
                                }
                            }

                            if (showMoodPopup.value) {
                                HumorPopup(
                                    humorDataList = state.data, // Corrigido para passar a lista
                                    onDismiss = { showMoodPopup.value = false },
                                    onSend = { estadoDeHumor, emoji ->
                                        humorViewModel.saveUserResponse(estadoDeHumor, emoji)
                                        showMoodPopup.value = false
                                    }
                                )
                            }
                        }
                        is HumorDataState.Error -> {
                            Text(
                                text = "Erro: ${state.message}",
                                color = Color.Red,
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                DiamondLine()
                Spacer(modifier = Modifier.height(18.dp))
                DashboardCard("Avaliação Psicossocial", "psychosocial", navController)
                DashboardCard("Recursos de Apoio", "support", navController)
                DashboardCard("Gráficos Pessoais", "graphic", navController)
            }
        }
    }
}


//package br.com.fiap.softwell.screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.wrapContentWidth
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Switch
//import androidx.compose.material3.SwitchDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import br.com.fiap.softwell.R
//import br.com.fiap.softwell.components.DashboardCard
//import br.com.fiap.softwell.components.DiamondLine
//import br.com.fiap.softwell.components.SessionTitle
//import br.com.fiap.softwell.database.dao.AppDatabase
//import br.com.fiap.softwell.database.repository.UserMoodRepository
//import br.com.fiap.softwell.model.HumorData
//import br.com.fiap.softwell.model.ThemeViewModel
//import br.com.fiap.softwell.ui.theme.Sora
//import br.com.fiap.softwell.viewmodel.HumorDataState
//import br.com.fiap.softwell.viewmodel.HumorViewModel
//
//@Composable
//fun DashboardScreen(navController: NavController, themeViewModel: ThemeViewModel) {
//    val scrollState = rememberScrollState()
//
//    val context = LocalContext.current
//    val moodOptionRepository = UserMoodRepository(context)
//    val db = remember { AppDatabase.getDatabase(context) }
//    val userMoodDao = db.userMoodDao()
//
//    // ViewModel para gerenciar o estado da API
//    val humorViewModel: HumorViewModel = viewModel()
//    val humorDataState by humorViewModel.humorDataState.collectAsState()
//    val showMoodPopup = remember { mutableStateOf(false) }
//
//    // Inicia a busca pelos dados da API quando a tela é carregada
//    LaunchedEffect(Unit) {
//        humorViewModel.fetchHumorData()
//    }
//
//    // Removidos os dados fixos
//    // val moodOptions = listOf(...)
//    // val emojis = listOf(...)
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
//                .shadow(
//                    elevation = 6.dp
//                )
//                .background(MaterialTheme.colorScheme.background),
//        ) {
//            Column(
//                modifier = Modifier.verticalScroll(scrollState)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        modifier = Modifier
//                            .size(32.dp)
//                            .clickable { navController.popBackStack() },
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Voltar",
//                        tint = colorResource(id = R.color.light_green)
//                    )
//                    Switch(
//                        checked = themeViewModel.isDarkTheme.value,
//                        onCheckedChange = { themeViewModel.toggleTheme() },
//                        colors = SwitchDefaults.colors(
//                            checkedThumbColor = colorResource(id = R.color.bg_light),
//                            uncheckedThumbColor = colorResource(id = R.color.bg_dark)
//                        )
//                    )
//                }
//                DiamondLine()
//
//                // Nova seção do Diário de Humor (agora dinâmica)
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(150.dp) // Altura fixa para o Box
//                        .padding(top = 8.dp)
//                ) {
//                    when (val state = humorDataState) {
//                        is HumorDataState.Loading -> {
//                            CircularProgressIndicator(
//                                modifier = Modifier
//                                    .align(Alignment.Center)
//                            )
//                        }
//                        is HumorDataState.Success -> {
//                            Column(
//                                modifier = Modifier.fillMaxSize(),
//                                horizontalAlignment = Alignment.CenterHorizontally,
//                                verticalArrangement = Arrangement.Center
//                            ) {
//                                SessionTitle(
//                                    text = "Diário de Humor",
//                                    modifier = Modifier
//                                        .padding(bottom = 8.dp)
//                                )
//                                Button(
//                                    onClick = { showMoodPopup.value = true },
//                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
//                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue))
//                                ) {
//                                    Text(text = "Abrir Diário de Humor")
//                                }
//                            }
//
//                            if (showMoodPopup.value) {
//                                HumorPopup(
//                                    humorData = state.data,
//                                    onDismiss = { showMoodPopup.value = false },
//                                    onSend = { moodId, emoji ->
//                                        // TODO: Lógica para enviar os dados para a API de backend
//                                        // Por enquanto, apenas fecha o pop-up
//                                        showMoodPopup.value = false
//                                    }
//                                )
//                            }
//                        }
//                        is HumorDataState.Error -> {
//                            Text(
//                                text = "Erro: ${state.message}",
//                                color = Color.Red,
//                                modifier = Modifier
//                                    .align(Alignment.Center)
//                            )
//                        }
//                    }
//                }
//
//                // ... o restante do seu código (DashboardCard, etc.)
//                Spacer(modifier = Modifier.height(24.dp))
//                DiamondLine()
//                Spacer(modifier = Modifier.height(18.dp))
//                DashboardCard("Avaliação Psicossocial", "psychosocial", navController)
//                DashboardCard("Recursos de Apoio", "support", navController)
//                DashboardCard("Gráficos Pessoais", "graphic", navController)
//            }
//        }
//    }
//}
