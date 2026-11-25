package com.digital.digitalclock.theme

/**
 * Describes a theme-specific setting so the settings panel can render it generically
 * and the state holder can persist it. Values are stored as [Float]:
 * - [Slider] stores its raw float value.
 * - [Toggle] stores 0f (off) / 1f (on).
 */
sealed class ThemeSettingSpec {
    abstract val key: String
    abstract val label: String

    data class Slider(
        override val key: String,
        override val label: String,
        val min: Float,
        val max: Float,
        val default: Float
    ) : ThemeSettingSpec()

    data class Toggle(
        override val key: String,
        override val label: String,
        val default: Boolean
    ) : ThemeSettingSpec()
}

/** The default stored float value for this spec. */
val ThemeSettingSpec.defaultValue: Float
    get() = when (this) {
        is ThemeSettingSpec.Slider -> default
        is ThemeSettingSpec.Toggle -> if (default) 1f else 0f
    }

/** Read a spec's float value from a settings map, falling back to its default. */
fun floatOf(settings: Map<String, Float>, spec: ThemeSettingSpec): Float =
    settings[spec.key] ?: spec.defaultValue

/** Read a spec's value from a settings map as a boolean, falling back to its default. */
fun boolOf(settings: Map<String, Float>, spec: ThemeSettingSpec): Boolean =
    floatOf(settings, spec) >= 0.5f
