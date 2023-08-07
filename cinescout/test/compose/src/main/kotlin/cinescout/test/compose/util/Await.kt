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

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.SemanticsNodeInteraction
import kotlin.time.Duration

context(ComposeUiTest)
fun <T : Any> T.await(duration: Duration): T =
    await(milliseconds = duration.inWholeMilliseconds)

context(ComposeUiTest)
fun <T : Any> T.await(milliseconds: Long): T = try {
    also { waitUntil(timeoutMillis = milliseconds) { false } }
} catch (e: Throwable) {
    this
}

context(ComposeUiTest)
fun SemanticsNodeInteraction.awaitDisplayed(): SemanticsNodeInteraction =
    also { waitUntil(timeoutMillis = Timeout) { nodeIsDisplayed(this) } }

context(ComposeUiTest)
fun SemanticsNodeInteraction.awaitHidden(): SemanticsNodeInteraction =
    also { waitUntil(timeoutMillis = Timeout) { nodeIsNotDisplayed(this) } }

private const val Timeout = 3000L
