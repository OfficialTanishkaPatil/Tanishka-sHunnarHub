package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = GeometricPrimaryDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF5C2B1B),
    onPrimaryContainer = Color(0xFFFFDBCF),
    secondary = Color(0xFFC7B19E),
    onSecondary = GeometricDarkContainer,
    secondaryContainer = GeometricDark,
    onSecondaryContainer = Color(0xFFF2EAE2),
    tertiary = GeometricSage,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF224434),
    onTertiaryContainer = GeometricSageContainer,
    background = GeometricBgDark,
    onBackground = GeometricTextPrimaryDark,
    surface = GeometricSurfaceDark,
    onSurface = GeometricTextPrimaryDark,
    surfaceVariant = GeometricSurfaceVariantDark,
    onSurfaceVariant = GeometricTextSecondaryDark,
    outline = GeometricBorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = GeometricPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF7EBE1),
    onPrimaryContainer = GeometricDarkContainer,
    secondary = GeometricDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFECE1D5),
    onSecondaryContainer = GeometricDark,
    tertiary = GeometricSage,
    onTertiary = Color.White,
    tertiaryContainer = GeometricSageContainer,
    onTertiaryContainer = Color(0xFF133224),
    background = GeometricBgLight,
    onBackground = GeometricTextPrimary,
    surface = GeometricSurfaceLight,
    onSurface = GeometricTextPrimary,
    surfaceVariant = GeometricSurfaceVariant,
    onSurfaceVariant = GeometricTextSecondary,
    outline = GeometricBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

