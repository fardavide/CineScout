package cinescout.android.startup

import cinescout.android.CineScoutApplicationContext

/**
 * Initialize a list of [Startup]s
 */
context(CineScoutApplicationContext)
fun init(first: Startup, vararg others: Startup) {
    buildList {
        add(first)
        addAll(others)
    }.forEach { it.init() }
}

interface Startup {

    context(CineScoutApplicationContext)
    fun init()
}
