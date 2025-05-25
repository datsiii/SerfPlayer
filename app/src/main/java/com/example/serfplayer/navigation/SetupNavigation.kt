package com.example.serfplayer.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.serfplayer.presentation.viewmodel.PlayerViewModel
import com.example.serfplayer.presentation.main_screen.MainScreen
import com.example.serfplayer.presentation.music_player_sheet.MusicPlayerSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupNavigation(
    playerViewModel: PlayerViewModel,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        sheetState.show()
    }

    NavHost(
        navController = navController,
        startDestination = Routes.Main.name
    ) {
        composable(Routes.Main.name) {
            MainScreen(
                navController = navController,
                playerViewModel = playerViewModel
            )

            ModalBottomSheet(
                onDismissRequest = { /* Можно оставить пустым или добавить логику */ },
                sheetState = sheetState
            ) {
                MusicPlayerSheet(
                    playerViewModel = playerViewModel,
                    navController = navController
                )
            }
        }
    }
}