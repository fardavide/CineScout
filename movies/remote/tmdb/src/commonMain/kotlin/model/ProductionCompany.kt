package model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionCompany(
    @SerialName("id")
    val id: Int, // 508
    @SerialName("logo_path")
    val logoPath: String, // /7PzJdsLGlR7oW4J0J5Xcd0pHGRg.png
    @SerialName("name")
    val name: String, // Regency Enterprises
    @SerialName("origin_country")
    val originCountry: String // US
)
