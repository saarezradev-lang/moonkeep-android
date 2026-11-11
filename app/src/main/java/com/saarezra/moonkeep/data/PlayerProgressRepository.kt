package com.saarezra.moonkeep.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "moonkeep_progress")

data class PlayerProgress(
    val coins: Int = 0,
    val highestUnlockedLevel: Int = 1,
    val selectedCharacter: String = "wanderer",
    val allCharactersUnlocked: Boolean = false
)

/**
 * All progress (coins, unlocked levels, selected character) is kept only on this device,
 * matching the MoonKeep privacy policy: no accounts, nothing sent off-device.
 */
class PlayerProgressRepository(private val context: Context) {

    private object Keys {
        val COINS = intPreferencesKey("coins")
        val HIGHEST_LEVEL = intPreferencesKey("highest_unlocked_level")
        val CHARACTER = stringPreferencesKey("selected_character")
        val ALL_CHARACTERS_UNLOCKED = booleanPreferencesKey("all_characters_unlocked")
    }

    val progress: Flow<PlayerProgress> = context.dataStore.data.map { prefs ->
        PlayerProgress(
            coins = prefs[Keys.COINS] ?: 0,
            highestUnlockedLevel = prefs[Keys.HIGHEST_LEVEL] ?: 1,
            selectedCharacter = prefs[Keys.CHARACTER] ?: "wanderer",
            allCharactersUnlocked = prefs[Keys.ALL_CHARACTERS_UNLOCKED] ?: false
        )
    }

    suspend fun addCoins(amount: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.COINS] = (prefs[Keys.COINS] ?: 0) + amount
        }
    }

    suspend fun spendCoins(amount: Int): Boolean {
        var success = false
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.COINS] ?: 0
            if (current >= amount) {
                prefs[Keys.COINS] = current - amount
                success = true
            }
        }
        return success
    }

    suspend fun unlockLevel(level: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.HIGHEST_LEVEL] ?: 1
            if (level > current) prefs[Keys.HIGHEST_LEVEL] = level
        }
    }

    suspend fun selectCharacter(characterId: String) {
        context.dataStore.edit { prefs -> prefs[Keys.CHARACTER] = characterId }
    }

    suspend fun setAllCharactersUnlocked() {
        context.dataStore.edit { prefs -> prefs[Keys.ALL_CHARACTERS_UNLOCKED] = true }
    }
}
