package client.cli.error

fun String.throwWrongCommand(): Nothing =
    throw IllegalArgumentException("Cannot parse command '$this'")
