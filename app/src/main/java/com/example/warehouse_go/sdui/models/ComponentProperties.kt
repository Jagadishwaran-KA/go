package com.example.warehouse_go.sdui.models

import kotlinx.serialization.Serializable

/**
 * Reusable component properties using enum values
 * No hardcoded values allowed - everything is semantic
 */
@Serializable
data class ComponentProperties(
    val spacing: Spacing = Spacing.MEDIUM,
    val padding: Spacing = Spacing.NONE,
    val elevation: Elevation = Elevation.NONE,
    val backgroundColor: ColorToken = ColorToken.SURFACE,
    val textColor: ColorToken = ColorToken.ON_SURFACE,
    val cornerRadius: CornerRadius = CornerRadius.MEDIUM,
    val visible: Boolean = true,
    val enabled: Boolean = true,
    val fillMaxWidth: Boolean = false,
    val fillMaxHeight: Boolean = false
)

@Serializable
enum class Spacing {
    NONE,
    EXTRA_SMALL,
    SMALL,
    MEDIUM,
    LARGE,
    EXTRA_LARGE
}

@Serializable
enum class Elevation {
    NONE,
    LOW,
    MEDIUM,
    HIGH
}

@Serializable
enum class ColorToken {
    PRIMARY,
    ON_PRIMARY,
    SECONDARY,
    ON_SECONDARY,
    SURFACE,
    ON_SURFACE,
    SURFACE_VARIANT,
    ON_SURFACE_VARIANT,
    BACKGROUND,
    ON_BACKGROUND,
    ERROR,
    ON_ERROR,
    SUCCESS,
    ON_SUCCESS,
    WARNING,
    ON_WARNING,
    INFO,
    ON_INFO
}

@Serializable
enum class CornerRadius {
    NONE,
    SMALL,
    MEDIUM,
    LARGE,
    FULL
}

/**
 * Conditional styling based on data values
 */
@Serializable
data class ConditionalStyle(
    val conditions: List<StyleCondition>
)

@Serializable
data class StyleCondition(
    val whenExpression: String,
    val style: StyleOverride
)

@Serializable
data class StyleOverride(
    val backgroundColor: ColorToken? = null,
    val textColor: ColorToken? = null,
    val fontWeight: FontWeight? = null
)
