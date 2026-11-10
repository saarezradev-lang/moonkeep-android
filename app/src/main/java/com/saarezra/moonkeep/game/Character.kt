package com.saarezra.moonkeep.game

import androidx.compose.ui.graphics.Color
import com.saarezra.moonkeep.ui.theme.MoonGold
import com.saarezra.moonkeep.ui.theme.MoonRose
import com.saarezra.moonkeep.ui.theme.MoonSilver
import com.saarezra.moonkeep.ui.theme.MoonTeal

data class CharacterSkin(
    val id: String,
    val displayName: String,
    val color: Color,
    val cost: Int
)

object Characters {
    val all = listOf(
        CharacterSkin("wanderer", "The Wanderer", MoonSilver, cost = 0),
        CharacterSkin("ember", "Ember Keeper", MoonRose, cost = 150),
        CharacterSkin("tide", "Tidekeeper", MoonTeal, cost = 250),
        CharacterSkin("gold", "Gilded Keeper", MoonGold, cost = 400)
    )
}
