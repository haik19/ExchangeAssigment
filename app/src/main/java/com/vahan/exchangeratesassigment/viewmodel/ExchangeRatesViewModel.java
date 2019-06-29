package com.vahan.exchangeratesassigment.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import com.vahan.exchangeratesassigment.service.Resource;
import com.vahan.exchangeratesassigment.service.model.BankSimpleModel;
import com.vahan.exchangeratesassigment.service.model.DetailsSimpleModel;
import com.vahan.exchangeratesassigment.service.repository.ExchangeRepository;
import com.vahan.exchangeratesassigment.service.repository.ExchangeRepositoryImpl;
import java.util.List;

public class ExchangeRatesViewModel extends ViewModel {

    private ExchangeRepository exchangeRepository;
    private MediatorLiveData<Resource<List<BankSimpleModel>>> exchangesMediatorLiveData;
    private MediatorLiveData<Resource<DetailsSimpleModel>> detailsMediatorLiveData;

    public ExchangeRatesViewModel(){
        exchangeRepository = new ExchangeRepositoryImpl();
        exchangesMediatorLiveData = new MediatorLiveData<>();
        detailsMediatorLiveData = new MediatorLiveData<>();
    }

    public void loadExchangesInfo(String langCode){
        final LiveData<Resource<List<BankSimpleModel>>> exchangesInfoLiveData = exchangeRepository.loadExchangesInfo(langCode);
        exchangesMediatorLiveData.addSource(exchangesInfoLiveData, new Observer<Resource<List<BankSimpleModel>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<BankSimpleModel>> exchangesInfoResource) {
                exchangesMediatorLiveData.setValue(exchangesInfoResource);
                if (exchangesInfoResource instanceof Resource.Success || exchangesInfoResource instanceof Resource.Error) {
                    exchangesMediatorLiveData.removeSource(exchangesInfoLiveData);
                }
           }
       });
    }

    public void filterByCurrency(String currency) {
        final LiveData<Resource<List<BankSimpleModel>>> filtereddata = exchangeRepository.filterByCurrency(currency);
        exchangesMediatorLiveData.addSource(filtereddata, new Observer<Resource<List<BankSimpleModel>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<BankSimpleModel>> exchangesInfoResource) {
                exchangesMediatorLiveData.setValue(exchangesInfoResource);
                exchangesMediatorLiveData.removeSource(filtereddata);
            }
        });
    }

    public void loadBranchDetail(String orgId){
       final LiveData<Resource<DetailsSimpleModel>> detailsLiveData =  exchangeRepository.loadDetailsByOrganization(orgId);
       detailsMediatorLiveData.addSource(detailsLiveData, new Observer<Resource<DetailsSimpleModel>>() {
           @Override
           public void onChanged(@Nullable Resource<DetailsSimpleModel> detailsResource) {
               if (detailsResource instanceof Resource.Success || detailsResource instanceof Resource.Error) {
                   detailsMediatorLiveData.setValue(detailsResource);
                   detailsMediatorLiveData.removeSource(detailsLiveData);
               }
           }
       });
    }

    public MediatorLiveData<Resource<DetailsSimpleModel>> getDetailsMediatorLiveData() {
        return detailsMediatorLiveData;
    }

    public MediatorLiveData<Resource<List<BankSimpleModel>>> getExchangesMediatorLiveData() {
        return exchangesMediatorLiveData;
    }
}
