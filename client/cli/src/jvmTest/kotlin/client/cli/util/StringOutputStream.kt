package client.cli.util

import java.io.OutputStream

/**
 * Use this for spy the output from the App.
 * @see System.out
 * @see System.err
 * ```
val stringStream = StringOutputStream()
System.setOut(PrintStream(stringStream))
val out = stringStream.output
 *```
 */
class StringOutputStream : OutputStream() {

    val output get() = String(ba).trim()
    private var ba = ByteArray(0)

    override fun write(b: Int) {
        ba += b.toByte()
    }
}
