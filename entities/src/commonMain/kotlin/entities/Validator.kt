package entities

typealias Validator<E> = Either.Companion.() -> Either<E, Validable<E>>

/**
 * Creates a [Validator] using a lambda that validate the entity
 *
 * The lambda can
 * * throw exception
 *   @see require
 *   ```
class PositiveNumber(val number: Int) : Validable by Validator({
    conditionally(number > 0, { PositiveNumber(number) })
})
 *   ```
 */
fun <E : ValidationError> Validator(validator: Validator<E>) = object : Validable<E> {
    override val validator = validator
}

object BlankStringError : ValidationError

/**
 * [Validator] that accepts only strings that are not blank
 */
fun <V : Validable<BlankStringError>> NotBlankStringValidator(
    constructor: (String) -> V,
    field: String
) = Validator {
    conditionally(field.isNotBlank(), { BlankStringError }, { constructor(field) })
}

interface RegexMismatchError : ValidationError

/**
 * [Validator] that validate using a [Regex]
 * @param field [String] field to validate
 */
fun <E : RegexMismatchError, V : Validable<E>> RegexValidator(
    constructor: (String) -> V,
    field: String,
    regex: Regex,
    error: E
) = Validator {
    conditionally(regex.matches(field), { error }, { constructor(field) })
}

/**
 * [Validator] that validate using a [Regex]
 * @param field [String] field to validate
 */
fun <E : RegexMismatchError, V : Validable<RegexMismatchError>> RegexValidator(
    constructor: (String) -> V,
    field: String,
    regex: String,
    error: E
) = RegexValidator(constructor, field, regex.toRegex(), error)
