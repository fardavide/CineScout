package profile.trakt.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("about")
    val about: String, // Co-founder of trakt.
    @SerialName("age")
    val age: Int, // 32
    @SerialName("gender")
    val gender: String, // male
    @SerialName("ids")
    val ids: Ids,
    @SerialName("images")
    val images: Images,
    @SerialName("joined_at")
    val joinedAt: String, // 2010-09-25T17:49:25.000Z
    @SerialName("location")
    val location: String, // San Diego, CA
    @SerialName("name")
    val name: String, // Justin Nemeth
    @SerialName("private")
    val `private`: Boolean, // false
    @SerialName("username")
    val username: String, // justin
    @SerialName("vip")
    val vip: Boolean, // true
    @SerialName("vip_ep")
    val vipEp: Boolean, // false
    @SerialName("vip_og")
    val vipOg: Boolean, // true
    @SerialName("vip_years")
    val vipYears: Int // 5
)
