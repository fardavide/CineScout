package cinescout.people.data.local.datasource

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cinescout.database.FindCastByScreenplayId
import cinescout.database.FindCrewByScreenplayId
import cinescout.database.PersonQueries
import cinescout.database.ScreenplayCastMemberQueries
import cinescout.database.ScreenplayCrewMemberQueries
import cinescout.database.util.suspendTransaction
import cinescout.people.data.datasource.LocalPeopleDataSource
import cinescout.people.data.local.mapper.DatabaseCreditsMapper
import cinescout.people.data.local.mapper.toDatabaseId
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.screenplay.data.local.mapper.toDatabaseId
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class RealLocalPeopleDataSource(
    private val mapper: DatabaseCreditsMapper,
    private val personQueries: PersonQueries,
    private val screenplayCastMemberQueries: ScreenplayCastMemberQueries,
    private val screenplayCrewMemberQueries: ScreenplayCrewMemberQueries,
    @Named(IoDispatcher) private val readDispatcher: CoroutineDispatcher,
    private val transacter: Transacter,
    @Named(DatabaseWriteDispatcher) private val writeDispatcher: CoroutineDispatcher
) : LocalPeopleDataSource {

    override fun findCredits(screenplayId: TmdbScreenplayId): Flow<ScreenplayCredits> = combine(
        findCast(screenplayId),
        findCrew(screenplayId)
    ) { cast, crew ->
        mapper.toCredits(cast, crew, screenplayId)
    }

    override suspend fun insertCredits(credits: ScreenplayCredits) {
        transacter.suspendTransaction(writeDispatcher) {
            for (castMember in credits.cast) {
                personQueries.insertPerson(
                    name = castMember.person.name,
                    profileImagePath = castMember.person.profileImage.orNull()?.path,
                    tmdbId = castMember.person.tmdbId.toDatabaseId()
                )
                screenplayCastMemberQueries.insertCastMember(
                    character = castMember.character.orNull(),
                    memberOrder = castMember.order.toLong(),
                    personId = castMember.person.tmdbId.toDatabaseId(),
                    screenplayId = credits.screenplayId.toDatabaseId()
                )
            }
            for (crewMember in credits.crew) {
                personQueries.insertPerson(
                    name = crewMember.person.name,
                    profileImagePath = crewMember.person.profileImage.orNull()?.path,
                    tmdbId = crewMember.person.tmdbId.toDatabaseId()
                )
                screenplayCrewMemberQueries.insertCrewMember(
                    job = crewMember.job.orNull(),
                    memberOrder = crewMember.order.toLong(),
                    personId = crewMember.person.tmdbId.toDatabaseId(),
                    screenplayId = credits.screenplayId.toDatabaseId()
                )
            }
        }
    }

    private fun findCast(screenplayId: TmdbScreenplayId): Flow<List<FindCastByScreenplayId>> =
        personQueries.findCastByScreenplayId(screenplayId.toDatabaseId()).asFlow().mapToList(readDispatcher)

    private fun findCrew(screenplayId: TmdbScreenplayId): Flow<List<FindCrewByScreenplayId>> =
        personQueries.findCrewByScreenplayId(screenplayId.toDatabaseId()).asFlow().mapToList(readDispatcher)
}
