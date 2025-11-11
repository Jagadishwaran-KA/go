package com.example.warehouse_go


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.warehouse_go.models.FeatureCard

@Composable
fun DashBoard(featureCard: FeatureCard,modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.size(160.dp),
        colors = CardDefaults.cardColors(containerColor = featureCard.color),
        shape = RoundedCornerShape(12.dp),
    ){
        Text(featureCard.count.toString(), textAlign = TextAlign.End, style = MaterialTheme.typography.titleLarge ,modifier = Modifier.fillMaxWidth().padding(8.dp), color = Color.White)
        Icon(
            imageVector = featureCard.icon,
            contentDescription = featureCard.name,
            modifier = Modifier
                       .size(34.dp).align(Alignment.CenterHorizontally),
            tint = Color.White,
        )
        Spacer(Modifier.height(6.dp))
        Text(featureCard.name, textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge,modifier = Modifier.fillMaxWidth().padding(8.dp),color = Color.White)
    }
}