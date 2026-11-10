package com.saarezra.moonkeep.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.saarezra.moonkeep.data.PlayerProgressRepository
import com.saarezra.moonkeep.game.Characters
import kotlinx.coroutines.launch

@Composable
fun ShopScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repo = remember { PlayerProgressRepository(context.applicationContext) }
    val progress by repo.progress.collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    val coins = progress?.coins ?: 0
    val selected = progress?.selectedCharacter ?: "wanderer"

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Shop", style = MaterialTheme.typography.headlineMedium)
        Text("Coins: $coins")
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(Characters.all) { character ->
                Card(modifier = Modifier.padding(vertical = 6.dp).fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(character.displayName)
                            if (character.cost > 0) Text("${character.cost} coins")
                        }
                        Button(
                            enabled = character.id == selected || coins >= character.cost,
                            onClick = {
                                scope.launch {
                                    if (character.id != selected) {
                                        if (character.cost == 0 || repo.spendCoins(character.cost)) {
                                            repo.selectCharacter(character.id)
                                        }
                                    }
                                }
                            }
                        ) {
                            Text(if (character.id == selected) "Selected" else "Unlock")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("More coins", style = MaterialTheme.typography.labelLarge)
        Text(
            "Coin packs purchased through Google Play will appear here once in-app " +
                "purchases are connected (see README for RevenueCat setup).",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onBack) { Text("Back") }
    }
}
