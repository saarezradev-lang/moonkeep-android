package com.saarezra.moonkeep.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.saarezra.moonkeep.data.PlayerProgressRepository
import com.saarezra.moonkeep.game.Levels
import com.saarezra.moonkeep.ui.theme.MoonSkyBackground

@Composable
fun LevelSelectScreen(onLevelSelected: (Int) -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    val repo = remember { PlayerProgressRepository(context.applicationContext) }
    val progress by repo.progress.collectAsState(initial = null)
    val highestUnlocked = progress?.highestUnlockedLevel ?: 1

    MoonSkyBackground {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Text("Choose a Level", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.weight(1f)) {
                items(Levels.all) { level ->
                    val unlocked = level.number <= highestUnlocked
                    Card(
                        modifier = Modifier.padding(8.dp),
                        onClick = { if (unlocked) onLevelSelected(level.number) }
                    ) {
                        Box(modifier = Modifier.padding(24.dp)) {
                            Text(if (unlocked) "${level.number}" else "🔒")
                        }
                    }
                }
            }
            TextButton(onClick = onBack) { Text("Back") }
        }
    }
}
