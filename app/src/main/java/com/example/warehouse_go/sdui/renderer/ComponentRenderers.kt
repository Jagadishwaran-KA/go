package com.example.warehouse_go.sdui.renderer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.warehouse_go.sdui.models.*

/**
 * Render TextField Component
 */
@Composable
fun ConsumeTextField(
    textField: TextFieldComponent,
    modifier: Modifier = Modifier,
    onAction: (ActionHandler, Map<String, Any?>) -> Unit
) {
    var value by remember { mutableStateOf("") }
    
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            value = newValue
            textField.meta.action?.let { action ->
                onAction(action, mapOf("query" to newValue))
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(textField.properties.padding.toDp()),
        placeholder = {
            Text(
                text = textField.meta.placeholder,
                style = MaterialTheme.typography.titleSmall
            )
        },
        leadingIcon = textField.meta.icon?.let { icon ->
            {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = icon
                )
            }
        },
        singleLine = true
    )
}

/**
 * Render Repeater Component (Lists)
 */
@Composable
fun ConsumeRepeater(
    repeater: RepeaterComponent,
    modifier: Modifier = Modifier,
    data: Map<String, Any?>,
    navController: NavController,
    onAction: (ActionHandler, Map<String, Any?>) -> Unit
) {
    // In real implementation, fetch data from dataSource.endpoint
    // For now, assume data is passed in
    val items = data["items"] as? List<Map<String, Any?>> ?: emptyList()
    
    if (items.isEmpty()) {
        repeater.meta.emptyState?.let { emptyState ->
            emptyState.Consume(
                navController = navController,
                onAction = onAction
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(repeater.properties.padding.toDp()),
            verticalArrangement = Arrangement.spacedBy(repeater.properties.spacing.toDp())
        ) {
            items(
                items = items,
                key = { item ->
                    // Resolve template key
                    val keyValue = item[repeater.meta.dataSource.keyField] as? String ?: ""
                    repeater.meta.itemTemplate.id.replace("{${repeater.meta.dataSource.keyField}}", keyValue)
                }
            ) { item ->
                repeater.meta.itemTemplate.Consume(
                    data = item,
                    navController = navController,
                    onAction = onAction
                )
            }
        }
    }
}

/**
 * Render Card Component
 */
@Composable
fun ConsumeCard(
    card: CardComponent,
    modifier: Modifier = Modifier,
    data: Map<String, Any?>,
    onAction: (ActionHandler, Map<String, Any?>) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (card.meta.clickable && card.meta.action != null) {
                    Modifier.clickable {
                        onAction(card.meta.action, data)
                    }
                } else {
                    Modifier
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = card.properties.backgroundColor.toColor(),
            contentColor = card.properties.textColor.toColor()
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = card.properties.elevation.toElevation()
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(card.properties.padding.toDp()),
            verticalArrangement = Arrangement.spacedBy(card.properties.spacing.toDp())
        ) {
            card.meta.fields.forEach { field ->
                field.Consume(
                    data = data,
                    navController = NavController(androidx.compose.ui.platform.LocalContext.current),
                    onAction = onAction
                )
            }
        }
    }
}

/**
 * Render Field Component (Label-Value pairs)
 */
@Composable
fun ConsumeField(
    field: FieldComponent,
    modifier: Modifier = Modifier,
    data: Map<String, Any?>
) {
    val value = data[field.meta.valueKey]?.toString() ?: ""
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = field.meta.label,
            style = MaterialTheme.typography.bodyMedium,
            color = field.properties.textColor.toColor()
        )
        
        when (field.meta.displayType) {
            DisplayType.TEXT -> {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = field.properties.textColor.toColor()
                )
            }
            DisplayType.BADGE -> {
                // Implement badge rendering
                Text(
                    text = value,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            else -> {
                Text(text = value)
            }
        }
    }
}

/**
 * Render Text Component
 */
@Composable
fun ConsumeText(
    text: TextComponent,
    modifier: Modifier = Modifier
) {
    Text(
        text = text.meta.text,
        modifier = modifier,
        style = when (text.meta.textContent.textSize) {
            TextSize.SMALL -> MaterialTheme.typography.bodySmall
            TextSize.MEDIUM -> MaterialTheme.typography.bodyMedium
            TextSize.LARGE -> MaterialTheme.typography.bodyLarge
            TextSize.EXTRA_LARGE -> MaterialTheme.typography.headlineMedium
        },
        fontWeight = when (text.meta.textContent.fontWeight) {
            FontWeight.LIGHT -> androidx.compose.ui.text.font.FontWeight.Light
            FontWeight.NORMAL -> androidx.compose.ui.text.font.FontWeight.Normal
            FontWeight.MEDIUM -> androidx.compose.ui.text.font.FontWeight.Medium
            FontWeight.BOLD -> androidx.compose.ui.text.font.FontWeight.Bold
        },
        color = text.properties.textColor.toColor()
    )
}

/**
 * Render Empty State Component
 */
@Composable
fun ConsumeEmptyState(
    emptyState: EmptyStateComponent,
    modifier: Modifier = Modifier,
    navController: NavController,
    onAction: (ActionHandler, Map<String, Any?>) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(emptyState.properties.padding.toDp()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = emptyState.meta.icon.toImageVector(),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = emptyState.meta.message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        emptyState.meta.actionButton?.let { button ->
            Spacer(modifier = Modifier.height(24.dp))
            button.Consume(
                navController = navController,
                onAction = onAction
            )
        }
    }
}

/**
 * Render Action Component (Buttons)
 */
@Composable
fun ConsumeAction(
    action: ActionComponent,
    modifier: Modifier = Modifier,
    data: Map<String, Any?> = emptyMap(),
    onAction: (ActionHandler, Map<String, Any?>) -> Unit
) {
    androidx.compose.material3.Button(
        onClick = { onAction(action.meta.handler, data) },
        modifier = modifier,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = action.properties.backgroundColor.toColor(),
            contentColor = action.properties.textColor.toColor()
        )
    ) {
        action.meta.icon?.let { icon ->
            Icon(
                imageVector = icon.toImageVector(),
                contentDescription = null
            )
        }
        Text(text = action.meta.label)
    }
}

/**
 * Render Container Component (Flexible Layouts)
 */
@Composable
fun ConsumeContainer(
    container: ContainerComponent,
    modifier: Modifier = Modifier,
    data: Map<String, Any?> = emptyMap(),
    navController: NavController,
    onAction: (ActionHandler, Map<String, Any?>) -> Unit
) {
    val spacing = container.meta.spacing.toDp()
    val padding = container.properties.padding.toDp()
    
    when (container.meta.arrangement) {
        Arrangement.COLUMN -> {
            val scrollModifier = if (container.meta.scrollable) {
                Modifier.verticalScroll(rememberScrollState())
            } else {
                Modifier
            }
            
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .then(scrollModifier),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(spacing),
                horizontalAlignment = when (container.meta.horizontalAlignment) {
                    HorizontalAlignment.START -> Alignment.Start
                    HorizontalAlignment.CENTER -> Alignment.CenterHorizontally
                    HorizontalAlignment.END -> Alignment.End
                }
            ) {
                container.meta.children.forEach { child ->
                    child.Consume(
                        data = data,
                        navController = navController,
                        onAction = onAction
                    )
                }
            }
        }
        
        Arrangement.ROW -> {
            val scrollModifier = if (container.meta.scrollable) {
                Modifier.horizontalScroll(rememberScrollState())
            } else {
                Modifier
            }
            
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .then(scrollModifier),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(spacing),
                verticalAlignment = when (container.meta.verticalAlignment) {
                    VerticalAlignment.TOP -> Alignment.Top
                    VerticalAlignment.CENTER -> Alignment.CenterVertically
                    VerticalAlignment.BOTTOM -> Alignment.Bottom
                }
            ) {
                container.meta.children.forEach { child ->
                    child.Consume(
                        data = data,
                        navController = navController,
                        onAction = onAction
                    )
                }
            }
        }
        
        Arrangement.GRID -> {
            val columns = container.meta.columns ?: 2
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(padding),
                contentPadding = PaddingValues(spacing),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(spacing),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(spacing)
            ) {
                items(container.meta.children) { child ->
                    child.Consume(
                        data = data,
                        navController = navController,
                        onAction = onAction
                    )
                }
            }
        }
        
        Arrangement.LAZY_COLUMN -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(padding),
                contentPadding = PaddingValues(spacing),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(spacing)
            ) {
                items(container.meta.children) { child ->
                    child.Consume(
                        data = data,
                        navController = navController,
                        onAction = onAction
                    )
                }
            }
        }
        
        Arrangement.LAZY_ROW -> {
            LazyRow(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(padding),
                contentPadding = PaddingValues(spacing),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(spacing)
            ) {
                items(container.meta.children) { child ->
                    child.Consume(
                        data = data,
                        navController = navController,
                        onAction = onAction
                    )
                }
            }
        }
        
        Arrangement.STAGGERED_GRID -> {
            val columns = container.meta.columns ?: 2
            
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(columns),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(padding),
                contentPadding = PaddingValues(spacing),
                verticalItemSpacing = spacing,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(spacing)
            ) {
                items(container.meta.children) { child ->
                    child.Consume(
                        data = data,
                        navController = navController,
                        onAction = onAction
                    )
                }
            }
        }
    }
}
