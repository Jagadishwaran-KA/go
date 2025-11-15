# Server-Driven UI (SDUI) Design System

## 📋 **Table of Contents**
1. [Overview](#overview)
2. [Design Principles](#design-principles)
3. [Architecture](#architecture)
4. [Component Catalog](#component-catalog)
5. [JSON Schema](#json-schema)
6. [Usage Examples](#usage-examples)
7. [Best Practices](#best-practices)

---

## 🎯 **Overview**

This Server-Driven UI design system enables dynamic UI rendering in Jetpack Compose applications. The backend controls the entire layout structure, while the client passively renders components based on JSON definitions.

### **Key Benefits**
- ✅ **Rapid Iteration**: Update UI without app releases
- ✅ **Cross-Platform Consistency**: Same JSON schema for web, iOS, Android
- ✅ **Centralized Control**: Product managers can modify layouts
- ✅ **Native Performance**: Fully native Compose rendering
- ✅ **Type-Safe**: Kotlin serialization with compile-time safety

---

## 🏛️ **Design Principles**

### **1. Separation of Concerns**
- **UI Definition (this system)**: What to render and where
- **Data API (separate)**: What content to display
- **Rendering Engine**: How to render using Compose

```
┌─────────────────┐      ┌──────────────────┐      ┌─────────────────┐
│ UI Definition   │──────▶│ Rendering Engine │──────▶│ Compose Screen  │
│ (JSON)          │      │ (Kotlin)         │      │ (Visual Output) │
└─────────────────┘      └──────────────────┘      └─────────────────┘
         │                        ▲
         │                        │
         ▼                        │
┌─────────────────┐              │
│ Data API        │──────────────┘
│ (JSON)          │
└─────────────────┘
```

### **2. No Hardcoded Values**
All styling properties use **semantic enum values**:

```json
{
  "properties": {
    "textSize": "MEDIUM",        // ✅ Not "16sp"
    "backgroundColor": "SURFACE", // ✅ Not "#F5F5F5"
    "spacing": "LARGE",          // ✅ Not "24dp"
    "elevation": "LOW"           // ✅ Not "2dp"
  }
}
```

### **3. Component Composition**
Every component can contain other components:

```json
{
  "type": "card",
  "meta": {
    "fields": [
      {"type": "field", ...},
      {"type": "field", ...}
    ]
  }
}
```

### **4. Template-Based Data Binding**
Use `{fieldName}` placeholders for dynamic values:

```json
{
  "id": "{receiptNo}",              // Template key
  "meta": {
    "label": "Receipt No",
    "valueKey": "receiptNo"         // Data binding key
  }
}
```

---

## 🏗️ **Architecture**

### **Folder Structure**
```
sdui/
├── models/               # Kotlin data classes
│   ├── UiComponent.kt
│   ├── PageDefinition.kt
│   ├── Layout.kt
│   ├── ContentComponent.kt
│   ├── ComponentProperties.kt
│   └── ActionHandler.kt
│
├── renderer/             # Composable rendering logic
│   ├── UiComponentRenderer.kt
│   ├── ComponentRenderers.kt
│   └── Extensions.kt
│
├── components/           # Custom reusable components
│
└── schema/               # JSON definitions
    ├── page-schema.json
    ├── warehouse-receipt-list-page.json
    └── sample-data-response.json
```

### **Data Flow**

```kotlin
// 1. Fetch UI Definition from Backend
val pageDefinition = repository.fetchPageDefinition("warehouse_receipt_list")

// 2. Fetch Data from Data API
val data = repository.fetchData("/api/v1/warehouse/receipts")

// 3. Render UI
pageDefinition.Consume(
    data = data,
    navController = navController,
    onAction = { handler, data -> handleAction(handler, data) }
)
```

---

## 📦 **Component Catalog**

### **Base Interface**
All components implement `UiComponent`:

```kotlin
sealed interface UiComponent {
    val id: String        // Unique identifier
    val version: Int      // For backward compatibility
    val type: String      // Component type
}
```

### **Component Types**

| Component | Type | Purpose | Example Use Case |
|-----------|------|---------|------------------|
| `PageDefinition` | `"page"` | Root page container | Full screen definition |
| `TopBarComponent` | `"topBar"` | App bar with title/actions | Navigation header |
| `TextFieldComponent` | `"textField"` | Input field | Search box |
| `RepeaterComponent` | `"repeater"` | Lists/Grids | Receipt list |
| `CardComponent` | `"card"` | Container with elevation | Receipt card |
| `FieldComponent` | `"field"` | Label-value pair | "Receipt No: WR-001" |
| `TextComponent` | `"text"` | Static text | Headers, labels |
| `EmptyStateComponent` | `"emptyState"` | No data view | "No receipts found" |
| `ActionComponent` | `"action"` | Button | "Create New" button |
| `FloatingActionComponent` | `"fab"` | FAB | Filter button |

---

## 📝 **JSON Schema**

### **Page Definition Structure**

```json
{
  "id": "unique_page_id",
  "version": 1,
  "type": "page",
  "meta": {
    "title": "Page Title",
    "pageType": "LIST",
    "cachePolicy": "NETWORK_FIRST"
  },
  "layout": {
    "type": "LIST",
    "areas": {
      "topBar": { /* TopBarComponent */ },
      "content": [ /* Array of ContentComponents */ ],
      "floatingAction": { /* FloatingActionComponent */ }
    }
  },
  "dataBinding": {
    "endpoint": "/api/v1/resource",
    "method": "GET",
    "keyField": "id"
  }
}
```

### **Component Properties Schema**

```json
{
  "properties": {
    "spacing": "NONE | EXTRA_SMALL | SMALL | MEDIUM | LARGE | EXTRA_LARGE",
    "padding": "NONE | EXTRA_SMALL | SMALL | MEDIUM | LARGE | EXTRA_LARGE",
    "elevation": "NONE | LOW | MEDIUM | HIGH",
    "backgroundColor": "PRIMARY | SECONDARY | SURFACE | BACKGROUND | etc.",
    "textColor": "ON_PRIMARY | ON_SECONDARY | ON_SURFACE | etc.",
    "cornerRadius": "NONE | SMALL | MEDIUM | LARGE | FULL",
    "visible": true,
    "enabled": true,
    "fillMaxWidth": false,
    "fillMaxHeight": false
  }
}
```

### **Layout Types (Adaptive Canonical Layout)**

```kotlin
enum class LayoutType {
    LIST_DETAIL,  // Master-detail with side panel
    ROW,          // Horizontal arrangement
    COLUMN,       // Vertical arrangement
    GRID,         // Grid layout
    LIST          // Simple list
}
```

---

## 💡 **Usage Examples**

### **Example 1: Simple Text Field**

```json
{
  "id": "search_field",
  "version": 1,
  "type": "textField",
  "meta": {
    "placeholder": "Search Warehouse Receipt",
    "inputType": "SEARCH",
    "icon": "search",
    "action": {
      "handlerType": "INPUT",
      "action": "FILTER",
      "params": {
        "filterField": "receiptNo"
      }
    }
  },
  "properties": {
    "spacing": "MEDIUM",
    "padding": "MEDIUM",
    "fillMaxWidth": true
  }
}
```

### **Example 2: Dynamic List with Template**

```json
{
  "id": "receipt_list",
  "version": 1,
  "type": "repeater",
  "meta": {
    "layoutType": "COLUMN",
    "itemTemplate": {
      "id": "{receiptNo}",
      "type": "card",
      "meta": {
        "fields": [
          {
            "type": "field",
            "meta": {
              "label": "Receipt No",
              "valueKey": "receiptNo",
              "fieldType": "CODE"
            }
          }
        ],
        "clickable": true,
        "action": {
          "handlerType": "NAVIGATION",
          "action": "DRILL_DOWN",
          "params": {
            "targetPage": "receiptDetail",
            "recordKey": "{receiptNo}"
          }
        }
      }
    },
    "dataSource": {
      "endpoint": "/api/v1/warehouse/receipts",
      "keyField": "receiptNo"
    }
  }
}
```

### **Example 3: Conditional Styling**

```json
{
  "type": "field",
  "meta": {
    "label": "Status",
    "valueKey": "status",
    "conditionalStyle": {
      "conditions": [
        {
          "whenExpression": "status == 'Open'",
          "style": {
            "backgroundColor": "WARNING",
            "textColor": "ON_WARNING",
            "fontWeight": "BOLD"
          }
        },
        {
          "whenExpression": "status == 'Closed'",
          "style": {
            "backgroundColor": "SUCCESS",
            "textColor": "ON_SUCCESS"
          }
        }
      ]
    }
  }
}
```

---

## 🎨 **Rendering in Compose**

### **1. Fetch UI Definition**

```kotlin
@HiltViewModel
class WarehouseReceiptViewModel @Inject constructor(
    private val repository: SduiRepository
) : ViewModel() {

    val pageDefinition: StateFlow<PageDefinition?> = repository
        .fetchPageDefinition("warehouse_receipt_list")
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    
    val receiptData: StateFlow<List<Map<String, Any?>>> = repository
        .fetchReceiptData()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
```

### **2. Render in Composable**

```kotlin
@Composable
fun WarehouseReceiptScreen(
    viewModel: WarehouseReceiptViewModel = hiltViewModel(),
    navController: NavController
) {
    val pageDefinition by viewModel.pageDefinition.collectAsStateWithLifecycle()
    val data by viewModel.receiptData.collectAsStateWithLifecycle()
    
    pageDefinition?.let { page ->
        page.Consume(
            data = mapOf("items" to data),
            navController = navController,
            onAction = { handler, actionData ->
                handleAction(handler, actionData, navController)
            }
        )
    }
}

fun handleAction(
    handler: ActionHandler,
    data: Map<String, Any?>,
    navController: NavController
) {
    when (handler.handlerType) {
        HandlerType.NAVIGATION -> {
            when (handler.action) {
                ActionName.DRILL_DOWN -> {
                    val targetPage = handler.params["targetPage"] ?: return
                    val recordKey = handler.params["recordKey"]
                        ?.resolveTemplateKey(data) ?: return
                    navController.navigate("$targetPage/$recordKey")
                }
                ActionName.NAVIGATE_BACK -> {
                    navController.popBackStack()
                }
                else -> { /* Handle other actions */ }
            }
        }
        HandlerType.INPUT -> {
            when (handler.action) {
                ActionName.FILTER -> {
                    // Trigger filtering logic
                }
                else -> { /* Handle other actions */ }
            }
        }
        else -> { /* Handle other handler types */ }
    }
}
```

---

## ✅ **Best Practices**

### **1. Versioning Strategy**
Always include version in components for backward compatibility:

```json
{
  "version": 1,  // Increment when breaking changes occur
  "id": "my_component"
}
```

In renderer:
```kotlin
when (component.version) {
    1 -> RenderVersion1(component)
    2 -> RenderVersion2(component)
    else -> RenderFallback(component)
}
```

### **2. Data Binding Keys**
Keep `valueKey` separate from `id`:

```json
{
  "id": "{receiptNo}_field",    // Unique UI identifier
  "meta": {
    "valueKey": "receiptNo"     // Data field name
  }
}
```

### **3. Action Handlers**
Always provide params for navigation:

```json
{
  "action": {
    "handlerType": "NAVIGATION",
    "action": "DRILL_DOWN",
    "params": {
      "targetPage": "receiptDetail",
      "recordKey": "{receiptNo}",
      "transition": "slide"
    }
  }
}
```

### **4. Empty States**
Always provide empty state for lists:

```json
{
  "type": "repeater",
  "meta": {
    "emptyState": {
      "type": "emptyState",
      "meta": {
        "icon": "inbox",
        "message": "No items found",
        "actionButton": { /* Create button */ }
      }
    }
  }
}
```

### **5. Error Handling**
Implement fallback rendering:

```kotlin
@Composable
fun UiComponent.Consume(...) {
    when (this) {
        is KnownComponent -> RenderComponent(this)
        else -> ConsumeDefaultUi(this)  // ← Fallback
    }
}
```

---

## 🚀 **Getting Started**

### **Step 1: Add Dependencies**

```kotlin
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
}
```

### **Step 2: Configure Plugin**

```kotlin
plugins {
    kotlin("plugin.serialization") version "2.0.21"
}
```

### **Step 3: Create Repository**

```kotlin
interface SduiRepository {
    fun fetchPageDefinition(pageId: String): Flow<PageDefinition>
    fun fetchReceiptData(): Flow<List<Map<String, Any?>>>
}
```

### **Step 4: Render UI**

```kotlin
@Composable
fun MyScreen() {
    val page = /* fetch page definition */
    page.Consume(...)
}
```

---

## 📚 **Additional Resources**

- [Kotlin Serialization Docs](https://kotlinlang.org/docs/serialization.html)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [JSON Schema Specification](https://json-schema.org/)

---

## 🔄 **Migration from Static UI**

### **Before (Static UI)**
```kotlin
@Composable
fun ReceiveInfoScreen() {
    Scaffold(
        topBar = { /* Hardcoded top bar */ }
    ) {
        LazyColumn {
            items(receipts) { receipt ->
                ReceiptCard(receipt)  // ← Hardcoded layout
            }
        }
    }
}
```

### **After (SDUI)**
```kotlin
@Composable
fun ReceiveInfoScreen() {
    val pageDefinition by viewModel.pageDefinition.collectAsState()
    pageDefinition?.Consume(...)  // ← Dynamic layout from server
}
```

---

## 📊 **Performance Considerations**

1. **Caching**: Use `cachePolicy` in page meta
2. **Lazy Loading**: Repeater components use `LazyColumn` automatically
3. **Key Stability**: Template keys ensure efficient recomposition
4. **Serialization**: Kotlinx.serialization is compile-time optimized

---

**Created**: November 15, 2025  
**Version**: 1.0  
**License**: MIT
