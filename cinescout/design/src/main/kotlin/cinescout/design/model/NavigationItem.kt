package cinescout.design.model

import cinescout.resources.ImageRes
import cinescout.resources.TextRes

data class NavigationItem(
    val label: TextRes,
    val icon: ImageRes,
    val selectedIcon: ImageRes = icon,
    val isSelected: Boolean,
    val onClick: () -> Unit
)
