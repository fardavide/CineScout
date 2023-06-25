package cinescout.screenplay.data.local.mapper

import cinescout.database.model.id.DatabaseTmdbGenreId
import cinescout.screenplay.domain.model.Genre
import org.koin.core.annotation.Factory

@Factory
internal class DatabaseGenreMapper {

    fun toGenre(id: DatabaseTmdbGenreId, name: String) = Genre(id.toDomainId(), name)
}
