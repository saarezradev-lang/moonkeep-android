package com.saarezra.moonkeep.game

data class LevelConfig(
    val number: Int,
    val durationSeconds: Int,
    val spawnIntervalMs: Long,
    val fallSpeedDpPerSec: Float,
    val lives: Int
)

object Levels {
    val all: List<LevelConfig> = (1..10).map { n ->
        LevelConfig(
            number = n,
            durationSeconds = 25 + n * 2,
            spawnIntervalMs = (900L - n * 60L).coerceAtLeast(300L),
            fallSpeedDpPerSec = 90f + n * 12f,
            lives = 3
        )
    }
}
