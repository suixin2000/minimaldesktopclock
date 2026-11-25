package com.digital.digitalclock.themes.ledglow

import androidx.compose.ui.graphics.Color
import com.digital.digitalclock.theme.ClockColorScheme

object LedGlowColorSchemes {
    val all = listOf(
        ClockColorScheme(
            id = "classic_green",
            name = "Classic LED Green",
            active = Color(0xFF66FF88),
            inactive = Color(0xFF113318),
            background = Color.Black,
            border = Color(0xFF22552B)
        ),
        ClockColorScheme(
            id = "vibrant_red",
            name = "Vibrant LED Red",
            active = Color(0xFFFF3333),
            inactive = Color(0xFF331111),
            background = Color.Black,
            border = Color(0xFF552222)
        ),
        ClockColorScheme(
            id = "amber_glow",
            name = "Amber Tube Glow",
            active = Color(0xFFFFB000),
            inactive = Color(0xFF332200),
            background = Color.Black,
            border = Color(0xFF553300)
        )
    )
}
