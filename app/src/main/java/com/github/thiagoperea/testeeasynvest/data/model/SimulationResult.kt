package com.github.thiagoperea.testeeasynvest.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SimulationResult(
    val investmentParameter: SimulationResultParameters,
    val grossAmount: Double,
    val taxesAmount: Double,
    val netAmount: Double,
    val grossAmountProfit: Double,
    val netAmountProfit: Double,
    val annualGrossRateProfit: Double,
    val monthlyGrossRateProfit: Double,
    val dailyGrossRateProfit: Double,
    val taxesRate: Double,
    val rateProfit: Double,
    val annualNetRateProfit: Double
) : Parcelable

@Parcelize
data class SimulationResultParameters(
    val investedAmount: Double,
    val yearlyInterestRate: Double,
    val maturityTotalDays: Int,
    val maturityBusinessDays: Int,
    val maturityDate: String,
    val rate: Double,
    val isTaxFree: Boolean
) : Parcelable
