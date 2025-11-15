package com.example.warehouse_go.sdui.models

import kotlinx.serialization.Serializable

/**
 * Root page definition containing metadata and layout structure
 */
@Serializable
data class PageDefinition(
    val id: String,
    val version: Int,
    val meta: PageMeta,
    val layout: PageLayout,
    val dataBinding: DataBinding
) : UiComponent {
    override val type: String = "page"
}

@Serializable
data class PageMeta(
    val title: String,
    val pageType: PageType,
    val generatedAt: String? = null,
    val cachePolicy: CachePolicy = CachePolicy.NO_CACHE,
    val scrollable: Boolean = true,  // JSON-controlled scrolling
    val refreshable: Boolean = false
)

@Serializable
enum class PageType {
    LIST,
    DETAIL,
    FORM,
    DASHBOARD
}

@Serializable
enum class CachePolicy {
    NO_CACHE,
    CACHE_FIRST,
    NETWORK_FIRST
}

@Serializable
data class DataBinding(
    val endpoint: String,
    val method: String = "GET",
    val keyField: String? = null,
    val refreshInterval: Long? = null
)
