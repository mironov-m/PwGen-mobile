package ru.mironov.pwgen.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object Colors {

    val LightColorScheme = lightColorScheme(
        // PRIMARY - Основной акцентный цвет
        primary = Color(0xFF5F6FE8),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFE0E3FF),
        onPrimaryContainer = Color(0xFF1A1B4F),

        // SECONDARY - Дополнительный акцент
        secondary = Color(0xFF00BFA5),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFB2F5EA),
        onSecondaryContainer = Color(0xFF003830),

        // TERTIARY - Третичный акцент
        tertiary = Color(0xFF9C4DCC),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFF3E5F5),
        onTertiaryContainer = Color(0xFF311B47),

        // ERROR - Ошибки и предупреждения
        error = Color(0xFFD32F2F),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410E0B),

        // BACKGROUND - Основной фон
        background = Color(0xFFFBFBFD),
        onBackground = Color(0xFF1A1C1E),

        // SURFACE - Поверхности (карточки, диалоги)
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF1A1C1E),
        surfaceVariant = Color(0xFFE3E3EB),
        onSurfaceVariant = Color(0xFF45464F),

        // OUTLINE - Границы и разделители
        outline = Color(0xFF757680),
        outlineVariant = Color(0xFFC6C6D0),

        // SURFACE TINT - Для elevation overlay
        surfaceTint = Color(0xFF5F6FE8),

        // INVERSE - Инверсные цвета (для snackbar, tooltips)
        inverseSurface = Color(0xFF2F3033),
        inverseOnSurface = Color(0xFFF1F0F4),
        inversePrimary = Color(0xFFBDC5FF),

        // SCRIM - Затемнение модальных окон
        scrim = Color(0xFF000000).copy(alpha = 0.32f)
    )

    val DarkColorScheme = darkColorScheme(
        primary = Color(0xFFBDC5FF),
        onPrimary = Color(0xFF2A3080),
        primaryContainer = Color(0xFF434B99),
        onPrimaryContainer = Color(0xFFE0E3FF),

        secondary = Color(0xFF6EFFD8),
        onSecondary = Color(0xFF003830),
        secondaryContainer = Color(0xFF005048),
        onSecondaryContainer = Color(0xFFB2F5EA),

        tertiary = Color(0xFFE0B3FF),
        onTertiary = Color(0xFF4A2461),
        tertiaryContainer = Color(0xFF633A79),
        onTertiaryContainer = Color(0xFFF7E5FF),

        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),

        background = Color(0xFF1A1C1E),
        onBackground = Color(0xFFE3E2E6),

        surface = Color(0xFF1A1C1E),
        onSurface = Color(0xFFE3E2E6),
        surfaceVariant = Color(0xFF45464F),
        onSurfaceVariant = Color(0xFFC6C5D0),

        outline = Color(0xFF8F909A),
        outlineVariant = Color(0xFF45464F),

        surfaceTint = Color(0xFFBDC5FF),

        inverseSurface = Color(0xFFE3E2E6),
        inverseOnSurface = Color(0xFF2F3033),
        inversePrimary = Color(0xFF5F6FE8),

        scrim = Color(0xFF000000).copy(alpha = 0.5f)
    )
}