package com.vahan.exchangeratesassigment.service.model

import com.google.gson.annotations.SerializedName

data class ContactInfo(@SerializedName("address") var address: String, @SerializedName("contacts") var contacts: String) {

}