package com.sromanuk.ipvigilante.ui.main

import android.content.res.Configuration
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.sromanuk.ipvigilante.R
import com.sromanuk.ipvigilante.repository.retrofit.api_providers.ip_vigilante.IPVigilanteData
import com.sromanuk.ipvigilante.repository.retrofit.api_providers.ip_vigilante.IPVigilanteError
import com.sromanuk.ipvigilante.view_model.main.IPVigilanteCallbacks
import com.sromanuk.ipvigilante.view_model.main.MainViewModel
import kotlinx.android.synthetic.main.main_fragment.*
import java.lang.StringBuilder

class MainFragment : Fragment(), IPVigilanteCallbacks {
    private val greenColour = Color.parseColor("#00FF00")
    private val redColour = Color.parseColor("#FF0000")

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.vigilanteCallbacks = this

        send_ip_request_button.setOnClickListener {
            send_ip_request_button.isEnabled = false

            getIPInfo()
        }

        ip_address_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (viewModel.checkIPAddressCorrectness(charSequence?.toString())) {
                    ip_address_status.text = getString(R.string.ip_address_is_correct)
                    ip_address_status.setTextColor(greenColour)
                }
                else {
                    ip_address_status.text = getString(R.string.ip_address_is_incorrect)
                    ip_address_status.setTextColor(redColour)
                }

                ip_address_response.visibility = View.INVISIBLE
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.vigilanteCallbacks = null
    }

    private fun getIPInfo() {
        val ipAddress = ip_address_input.text.toString()
        viewModel.getIPAddressInfo(ipAddress, this)
    }

    override fun setToConnectionError() {
        send_ip_request_button.isEnabled = true

        ip_address_status.text = getString(R.string.connection_error_message)
        ip_address_status.setTextColor(redColour)
        ip_address_response.visibility = View.INVISIBLE
    }

    override fun setToResponseError(errors: List<IPVigilanteError>?) {
        send_ip_request_button.isEnabled = true
        ip_address_response.visibility = View.INVISIBLE

        ip_address_status.setTextColor(redColour)
        if (errors == null || errors.isEmpty())
            ip_address_status.text = getString(R.string.unkniwn_error_message)
        else {
            ip_address_status.text = errors[0].message
        }
    }

    override fun setToResponseSuccess(data: IPVigilanteData?) {
        send_ip_request_button.isEnabled = true
        ip_address_response.visibility = View.VISIBLE

        if (data == null) {
            ip_address_status.setTextColor(redColour)
            ip_address_status.text = getString(R.string.parsing_problem_message)
        }
        else {
            ip_address_status.setTextColor(greenColour)
            ip_address_status.text = getString(R.string.info_received_message)
            ip_address_response.text = createDescriptionText(data)
        }
    }

    private fun createDescriptionText(data: IPVigilanteData): String {
        val stringBuilder = StringBuilder()

        data.hostname?.let {
            stringBuilder.append("Hostname is ")
            stringBuilder.append(it)
            stringBuilder.append("\n")
        }

        data.continent_name?.let {
            stringBuilder.append(it)
            stringBuilder.append("\n")
        }

        data.country_name?.let {
            stringBuilder.append(it)
            stringBuilder.append("\n")
        }

        data.city_name?.let {
            stringBuilder.append(it)

            data.subdivision_1_iso_code?.let {
                stringBuilder.append("(")
                stringBuilder.append(it)
                stringBuilder.append(")")
            }

            stringBuilder.append("\n")
        }

        data.latitude?.let {
            stringBuilder.append("Latitude: ")
            stringBuilder.append(it)
            stringBuilder.append("\n")
        }

        data.longitude?.let {
            stringBuilder.append("Longitude: ")
            stringBuilder.append(it)
            stringBuilder.append("\n")
        }

        return stringBuilder.toString()
    }

}