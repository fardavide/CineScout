package client.cli

@Suppress("PropertyName")
interface Palette {

    val Reset get() = "\u001B[0m".mayColorize()

    val black get() = "\u001B[30m".mayColorize()
    val red get() = "\u001B[31m".mayColorize()
    val green get() = "\u001B[32m".mayColorize()
    val yellow get() = "\u001B[33m".mayColorize()
    val blue get() = "\u001B[34m".mayColorize()
    val purple get() = "\u001B[35m".mayColorize()
    val cyan get() = "\u001B[36m".mayColorize()
    val white get() = "\u001B[37m".mayColorize()

    val black_bg get() = "\u001B[40m".mayColorize()
    val reg_bg get() = "\u001B[41m".mayColorize()
    val green_bg get() = "\u001B[42m".mayColorize()
    val yellow_bg get() = "\u001B[43m".mayColorize()
    val blue_bg get() = "\u001B[44m".mayColorize()
    val purple_bg get() = "\u001B[45m".mayColorize()
    val cyan_bg get() = "\u001B[46m".mayColorize()
    val white_bg get() = "\u001B[47m".mayColorize()
}

private fun String.mayColorize() =
    if (theme.colorized) this
    else ""
