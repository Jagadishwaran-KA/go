package com.example.warehouse_go.sdui.models

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

/**
 * Base interface for all UI components in the SDUI system.
 * Every renderable component must implement this interface.
 */
@Stable
@Serializable
sealed interface UiComponent {
    val id: String
    val version: Int
    val type: String
}
