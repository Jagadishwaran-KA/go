import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.random.Random


data class FeatureCard(
    val name: String,
    val count: Int,
    val icon: ImageVector,
    val color: Color
)


val featureCards = arrayOf(
    FeatureCard(
        name = "Receive",
        count = Random.nextInt(1, 50),
        icon = Icons.Filled.Inventory,
        color = Color(0xFFED8B48) // #ed8b48
    ),
    FeatureCard(
        name = "Put Away",
        count = Random.nextInt(1, 50),
        icon = Icons.Filled.MoveToInbox,
        color = Color(0xFFBF5148) // #bf5148
    ),
    FeatureCard(
        name = "Pick",
        count = Random.nextInt(1, 50),
        icon = Icons.Filled.ShoppingCart,
        color = Color(0xFF39824B) // #39824b
    ),
    FeatureCard(
        name = "Production",
        count = Random.nextInt(1, 50),
        icon = Icons.Filled.Build,
        color = Color(0xFF395C92) // #395c92
    ),
    FeatureCard(
        name = "Inventory Movement",
        count = Random.nextInt(1, 50),
        icon = Icons.Filled.SwapHoriz,
        color = Color(0xFF494F5A) // #494f5a
    ),
    FeatureCard(
        name = "Planned Movement",
        count = Random.nextInt(1, 50),
        icon = Icons.Filled.Schedule,
        color = Color(0xFF39824B) // #39824b
    ),
    FeatureCard(
        name = "Queue",
        count = Random.nextInt(1, 50),
        icon = Icons.Filled.Queue,
        color = Color(0xFFED8B48) // #ed8b48
    ),
    FeatureCard(
        name = "Inventory",
        count = Random.nextInt(1, 50),
        icon = Icons.Filled.Inventory,
        color = Color(0xFF316B80) // #316b80
    )
)
