package com.example.warehouse_go.sdui.models

import kotlinx.serialization.Serializable

/**
 * Base sealed class for all content components
 * 
 * Polymorphic serialization enabled - supports:
 * - TextFieldComponent
 * - RepeaterComponent
 * - CardComponent
 * - FieldComponent
 * - TextComponent
 * - EmptyStateComponent
 * - ActionComponent
 * - ContainerComponent (for flexible layouts)
 */
@Serializable
sealed class ContentComponent : UiComponent {
    abstract val properties: ComponentProperties
}

/**
 * Text Field Component
 */
@Serializable
data class TextFieldComponent(
    override val id: String,
    override val version: Int,
    val meta: TextFieldMeta,
    override val properties: ComponentProperties
) : ContentComponent() {
    override val type: String = "textField"
}

@Serializable
data class TextFieldMeta(
    val placeholder: String,
    val label: String? = null,
    val inputType: InputType = InputType.TEXT,
    val icon: String? = null,
    val iconPosition: IconPosition = IconPosition.LEADING,
    val action: ActionHandler? = null
)

@Serializable
enum class InputType {
    TEXT,
    NUMBER,
    EMAIL,
    PHONE,
    PASSWORD,
    SEARCH
}

@Serializable
enum class IconPosition {
    LEADING,
    TRAILING
}

/**
 * Repeater Component (Lists/Grids)
 */
@Serializable
data class RepeaterComponent(
    override val id: String,
    override val version: Int,
    val meta: RepeaterMeta,
    override val properties: ComponentProperties
) : ContentComponent() {
    override val type: String = "repeater"
}

@Serializable
data class RepeaterMeta(
    val layoutType: RepeaterLayoutType,
    val itemTemplate: CardComponent,
    val dataSource: DataSourceConfig,
    val emptyState: EmptyStateComponent? = null,
    val enableSwipeActions: Boolean = false,
    val swipeActions: List<SwipeAction> = emptyList()
)

@Serializable
enum class RepeaterLayoutType {
    COLUMN,
    GRID,
    ROW
}

@Serializable
data class DataSourceConfig(
    val endpoint: String,
    val keyField: String,
    val displayFields: List<String>,
    val orderBy: String? = null,
    val filters: List<FilterConfig> = emptyList()
)

@Serializable
data class FilterConfig(
    val field: String,
    val operator: FilterOperator,
    val value: String
)

@Serializable
enum class FilterOperator {
    EQUALS,
    NOT_EQUALS,
    CONTAINS,
    STARTS_WITH,
    ENDS_WITH,
    GREATER_THAN,
    LESS_THAN
}

@Serializable
data class SwipeAction(
    val id: String,
    val label: String,
    val icon: String,
    val direction: SwipeDirection,
    val action: ActionHandler
)

@Serializable
enum class SwipeDirection {
    START_TO_END,
    END_TO_START
}

/**
 * Card Component
 */
@Serializable
data class CardComponent(
    override val id: String,
    override val version: Int,
    val meta: CardMeta,
    override val properties: ComponentProperties
) : ContentComponent() {
    override val type: String = "card"
}

@Serializable
data class CardMeta(
    val fields: List<FieldComponent>,
    val clickable: Boolean = false,
    val action: ActionHandler? = null
)

/**
 * Field Component (Label-Value pairs)
 */
@Serializable
data class FieldComponent(
    override val id: String,
    override val version: Int,
    val meta: FieldMeta,
    override val properties: ComponentProperties
) : ContentComponent() {
    override val type: String = "field"
}

@Serializable
data class FieldMeta(
    val label: String,
    val valueKey: String,
    val fieldType: FieldType = FieldType.TEXT,
    val displayType: DisplayType = DisplayType.TEXT,
    val format: String? = null,
    val conditionalStyle: ConditionalStyle? = null
)

@Serializable
enum class FieldType {
    TEXT,
    NUMBER,
    DATE,
    TIME,
    DATETIME,
    BOOLEAN,
    CODE,
    OPTION
}

@Serializable
enum class DisplayType {
    TEXT,
    BADGE,
    CHIP,
    ICON,
    PROGRESS
}

/**
 * Text Component
 */
@Serializable
data class TextComponent(
    override val id: String,
    override val version: Int,
    val meta: TextMeta,
    override val properties: ComponentProperties
) : ContentComponent() {
    override val type: String = "text"
}

@Serializable
data class TextMeta(
    val text: String,
    val textContent: TextContent
)

@Serializable
data class TextContent(
    val text: String,
    val textSize: TextSize = TextSize.MEDIUM,
    val fontWeight: FontWeight = FontWeight.NORMAL,
    val textAlign: TextAlign = TextAlign.START
)

@Serializable
enum class TextSize {
    SMALL,
    MEDIUM,
    LARGE,
    EXTRA_LARGE
}

@Serializable
enum class FontWeight {
    LIGHT,
    NORMAL,
    MEDIUM,
    BOLD
}

@Serializable
enum class TextAlign {
    START,
    CENTER,
    END
}

/**
 * Empty State Component
 */
@Serializable
data class EmptyStateComponent(
    override val id: String,
    override val version: Int,
    val meta: EmptyStateMeta,
    override val properties: ComponentProperties
) : ContentComponent() {
    override val type: String = "emptyState"
}

@Serializable
data class EmptyStateMeta(
    val icon: String,
    val message: String,
    val actionButton: ActionComponent? = null
)

/**
 * Action Component (Buttons)
 */
@Serializable
data class ActionComponent(
    override val id: String,
    override val version: Int,
    val meta: ActionMeta,
    override val properties: ComponentProperties
) : ContentComponent() {
    override val type: String = "action"
}

@Serializable
data class ActionMeta(
    val label: String,
    val icon: String? = null,
    val actionType: ActionType = ActionType.PRIMARY,
    val handler: ActionHandler
)

@Serializable
enum class ActionType {
    PRIMARY,
    SECONDARY,
    TEXT,
    ICON
}
