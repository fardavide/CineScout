package database

import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import database.credentials.TmdbCredential
import database.movies.Actor
import database.movies.Genre
import database.movies.Movie
import database.movies.Movie_actor
import database.movies.Movie_genre
import database.movies.Movie_video
import database.movies.Video
import database.movies.YearRange
import database.profile.Profile
import database.stats.Stat
import database.stats.StatType
import database.stats.Watchlist
import entities.IntId
import entities.TmdbId
import entities.TmdbStringId
import entities.model.Name
import org.koin.core.qualifier.named
import org.koin.dsl.module

val actorAdapter = named("ActorAdapter")
val genreAdapter = named("GenreAdapter")
val movieAdapter = named("MovieAdapter")
val movieActorAdapter = named("MovieActorAdapter")
val movieGenreAdapter = named("MovieGenreAdapter")
val movieVideoAdapter = named("MovieVideoAdapter")
val siteAdapter = named("SiteAdapter")
val profileAdapter = named("ProfileAdapter")
val statAdapter = named("StatAdapter")
val tmdbCredentialAdapter = named("TmdbCredentialAdapter")
val videoAdapter = named("VideoAdapter")
val videoTypeAdapter = named("VideoTypeAdapter")
val yearRangeAdapter = named("YearRangeAdapter")
val watchlistAdapter = named("WatchlistAdapter")

val uIntAdapter = named("UIntAdapter")
val intIdAdapter = named("IntIdAdapter")
val tmdbIdAdapter = named("TmdbIdAdapter")
val tmdbStringIdAdapter = named("TmdbStringIdAdapter")
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
            movie_videoAdapter = get(movieVideoAdapter),
            profileAdapter = get(profileAdapter),
            statAdapter = get(statAdapter),
            tmdbCredentialAdapter = get(tmdbCredentialAdapter),
            videoAdapter = get(videoAdapter),
            yearRangeAdapter = get(yearRangeAdapter),
            watchlistAdapter = get(watchlistAdapter)
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
    factory(movieVideoAdapter) {
        Movie_video.Adapter(movieIdAdapter = get(intIdAdapter), videoIdAdapter = get(intIdAdapter))
    }
    factory(profileAdapter) {
        Profile.Adapter(
            idAdapter = get(intIdAdapter),
            tmdbIdAdapter = get(tmdbIdAdapter),
            usernameAdapter = get(nameAdapter),
            nameAdapter = get(nameAdapter)
        )
    }
    factory(statAdapter) {
        Stat.Adapter(idAdapter = get(intIdAdapter), statIdAdapter = get(intIdAdapter), typeAdapter = get(statTypeAdapter))
    }
    factory(tmdbCredentialAdapter) {
        TmdbCredential.Adapter(v3accountIdAdapter = get(tmdbIdAdapter), v4accountIdAdapter = get(tmdbStringIdAdapter))
    }
    factory(videoAdapter) {
        Video.Adapter(
            idAdapter = get(intIdAdapter),
            tmdbIdAdapter = get(tmdbStringIdAdapter),
            nameAdapter = get(nameAdapter),
            siteAdapter = get(siteAdapter),
            typeAdapter = get(videoTypeAdapter)
        )
    }
    factory(yearRangeAdapter) {
        YearRange.Adapter(idAdapter = get(uIntAdapter))
    }
    factory(watchlistAdapter) {
        Watchlist.Adapter(idAdapter = get(intIdAdapter), movieIdAdapter = get(intIdAdapter))
    }

    factory<ColumnAdapter<UInt, Long>>(uIntAdapter) { UIntAdapter() }
    factory<ColumnAdapter<IntId, Long>>(intIdAdapter) { IntIdAdapter() }
    factory<ColumnAdapter<TmdbId, Long>>(tmdbIdAdapter) { TmdbIdAdapter() }
    factory<ColumnAdapter<TmdbStringId, String>>(tmdbStringIdAdapter) { TmdbStringIdAdapter() }
    factory<ColumnAdapter<Name, String>>(nameAdapter) { NameAdapter() }
    factory<ColumnAdapter<entities.model.Video.Site, String>>(siteAdapter) { SiteAdapter() }
    factory<ColumnAdapter<StatType, String>>(statTypeAdapter) { StatTypeAdapter() }
    factory<ColumnAdapter<entities.model.Video.Type, String>>(videoTypeAdapter) { VideoTypeAdapter() }
}

internal const val DATABASE_FILE_NAME = "cinescout.db"
internal expect val DatabaseDriver: SqlDriver
