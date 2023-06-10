package cinescout.progress.domain.model

import cinescout.screenplay.domain.model.Screenplay

sealed interface ScreenplayProgress {

    val screenplay: Screenplay
}
