package cinescout.screenplay.domain.store

import arrow.core.Nel
import cinescout.screenplay.domain.model.Genre
import cinescout.store5.Store5

interface GenresStore : Store5<Unit, Nel<Genre>>
