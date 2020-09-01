package entities.util

/**
 * @param i must be between 0 and 100
 */
data class Percentage(val i: Int) {

    init {
        require(i in 0..100)
    }
}

/**
 * @return [Percentage] for given [number]
 */
fun Percentage(number: Number) =
    Percentage(number.toInt())

/**
 * @return [Percentage] for receiver [Number]
 */
val Number.percent get() = Percentage(this)

/**
 * Return opposite of the current percentage
 * Example `` -Percentage(80) // = percentage(20) ``
 */
operator fun Percentage.unaryMinus() =
    Percentage(100 - i)


operator fun Float.times(percentage: Percentage) =
    this * percentage.i / 100
