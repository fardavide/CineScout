package client.cli

@Suppress("PropertyName")
interface Palette {

    val Reset get() = "\u001B[0m"

    val black get() = "\u001B[30m"
    val red get() = "\u001B[31m"
    val green get() = "\u001B[32m"
    val yellow get() = "\u001B[33m"
    val blue get() = "\u001B[34m"
    val purple get() = "\u001B[35m"
    val cyan get() = "\u001B[36m"
    val white get() = "\u001B[37m"

    val black_bg get() = "\u001B[40m"
    val reg_bg get() = "\u001B[41m"
    val green_bg get() = "\u001B[42m"
    val yellow_bg get() = "\u001B[43m"
    val blue_bg get() = "\u001B[44m"
    val purple_bg get() = "\u001B[45m"
    val cyan_bg get() = "\u001B[46m"
    val white_bg get() = "\u001B[47m"
}
