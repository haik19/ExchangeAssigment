package com.vahan.exchangeratesassigment.service.model

import com.google.gson.annotations.SerializedName

data class Details(@SerializedName("list") var detailsMap: Map<String, Map<String, ContactInfo>>)
