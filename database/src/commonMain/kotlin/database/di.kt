package database

import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import database.stats.*
import entities.IntId
import entities.Name
import entities.TmdbId
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val actorAdapter = named("ActorAdapter")
private val genreAdapter = named("GenreAdapter")
private val movieAdapter = named("MovieAdapter")

private val uIntAdapter = named("UIntAdapter")
private val intIdAdapter = named("IntIdAdapter")
private val tmdbIdAdapter = named("TmdbIdAdapter")
private val nameAdapter = named("NameAdapter")

val databaseModule = module {
    single {
        Database(
            driver = DatabaseDriver,
            actorAdapter = get(actorAdapter),
            genreAdapter = get(genreAdapter),
            movieAdapter = get(movieAdapter),
            movie_actorAdapter = get(),
            movie_genreAdapter = get(),
            statAdapter = get(),
            yearRangeAdapter = get(),
        )
    }

    factory(actorAdapter) {
        Actor.Adapter(
            idAdapter = get(intIdAdapter),
            tmdbIdAdapter = get(tmdbIdAdapter),
            nameAdapter = get(nameAdapter)
        )
    }
    factory(genreAdapter) {
        Genre.Adapter(
            idAdapter = get(intIdAdapter),
            tmdbIdAdapter = get(tmdbIdAdapter),
            nameAdapter = get(nameAdapter)
        )
    }
    factory(movieAdapter) {
        Movie.Adapter(
            idAdapter = get(intIdAdapter),
            tmdbIdAdapter = get(tmdbIdAdapter),
            titleAdapter = get(nameAdapter),
            yearAdapter = get(uIntAdapter)
        )
    }

    factory<ColumnAdapter<UInt, Long>>(uIntAdapter) { UIntAdapter() }
    factory<ColumnAdapter<IntId, Long>>(intIdAdapter) { IntIdAdapter() }
    factory<ColumnAdapter<TmdbId, Long>>(tmdbIdAdapter) { TmdbIdAdapter() }
    factory<ColumnAdapter<Name, String>>(nameAdapter) { NameAdapter() }
}

internal const val DATABASE_FILE_NAME = "cinescout.db"
internal expect val DatabaseDriver: SqlDriver
