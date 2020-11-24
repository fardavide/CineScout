package profile.trakt

import assert4k.*
import entities.Either
import entities.MissingCache
import entities.ResourceError
import entities.TestData.DummyTraktProfile
import entities.left
import entities.model.GravatarImage
import entities.model.TraktProfile
import entities.right
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

class TraktProfileRepositoryImplTest : CoroutinesTest {

    private val profile1 = DummyTraktProfile
    private val profile2 = profile1.copy(avatar = GravatarImage("thumb2", "full2"))
    private val profile3 = profile1.copy(avatar = GravatarImage("thumb3", "full3"))
    private val profile4 = profile1.copy(avatar = GravatarImage("thumb4", "full4"))

    private val remoteRepository = mockk<RemoteTraktProfileSource> {
        val allProfiles = listOf(profile1, profile2, profile3, profile4)
        var last = -1
        coEvery { getPersonalProfile() } answers {
            last = (++last).takeIf { it in allProfiles.indices } ?: 0
            allProfiles[last].right()
        }
    }
    private val localRepository = mockk<LocalTraktProfileSource> {
        val flow = MutableStateFlow<Either<ResourceError, TraktProfile>>(ResourceError.Local(MissingCache).left())
        every { findPersonalProfile() } returns flow
        coEvery { storePersonalProfile(any()) } coAnswers { flow.value = firstArg<TraktProfile>().right() }
    }
    private val repository = TraktProfileRepositoryImpl(localRepository, remoteRepository)

    @Test
    fun `findPersonalProfile works correctly`() = coroutinesTest(ignoreUnfinishedJobs = true) {
        val result = repository.findPersonalProfile().take(5).toList()

        assert that result equals listOf(
            ResourceError.Local(MissingCache).left(),
            profile1.right(),
            profile2.right(),
            profile3.right(),
            profile4.right()
        )
    }

    @Test
    fun `findPersonalProfile read from database first`() = coroutinesTest {
        coEvery { remoteRepository.getPersonalProfile() } coAnswers {
            delay(10.seconds)
            profile2.right()
        }
        localRepository.storePersonalProfile(profile1)
        val result = repository.findPersonalProfile().take(1).toList()

        assert that result equals listOf(profile1.right())
    }
}
