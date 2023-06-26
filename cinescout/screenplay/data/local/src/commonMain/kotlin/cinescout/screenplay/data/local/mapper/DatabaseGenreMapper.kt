package cinescout.screenplay.data.local.mapper

import cinescout.database.model.id.DatabaseGenreSlug
import cinescout.screenplay.domain.model.Genre
import org.koin.core.annotation.Factory

@Factory
internal class DatabaseGenreMapper {

    fun toGenre(id: DatabaseGenreSlug, name: String) = Genre(name = name, slug = id.toDomainId())
}
