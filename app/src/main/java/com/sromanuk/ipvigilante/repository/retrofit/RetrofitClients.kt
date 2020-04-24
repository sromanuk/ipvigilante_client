package com.sromanuk.ipvigilante.repository.retrofit

import com.sromanuk.ipvigilante.repository.retrofit.api_providers.ip_vigilante.IPVigilanteService
import com.sromanuk.ipvigilante.repository.utils.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class IPVigilanteRetrofitClient {
    private fun getRetrofitObject(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://ipvigilante.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }

    fun getIPVigilanteService(): IPVigilanteService {
        return getRetrofitObject().create(IPVigilanteService::class.java)
    }
}