package com.example.warehouse_go.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


object AppButtons{

    @Composable
    fun AppOutlinedButton(modifier: Modifier = Modifier, label: String, icon: ImageVector? = null, enabled: Boolean = true,colors: ButtonColors = ButtonDefaults.outlinedButtonColors(), click: () -> Unit) {
        OutlinedButton(
            onClick = click,
            enabled = enabled,
            shape = RoundedCornerShape(8.dp),
            modifier = modifier.padding(4.dp),
            colors = colors
        ) {
            icon?.let {
                    it -> Icon(imageVector = it, contentDescription = label)
            }
            Text(
                label,
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(4.dp),
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    fun AppFilledButton(modifier: Modifier = Modifier, label: String, enabled: Boolean = true,buttonColors: ButtonColors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary), click: () -> Unit) {
        Button(
            onClick = click,
            enabled = enabled,
            colors = buttonColors,
            shape = RoundedCornerShape(8.dp),
            modifier = modifier.padding(4.dp)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(4.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}


object AppTextField{

    @Composable
    fun AppOutlinedTextField(
        modifier: Modifier = Modifier,
        label: String,
        value: String,
        placeholder: String? = null,
        suffixIcon: ImageVector? = null,
        enabled: Boolean = true,
        onValueChange: (String) -> Unit,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label, style = MaterialTheme.typography.labelLarge) },
            placeholder = placeholder?.let {
                { Text(text = it, style = MaterialTheme.typography.labelMedium) }
            },
            trailingIcon = suffixIcon?.let {
                { Icon(imageVector = it, contentDescription = label) }
            },
            singleLine = true,
            enabled = enabled,
            modifier = modifier
                .fillMaxWidth()
                .padding(6.dp)
        )
    }

    @Composable
    fun InputTextField(
        modifier: Modifier = Modifier,
        label: String,
        value: String = "",
        placeholder: String? = null,
        leadingIcon: ImageVector? = null,
        trailingIcon: ImageVector? = null,
        onValueChange: (String) -> Unit
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = placeholder ?: "") },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            ),
            leadingIcon = leadingIcon?.let { icon -> { Icon(imageVector = icon, contentDescription = placeholder) } },
            trailingIcon = trailingIcon?.let{icon -> {Icon(imageVector = icon, contentDescription = placeholder)} },
            maxLines = 1,
            label = { Text(label) },
            modifier = modifier
        )
    }
}


object LayoutHelpers{

    @Composable
    fun HorizontalDivider(modifier: Modifier = Modifier) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.tertiary,
            modifier = modifier,
        )
    }

    @Composable
    fun CenteredSpacer(
        modifier: Modifier = Modifier,
        height: Dp = 16.dp,
        content: @Composable () -> Unit
    ) {
        Spacer(modifier = Modifier.height(height))
        Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            content()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppTopBar(modifier: Modifier = Modifier, title:@Composable () -> Unit, actions: @Composable (RowScope.() -> Unit) = {}) {
        TopAppBar(
            title = title,
            modifier = modifier ,
            actions = actions,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )

        )
    }

    @Composable
    fun LabelValueRow(label: String, value: String, modifier: Modifier = Modifier) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                label,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                value,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    @Composable
    fun AppCard(modifier: Modifier = Modifier,colors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
        content: () -> Unit ) {
        Card(
            colors = colors,
            shape = RoundedCornerShape(10.dp),
            modifier = modifier.fillMaxWidth()
        ) {
            content()
        }
    }
}






