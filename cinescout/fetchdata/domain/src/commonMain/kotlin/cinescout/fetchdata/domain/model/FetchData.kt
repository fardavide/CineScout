package cinescout.fetchdata.domain.model

import korlibs.time.DateTime

data class FetchData(
    val dateTime: DateTime,
    val page: Int = 0
)
