package com.digital.digitalclock.themes.ledglow

import com.digital.digitalclock.theme.ClockLayout

object LedGlowLayouts {
    const val ID_HH_MM_SS = "hh_mm_ss"
    const val ID_HH_MM = "hh_mm"
    const val ID_HH_MM_HALFSS = "hh_mm_halfss"

    val all = listOf(
        ClockLayout(ID_HH_MM_SS, "HH:MM:SS"),
        ClockLayout(ID_HH_MM, "HH:MM"),
        ClockLayout(ID_HH_MM_HALFSS, "HH:MM:HalfSS")
    )
}
