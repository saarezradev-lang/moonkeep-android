package com.saarezra.moonkeep.billing

/**
 * Thin seam for in-app purchases. MoonKeep has no ads and no accounts; the only
 * purchases are cosmetic coin packs. Wire this up to the RevenueCat Android SDK
 * once you have a RevenueCat project and API key - see README.md for the steps.
 */
interface BillingManager {
    suspend fun fetchCoinPackOfferings(): List<CoinPackOffer>
    suspend fun purchase(offer: CoinPackOffer): Result<Int>
}

data class CoinPackOffer(
    val productId: String,
    val displayName: String,
    val priceLabel: String,
    val coins: Int
)

/** No-op placeholder used until a real billing SDK is wired in. */
class NoOpBillingManager : BillingManager {
    override suspend fun fetchCoinPackOfferings(): List<CoinPackOffer> = emptyList()

    override suspend fun purchase(offer: CoinPackOffer): Result<Int> =
        Result.failure(IllegalStateException("Billing is not configured yet."))
}
