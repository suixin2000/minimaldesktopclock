package com.digital.digitalclock.themes.dsegclassic

import androidx.compose.runtime.Composable
import com.digital.digitalclock.theme.ClockColorScheme
import com.digital.digitalclock.theme.ClockLayout
import com.digital.digitalclock.theme.ClockTheme
import com.digital.digitalclock.theme.RenderContext
import com.digital.digitalclock.theme.ThemeSettingSpec

/**
 * DSEG Classic theme, rewritten under the layered protocol. Numbers and text are
 * rendered with the DSEG fonts (see [DsegClassicRenderer]). It owns three layouts,
 * five color schemes, and three theme-specific settings.
 */
object DsegClassicTheme : ClockTheme {

    const val SETTING_INACTIVE_ALPHA = "inactiveAlpha"
    const val SETTING_HIDE_WEEK = "hideWeek"
    const val SETTING_HIDE_DATE = "hideDate"

    override val id: String = "dseg_classic"
    override val displayName: String = "DSEG Classic"

    override val layouts: List<ClockLayout> = DsegClassicLayouts.all
    override val colorSchemes: List<ClockColorScheme> = DsegClassicColorSchemes.all

    override val settingSpecs: List<ThemeSettingSpec> = listOf(
        // 0% = ghost segments fully visible, 100% = fully transparent (hidden)
        ThemeSettingSpec.Slider(
            key = SETTING_INACTIVE_ALPHA,
            label = "Non-show path transparency",
            min = 0f,
            max = 100f,
            default = 0f
        ),
        ThemeSettingSpec.Toggle(key = SETTING_HIDE_WEEK, label = "Hide week", default = false),
        ThemeSettingSpec.Toggle(key = SETTING_HIDE_DATE, label = "Hide date", default = false)
    )

    @Composable
    override fun Render(ctx: RenderContext) {
        DsegClassicRender(ctx)
    }
}
