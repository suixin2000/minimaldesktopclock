package com.digital.digitalclock.theme

import androidx.compose.runtime.Composable

/**
 * The theme-layer contract. A theme owns its appearance: how numbers/text are
 * rendered (font or code), its visual features, and its own set of layouts,
 * color schemes and theme-specific settings.
 *
 * Layouts and color schemes are theme-local, not global: switching theme swaps the
 * whole set, while switching layout/color within a theme keeps the theme intact.
 */
interface ClockTheme {
    val id: String
    val displayName: String
    val layouts: List<ClockLayout>
    val colorSchemes: List<ClockColorScheme>

    /**
     * Theme-specific settings shown in the settings menu. The menu checks this list
     * for the currently active theme before it is presented.
     */
    val settingSpecs: List<ThemeSettingSpec>

    @Composable
    fun Render(ctx: RenderContext)
}
