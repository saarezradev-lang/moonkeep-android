# MoonKeep (טירת הירח) - Android

Native Android build of MoonKeep (Kotlin + Jetpack Compose), aimed at Google Play.

**Important context:** the original MoonKeep game ships on iOS, written in Swift/Xcode.
That source wasn't available to build from here (it lives only on the original
author's computer, not in any connected repo), so this is a **fresh native Android
implementation** of the game's described mechanics (levels, score, coins,
unlockable characters, local-only progress, no ads, in-app purchases via
RevenueCat) - not a line-for-line port of the Swift code. Swift/iOS-native code
cannot run on Android regardless; a real "port" always means rebuilding the
gameplay in an Android-capable stack like this one. If you can share the actual
Swift project (source files, or just a description of the exact level/scoring
rules), the gameplay here can be adjusted to match it more closely.

## What's here

A playable vertical slice:
- **Home** → **Level Select** (10 levels, unlocked in order) → **Gameplay** (tap the
  falling moon shards before they land; limited lives, a timer per level) →
  **Result** (coins earned, next level or back to level select)
- **Shop**: spend coins on unlockable character colors
- All progress (coins, unlocked levels, selected character) is saved only on the
  device via Jetpack DataStore - no accounts, matching the existing privacy policy
  at `moonkeep-privacy`.
- No ads anywhere.
- `billing/BillingManager.kt` is a clean seam for in-app purchases (coin packs) -
  currently a no-op placeholder. See "Connecting RevenueCat" below.

## Opening it in Android Studio

1. Install **Android Studio** (Hedgehog/2023.1 or newer) if you don't have it.
2. **Open** this folder as a project (`File > Open`, pick the `moonkeep-android`
   folder - it already contains `settings.gradle.kts` at the root).
3. Let Gradle sync. The Gradle wrapper is committed (`gradlew`), so Android Studio
   will download Gradle 8.4 itself; no local Gradle install is required.
4. If prompted, let it download the Android SDK platform matching `compileSdk = 34`
   and Build-Tools - Android Studio handles this through its SDK Manager.
5. Run the `app` configuration on an emulator or device.

This project was **not build-verified in this session** (no Android SDK or
emulator available in this cloud container) - only carefully written and
reviewed by hand. Android Studio's first sync/build is the real check; if
anything doesn't compile, that's the next thing to fix together.

## Project layout

```
app/src/main/java/com/saarezra/moonkeep/
  MainActivity.kt
  ui/              Compose screens + navigation (MoonKeepApp.kt, screens/)
  ui/theme/         Color palette, typography, MaterialTheme setup
  game/             Level config, character skins, the gameplay loop (GameplayViewModel)
  data/             Local-only progress storage (DataStore)
  billing/          IAP seam, not yet wired to a real store
```

## Connecting RevenueCat (in-app purchases)

Not wired in yet, to avoid guessing at SDK versions / API keys that would break
your build. To add it:

1. Create a RevenueCat project and Android app, matching `applicationId =
   "com.saarezra.moonkeep"` (or whatever you rename it to).
2. Follow RevenueCat's Android quickstart to add the `purchases` SDK dependency
   and your public API key.
3. Implement `BillingManager` (in `billing/BillingManager.kt`) against that SDK
   instead of `NoOpBillingManager`, and wire a coin-pack purchase button into
   `ShopScreen.kt`.
4. Create the matching in-app products in Google Play Console once you have a
   signed app there (see below).

## Publishing to Google Play

This is something only you can do - it needs your own Google Play Console
developer account (one-time $25 fee) and your own signing key. What's ready
here vs. what's still your side:

**Ready here:**
- The app itself, buildable as a release Android App Bundle.
- A privacy policy already published at the `moonkeep-privacy` repo/page.

**Still needed from you:**
1. **Signing key**: generate a keystore (`keytool -genkeypair -v -keystore
   moonkeep-release.keystore -alias moonkeep -keyalg RSA -keysize 2048 -validity
   10000`), then add a `signingConfigs { release { ... } }` block in
   `app/build.gradle.kts` pointing at it (never commit the keystore or its
   passwords - `.gitignore` already excludes `*.keystore`/`*.jks`).
2. **Build the release bundle**: `./gradlew bundleRelease` → produces
   `app/build/outputs/bundle/release/app-release.aab`.
3. **Google Play Console**: create the app, fill in the store listing
   (screenshots, description, icon), the content rating questionnaire, and the
   data-safety form (matching the privacy policy - no personal data collected,
   RevenueCat for purchase validation only), link the privacy policy URL, then
   upload the `.aab` under a release track (start with **Internal testing**).
4. Double-check `compileSdk`/`targetSdk` against Play's current minimum target
   API level requirement at the time you submit - it moves roughly yearly and
   may have gone up since this was written.

I can't submit anything to Play Console myself (no access to your Google
account), but I can keep building/adjusting the app code, the signing config
scaffolding, and anything else short of the actual account-side submission.
