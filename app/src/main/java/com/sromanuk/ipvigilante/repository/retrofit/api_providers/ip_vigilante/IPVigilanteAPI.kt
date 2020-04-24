package com.sromanuk.ipvigilante.repository.retrofit.api_providers.ip_vigilante

import androidx.lifecycle.LiveData
import com.sromanuk.ipvigilante.repository.utils.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface IPVigilanteService {
    @GET("json/{ip_address}/full")
    fun getIPInformation(@Path("ip_address") ipAddress: String?): LiveData<ApiResponse<IPVigilanteAPIResponse?>>?
}
