# LIST_DETAIL Layout - Complete Guide

## 🎯 **What is LIST_DETAIL?**

LIST_DETAIL is an **Adaptive Canonical Layout** that automatically adjusts based on screen size:

### **📱 Phone (Compact Screen)**
- Shows **master pane only** (list of items)
- When item is clicked → Navigate to **separate detail screen**
- Traditional mobile navigation pattern

### **📟 Tablet/Desktop (Expanded Screen)**
- Shows **two panes side-by-side**:
  - **Left pane**: Master list (360dp fixed width)
  - **Right pane**: Detail view (fills remaining space)
- When item is clicked → Detail updates **in the same screen**
- No navigation, just state update

---

## 🏗️ **JSON Structure**

### **Main Page JSON** (warehouse-receipt-list-detail-page.json)

```json
{
  "layout": {
    "type": "LIST_DETAIL",  // ← Key: Enables adaptive layout
    "areas": {
      "topBar": { /* TopBarComponent */ },
      
      "masterPane": [  // ← Left pane (or full screen on phone)
        { "type": "textField", ... },  // Search box
        { "type": "repeater", ... }    // List of items
      ],
      
      "detailPane": [  // ← Right pane (or separate screen on phone)
        { "type": "emptyState", ... }  // Default: "Select an item"
      ],
      
      "floatingAction": { /* FAB */ }
    }
  }
}
```

### **Key Differences from Regular LIST Layout**

| Property | LIST Layout | LIST_DETAIL Layout |
|----------|-------------|-------------------|
| `areas.content` | ✅ Used | ❌ Not used |
| `areas.masterPane` | ❌ Not used | ✅ Used (left side) |
| `areas.detailPane` | ❌ Not used | ✅ Used (right side) |
| Adaptive Behavior | ❌ No | ✅ Yes (responds to screen size) |

---

## 🔄 **How Selection Works**

### **1. Master Pane Item Click**

When user clicks an item in the list:

```json
{
  "type": "card",
  "meta": {
    "clickable": true,
    "action": {
      "handlerType": "NAVIGATION",
      "action": "SELECT_ITEM",  // ← Special action for LIST_DETAIL
      "params": {
        "targetPane": "detailPane",
        "recordKey": "{receiptNo}"
      }
    }
  }
}
```

### **2. Renderer Handles It Automatically**

**On Tablet/Desktop:**
```kotlin
if (handler.action == ActionName.SELECT_ITEM) {
    selectedDetailData = data  // ← Update state (no navigation)
    // Detail pane re-renders with new data
}
```

**On Phone:**
```kotlin
if (handler.action == ActionName.SELECT_ITEM) {
    val recordKey = handler.params["recordKey"]
    navController.navigate("detail/$recordKey")  // ← Navigate to separate screen
}
```

---

## 📄 **Detail Fragment JSON** (warehouse-receipt-detail-fragment.json)

This is a **separate JSON** fetched when:
- On **phone**: User navigates to detail screen
- On **tablet**: Item is selected (optional, can load dynamically)

```json
{
  "id": "warehouse_receipt_detail_{receiptNo}",
  "type": "detailFragment",  // ← Not a full page
  "meta": {
    "parentPage": "warehouse_receipt_list_detail",
    "targetPane": "detailPane"
  },
  "content": [
    { "type": "card", "meta": { "title": "Vendor Information" } },
    { "type": "card", "meta": { "title": "Receipt Lines" } },
    { "type": "card", "meta": { "title": "Actions" } }
  ]
}
```

---

## 🎬 **Complete Flow Example**

### **Scenario**: User clicks on "WR-001" receipt

#### **Phone (Compact)**
```
1. User sees master pane (list)
   ┌─────────────────────┐
   │ Search: [         ] │
   │ ┌─────────────────┐ │
   │ │ WR-001          │ │ ← User clicks here
   │ │ Vendor: ABC     │ │
   │ │ Status: Open    │ │
   │ └─────────────────┘ │
   └─────────────────────┘

2. Renderer detects SELECT_ITEM action
   → navController.navigate("receiptDetail/WR-001")

3. New screen shows detail fragment
   ┌─────────────────────┐
   │ ← Receipt Details   │
   │ ─────────────────── │
   │ WR-001              │
   │ Status: Open        │
   │ ─────────────────── │
   │ Vendor Information  │
   │ Receipt Lines       │
   │ Actions             │
   └─────────────────────┘
```

#### **Tablet (Expanded)**
```
1. User sees two-pane layout
   ┌─────────────┬────────────────────────┐
   │ Search:     │ Select a receipt       │
   │ [         ] │ to view details        │
   │ ┌─────────┐ │                        │
   │ │ WR-001  │ │ ← User clicks          │
   │ │ ABC     │ │                        │
   │ │ Open    │ │                        │
   │ └─────────┘ │                        │
   └─────────────┴────────────────────────┘

2. Renderer detects SELECT_ITEM action
   → selectedDetailData = {receiptNo: "WR-001", ...}

3. Detail pane updates (NO navigation)
   ┌─────────────┬────────────────────────┐
   │ Search:     │ WR-001                 │
   │ [         ] │ Status: Open           │
   │ ┌─────────┐ │ ───────────────────── │
   │ │ WR-001  │ │ Vendor Information    │
   │ │ ABC     │ │ Receipt Lines         │
   │ │ Open    │ │ Actions               │
   │ └─────────┘ │                        │
   └─────────────┴────────────────────────┘
```

---

## 💻 **Implementation Details**

### **1. Adaptive Detection**

The renderer automatically detects screen size:

```kotlin
val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
val useTabletLayout = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED

if (isListDetailLayout && useTabletLayout) {
    ConsumeListDetailLayout(...)  // Two-pane
} else if (isListDetailLayout) {
    ConsumeMasterPane(...)        // Single pane
}
```

### **2. Two-Pane Rendering**

```kotlin
Row(modifier = modifier.fillMaxSize()) {
    // Left: Master Pane (360dp)
    Column(modifier = Modifier.width(360.dp)) {
        masterPane?.forEach { it.Consume(...) }
    }
    
    HorizontalDivider(...)
    
    // Right: Detail Pane (remaining space)
    Box(modifier = Modifier.weight(1f)) {
        if (selectedDetailData != null) {
            detailPane?.forEach { it.Consume(data = selectedDetailData) }
        } else {
            // Show "Select an item" message
        }
    }
}
```

### **3. State Management**

```kotlin
var selectedDetailData by remember { mutableStateOf<Map<String, Any?>?>(null) }

// When item is clicked
onAction = { handler, data ->
    if (handler.action == ActionName.SELECT_ITEM) {
        selectedDetailData = data  // ← Triggers recomposition
    }
}
```

---

## ✅ **Best Practices**

### **1. Always Provide Empty State**

```json
{
  "detailPane": [
    {
      "type": "emptyState",
      "meta": {
        "icon": "info",
        "message": "Select a receipt to view details",
        "hideActionButton": true
      }
    }
  ]
}
```

### **2. Use Consistent Width for Master Pane**

- **Recommended**: 360dp (standard phone width)
- **Minimum**: 280dp
- **Maximum**: 400dp

### **3. Handle No Selection Gracefully**

```json
{
  "type": "action",
  "meta": {
    "label": "Post Receipt",
    "conditionalVisibility": {
      "whenExpression": "receiptNo != null"  // Only show when item selected
    }
  }
}
```

### **4. Optimize Data Fetching**

**Master List**: Fetch minimal data (ID, name, status)
```json
{
  "dataSource": {
    "endpoint": "/api/v1/warehouse/receipts?$select=receiptNo,vendorName,status"
  }
}
```

**Detail View**: Fetch complete data only when selected
```json
{
  "dataBinding": {
    "endpoint": "/api/v1/warehouse/receipts/{receiptNo}?$expand=lines,vendor"
  }
}
```

---

## 🚀 **Usage in Your App**

### **Step 1: Create Repository Method**

```kotlin
interface SduiRepository {
    fun fetchListDetailPage(pageId: String): Flow<PageDefinition>
    fun fetchDetailFragment(receiptNo: String): Flow<DetailFragment>
}
```

### **Step 2: ViewModel**

```kotlin
@HiltViewModel
class ReceiptListViewModel @Inject constructor(
    private val repository: SduiRepository
) : ViewModel() {
    
    val pageDefinition = repository
        .fetchListDetailPage("warehouse_receipt_list_detail")
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
    
    fun fetchDetailFragment(receiptNo: String) = repository
        .fetchDetailFragment(receiptNo)
}
```

### **Step 3: Composable**

```kotlin
@Composable
fun ReceiptListScreen(
    viewModel: ReceiptListViewModel = hiltViewModel(),
    navController: NavController
) {
    val page by viewModel.pageDefinition.collectAsStateWithLifecycle()
    
    page?.Consume(
        navController = navController,
        onAction = { handler, data -> 
            // Handle actions (the renderer handles SELECT_ITEM automatically)
            when (handler.action) {
                ActionName.POST -> viewModel.postReceipt(data)
                ActionName.DELETE -> viewModel.deleteReceipt(data)
                else -> { /* Other actions */ }
            }
        }
    )
}
```

---

## 📊 **Comparison: LIST vs LIST_DETAIL**

| Feature | LIST | LIST_DETAIL |
|---------|------|-------------|
| **Phone Behavior** | Single column list | Single column list |
| **Tablet Behavior** | Single column list (wasted space) | Two-pane master-detail |
| **Navigation on Item Click** | Navigate to new screen | No navigation (update pane) |
| **JSON Structure** | `areas.content` | `areas.masterPane` + `areas.detailPane` |
| **Best For** | Simple lists, feeds | Complex items with detail views |
| **Example Use Cases** | Notifications, logs | Emails, receipts, orders |

---

## 🎯 **When to Use LIST_DETAIL**

✅ **Use LIST_DETAIL when:**
- Items have **substantial detail** that needs a full view
- Users frequently **compare** items while browsing
- Tablet users would benefit from **side-by-side** view
- You want **optimized tablet experience**

❌ **Don't use LIST_DETAIL when:**
- Items are simple (e.g., settings menu)
- Detail view is very short
- All info fits in the list item itself
- Navigation makes more sense (e.g., multi-step flow)

---

**That's it!** Your SDUI system now supports adaptive LIST_DETAIL layouts that work beautifully on both phones and tablets. 🎉
