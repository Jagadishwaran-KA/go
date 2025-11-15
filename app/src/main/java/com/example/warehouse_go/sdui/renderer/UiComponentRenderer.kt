package com.example.warehouse_go.sdui.renderer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.warehouse_go.sdui.models.*

/**
 * Main extension function to consume and render any UiComponent
 */
@Composable
fun UiComponent.Consume(
    modifier: Modifier = Modifier,
    data: Map<String, Any?> = emptyMap(),
    navController: NavController,
    onAction: (ActionHandler, Map<String, Any?>) -> Unit = { _, _ -> }
) {
    when (this) {
        is PageDefinition -> ConsumePage(
            page = this,
            modifier = modifier,
            navController = navController,
            onAction = onAction
        )
        
        is TextFieldComponent -> ConsumeTextField(
            textField = this,
            modifier = modifier,
            onAction = onAction
        )
        
        is RepeaterComponent -> ConsumeRepeater(
            repeater = this,
            modifier = modifier,
            data = data,
            navController = navController,
            onAction = onAction
        )
        
        is CardComponent -> ConsumeCard(
            card = this,
            modifier = modifier,
            data = data,
            onAction = onAction
        )
        
        is FieldComponent -> ConsumeField(
            field = this,
            modifier = modifier,
            data = data
        )
        
        is TextComponent -> ConsumeText(
            text = this,
            modifier = modifier
        )
        
        is EmptyStateComponent -> ConsumeEmptyState(
            emptyState = this,
            modifier = modifier,
            navController = navController,
            onAction = onAction
        )
        
        is ActionComponent -> ConsumeAction(
            action = this,
            modifier = modifier,
            data = data,
            onAction = onAction
        )
        
        is ContainerComponent -> ConsumeContainer(
            container = this,
            modifier = modifier,
            data = data,
            navController = navController,
            onAction = onAction
        )
        
        else -> ConsumeDefaultUi(
            component = this,
            modifier = modifier
        )
    }
}

/**
 * Render the entire page
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ConsumePage(
    page: PageDefinition,
    modifier: Modifier = Modifier,
    navController: NavController,
    onAction: (ActionHandler, Map<String, Any?>) -> Unit
) {
    val isListDetailLayout = page.layout.type == LayoutType.LIST_DETAIL
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            page.layout.areas.topBar?.let { topBar ->
                TopAppBar(
                    title = {
                        Text(
                            text = topBar.meta.title.text,
                            style = when (topBar.meta.title.textSize) {
                                TextSize.SMALL -> MaterialTheme.typography.titleSmall
                                TextSize.MEDIUM -> MaterialTheme.typography.titleMedium
                                TextSize.LARGE -> MaterialTheme.typography.titleLarge
                                TextSize.EXTRA_LARGE -> MaterialTheme.typography.headlineMedium
                            }
                        )
                    },
                    navigationIcon = {
                        if (topBar.meta.showBackButton) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                    actions = {
                        topBar.meta.actions.forEach { action ->
                            action.Consume(
                                navController = navController,
                                onAction = onAction
                            )
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            page.layout.areas.floatingAction?.let { fab ->
                FloatingActionButton(
                    onClick = {
                        onAction(fab.meta.action, emptyMap())
                    }
                ) {
                    Icon(
                        imageVector = fab.meta.icon.toImageVector(),
                        contentDescription = fab.meta.label
                    )
                }
            }
        }
    ) { innerPadding ->
        if (isListDetailLayout) {
            // Use Material 3 Adaptive ListDetailPaneScaffold
            ConsumeListDetailLayout(
                page = page,
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                onAction = onAction
            )
        } else {
            // Standard single column layout with optional scrolling
            val hasLazyComponents = page.hasLazyComponents()
            val shouldScroll = page.meta.scrollable && !hasLazyComponents
            
            val scrollModifier = if (shouldScroll) {
                Modifier.verticalScroll(rememberScrollState())
            } else {
                Modifier
            }
            
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(page.layout.areas.topBar?.properties?.padding?.toDp() ?: 0.dp)
                    .fillMaxSize()
                    .then(scrollModifier),
                verticalArrangement = Arrangement.spacedBy(
                    page.layout.areas.topBar?.properties?.spacing?.toDp() ?: 0.dp
                )
            ) {
                page.layout.areas.content?.forEach { component ->
                    component.Consume(
                        navController = navController,
                        onAction = onAction
                    )
                }
            }
        }
    }
}

/**
 * Material 3 Adaptive ListDetailPaneScaffold
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ConsumeListDetailLayout(
    page: PageDefinition,
    modifier: Modifier = Modifier,
    navController: NavController,
    onAction: (ActionHandler, Map<String, Any?>) -> Unit
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    
    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    page.layout.areas.masterPane?.forEach { component ->
                        component.Consume(
                            navController = navController,
                            onAction = { handler, data ->
                                // Handle SELECT_ITEM action
                                if (handler.action == ActionName.SELECT_ITEM) {
                                    // Navigate to detail pane
                                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, data)
                                }
                                onAction(handler, data)
                            }
                        )
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                val selectedData = navigator.currentDestination?.content as? Map<*, *>
                
                if (selectedData != null) {
                    // Render detail with selected data
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        page.layout.areas.detailPane?.forEach { component ->
                            component.Consume(
                                navController = navController,
                                data = selectedData as Map<String, Any?>,
                                onAction = onAction
                            )
                        }
                    }
                } else {
                    // Show empty state when no item selected
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        page.layout.areas.detailPane?.forEach { component ->
                            component.Consume(
                                navController = navController,
                                onAction = onAction
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier
    )
}

/**
 * Fallback for unknown component types
 */
@Composable
fun ConsumeDefaultUi(
    component: UiComponent,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Unknown component type: ${component.type}",
        modifier = modifier,
        color = MaterialTheme.colorScheme.error
    )
}

/**
 * Helper function to detect if page contains lazy components
 */
fun PageDefinition.hasLazyComponents(): Boolean {
    val allComponents = mutableListOf<ContentComponent>()
    
    // Collect all components from all areas
    layout.areas.content?.let { allComponents.addAll(it) }
    layout.areas.masterPane?.let { allComponents.addAll(it) }
    layout.areas.detailPane?.let { allComponents.addAll(it) }
    
    return allComponents.any { component ->
        component.isLazyComponent() || 
        (component is ContainerComponent && component.meta.hasLazyArrangement())
    }
}

/**
 * Check if a component is inherently lazy
 */
fun ContentComponent.isLazyComponent(): Boolean {
    return when (this) {
        is RepeaterComponent -> true  // Uses LazyColumn
        is ContainerComponent -> meta.hasLazyArrangement()
        else -> false
    }
}

/**
 * Check if container has lazy arrangement
 */
fun ContainerMeta.hasLazyArrangement(): Boolean {
    return arrangement in listOf(
        com.example.warehouse_go.sdui.models.Arrangement.LAZY_COLUMN,
        com.example.warehouse_go.sdui.models.Arrangement.LAZY_ROW,
        com.example.warehouse_go.sdui.models.Arrangement.GRID,
        com.example.warehouse_go.sdui.models.Arrangement.STAGGERED_GRID
    )
}
