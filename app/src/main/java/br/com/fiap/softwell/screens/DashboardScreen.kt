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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import br.com.fiap.softwell.model.ThemeViewModel
import br.com.fiap.softwell.ui.theme.Sora
import br.com.fiap.softwell.viewmodel.HumorDataState
import br.com.fiap.softwell.viewmodel.HumorViewModel
import br.com.fiap.softwell.service.AuthTokenManager


@Composable
fun DashboardScreen(navController: NavController, themeViewModel: ThemeViewModel) {
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }

    val humorViewModel: HumorViewModel = viewModel()
    val humorDataState by humorViewModel.humorDataState.collectAsState()
    val showMoodPopup = remember { mutableStateOf(false) }

    // ✅ 2. VERIFICAR SE O USUÁRIO É ADMIN
    // Usamos 'remember' para que a verificação seja feita apenas uma vez.
    val isAdmin = remember { AuthTokenManager.isUserAdmin() }

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
    val isDarkTheme = themeViewModel.isDarkTheme.value
    val themeIcon: ImageVector = if (isDarkTheme) Icons.Filled.WbSunny else Icons.Filled.NightsStay
    val iconTint = colorResource(id = R.color.bg_light)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(diagonalGradient)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(0.dp))
                .shadow(elevation = 0.dp)
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // ... seu cabeçalho e título ...
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
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
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { themeViewModel.toggleTheme() },
                        imageVector = themeIcon,
                        contentDescription = if (isDarkTheme) "Mudar para modo claro (Sol)" else "Mudar para modo escuro (Lua)",
                        tint = iconTint
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = "SOFTWELL",
                        fontFamily = Sora,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp,
                        color = colorResource(id = R.color.light_blue),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                DiamondLine()

                // === BLOCO DIÁRIO DE HUMOR E BOTÕES ===
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    when (val state = humorDataState) {
                        is HumorDataState.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                        is HumorDataState.Success -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SessionTitle(text = "Diário de Humor", modifier = Modifier.padding(bottom = 8.dp))
                                Button(
                                    onClick = { showMoodPopup.value = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue))
                                ) {
                                    Text(
                                        text = "ABRIR DIÁRIO DE HUMOR",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = colorResource(id = R.color.primary)
                                    )
                                }

                                // ✅ 3. CONTROLE DE VISIBILIDADE PARA O PAINEL DE ADMIN
                                if (isAdmin) {
                                    Button(
                                        onClick = { navController.navigate("adminHumorScreen") }, // Supondo que o nome da rota seja este
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 0.dp),
                                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.light_blue))
                                    ) {
                                        Text(
                                            text = "PAINEL DE ADMIN",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = colorResource(id = R.color.primary)
                                        )
                                    }
                                }
                            }

                            if (showMoodPopup.value) {
                                HumorPopup(
                                    humorDataList = state.data,
                                    onDismiss = { showMoodPopup.value = false },
                                    onSend = { estadoDeHumor, emoji ->
                                        humorViewModel.saveUserResponse(estadoDeHumor, emoji)
                                        showMoodPopup.value = false
                                    }
                                )
                            }
                        }
                        is HumorDataState.Error -> {
                            Text(text = "Erro: ${state.message}", color = Color.Red, modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                DiamondLine()
                Spacer(modifier = Modifier.height(18.dp))

                // === BLOCO DE DASHBOARD CARDS ===
                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    DashboardCard("Avaliação Psicossocial", "psychosocial", navController)
                    DashboardCard("Recursos de Apoio", "support", navController)
                    DashboardCard("Gráficos Pessoais", "graphic", navController)

                    // ✅ 4. CONTROLE DE VISIBILIDADE PARA O HISTÓRICO
                    if (isAdmin) {
                        DashboardCard("Histórico de Respostas", "historic", navController)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
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
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.NightsStay
//import androidx.compose.material.icons.filled.WbSunny
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
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
//import androidx.compose.ui.graphics.vector.ImageVector
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
//    val db = remember { AppDatabase.getDatabase(context) }
//
//    val humorViewModel: HumorViewModel = viewModel()
//    val humorDataState by humorViewModel.humorDataState.collectAsState()
//    val showMoodPopup = remember { mutableStateOf(false) }
//
//    LaunchedEffect(Unit) {
//        humorViewModel.fetchHumorData()
//    }
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
//    val isDarkTheme = themeViewModel.isDarkTheme.value
//    val themeIcon: ImageVector = if (isDarkTheme) Icons.Filled.WbSunny else Icons.Filled.NightsStay
//    val iconTint = colorResource(id = R.color.bg_light)
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(diagonalGradient)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 8.dp)
//                .clip(RoundedCornerShape(0.dp))
//                .shadow(elevation = 0.dp)
//                .background(MaterialTheme.colorScheme.background),
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .verticalScroll(scrollState)
//            ) {
//                Spacer(modifier = Modifier.height(30.dp))
//
//                // === CABEÇALHO (SETA E TEMA) ===
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 8.dp, vertical = 8.dp),
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
//                    Icon(
//                        modifier = Modifier
//                            .size(32.dp)
//                            .clickable { themeViewModel.toggleTheme() },
//                        imageVector = themeIcon,
//                        contentDescription = if (isDarkTheme) "Mudar para modo claro (Sol)" else "Mudar para modo escuro (Lua)",
//                        tint = iconTint
//                    )
//                }
//
//                // === TÍTULO SOFTWELL GRANDE E SIMPLES (ANTES DA LINHA) ===
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 16.dp, bottom = 16.dp)
//                ) {
//                    // Nota: Corrigido para light_green para consistência com os ícones,
//                    // mas se light_blue for o correto para o logo, mude de volta.
//                    Text(
//                        text = "SOFTWELL",
//                        fontFamily = Sora,
//                        fontWeight = FontWeight.ExtraBold,
//                        fontSize = 32.sp,
//                        color = colorResource(id = R.color.light_blue),
//                        modifier = Modifier.align(Alignment.Center)
//                    )
//                }
//
//                DiamondLine()
//
//                // === BLOCO DIÁRIO DE HUMOR E BOTÕES ===
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 16.dp)
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
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalAlignment = Alignment.CenterHorizontally,
//                                verticalArrangement = Arrangement.spacedBy(8.dp)
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
//                                    Text(
//                                        text = "ABRIR DIÁRIO DE HUMOR",
//                                        fontSize = 18.sp,
//                                        fontWeight = FontWeight.ExtraBold,
//                                        color = colorResource(id = R.color.primary)
//                                    )
//                                }
//                                Button(
//                                    onClick = { navController.navigate("adminHumorScreen") },
//                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 0.dp),
//                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.light_blue))
//                                ) {
//                                    Text(
//                                        text = "PAINEL DE ADMIN",
//                                        fontSize = 18.sp,
//                                        fontWeight = FontWeight.ExtraBold,
//                                        color = colorResource(id = R.color.primary)
//                                    )
//                                }
//                            }
//
//                            if (showMoodPopup.value) {
//                                HumorPopup(
//                                    humorDataList = state.data,
//                                    onDismiss = { showMoodPopup.value = false },
//                                    onSend = { estadoDeHumor, emoji ->
//                                        humorViewModel.saveUserResponse(estadoDeHumor, emoji)
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
//                Spacer(modifier = Modifier.height(24.dp))
//                DiamondLine()
//                Spacer(modifier = Modifier.height(18.dp))
//
//                // === BLOCO DE DASHBOARD CARDS (Incluindo o novo Histórico de Respostas) ===
//                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
//                    DashboardCard("Avaliação Psicossocial", "psychosocial", navController)
//                    DashboardCard("Recursos de Apoio", "support", navController)
//                    DashboardCard("Gráficos Pessoais", "graphic", navController)
//
//                    // 🚀 NOVO BOTÃO: Histórico de Respostas (no formato Card)
//                    DashboardCard("Histórico de Respostas", "historic", navController)
//                }
//
//                Spacer(modifier = Modifier.height(32.dp))
//            }
//        }
//    }
//}