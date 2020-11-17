package entities.model

import entities.Either
import entities.RegexMismatchError
import entities.RegexValidator
import entities.Validable
import entities.Validable.Companion.validate
import entities.Validator
import entities.left
import entities.right

typealias Either_EmailAddress = Either<InvalidEmailError, EmailAddress>

/**
 * Entity representing an email address
 * [Validable] by [RegexValidator]
 */
data class EmailAddress private constructor(val s: String) :
    Validable<InvalidEmailError> by (Validator {
        when {
            s.isBlank() -> InvalidEmailError.Empty.left()
            VALIDATION_REGEX.matches(s).not() -> InvalidEmailError.WrongFormat.left()
            else -> EmailAddress(s).right()
        }
    }) {

    companion object {

        operator fun invoke(s: String) = EmailAddress(s).validate()

        @Suppress("MaxLineLength") // Nobody can read it anyway ¯\_(ツ)_/¯
        const val VALIDATION_PATTERN =
            """(?:[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])"""
        val VALIDATION_REGEX = VALIDATION_PATTERN.toRegex(RegexOption.IGNORE_CASE)
    }
}

sealed class InvalidEmailError : RegexMismatchError {
    object Empty : InvalidEmailError()
    object WrongFormat : InvalidEmailError()
}
