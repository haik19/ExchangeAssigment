package com.vahan.exchangeratesassigment.service.model

import com.google.gson.annotations.SerializedName

data class CurrencyValue  (@SerializedName("buy") val buy: String = "-1", @SerializedName("sell") val sell: String = "-1")

