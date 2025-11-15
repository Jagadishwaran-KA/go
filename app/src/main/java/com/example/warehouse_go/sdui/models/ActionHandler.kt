package com.example.warehouse_go.sdui.models

import kotlinx.serialization.Serializable

/**
 * Action handler for user interactions
 */
@Serializable
data class ActionHandler(
    val handlerType: HandlerType,
    val action: ActionName,
    val params: Map<String, String> = emptyMap()
)

@Serializable
enum class HandlerType {
    NAVIGATION,
    PROCESSING,
    DIALOG,
    EXTERNAL,
    INPUT
}

@Serializable
enum class ActionName {
    // Navigation actions
    NAVIGATE_TO,
    NAVIGATE_BACK,
    DRILL_DOWN,
    SELECT_ITEM,       // For LIST_DETAIL selection
    CREATE_NEW,
    EDIT,
    
    // Processing actions
    POST,
    DELETE,
    UPDATE,
    REFRESH,
    
    // Dialog actions
    SHOW_DIALOG,
    SHOW_LOOKUP,
    SHOW_ASSIST_EDIT,
    SHOW_FILTERS,
    
    // External actions
    OPEN_URL,
    SHARE,
    CALL,
    EMAIL,
    
    // Input actions
    FILTER,
    SEARCH,
    SORT
}
