package client.cli.state

import client.cli.controller.RateMovieController
import client.cli.headerThemed
import client.cli.themed
import com.jakewharton.picnic.TextAlignment.MiddleCenter
import com.jakewharton.picnic.renderText
import com.jakewharton.picnic.table

class RateMovieState : State<RateMovieController>(RateMovieController::class) {

    override fun render() = table {
        themed()

        header {
            headerThemed()

            row {
                cell("${cyan}Insert the TMDB id of the Movie that you want to rate positively${Reset}") {
                    alignment = MiddleCenter
                    columnSpan = 3
                }
            }
        }
        for (action in actions) {
            row(action.description, *action.commands.toTypedArray())
        }

    }.renderText()
}
