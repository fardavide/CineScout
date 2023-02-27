package cinescout.design.model

import cinescout.design.ImageRes
import cinescout.design.TextRes

data class NavigationItem(
    val label: TextRes,
    val icon: ImageRes,
    val isSelected: Boolean,
    val onClick: () -> Unit
)
