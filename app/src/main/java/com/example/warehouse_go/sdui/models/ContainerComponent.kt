package com.example.warehouse_go.sdui.models

import kotlinx.serialization.Serializable

/**
 * Container component for flexible layout arrangements
 * Supports COLUMN, ROW, GRID, LAZY_COLUMN, LAZY_ROW, STAGGERED_GRID
 */
@Serializable
data class ContainerComponent(
    override val id: String,
    override val version: Int,
    val meta: ContainerMeta,
    val properties: ComponentProperties = ComponentProperties()
) : ContentComponent {
    override val type: String = "container"
}

@Serializable
data class ContainerMeta(
    val arrangement: Arrangement,
    val children: List<ContentComponent>,
    val columns: Int? = null,  // Required for GRID and STAGGERED_GRID
    val scrollable: Boolean = false,  // For ROW (horizontal scroll) or COLUMN (vertical scroll)
    val spacing: Spacing = Spacing.MEDIUM,
    val horizontalAlignment: HorizontalAlignment = HorizontalAlignment.START,
    val verticalAlignment: VerticalAlignment = VerticalAlignment.TOP
)

@Serializable
enum class Arrangement {
    COLUMN,          // Vertical arrangement
    ROW,             // Horizontal arrangement
    GRID,            // LazyVerticalGrid with fixed columns
    LAZY_COLUMN,     // Scrollable vertical list
    LAZY_ROW,        // Scrollable horizontal list
    STAGGERED_GRID   // LazyVerticalStaggeredGrid
}

@Serializable
enum class HorizontalAlignment {
    START,
    CENTER,
    END
}

@Serializable
enum class VerticalAlignment {
    TOP,
    CENTER,
    BOTTOM
}
