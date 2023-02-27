package cinescout.design.model

import cinescout.design.ImageRes
import cinescout.design.TextRes

data class NavigationItem(
    val label: TextRes,
    val icon: ImageRes,
    val selectedIcon: ImageRes = icon,
    val isSelected: Boolean,
    val onClick: () -> Unit
)
