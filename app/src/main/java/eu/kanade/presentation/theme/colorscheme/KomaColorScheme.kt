package eu.kanade.presentation.theme.colorscheme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Koma "Midnight Deep" color scheme
 *
 * A premium, modern dark-first design inspired by contemporary reading apps.
 * Uses a deep navy base with vibrant cyan-blue accents and soft warm highlights.
 */
internal object KomaColorScheme : BaseColorScheme() {

    override val darkScheme = darkColorScheme(
        // Primary - Vibrant cyan-blue for key interactive elements
        primary = Color(0xFF7DD3FC),
        onPrimary = Color(0xFF00344D),
        primaryContainer = Color(0xFF004D6E),
        onPrimaryContainer = Color(0xFFC5EAFF),
        inversePrimary = Color(0xFF006590),

        // Secondary - Soft teal for supporting elements
        secondary = Color(0xFF80CBC4),
        onSecondary = Color(0xFF003733),
        secondaryContainer = Color(0xFF1A4D49),
        onSecondaryContainer = Color(0xFFA7F3EE),

        // Tertiary - Warm amber accent for badges/notifications
        tertiary = Color(0xFFFFB74D),
        onTertiary = Color(0xFF3E2700),
        tertiaryContainer = Color(0xFF5A3A00),
        onTertiaryContainer = Color(0xFFFFDDB3),

        // Surfaces - Deep navy with layered depth
        background = Color(0xFF0B0F1A),
        onBackground = Color(0xFFE4E6ED),
        surface = Color(0xFF0B0F1A),
        onSurface = Color(0xFFE4E6ED),
        surfaceVariant = Color(0xFF1E2433),
        onSurfaceVariant = Color(0xFFC1C6D6),
        surfaceTint = Color(0xFF7DD3FC),

        // Inverse
        inverseSurface = Color(0xFFE4E6ED),
        inverseOnSurface = Color(0xFF1A1E2A),

        // Outline
        outline = Color(0xFF6B7189),
        outlineVariant = Color(0xFF3D4355),

        // Surface containers - Graduated depth for card layering
        surfaceContainerLowest = Color(0xFF060810),
        surfaceContainerLow = Color(0xFF0E1321),
        surfaceContainer = Color(0xFF141928),
        surfaceContainerHigh = Color(0xFF1C2132),
        surfaceContainerHighest = Color(0xFF252A3C),
    )

    override val lightScheme = lightColorScheme(
        // Primary - Rich blue
        primary = Color(0xFF006590),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFC5EAFF),
        onPrimaryContainer = Color(0xFF001E30),
        inversePrimary = Color(0xFF7DD3FC),

        // Secondary - Teal
        secondary = Color(0xFF006B64),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFF70F7EC),
        onSecondaryContainer = Color(0xFF00201D),

        // Tertiary - Amber
        tertiary = Color(0xFF8A5100),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFFFDDB3),
        onTertiaryContainer = Color(0xFF2C1600),

        // Surfaces - Clean, bright
        background = Color(0xFFF8FAFE),
        onBackground = Color(0xFF191C22),
        surface = Color(0xFFF8FAFE),
        onSurface = Color(0xFF191C22),
        surfaceVariant = Color(0xFFDDE3EF),
        onSurfaceVariant = Color(0xFF424752),
        surfaceTint = Color(0xFF006590),

        // Inverse
        inverseSurface = Color(0xFF2E3138),
        inverseOnSurface = Color(0xFFEFF1F8),

        // Outline
        outline = Color(0xFF727783),
        outlineVariant = Color(0xFFC2C7D3),

        // Surface containers
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFF1F3FA),
        surfaceContainer = Color(0xFFEBEDF4),
        surfaceContainerHigh = Color(0xFFE5E8EE),
        surfaceContainerHighest = Color(0xFFE0E2E9),
    )
}
