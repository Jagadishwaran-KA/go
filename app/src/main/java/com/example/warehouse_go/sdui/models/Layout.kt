package com.example.warehouse_go.sdui.models

import kotlinx.serialization.Serializable

/**
 * Page layout structure using Adaptive Canonical Layout
 */
@Serializable
data class PageLayout(
    val type: LayoutType,
    val areas: LayoutAreas
)

@Serializable
enum class LayoutType {
    LIST_DETAIL,
    ROW,
    COLUMN,
    GRID,
    LIST
}

@Serializable
data class LayoutAreas(
    val topBar: TopBarComponent? = null,
    val content: List<ContentComponent>? = null,
    val bottomBar: BottomBarComponent? = null,
    val floatingAction: FloatingActionComponent? = null,
    // For LIST_DETAIL layout
    val masterPane: List<ContentComponent>? = null,
    val detailPane: List<ContentComponent>? = null
)

@Serializable
data class TopBarComponent(
    override val id: String,
    override val version: Int,
    val meta: TopBarMeta,
    val properties: ComponentProperties
) : UiComponent {
    override val type: String = "topBar"
}

@Serializable
data class TopBarMeta(
    val title: TextContent,
    val showBackButton: Boolean = false,
    val actions: List<ActionComponent> = emptyList()
)

@Serializable
data class BottomBarComponent(
    override val id: String,
    override val version: Int,
    val meta: BottomBarMeta,
    val properties: ComponentProperties
) : UiComponent {
    override val type: String = "bottomBar"
}

@Serializable
data class BottomBarMeta(
    val items: List<BottomBarItem>
)

@Serializable
data class BottomBarItem(
    val id: String,
    val label: String,
    val icon: String,
    val action: ActionHandler
)

@Serializable
data class FloatingActionComponent(
    override val id: String,
    override val version: Int,
    val meta: FloatingActionMeta,
    val properties: ComponentProperties
) : UiComponent {
    override val type: String = "fab"
}

@Serializable
data class FloatingActionMeta(
    val icon: String,
    val label: String? = null,
    val action: ActionHandler
)
