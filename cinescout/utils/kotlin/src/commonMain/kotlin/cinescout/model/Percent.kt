package cinescout.model

@JvmInline
value class Percent internal constructor(val value: Number)

val Number.percent: Percent
    get() {
        check(this in 0..100) { "Invalid percent: $this" }
        return Percent(this)
    }
