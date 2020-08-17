package client.cli

import client.cli.error.throwWrongCommand
import entities.util.equalsNoCase

sealed class State {

    abstract infix fun parse(command: String): State

    object Menu : State() {

        override fun parse(command: String): State {
            return when {
                setOf("1", "search").any { it equalsNoCase command } -> Search
                setOf("2", "rate").any { it equalsNoCase command } -> Rate
                setOf("3", "suggestion").any { it equalsNoCase command } -> GetSuggestions
                else -> command.throwWrongCommand()
            }
        }
    }

    object Search : State() {

        override fun parse(command: String): State {
            TODO("Not yet implemented")
        }
    }

    object Rate : State() {

        override fun parse(command: String): State {
            TODO("Not yet implemented")
        }
    }

    object GetSuggestions : State() {

        override fun parse(command: String): State {
            TODO("Not yet implemented")
        }
    }
}
