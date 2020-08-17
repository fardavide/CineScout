package client.cli.util

import java.io.OutputStream

class StringOutputStream : OutputStream() {

    val output get() = String(ba).trim()
    private var ba = ByteArray(0)

    override fun write(b: Int) {
        ba += b.toByte()
    }
}
