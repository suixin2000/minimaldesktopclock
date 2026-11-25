package com.digital.digitalclock.core

import android.content.Context
import org.json.JSONObject
import java.util.TimeZone

/** Per-theme persisted selection. */
data class ThemeSelection(
    val layoutIndex: Int = 0,
    val colorSchemeIndex: Int = 0,
    val settings: Map<String, Float> = emptyMap()
)

/** Everything the app persists across restarts. */
data class PersistedState(
    val is24Hour: Boolean = false,
    val zoneId: String = TimeZone.getDefault().id,
    val themeIndex: Int = 0,
    val themes: Map<String, ThemeSelection> = emptyMap()
)

/**
 * [SharedPreferences]-backed persistence, serialized with org.json (both are Android
 * SDK built-ins, so no extra Gradle dependency is required).
 */
class SettingsStore(context: Context) {

    private val prefs = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun load(): PersistedState {
        val raw = prefs.getString(KEY_STATE, null) ?: return PersistedState()
        return try {
            parse(JSONObject(raw))
        } catch (_: Exception) {
            PersistedState()
        }
    }

    fun save(state: PersistedState) {
        prefs.edit().putString(KEY_STATE, toJson(state).toString()).apply()
    }

    private fun parse(obj: JSONObject): PersistedState {
        val themes = mutableMapOf<String, ThemeSelection>()
        val themesObj = obj.optJSONObject(KEY_THEMES)
        if (themesObj != null) {
            for (id in themesObj.keys()) {
                val t = themesObj.optJSONObject(id) ?: continue
                val settings = mutableMapOf<String, Float>()
                val settingsObj = t.optJSONObject(KEY_SETTINGS)
                if (settingsObj != null) {
                    for (k in settingsObj.keys()) {
                        settings[k] = settingsObj.optDouble(k, 0.0).toFloat()
                    }
                }
                themes[id] = ThemeSelection(
                    layoutIndex = t.optInt(KEY_LAYOUT, 0),
                    colorSchemeIndex = t.optInt(KEY_COLOR, 0),
                    settings = settings
                )
            }
        }
        return PersistedState(
            is24Hour = obj.optBoolean(KEY_24H, false),
            zoneId = obj.optString(KEY_ZONE, TimeZone.getDefault().id),
            themeIndex = obj.optInt(KEY_THEME_INDEX, 0),
            themes = themes
        )
    }

    private fun toJson(state: PersistedState): JSONObject {
        val themesObj = JSONObject()
        for ((id, sel) in state.themes) {
            val settingsObj = JSONObject()
            for ((k, v) in sel.settings) settingsObj.put(k, v.toDouble())
            themesObj.put(
                id,
                JSONObject()
                    .put(KEY_LAYOUT, sel.layoutIndex)
                    .put(KEY_COLOR, sel.colorSchemeIndex)
                    .put(KEY_SETTINGS, settingsObj)
            )
        }
        return JSONObject()
            .put(KEY_24H, state.is24Hour)
            .put(KEY_ZONE, state.zoneId)
            .put(KEY_THEME_INDEX, state.themeIndex)
            .put(KEY_THEMES, themesObj)
    }

    private companion object {
        const val FILE = "clock_prefs"
        const val KEY_STATE = "state"
        const val KEY_24H = "is24h"
        const val KEY_ZONE = "zone"
        const val KEY_THEME_INDEX = "themeIndex"
        const val KEY_THEMES = "themes"
        const val KEY_LAYOUT = "layout"
        const val KEY_COLOR = "color"
        const val KEY_SETTINGS = "settings"
    }
}
