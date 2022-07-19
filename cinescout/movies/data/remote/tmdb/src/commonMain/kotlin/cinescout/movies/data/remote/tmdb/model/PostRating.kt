package cinescout.movies.data.remote.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface PostRating {

    @Serializable
    data class Request(

        @SerialName(Value)
        val value: Double
    ) {

        companion object {

            private const val Value = "value"
        }
    }
}
