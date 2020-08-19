package client.cli.view

import client.cli.Palette

interface View: Palette {

    fun render(): Any
}
