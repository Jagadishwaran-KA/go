package com.example.warehouse_go.sdui.renderer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.warehouse_go.sdui.models.*

/**
 * Extension functions to convert enum values to Compose values
 */

fun Spacing.toDp(): Dp = when (this) {
    Spacing.NONE -> 0.dp
    Spacing.EXTRA_SMALL -> 4.dp
    Spacing.SMALL -> 8.dp
    Spacing.MEDIUM -> 16.dp
    Spacing.LARGE -> 24.dp
    Spacing.EXTRA_LARGE -> 32.dp
}

fun Elevation.toElevation(): Dp = when (this) {
    Elevation.NONE -> 0.dp
    Elevation.LOW -> 2.dp
    Elevation.MEDIUM -> 4.dp
    Elevation.HIGH -> 8.dp
}

@Composable
fun ColorToken.toColor(): Color = when (this) {
    ColorToken.PRIMARY -> MaterialTheme.colorScheme.primary
    ColorToken.ON_PRIMARY -> MaterialTheme.colorScheme.onPrimary
    ColorToken.SECONDARY -> MaterialTheme.colorScheme.secondary
    ColorToken.ON_SECONDARY -> MaterialTheme.colorScheme.onSecondary
    ColorToken.SURFACE -> MaterialTheme.colorScheme.surface
    ColorToken.ON_SURFACE -> MaterialTheme.colorScheme.onSurface
    ColorToken.SURFACE_VARIANT -> MaterialTheme.colorScheme.surfaceVariant
    ColorToken.ON_SURFACE_VARIANT -> MaterialTheme.colorScheme.onSurfaceVariant
    ColorToken.BACKGROUND -> MaterialTheme.colorScheme.background
    ColorToken.ON_BACKGROUND -> MaterialTheme.colorScheme.onBackground
    ColorToken.ERROR -> MaterialTheme.colorScheme.error
    ColorToken.ON_ERROR -> MaterialTheme.colorScheme.onError
    ColorToken.SUCCESS -> MaterialTheme.colorScheme.primary // Use tertiary or custom extension
    ColorToken.ON_SUCCESS -> MaterialTheme.colorScheme.onPrimary
    ColorToken.WARNING -> MaterialTheme.colorScheme.tertiary
    ColorToken.ON_WARNING -> MaterialTheme.colorScheme.onTertiary
    ColorToken.INFO -> MaterialTheme.colorScheme.primaryContainer
    ColorToken.ON_INFO -> MaterialTheme.colorScheme.onPrimaryContainer
}

fun String.toImageVector(): ImageVector = when (this.lowercase()) {
    "search" -> Icons.Default.Search
    "filter_list" -> Icons.Default.FilterList
    "inbox" -> Icons.Default.Inbox
    "add" -> Icons.Default.Add
    "delete" -> Icons.Default.Delete
    "arrow_back" -> Icons.Default.ArrowBack
    "edit" -> Icons.Default.Edit
    "done" -> Icons.Default.Done
    "close" -> Icons.Default.Close
    "info" -> Icons.Default.Info
    "warning" -> Icons.Default.Warning
    else -> Icons.Default.Help
}

/**
 * Template key resolution
 */
fun String.resolveTemplateKey(data: Map<String, Any?>): String {
    var resolvedKey = this
    
    // Find all placeholders like {fieldName}
    val placeholderPattern = "\\{([^}]+)\\}".toRegex()
    
    placeholderPattern.findAll(this).forEach { match ->
        val fieldName = match.groupValues[1]
        val fieldValue = data[fieldName]?.toString() ?: "unknown"
        resolvedKey = resolvedKey.replace("{$fieldName}", fieldValue)
    }
    
    return resolvedKey
}
