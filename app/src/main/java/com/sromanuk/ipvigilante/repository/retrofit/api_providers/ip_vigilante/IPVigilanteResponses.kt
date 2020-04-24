package com.sromanuk.ipvigilante.repository.retrofit.api_providers.ip_vigilante

data class IPVigilanteAPIResponse(
    val status: String,
    val data: IPVigilanteData?,
    val errors: List<IPVigilanteError>?
) {
    fun hasError(): Boolean = status == "error"
}

data class IPVigilanteData(
    val ipv4: String?,
    val hostname: String?,
    val continent_code: String?,
    val continent_name: String?,
    val country_iso_code: String?,
    val country_name: String?,
    val subdivision_1_iso_code: String?,
    val subdivision_1_name: String?,
    val subdivision_2_iso_code: String?,
    val subdivision_2_name: String?,
    val city_name: String?,
    val metro_code: Int?,
    val time_zone: String?,
    val postal_code: String?,
    val latitude: String?,
    val longitude: String?,
    val accuracy_radius: Int?
)

data class IPVigilanteError(
    val code: String?,
    val message: String?,
    val numberErrors: Int?
)