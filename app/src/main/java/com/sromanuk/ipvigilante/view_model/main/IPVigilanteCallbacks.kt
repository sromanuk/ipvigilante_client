package com.sromanuk.ipvigilante.view_model.main

import com.sromanuk.ipvigilante.repository.retrofit.api_providers.ip_vigilante.IPVigilanteData
import com.sromanuk.ipvigilante.repository.retrofit.api_providers.ip_vigilante.IPVigilanteError

interface IPVigilanteCallbacks {
    fun setToConnectionError()
    fun setToResponseError(errors: List<IPVigilanteError>?)
    fun setToResponseSuccess(data: IPVigilanteData?)
}