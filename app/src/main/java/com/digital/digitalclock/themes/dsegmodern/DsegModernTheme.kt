package com.digital.digitalclock.themes.dsegmodern

import androidx.compose.runtime.Composable
import com.digital.digitalclock.theme.ClockColorScheme
import com.digital.digitalclock.theme.ClockLayout
import com.digital.digitalclock.theme.ClockTheme
import com.digital.digitalclock.theme.RenderContext
import com.digital.digitalclock.theme.ThemeSettingSpec

/**
 * DSEG Modern theme, using Modern Bold Italic fonts. Same layouts, color schemes,
 * and settings as DSEG Classic.
 */
object DsegModernTheme : ClockTheme {

    const val SETTING_INACTIVE_ALPHA = "inactiveAlpha"
    const val SETTING_HIDE_WEEK = "hideWeek"
    const val SETTING_HIDE_DATE = "hideDate"

    override val id: String = "dseg_modern"
    override val displayName: String = "DSEG Modern"

    override val layouts: List<ClockLayout> = DsegModernLayouts.all
    override val colorSchemes: List<ClockColorScheme> = DsegModernColorSchemes.all

    override val settingSpecs: List<ThemeSettingSpec> = listOf(
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
        DsegModernRender(ctx)
    }
}
