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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.saarezra.moonkeep.ui.theme.MoonSkyBackground

@Composable
fun HomeScreen(onPlay: () -> Unit, onShop: () -> Unit) {
    MoonSkyBackground {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("MoonKeep", style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
            Text("טירת הירח", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(48.dp))
            Button(onClick = onPlay, modifier = Modifier.fillMaxWidth()) {
                Text("Play")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = onShop, modifier = Modifier.fillMaxWidth()) {
                Text("Shop")
            }
        }
    }
}
