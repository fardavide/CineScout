package client.cli.state

import client.cli.Palette
import client.cli.controller.Controller
import client.cli.inject
import org.koin.core.KoinComponent
import kotlin.reflect.KClass

abstract class State<in C : Controller>(controllerClass: KClass<C>): Palette, KoinComponent {

    private val controller by inject(controllerClass)
    protected val actions = controller.actions

    suspend infix fun execute(command: String) =
        controller execute command

    abstract fun render(): String

}

typealias AnyState = State<*>
