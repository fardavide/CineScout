package cinescout.design.util

import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.Visibility

fun ConstrainScope.visibleIf(condition: Boolean): Visibility =
    if (condition) Visibility.Visible else Visibility.Gone
