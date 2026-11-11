package com.saarezra.moonkeep.billing

/**
 * Thin seam for in-app purchases. MoonKeep has no ads and no accounts; the only
 * purchases are cosmetic coin packs. Wire this up to the RevenueCat Android SDK
 * once you have a RevenueCat project and API key - see README.md for the steps.
 */
interface BillingManager {
    suspend fun fetchCoinPackOfferings(): List<CoinPackOffer>
    suspend fun purchase(offer: CoinPackOffer): Result<Int>

    /** One-time real-money purchase that unlocks every character skin at once. */
    suspend fun purchaseUnlockAllCharacters(): Result<Unit>
}

data class CoinPackOffer(
    val productId: String,
    val displayName: String,
    val priceLabel: String,
    val coins: Int
)

/**
 * The real price (₪5.99) is configured as this product's price in Google Play
 * Console / RevenueCat once billing is wired in - this label is just what the UI
 * shows before that's connected.
 */
object UnlockAllCharactersOffer {
    const val PRODUCT_ID = "unlock_all_characters"
    const val PRICE_LABEL = "₪5.99"
}

/** No-op placeholder used until a real billing SDK is wired in. */
class NoOpBillingManager : BillingManager {
    override suspend fun fetchCoinPackOfferings(): List<CoinPackOffer> = emptyList()

    override suspend fun purchase(offer: CoinPackOffer): Result<Int> =
        Result.failure(IllegalStateException("Billing is not configured yet."))

    override suspend fun purchaseUnlockAllCharacters(): Result<Unit> =
        Result.failure(IllegalStateException("Billing is not configured yet."))
}
