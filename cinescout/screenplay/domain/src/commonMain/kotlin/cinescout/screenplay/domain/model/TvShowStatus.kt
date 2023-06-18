package cinescout.screenplay.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
// Serial names map directly to Trakt as it wouldn't be worth it to create a mapping layer (extra model or custom
//  serializer)
// https://trakt.docs.apiary.io/#reference/shows/summary/get-a-single-show
enum class TvShowStatus {

    @SerialName("canceled")
    Canceled,

    @SerialName("continuing")
    Continuing,

    @SerialName("ended")
    Ended,

    @SerialName("in production")
    InProduction,

    @SerialName("pilot")
    Pilot,

    @SerialName("planned")
    Planned,

    @SerialName("returning series")
    ReturningSeries,

    Rumored,

    @SerialName("upcoming")
    Upcoming
}
