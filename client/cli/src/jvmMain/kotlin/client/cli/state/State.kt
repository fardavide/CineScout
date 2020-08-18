package client.cli.state

import client.cli.Palette
import client.cli.controller.Controller
import client.cli.inject
import entities.util.unsupported
import org.koin.core.KoinComponent
import kotlin.reflect.KClass

abstract class State<in C : Controller>(controllerClass: KClass<C>): Palette, KoinComponent {

    private val controller by inject(controllerClass)
    protected val actions = controller.actions

    infix fun execute(command: String) =
        controller execute command

    // TODO make abstract
    open fun render(): String = unsupported

}

typealias AnyState = State<*>
