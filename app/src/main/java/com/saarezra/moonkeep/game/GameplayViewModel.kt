package com.saarezra.moonkeep.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class Shard(
    val id: Long,
    val x: Float,
    val y: Float,
    val speed: Float
)

enum class RoundStatus { Ready, Playing, Won, Lost }

/** Drives one round of the "catch the falling moon shards" minigame. */
class GameplayViewModel : ViewModel() {

    var shards by mutableStateOf<List<Shard>>(emptyList())
        private set
    var score by mutableStateOf(0)
        private set
    var lives by mutableStateOf(3)
        private set
    var timeLeftSeconds by mutableStateOf(0)
        private set
    var status by mutableStateOf(RoundStatus.Ready)
        private set

    private var loopJob: Job? = null
    private var nextId = 0L
    private lateinit var config: LevelConfig

    fun start(levelConfig: LevelConfig) {
        config = levelConfig
        shards = emptyList()
        score = 0
        lives = levelConfig.lives
        timeLeftSeconds = levelConfig.durationSeconds
        status = RoundStatus.Playing
        nextId = 0L

        loopJob?.cancel()
        loopJob = viewModelScope.launch {
            var elapsedMs = 0L
            var sinceSpawnMs = 0L
            val frameMs = 16L

            while (status == RoundStatus.Playing) {
                delay(frameMs)
                elapsedMs += frameMs
                sinceSpawnMs += frameMs

                if (sinceSpawnMs >= config.spawnIntervalMs) {
                    sinceSpawnMs = 0L
                    spawnShard()
                }

                advanceShards(frameMs / 1000f)
                timeLeftSeconds = (config.durationSeconds - elapsedMs / 1000).toInt().coerceAtLeast(0)

                if (timeLeftSeconds <= 0) {
                    status = RoundStatus.Won
                } else if (lives <= 0) {
                    status = RoundStatus.Lost
                }
            }
        }
    }

    private fun spawnShard() {
        val speedFractionPerSec = config.fallSpeedDpPerSec / 800f
        shards = shards + Shard(
            id = nextId++,
            x = Random.nextFloat() * 0.85f + 0.05f,
            y = 0f,
            speed = speedFractionPerSec
        )
    }

    private fun advanceShards(deltaSeconds: Float) {
        val survivors = mutableListOf<Shard>()
        var missed = 0
        for (shard in shards) {
            val newY = shard.y + shard.speed * deltaSeconds
            if (newY >= 1f) {
                missed++
            } else {
                survivors.add(shard.copy(y = newY))
            }
        }
        shards = survivors
        if (missed > 0) lives = (lives - missed).coerceAtLeast(0)
    }

    fun onShardTapped(id: Long) {
        if (status != RoundStatus.Playing) return
        if (shards.any { it.id == id }) {
            shards = shards.filterNot { it.id == id }
            score += 10
        }
    }

    override fun onCleared() {
        loopJob?.cancel()
    }
}
