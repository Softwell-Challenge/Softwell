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
// 💡 Importações de ícones para o alternador de tema
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
// import androidx.compose.material3.Switch // Não é mais necessário
// import androidx.compose.material3.SwitchDefaults // Não é mais necessário
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
import androidx.compose.ui.graphics.vector.ImageVector // Para o ícone dinâmico
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
    val db = remember { AppDatabase.getDatabase(context) }

    val humorViewModel: HumorViewModel = viewModel()
    val humorDataState by humorViewModel.humorDataState.collectAsState()
    val showMoodPopup = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        humorViewModel.fetchHumorData()
    }

    // 🚀 Definição do gradiente de fundo
    val diagonalGradient = Brush.linearGradient(
        colors = listOf(
            colorResource(id = R.color.bg_dark),
            colorResource(id = R.color.bg_middle),
            colorResource(id = R.color.bg_light)
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )

    // Determina o ícone e a cor com base no tema atual
    val isDarkTheme = themeViewModel.isDarkTheme.value
    val themeIcon: ImageVector = if (isDarkTheme) Icons.Filled.WbSunny else Icons.Filled.NightsStay
    val iconTint = colorResource(id = R.color.light_green)


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
                // Espaçamento de 30dp para a Status Bar
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

                    // 💡 NOVO: Ícone clicável para alternar o tema
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { themeViewModel.toggleTheme() },
                        imageVector = themeIcon,
                        contentDescription = if (isDarkTheme) "Mudar para modo claro (Sol)" else "Mudar para modo escuro (Lua)",
                        tint = iconTint
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

                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    DashboardCard("Avaliação Psicossocial", "psychosocial", navController)
                    DashboardCard("Recursos de Apoio", "support", navController)
                    DashboardCard("Gráficos Pessoais", "graphic", navController)
                }

                // Espaçamento inferior grande (32dp)
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
//    val db = remember { AppDatabase.getDatabase(context) }
//    // As linhas abaixo não estão sendo usadas, mas as mantenho caso você precise delas:
//    // val moodOptionRepository = UserMoodRepository(context)
//    // val userMoodDao = db.userMoodDao()
//
//    val humorViewModel: HumorViewModel = viewModel()
//    val humorDataState by humorViewModel.humorDataState.collectAsState()
//    val showMoodPopup = remember { mutableStateOf(false) }
//
//    LaunchedEffect(Unit) {
//        humorViewModel.fetchHumorData()
//    }
//
//    // 🚀 NOVO: Definição do gradiente de fundo
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
//            // 🚀 NOVO: Aplicando o gradiente de fundo
//            .background(diagonalGradient)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                // ✅ AJUSTE: Aplicando o padding horizontal padrão (8.dp)
//                .padding(horizontal = 8.dp)
//                // ✅ AJUSTE: Removendo arredondamento
//                .clip(RoundedCornerShape(0.dp))
//                // ✅ AJUSTE: Removendo sombra
//                .shadow(elevation = 0.dp)
//                .background(MaterialTheme.colorScheme.background),
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .verticalScroll(scrollState)
//            ) {
//                // 🚀 NOVO: Espaçamento de 30dp para a Status Bar
//                Spacer(modifier = Modifier.height(30.dp))
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        // ✅ AJUSTE: Padding vertical consistente (8.dp)
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
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(150.dp)
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
//                                // Novo botão para o administrador
//                                Button(
//                                    onClick = { navController.navigate("adminHumorScreen") }, // Rota para a tela de admin
//                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
//                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.light_green))
//                                ) {
//                                    Text(text = "Painel de Admin")
//                                }
//                            }
//
//                            if (showMoodPopup.value) {
//                                HumorPopup(
//                                    humorDataList = state.data, // Corrigido para passar a lista
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
//                // Os DashboardCards não precisam de padding horizontal extra, pois o Box externo já tem 8dp.
//                // Mas vamos envolver o conteúdo em um padding para alinhar com os botões acima, usando 16.dp.
//                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
//                    DashboardCard("Avaliação Psicossocial", "psychosocial", navController)
//                    DashboardCard("Recursos de Apoio", "support", navController)
//                    DashboardCard("Gráficos Pessoais", "graphic", navController)
//                }
//
//                // ✅ AJUSTE: Manter o espaçamento inferior grande (32dp)
//                Spacer(modifier = Modifier.height(32.dp))
//            }
//        }
//    }
//}