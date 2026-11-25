package com.digital.digitalclock.theme

import com.digital.digitalclock.themes.dsegclassic.DsegClassicTheme
import com.digital.digitalclock.themes.dsegmodern.DsegModernTheme
import com.digital.digitalclock.themes.ledglow.LedGlowTheme
import com.digital.digitalclock.themes.matrix.MatrixTheme

/**
 * Ordered registry of all available themes. The order defines the short-press
 * (tap) cycle order. Add new themes here to make them selectable.
 */
val AllThemes: List<ClockTheme> = listOf(
    LedGlowTheme,
    DsegClassicTheme,
    DsegModernTheme,
    MatrixTheme
)
