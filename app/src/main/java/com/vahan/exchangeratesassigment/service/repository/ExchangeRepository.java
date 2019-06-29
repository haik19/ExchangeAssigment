package com.vahan.exchangeratesassigment.service.repository;

import android.arch.lifecycle.LiveData;

import com.vahan.exchangeratesassigment.service.Resource;
import com.vahan.exchangeratesassigment.service.model.BankSimpleModel;
import com.vahan.exchangeratesassigment.service.model.DetailsSimpleModel;

import java.util.List;

public interface ExchangeRepository {

    LiveData<Resource<List<BankSimpleModel>>> loadExchangesInfo(String langeCode);

    LiveData<Resource<List<BankSimpleModel>>> filterByCurrency(String filterByCurrency);

    LiveData<Resource<DetailsSimpleModel>> loadDetailsByOrganization(String orgId);





}
