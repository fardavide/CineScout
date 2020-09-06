package database

import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import database.movies.Actor
import database.movies.Genre
import database.movies.Movie
import database.movies.Movie_actor
import database.movies.Movie_genre
import database.movies.YearRange
import database.stats.Stat
import database.stats.StatType
import entities.IntId
import entities.Name
import entities.TmdbId
import org.koin.core.qualifier.named
import org.koin.dsl.module

val actorAdapter = named("ActorAdapter")
val genreAdapter = named("GenreAdapter")
val movieAdapter = named("MovieAdapter")
val movieActorAdapter = named("MovieActorAdapter")
val movieGenreAdapter = named("MovieGenreAdapter")
val statAdapter = named("StatAdapter")
val yearRangeAdapter = named("YearRangeAdapter")

val uIntAdapter = named("UIntAdapter")
val intIdAdapter = named("IntIdAdapter")
val tmdbIdAdapter = named("TmdbIdAdapter")
val nameAdapter = named("NameAdapter")
val statTypeAdapter = named("StatTypeAdapter")

val databaseModule = module {
    single {
        Database(
            driver = DatabaseDriver,
            actorAdapter = get(actorAdapter),
            genreAdapter = get(genreAdapter),
            movieAdapter = get(movieAdapter),
            movie_actorAdapter = get(movieActorAdapter),
            movie_genreAdapter = get(movieGenreAdapter),
            statAdapter = get(statAdapter),
            yearRangeAdapter = get(yearRangeAdapter)
        ).also {
            Database.Schema.create(DatabaseDriver)
        }
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
    factory(movieActorAdapter) {
        Movie_actor.Adapter(movieIdAdapter = get(intIdAdapter), actorIdAdapter = get(intIdAdapter))
    }
    factory(movieGenreAdapter) {
        Movie_genre.Adapter(movieIdAdapter = get(intIdAdapter), genreIdAdapter = get(intIdAdapter))
    }
    factory(statAdapter) {
        Stat.Adapter(idAdapter = get(intIdAdapter), statIdAdapter = get(intIdAdapter), typeAdapter = get(statTypeAdapter))
    }
    factory(yearRangeAdapter) {
        YearRange.Adapter(idAdapter = get(uIntAdapter))
    }

    factory<ColumnAdapter<UInt, Long>>(uIntAdapter) { UIntAdapter() }
    factory<ColumnAdapter<IntId, Long>>(intIdAdapter) { IntIdAdapter() }
    factory<ColumnAdapter<TmdbId, Long>>(tmdbIdAdapter) { TmdbIdAdapter() }
    factory<ColumnAdapter<Name, String>>(nameAdapter) { NameAdapter() }
    factory<ColumnAdapter<StatType, String>>(statTypeAdapter) { StatTypeAdapter() }
}

internal const val DATABASE_FILE_NAME = "cinescout.db"
internal expect val DatabaseDriver: SqlDriver
