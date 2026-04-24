package eu.kanade.presentation.theme.colorscheme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

internal object KomaColorScheme : BaseColorScheme() {

    override val darkScheme = darkColorScheme(
        primary = Color(0xFF58A6FF),
        onPrimary = Color(0xFF003258),
        primaryContainer = Color(0xFF00497D),
        onPrimaryContainer = Color(0xFFD1E4FF),
        inversePrimary = Color(0xFF0061A4),
        secondary = Color(0xFF58A6FF),
        onSecondary = Color(0xFF0D1220),
        secondaryContainer = Color(0xFF00497D),
        onSecondaryContainer = Color(0xFFD1E4FF),
        tertiary = Color(0xFFFF6B6B),
        onTertiary = Color(0xFF410002),
        tertiaryContainer = Color(0xFF93000A),
        onTertiaryContainer = Color(0xFFFFDAD6),
        background = Color(0xFF0D1220),
        onBackground = Color(0xFFE2E2E6),
        surface = Color(0xFF0D1220),
        onSurface = Color(0xFFE2E2E6),
        surfaceVariant = Color(0xFF1B2236),
        onSurfaceVariant = Color(0xFFC4C6D0),
        surfaceTint = Color(0xFF58A6FF),
        inverseSurface = Color(0xFFE2E2E6),
        inverseOnSurface = Color(0xFF0D1220),
        outline = Color(0xFF8E9099),
        surfaceContainerLowest = Color(0xFF050811),
        surfaceContainerLow = Color(0xFF0D1220),
        surfaceContainer = Color(0xFF13192B),
        surfaceContainerHigh = Color(0xFF1B2236),
        surfaceContainerHighest = Color(0xFF242C44),
    )

    override val lightScheme = lightColorScheme(
        primary = Color(0xFF0061A4),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFD1E4FF),
        onPrimaryContainer = Color(0xFF001D36),
        inversePrimary = Color(0xFF58A6FF),
        secondary = Color(0xFF0061A4),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFD1E4FF),
        onSecondaryContainer = Color(0xFF001D36),
        tertiary = Color(0xFFC00015),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFFFDAD6),
        onTertiaryContainer = Color(0xFF410002),
        background = Color(0xFFFDFCFF),
        onBackground = Color(0xFF1A1C1E),
        surface = Color(0xFFFDFCFF),
        onSurface = Color(0xFF1A1C1E),
        surfaceVariant = Color(0xFFDFE2EB),
        onSurfaceVariant = Color(0xFF43474E),
        surfaceTint = Color(0xFF0061A4),
        inverseSurface = Color(0xFF2F3033),
        inverseOnSurface = Color(0xFFF1F0F4),
        outline = Color(0xFF73777F),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFF3F3F6),
        surfaceContainer = Color(0xFFEDEEF1),
        surfaceContainerHigh = Color(0xFFE7E8EB),
        surfaceContainerHighest = Color(0xFFE2E2E6),
    )
}
