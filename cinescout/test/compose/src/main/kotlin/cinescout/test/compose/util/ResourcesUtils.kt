/*
 * Copyright (c) 2022 Proton Technologies AG
 * This file is part of Proton Technologies AG and Proton Mail.
 *
 * Proton Mail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Proton Mail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Proton Mail. If not, see <https://www.gnu.org/licenses/>.
 */

package cinescout.test.compose.util

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.test.core.app.ApplicationProvider
import cinescout.resources.TextRes

fun getString(@StringRes resId: Int): String = context.getString(resId)

fun getString(@StringRes resId: Int, vararg formatArgs: Any): String =
    String.format(getString(resId), *formatArgs)

fun getPluralString(
    @PluralsRes resId: Int,
    quantity: Int,
    vararg formatArgs: Any
): String = context.resources.getQuantityString(resId, quantity, *formatArgs)

fun getString(textRes: TextRes): String = when (textRes) {
    is TextRes.Plain -> textRes.value
    is TextRes.Resource -> getString(textRes.resId)
    is TextRes.ResourceWithArgs -> getString(textRes.resId, *textRes.args.toTypedArray())
    is TextRes.PluralResourceWithArgs -> getPluralString(textRes.resId, textRes.quantity, *textRes.args.toTypedArray())
}

private val context: Context
    get() = ApplicationProvider.getApplicationContext()
