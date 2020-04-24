package com.sromanuk.ipvigilante.use_cases.main

import androidx.lifecycle.LiveData
import com.sromanuk.ipvigilante.repository.retrofit.api_providers.ip_vigilante.IPVigilanteAPIResponse
import com.sromanuk.ipvigilante.repository.retrofit.api_providers.ip_vigilante.IPVigilanteService
import com.sromanuk.ipvigilante.repository.utils.ApiResponse

class IPVigilanteUseCase {
    fun checkIPAddressForConsistency(ipAddress: String?): Boolean {
        if (ipAddress == null || ipAddress.isBlank())
            return false

        if (ipAddress.length < 7 || ipAddress.length > 15)
            return false

        val ipAddressChunks = ipAddress.split('.')
        if (ipAddressChunks.size != 4)
            return false

        for (chunk in ipAddressChunks) {
            if (chunk.isBlank() || chunk.length > 3)
                return false

            try {
                val value = chunk.toInt()
                if (value !in 0..255)
                    return false

            } catch (ex: NumberFormatException) {
                return false
            }
        }

        return true
    }

    fun sendIPInfoRequest(vigilanteService: IPVigilanteService, ipAddress: String?): LiveData<ApiResponse<IPVigilanteAPIResponse?>>? {
        if (checkIPAddressForConsistency(ipAddress))
            return vigilanteService.getIPInformation(ipAddress)

        return null
    }
}