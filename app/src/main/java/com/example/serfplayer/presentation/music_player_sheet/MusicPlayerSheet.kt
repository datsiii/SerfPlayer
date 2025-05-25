package com.example.serfplayer.presentation.music_player_sheet

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.serfplayer.presentation.music_player_sheet.component.MotionContent
import com.example.serfplayer.presentation.music_player_sheet.component.SheetContent
import com.example.serfplayer.presentation.viewmodel.PlayerViewModel
import com.example.serfplayer.utils.currentFraction
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerSheet(
    navController: NavController,
    playerViewModel: PlayerViewModel
) {
    val musicUiState by playerViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val motionProgress = sheetState.currentFraction

    BackHandler(enabled = sheetState.isVisible) {
        scope.launch {
            sheetState.hide()
        }
    }

    ModalBottomSheet(
        onDismissRequest = { scope.launch { sheetState.hide() } },
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        MotionContent(
            playerViewModel = playerViewModel,
            fraction = motionProgress,
        )

        SheetContent(
            isExpanded = sheetState.currentValue == androidx.compose.material3.SheetValue.Expanded,
            playerViewModel = playerViewModel,
            onBack = {
                scope.launch {
                    sheetState.hide()
                }
            }
        )
    }
}
