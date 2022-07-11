package cinescout.movies.data.remote.tmdb.model

import kotlinx.serialization.SerialName

interface PostRating {

    @kotlinx.serialization.Serializable
    data class Request(

        @SerialName(Value)
        val value: Int
    ) {

        companion object {

            private const val Value = "value"
        }
    }
}
