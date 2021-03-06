package com.github.thiagoperea.testeeasynvest.presentation.startsimulation

import androidx.annotation.IdRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.thiagoperea.testeeasynvest.R
import com.github.thiagoperea.testeeasynvest.data.model.SimulationResult
import com.github.thiagoperea.testeeasynvest.extensions.formatDate
import com.github.thiagoperea.testeeasynvest.usecase.SimulationUsecase
import java.text.ParseException

class SimulationStartViewModel(val simulationUsecase: SimulationUsecase) : ViewModel() {
    var totalValueRaw = ""
        set(value) {
            field = value
            validateFields()
        }

    var dateRaw = ""
        set(value) {
            field = value
            validateFields()
        }

    var percentRaw = ""
        set(value) {
            field = value
            validateFields()
        }

    val simulationButtonEnabled = MutableLiveData<Boolean>()
    val showLoading = MutableLiveData<Boolean>()
    val simulationResult = MutableLiveData<SimulationResult>()

    val dateValidationError = MutableLiveData<@IdRes Int>()
    val valueValidationError = MutableLiveData<@IdRes Int>()
    val percentValidationError = MutableLiveData<@IdRes Int>()
    val simulationError = MutableLiveData<String>()

    fun validateFields() {
        simulationButtonEnabled.postValue(
            totalValueRaw.isNotEmpty() && dateRaw.isNotEmpty() && percentRaw.isNotEmpty()
        )
    }

    fun startSimulation() {
        val value = getFormattedValue()
        val date = getFormattedDate()
        val percent = getFormattedPercent()

        if (value != null && date != null && percent != null) {
            showLoading.postValue(true)

            simulationUsecase.execute(
                value,
                date,
                percent,
                onSuccess = { result ->
                    showLoading.postValue(false)
                    simulationResult.postValue(result)
                },
                onError = { error ->
                    showLoading.postValue(false)
                    simulationError.postValue(error.localizedMessage)
                }
            )
        }
    }

    fun getFormattedPercent(): Int? {
        val digitsOnly = percentRaw.replace("%", "")

        if (digitsOnly.isEmpty() || digitsOnly.toInt() == 0) {
            percentValidationError.postValue(R.string.percent_validation_error)
            return null
        }

        return digitsOnly.toInt()
    }

    fun getFormattedValue(): Double? {
        val digitsOnly = totalValueRaw.filter { it.isDigit() }

        if (digitsOnly.isEmpty() || digitsOnly.toDouble() == 0.0) {
            valueValidationError.postValue(R.string.value_validation_error)
            return null
        }

        return digitsOnly.toDouble() / 100
    }

    fun getFormattedDate(): String? {
        if (dateRaw.length < 10) {
            dateValidationError.postValue(R.string.date_validation_error)
            return null
        }

        try {
            return dateRaw.formatDate("dd/MM/yyyy", "yyyy-MM-dd")
        } catch (error: ParseException) {
            dateValidationError.postValue(R.string.date_validation_error)
            return null
        }
    }
}