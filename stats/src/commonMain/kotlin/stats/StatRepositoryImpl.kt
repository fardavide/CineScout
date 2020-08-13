package stats

import entities.stats.StatRepository

internal class StatRepositoryImpl(
    private val localSource: LocalStatSource
) : StatRepository by localSource
