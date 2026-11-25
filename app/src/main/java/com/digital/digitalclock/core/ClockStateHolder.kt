package com.digital.digitalclock.core

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.digital.digitalclock.theme.AllThemes
import com.digital.digitalclock.theme.ClockColorScheme
import com.digital.digitalclock.theme.ClockLayout
import com.digital.digitalclock.theme.ClockTheme
import com.digital.digitalclock.theme.defaultValue
import java.util.TimeZone

/**
 * Function-layer state holder. Owns the active theme plus each theme's local
 * layout / color scheme / settings, and the global 12-24h + time-zone choices.
 *
 * Layout and color scheme are theme-local: cycling them never changes the theme,
 * and switching theme restores that theme's own last-used selection. Every mutation
 * is persisted through [SettingsStore].
 */
class ClockStateHolder(context: Context) {

    private val store = SettingsStore(context)

    val themes: List<ClockTheme> = AllThemes

    private val loaded = store.load()

    var themeIndex by mutableIntStateOf(loaded.themeIndex.coerceIn(0, themes.lastIndex))
        private set

    var is24Hour by mutableStateOf(loaded.is24Hour)
        private set

    var zoneId by mutableStateOf(
        loaded.zoneId.ifBlank { TimeZone.getDefault().id }
    )
        private set

    /** Per-theme selections, keyed by theme id. Absent -> theme defaults. */
    private val selections = mutableStateMapOf<String, ThemeSelection>().apply {
        putAll(loaded.themes)
    }

    val currentTheme: ClockTheme get() = themes[themeIndex]

    private fun selection(theme: ClockTheme): ThemeSelection =
        selections[theme.id] ?: ThemeSelection()

    val currentLayout: ClockLayout
        get() {
            val t = currentTheme
            return t.layouts[selection(t).layoutIndex.coerceIn(0, t.layouts.lastIndex)]
        }

    val currentColorScheme: ClockColorScheme
        get() {
            val t = currentTheme
            return t.colorSchemes[selection(t).colorSchemeIndex.coerceIn(0, t.colorSchemes.lastIndex)]
        }

    /** Theme-specific setting values with spec defaults filled in. */
    val resolvedSettings: Map<String, Float>
        get() {
            val t = currentTheme
            val stored = selection(t).settings
            return t.settingSpecs.associate { spec -> spec.key to (stored[spec.key] ?: spec.defaultValue) }
        }

    // --- Mutations -------------------------------------------------------------

    /** Short press: advance to the next theme (tap-cycle order). */
    fun nextTheme() {
        themeIndex = (themeIndex + 1) % themes.size
        persist()
    }

    /** Horizontal swipe: cycle the current theme's color scheme only. */
    fun cycleColorScheme(direction: Int) {
        val t = currentTheme
        if (t.colorSchemes.size < 2) return
        val sel = selection(t)
        val idx = (sel.colorSchemeIndex.coerceIn(0, t.colorSchemes.lastIndex) + direction + t.colorSchemes.size) % t.colorSchemes.size
        selections[t.id] = sel.copy(colorSchemeIndex = idx)
        persist()
    }

    /** Vertical swipe: cycle the current theme's layout only. */
    fun cycleLayout(direction: Int) {
        val t = currentTheme
        if (t.layouts.size < 2) return
        val sel = selection(t)
        val idx = (sel.layoutIndex.coerceIn(0, t.layouts.lastIndex) + direction + t.layouts.size) % t.layouts.size
        selections[t.id] = sel.copy(layoutIndex = idx)
        persist()
    }

    fun setUse24Hour(value: Boolean) {
        is24Hour = value
        persist()
    }

    fun setZone(id: String) {
        zoneId = id
        persist()
    }

    /** Update one theme-specific setting for the current theme. */
    fun setThemeSetting(key: String, value: Float) {
        val t = currentTheme
        val sel = selection(t)
        selections[t.id] = sel.copy(settings = sel.settings + (key to value))
        persist()
    }

    private fun persist() {
        store.save(
            PersistedState(
                is24Hour = is24Hour,
                zoneId = zoneId,
                themeIndex = themeIndex,
                themes = selections.toMap()
            )
        )
    }
}
