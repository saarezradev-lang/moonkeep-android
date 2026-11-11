package com.saarezra.moonkeep.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

/**
 * A simple night-sky backdrop (gradient + scattered stars) shared by every screen,
 * so MoonKeep has a bit of its own identity instead of a plain Material background.
 * No image assets required - everything here is drawn.
 */
@Composable
fun MoonSkyBackground(content: @Composable BoxScope.() -> Unit) {
    val stars = remember {
        val random = Random(42)
        List(60) {
            Triple(random.nextFloat(), random.nextFloat(), 1f + random.nextFloat() * 1.5f)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(MoonNight, MoonNightDeep)
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            stars.forEach { (fx, fy, radius) ->
                drawCircle(
                    color = MoonSilver.copy(alpha = 0.55f),
                    radius = radius,
                    center = Offset(fx * size.width, fy * size.height)
                )
            }
        }
        content()
    }
}
