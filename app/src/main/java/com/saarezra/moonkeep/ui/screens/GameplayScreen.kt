package com.saarezra.moonkeep.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saarezra.moonkeep.data.PlayerProgressRepository
import com.saarezra.moonkeep.game.GameplayViewModel
import com.saarezra.moonkeep.game.Levels
import com.saarezra.moonkeep.game.RoundStatus
import kotlinx.coroutines.launch

@Composable
fun GameplayScreen(
    level: Int,
    onFinished: (score: Int, won: Boolean) -> Unit,
    viewModel: GameplayViewModel = viewModel()
) {
    val context = LocalContext.current
    val repo = remember { PlayerProgressRepository(context.applicationContext) }
    val scope = rememberCoroutineScope()
    val levelConfig = remember(level) { Levels.all.firstOrNull { it.number == level } ?: Levels.all.first() }

    LaunchedEffect(level) {
        viewModel.start(levelConfig)
    }

    LaunchedEffect(viewModel.status) {
        val finished = viewModel.status == RoundStatus.Won || viewModel.status == RoundStatus.Lost
        if (finished) {
            val won = viewModel.status == RoundStatus.Won
            if (won) {
                scope.launch {
                    repo.addCoins(viewModel.score / 5)
                    repo.unlockLevel(level + 1)
                }
            }
            onFinished(viewModel.score, won)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    val w = size.width.toFloat()
                    val h = size.height.toFloat()
                    val target = viewModel.shards.minByOrNull { shard ->
                        val shardOffset = Offset(shard.x * w, shard.y * h)
                        (shardOffset - tapOffset).getDistanceSquared()
                    }
                    target?.let { shard ->
                        val shardOffset = Offset(shard.x * w, shard.y * h)
                        if ((shardOffset - tapOffset).getDistance() < 100f) {
                            viewModel.onShardTapped(shard.id)
                        }
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            viewModel.shards.forEach { shard ->
                drawCircle(
                    color = Color(0xFFE8C872),
                    radius = 36f,
                    center = Offset(shard.x * size.width, shard.y * size.height)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Score: ${viewModel.score}")
            Text("Lives: ${viewModel.lives}")
            Text("${viewModel.timeLeftSeconds}s")
        }
    }
}
