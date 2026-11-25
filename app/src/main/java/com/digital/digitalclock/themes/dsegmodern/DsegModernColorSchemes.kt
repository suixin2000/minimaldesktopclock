package com.digital.digitalclock.themes.dsegmodern

import androidx.compose.ui.graphics.Color
import com.digital.digitalclock.theme.ClockColorScheme

/**
 * The five color skins for the DSEG Modern theme. Same appearance as DSEG Classic.
 */
object DsegModernColorSchemes {

    val Blue = ClockColorScheme(
        id = "blue",
        name = "Blue",
        active = Color(0xFF00E5FF),
        inactive = Color(0xFF153B3B),
        background = Color.Black,
        border = Color.Black
    )

    val Yellow = ClockColorScheme(
        id = "yellow",
        name = "Yellow",
        active = Color(0xFFFBFA72),
        inactive = Color(0xFF4A4000),
        background = Color.Black,
        border = Color.Black
    )

    val Green = ClockColorScheme(
        id = "green",
        name = "Green",
        active = Color(0xFF33FF66),
        inactive = Color(0xFF003311),
        background = Color.Black,
        border = Color.Black
    )

    val Orange = ClockColorScheme(
        id = "orange",
        name = "Orange",
        active = Color.Black,
        inactive = Color(0xFFD35400),
        background = Color(0xFFFF9900),
        border = Color.Black
    )

    val Classic = ClockColorScheme(
        id = "classic",
        name = "Classic",
        active = Color(0xFF111810),
        inactive = Color(0xFF7E8D7B),
        background = Color(0xFF92A18D),
        border = Color.White
    )

    val all: List<ClockColorScheme> = listOf(Blue, Yellow, Green, Orange, Classic)
}
