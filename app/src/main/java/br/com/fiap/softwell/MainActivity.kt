package br.com.fiap.softwell

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.softwell.screens.DashboardScreen
import br.com.fiap.softwell.screens.LoginScreen
import br.com.fiap.softwell.screens.PsychosocialScreen
import br.com.fiap.softwell.screens.SupportScreen
import br.com.fiap.softwell.ui.theme.SoftwellTheme
import androidx.lifecycle.viewmodel.compose.viewModel
//import br.com.fiap.softwell.model.MoodViewModel
import br.com.fiap.softwell.model.ThemeViewModel
import br.com.fiap.softwell.screens.AdminScreen

import br.com.fiap.softwell.screens.HistoricScreen
import br.com.fiap.softwell.screens.RegisterScreen
import br.com.fiap.softwell.service.RetrofitFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityApiService = RetrofitFactory.getActivityService()

        setContent {

            val themeViewModel: ThemeViewModel = viewModel() // ViewModel para controlar o tema

            SoftwellTheme(darkTheme = themeViewModel.isDarkTheme.value) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "login") {
                        composable(route = "login") { LoginScreen(navController) }
                        composable(route = "register") { RegisterScreen(navController) }
                        composable(route = "dashboard") { DashboardScreen(navController, themeViewModel) }
                        composable(route = "psychosocial") { PsychosocialScreen(navController) }
                        composable(route = "support") {
                            SupportScreen(navController, activityApiService)
                        }

                        composable(route = "historic") { HistoricScreen(navController) }
                        composable("adminHumorScreen") { AdminScreen(navController) }
                    }
                }
            }
        }
    }
}

