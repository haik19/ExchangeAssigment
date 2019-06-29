package com.vahan.exchangeratesassigment.service.model

import com.google.gson.annotations.SerializedName

data class BankInfo(@SerializedName("title") val title: String,
                    @SerializedName("list") val bankDetails: Map<String, Map<String, CurrencyValue>>)