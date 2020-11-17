package profile.tmdb

import assert4k.*
import entities.TmdbId
import entities.model.GravatarImage
import entities.model.Name
import entities.model.Profile
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import util.test.CoroutinesTest
import kotlin.test.*
import kotlin.time.seconds

class TmdbProfileRepositoryImplTest : CoroutinesTest {

    private val profile1 = Profile(
        id = TmdbId(1),
        name = Name("Davide"),
        username = Name("davide"),
        avatar = GravatarImage("thumb1", "full1"),
        adult = true
    )
    private val profile2 = profile1.copy(avatar = GravatarImage("thumb2", "full2"))
    private val profile3 = profile1.copy(avatar = GravatarImage("thumb3", "full3"))
    private val profile4 = profile1.copy(avatar = GravatarImage("thumb4", "full4"))

    private val remoteRepository = mockk<RemoteTmdbProfileRepository> {
        val allProfiles = listOf(profile1, profile2, profile3, profile4)
        var last = -1
        coEvery { getPersonalProfile() } answers {
            last = (++last).takeIf { it in allProfiles.indices } ?: 0
            allProfiles[last]
        }
    }
    private val localRepository = mockk<LocalTmdbProfileRepository> {
        val flow = MutableStateFlow<Profile?>(null)
        every { findPersonalProfile() } returns flow
        coEvery { storePersonalProfile(any()) } coAnswers { flow.value = firstArg() }
    }
    private val repository = TmdbProfileRepositoryImpl(localRepository, remoteRepository)

    @Test
    fun `findPersonalProfile works correctly`() = coroutinesTest {
        val result = repository.findPersonalProfile().take(5).toList()

        assert that result equals listOf(null, profile1, profile2, profile3, profile4)
    }

    @Test
    fun `findPersonalProfile read from database first`() = coroutinesTest {
        coEvery { remoteRepository.getPersonalProfile() } coAnswers {
            delay(10.seconds)
            profile2
        }
        localRepository.storePersonalProfile(profile1)
        val result = repository.findPersonalProfile().take(1).toList()

        assert that result equals listOf(profile1)
    }
}
