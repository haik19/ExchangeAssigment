package com.vahan.exchangeratesassigment.service.repository;

import com.vahan.exchangeratesassigment.service.model.BankInfo;
import com.vahan.exchangeratesassigment.service.model.Details;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ExchangeService {


    @GET("/ws/mobile/v2/rates.ashx")
    Call<Map<String, BankInfo>> loadExchangeInfo(@Query("lang") String langCode);

    @GET("http://rate.am/ws/mobile/v2/branches.ashx")
    Call<Details> loadDetailsByOrganization(@Query("id") String organizationId);
}
