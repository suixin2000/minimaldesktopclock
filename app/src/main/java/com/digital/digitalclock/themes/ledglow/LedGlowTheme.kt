package com.digital.digitalclock.themes.ledglow

import androidx.compose.runtime.Composable
import com.digital.digitalclock.theme.ClockColorScheme
import com.digital.digitalclock.theme.ClockLayout
import com.digital.digitalclock.theme.ClockTheme
import com.digital.digitalclock.theme.RenderContext
import com.digital.digitalclock.theme.ThemeSettingSpec

object LedGlowTheme : ClockTheme {
    override val id: String = "led_glow"
    override val displayName: String = "LED Glow"

    override val layouts: List<ClockLayout> = LedGlowLayouts.all
    override val colorSchemes: List<ClockColorScheme> = LedGlowColorSchemes.all
    override val settingSpecs: List<ThemeSettingSpec> = emptyList()

    @Composable
    override fun Render(ctx: RenderContext) {
        LedGlowRender(ctx)
    }
}
