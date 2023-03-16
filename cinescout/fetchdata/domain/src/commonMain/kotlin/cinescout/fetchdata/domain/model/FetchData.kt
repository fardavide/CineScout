package cinescout.fetchdata.domain.model

import com.soywiz.klock.DateTime

data class FetchData(
    val dateTime: DateTime,
    val page: Int
)
