package cinescout.database.model

enum class DatabaseTvShowStatus {
    Canceled,
    Continuing,
    Ended,
    InProduction,
    Pilot,
    Planned,
    ReturningSeries,
    Rumored,
    Upcoming;

    companion object {

        fun fromName(name: String) = DatabaseTvShowStatus.values().first { it.name == name }
    }
}
