package com.saarezra.moonkeep.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResultScreen(
    level: Int,
    score: Int,
    won: Boolean,
    onNext: () -> Unit,
    onBackToLevels: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            if (won) "Level $level Complete!" else "Level $level Failed",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Score: $score")
        Spacer(modifier = Modifier.height(32.dp))
        if (won) {
            Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) { Text("Next Level") }
            Spacer(modifier = Modifier.height(12.dp))
        }
        OutlinedButton(onClick = onBackToLevels, modifier = Modifier.fillMaxWidth()) { Text("Level Select") }
    }
}
