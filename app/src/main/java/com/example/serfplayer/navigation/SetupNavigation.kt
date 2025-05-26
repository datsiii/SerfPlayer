package com.example.serfplayer.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.serfplayer.presentation.viewmodel.PlayerViewModel
import com.example.serfplayer.presentation.main_screen.MainScreen
import com.example.serfplayer.presentation.music_player_sheet.PlayerScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupNavigation(
    playerViewModel: PlayerViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.Main.name
    ) {
        composable(Routes.Main.name) {
            MainScreen(
                navController = navController,
                playerViewModel = playerViewModel
            )
        }
        composable(Routes.Player.name) {
            PlayerScreen(
                playerViewModel = playerViewModel
            )
        }
    }
}
