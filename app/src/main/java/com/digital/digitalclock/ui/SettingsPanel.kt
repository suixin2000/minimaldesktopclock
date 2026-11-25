package com.digital.digitalclock.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.digital.digitalclock.core.ClockStateHolder
import com.digital.digitalclock.core.zoneLabel
import com.digital.digitalclock.theme.ThemeSettingSpec
import java.util.TimeZone
import kotlin.math.roundToInt

/**
 * Settings menu shown on long press. Presents global settings (12/24h, time zone)
 * plus the theme-specific settings declared by the currently active theme. The theme
 * section is built from [ClockStateHolder.currentTheme]'s setting specs, so it always
 * reflects what the current theme defines before the menu is shown.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPanel(state: ClockStateHolder, onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Text("Settings", style = MaterialTheme.typography.titleLarge)

            SectionHeader("Global")

            // 12h / 24h
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("24-hour format")
                Switch(checked = state.is24Hour, onCheckedChange = { state.setUse24Hour(it) })
            }

            // Time zone
            Text("Time zone", modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
            TimeZoneDropdown(
                selectedId = state.zoneId,
                onSelect = { state.setZone(it) }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Theme-specific settings
            SectionHeader("${state.currentTheme.displayName} theme")
            val specs = state.currentTheme.settingSpecs
            if (specs.isEmpty()) {
                Text(
                    "No theme-specific settings",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                val values = state.resolvedSettings
                specs.forEach { spec ->
                    when (spec) {
                        is ThemeSettingSpec.Slider -> SliderSetting(
                            label = spec.label,
                            value = values[spec.key] ?: spec.default,
                            range = spec.min..spec.max,
                            onChange = { state.setThemeSetting(spec.key, it) }
                        )
                        is ThemeSettingSpec.Toggle -> ToggleSetting(
                            label = spec.label,
                            checked = (values[spec.key] ?: (if (spec.default) 1f else 0f)) >= 0.5f,
                            onChange = { state.setThemeSetting(spec.key, if (it) 1f else 0f) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
    )
}

@Composable
private fun SliderSetting(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label)
            Text("${value.roundToInt()}%")
        }
        Slider(value = value, onValueChange = onChange, valueRange = range)
    }
}

@Composable
private fun ToggleSetting(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Switch(checked = checked, onCheckedChange = onChange)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeZoneDropdown(selectedId: String, onSelect: (String) -> Unit) {
    // Build the full list of system zones with labels once.
    val zones = remember {
        TimeZone.getAvailableIDs().sorted().map { id -> id to zoneLabel(TimeZone.getTimeZone(id)) }
    }
    val selectedLabel = remember(selectedId) {
        zones.firstOrNull { it.first == selectedId }?.second ?: zoneLabel(TimeZone.getTimeZone(selectedId))
    }

    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        androidx.compose.material3.OutlinedTextField(
            value = selectedLabel,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = { Text("Zone") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            zones.forEach { (id, label) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onSelect(id)
                        expanded = false
                    }
                )
            }
        }
    }
}
