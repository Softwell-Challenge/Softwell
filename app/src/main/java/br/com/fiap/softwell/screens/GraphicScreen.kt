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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import br.com.fiap.softwell.R
import br.com.fiap.softwell.database.dao.AppDatabase
import br.com.fiap.softwell.database.repository.UserMoodRepository
import br.com.fiap.softwell.model.Mood
import br.com.fiap.softwell.model.UserMood
import br.com.fiap.softwell.viewmodel.MoodViewModel

@Composable
fun GraphicScreen(navController: NavController,moodViewModel: MoodViewModel) {

    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val moodOptionRepository = UserMoodRepository(context)
    val db = remember { AppDatabase.getDatabase(context) }

    val apiMoods by moodViewModel.moods.collectAsStateWithLifecycle()
    val isLoading by moodViewModel.isLoading.collectAsStateWithLifecycle()
    val error by moodViewModel.error.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
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
                modifier = Modifier
                    .verticalScroll(scrollState)
            ){
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
                        tint = colorResource(id = R.color.light_green),

                    )
                }
            }
            Text("Humores da API", modifier = Modifier.padding(16.dp), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally as Alignment))
            } else if(error != null){
                Text(text = "Erro ao carregar da API: $error", color = Color.Red, modifier = Modifier.padding(16.dp))
            } else {
                ApiMoodList(moods = apiMoods)
            }
        }
    }
}
@Composable
fun moodList(listarMood: MutableState<List<UserMood>>) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
        ){
        for (mood in listarMood.value){
            cardMood(mood)
            Spacer(modifier = Modifier.height(4.dp))
        }

    }
}

@Composable
fun cardMood(mood: UserMood) {
    Card (
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Column (
                modifier = Modifier
                    .padding(8.dp)
                    .weight(2f)
            ){
                Text(
                    text = mood.moodId,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = mood.emoji,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ---------- NOVO COMPOSABLE PARA A LISTA DA API ----------
@Composable
fun ApiMoodList(moods: List<Mood>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if(moods.isEmpty()){
            Text("Nenhum dado da API para exibir. Tente buscar na tela anterior.")
        } else {
            for (mood in moods) {
                CardApiMood(mood = mood)
            }
        }
    }
}

// ---------- NOVO CARD PARA O TIPO 'Mood' DA API ----------
@Composable
fun CardApiMood(mood: Mood) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.light_blue))
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mood.moodId,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "(ID: ${mood.id})",
                fontSize = 18.sp
            )
        }
    }
}