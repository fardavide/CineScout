package entities

/**
 * An entity that can be validated
 *
 * @author Davide Farella
 */
interface Validable<E : ValidationError> {

    val validator: Validator<E>

    companion object {

        /**
         * Validate the entity
         * @return [Either] of [E] and [Validable]
         */
        fun <V : Validable<E>, E : ValidationError> V.validate(): Either<E, V> =
            validator(Either).map { this }
    }
}

/**
 * Complete if validation is successful
 * @throws ValidationException
 *
 * @return `this` [Validable]
 */
fun <V : Validable<E>, E : ValidationError> Either<E, V>.requireValid(): V =
    rightOrNull()
        ?: let { throw ValidationException(leftOrNull()!!) }

/**
 * An [Error] for [Validable]s
 */
interface ValidationError : Error

/**
 * An exception thrown from [requireValid] in case the validation fails
 */
data class ValidationException(val reason: ValidationError) :
    IllegalArgumentException("'requireValid' on Validable thrown with reason: ${reason::class.simpleName}")
