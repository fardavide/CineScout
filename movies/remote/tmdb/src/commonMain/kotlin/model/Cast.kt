package model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cast(
    @SerialName("cast_id")
    val castId: Int, // 17
    @SerialName("character")
    val character: String, // Rick Flag
    @SerialName("credit_id")
    val creditId: String, // 554388d6c3a3680cd7001ef5
    @SerialName("gender")
    val gender: Int, // 2
    @SerialName("id")
    val id: Int, // 92404
    @SerialName("name")
    val name: String, // Joel Kinnaman
    @SerialName("order")
    val order: Int, // 0
    @SerialName("profile_path")
    val profilePath: String // /45CWHmi09y7s7fBnkjtkEgeBr99.jpg
)
