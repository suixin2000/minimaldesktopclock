package com.digital.digitalclock.theme

import androidx.compose.ui.graphics.Color

/**
 * A theme-local color skin. Changing the color scheme only changes the appearance
 * ("skin") of the clock; it never changes the layout or the theme itself.
 *
 * [inactive] is the base color of the non-show paths (e.g. the ghost "8" behind a
 * digit). Themes that expose a transparency setting apply an alpha on top of this.
 */
data class ClockColorScheme(
    val id: String,
    val name: String,
    val active: Color,
    val inactive: Color,
    val background: Color,
    val border: Color
)
