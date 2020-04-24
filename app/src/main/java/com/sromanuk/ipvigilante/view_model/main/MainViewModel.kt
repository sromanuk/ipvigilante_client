package com.sromanuk.ipvigilante.view_model.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.sromanuk.ipvigilante.repository.retrofit.IPVigilanteRetrofitClient
import com.sromanuk.ipvigilante.use_cases.main.IPVigilanteUseCase

class MainViewModel : ViewModel() {
    private val ipVigilanteService = IPVigilanteRetrofitClient().getIPVigilanteService()
    private val ipVigilanteUseCase = IPVigilanteUseCase()
    var vigilanteCallbacks: IPVigilanteCallbacks? = null


    fun getIPAddressInfo(ipAddress: String? = null, context: LifecycleOwner) {
        val result = ipVigilanteUseCase.sendIPInfoRequest(ipVigilanteService, ipAddress)
        if (result == null) {
            vigilanteCallbacks?.setToConnectionError()
        }
        else {
            result.observe(context, Observer { response ->
                when {
                    response?.body == null -> {
                        vigilanteCallbacks?.setToConnectionError()
                    }
                    response.body.hasError() -> {
                        vigilanteCallbacks?.setToResponseError(response.body.errors)
                    }
                    else -> vigilanteCallbacks?.setToResponseSuccess(response.body.data)
                }
            })
        }
    }

    fun checkIPAddressCorrectness(ipAddress: String?): Boolean =
        ipVigilanteUseCase.checkIPAddressForConsistency(ipAddress)
}