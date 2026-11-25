package com.digital.digitalclock.theme

import androidx.compose.ui.unit.Dp
import com.digital.digitalclock.core.ClockTime

/**
 * Everything a theme needs to render one frame. Built by the UI layer from the
 * function-layer time data plus the currently selected layout / color scheme /
 * theme-specific settings.
 */
data class RenderContext(
    val time: ClockTime,
    val is24Hour: Boolean,
    val layout: ClockLayout,
    val colorScheme: ClockColorScheme,
    val settings: Map<String, Float>,
    val constraintsWidth: Dp
)
