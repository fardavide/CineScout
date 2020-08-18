package client.cli

val HomeAction = Action("Back to the main menu", "*home", "*h")

data class Action(
    val description: String,
    val commands: Set<String>
)

fun Action(description: String, vararg commands: String) =
    Action(description, commands.toSet())

