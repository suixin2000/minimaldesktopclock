package com.digital.digitalclock.themes.matrix

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.digital.digitalclock.theme.ClockColorScheme
import com.digital.digitalclock.theme.ClockLayout
import com.digital.digitalclock.theme.ClockTheme
import com.digital.digitalclock.theme.RenderContext
import com.digital.digitalclock.theme.ThemeSettingSpec

/**
 * Matrix theme with dot-matrix rendering. Now includes multiple layouts, color schemes,
 * and theme-specific settings similar to DSEG Classic.
 */
object MatrixTheme : ClockTheme {

    const val SETTING_INACTIVE_ALPHA = "inactiveAlpha"
    const val SETTING_HIDE_WEEK = "hideWeek"
    const val SETTING_HIDE_DATE = "hideDate"

    override val id: String = "matrix"
    override val displayName: String = "Matrix"

    override val layouts: List<ClockLayout> = listOf(
        ClockLayout("hh:mm:ss", "hh:mm:ss"),
        ClockLayout("hh:mm", "hh:mm"),
        ClockLayout("hh:mm:halfss", "hh:mm:halfss")
    )

    override val colorSchemes: List<ClockColorScheme> = listOf(
        ClockColorScheme(
            id = "blue",
            name = "Blue",
            active = Color(0xFF00E5FF),
            inactive = Color(0xFF153B3B),
            background = Color.Black,
            border = Color.Black
        ),
        ClockColorScheme(
            id = "yellow",
            name = "Yellow",
            active = Color(0xFFFBFA72),
            inactive = Color(0xFF4A4000),
            background = Color.Black,
            border = Color.Black
        ),
        ClockColorScheme(
            id = "green",
            name = "Green",
            active = Color(0xFF33FF66),
            inactive = Color(0xFF003311),
            background = Color.Black,
            border = Color.Black
        ),
        ClockColorScheme(
            id = "orange",
            name = "Orange",
            active = Color.Black,
            inactive = Color(0xFFD35400),
            background = Color(0xFFFF9900),
            border = Color.Black
        ),
        ClockColorScheme(
            id = "classic",
            name = "Classic",
            active = Color(0xFF111810),
            inactive = Color(0xFF7E8D7B),
            background = Color(0xFF92A18D),
            border = Color.White
        )
    )

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
        MatrixRender(ctx)
    }
}
