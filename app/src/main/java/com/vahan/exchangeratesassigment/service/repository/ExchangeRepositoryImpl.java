package com.vahan.exchangeratesassigment.service.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.vahan.exchangeratesassigment.service.Resource;
import com.vahan.exchangeratesassigment.service.model.BankInfo;
import com.vahan.exchangeratesassigment.service.model.BankSimpleModel;
import com.vahan.exchangeratesassigment.service.model.ContactInfo;
import com.vahan.exchangeratesassigment.service.model.CurrencyValue;
import com.vahan.exchangeratesassigment.service.model.Details;
import com.vahan.exchangeratesassigment.service.model.DetailsSimpleModel;
import com.vahan.exchangeratesassigment.view.CommonConstats;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExchangeRepositoryImpl implements ExchangeRepository {

	private ExchangeService exchangeService;
	private Map<String, BankInfo> lastResponce;

	public ExchangeRepositoryImpl() {
		String BASE_URL = "http://rate.am";
		Gson gson = new GsonBuilder().create();
		Retrofit retrofit = new Retrofit
				.Builder()
				.baseUrl(BASE_URL)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build();
		exchangeService = retrofit.create(ExchangeService.class);
	}

	/*
	 * load exchange info by language code
	 * @param langCode is localization code
	 * @return exchanges info details, included currency and distance
	 */
	@Override
	public LiveData<Resource<List<BankSimpleModel>>> loadExchangesInfo(String langCode) {
		final MutableLiveData<Resource<List<BankSimpleModel>>> exchangesInfoLiveData = new MutableLiveData<>();
		exchangesInfoLiveData.setValue(new Resource.Loading<List<BankSimpleModel>>(null));
		exchangeService.loadExchangeInfo(langCode).enqueue(new Callback<Map<String, BankInfo>>() {
			@Override
			public void onResponse(Call<Map<String, BankInfo>> call, Response<Map<String, BankInfo>> response) {
				if (response.body() == null || !response.isSuccessful()) {
					exchangesInfoLiveData.setValue(new Resource.Error<List<BankSimpleModel>>("Something went wrong", null));
				} else {
					lastResponce = response.body();
					exchangesInfoLiveData.setValue(new Resource.Success<>(mapToSimpleModel(response.body(), CommonConstats.USD)));
				}
			}

			@Override
			public void onFailure(Call<Map<String, BankInfo>> call, Throwable t) {
				exchangesInfoLiveData.setValue(new Resource.Error<List<BankSimpleModel>>(t.getMessage(), null));
			}
		});
		return exchangesInfoLiveData;
	}

	/*
	 * load details info by bank branche id
	 * @param orgId is branche id
	 * @return bank details
	 */
	@Override
	public LiveData<Resource<DetailsSimpleModel>> loadDetailsByOrganization(String orgId) {
		final MutableLiveData<Resource<DetailsSimpleModel>> detailsLiveData = new MutableLiveData<>();
		detailsLiveData.setValue(new Resource.Loading<DetailsSimpleModel>(null));
		exchangeService.loadDetailsByOrganization(orgId).enqueue(new Callback<Details>() {
			@Override
			public void onResponse(@NotNull Call<Details> call, Response<Details> response) {
				if (response.body() == null || !response.isSuccessful()) {
					detailsLiveData.setValue(new Resource.Error<DetailsSimpleModel>("Something went wrong", null));
				} else {
					detailsLiveData.setValue(new Resource.Success<>(mapToSimpleModel(response.body())));
				}
			}

			@Override
			public void onFailure(@NotNull Call<Details> call, Throwable t) {
				detailsLiveData.setValue(new Resource.Error<DetailsSimpleModel>(t.getMessage(), null));
			}
		});
		return detailsLiveData;
	}

	/*
	 * filter data by currency
	 * @param filterByCurrency current currency
	 * @return filtered data
	 */
	@Override
	public LiveData<Resource<List<BankSimpleModel>>> filterByCurrency(String filterByCurrency) {
		MutableLiveData<Resource<List<BankSimpleModel>>> filteredData = new MutableLiveData<>();
		List<BankSimpleModel> bankSimpleModel = mapToSimpleModel(lastResponce, filterByCurrency);
		filteredData.setValue(new Resource.Success<>(bankSimpleModel));
		return filteredData;
	}

	/*
	 * map difficult maps to simply model
	 * @param map last response,
	 * @param filterBy selected currency
	 * @return list of BankSimpleModel
	 */
	private List<BankSimpleModel> mapToSimpleModel(Map<String, BankInfo> map, String fillterBy) {
		List<BankSimpleModel> bankSimpleModels = new ArrayList<>();
		if (map == null || map.isEmpty()) {
			return bankSimpleModels;
		}
		List<String> keys = new ArrayList<>(map.keySet());
		for (String key : keys) {
			BankSimpleModel bankSimpleModel = new BankSimpleModel();
			BankInfo bankInfo = map.get(key);
			bankSimpleModel.setBankId(key);
			bankSimpleModel.setName(bankInfo.getTitle());
			Map<String, Map<String, CurrencyValue>> b = bankInfo.getBankDetails();
			Map<String, CurrencyValue> currencyValueMap = b.get(fillterBy);
			if (currencyValueMap == null) {
				continue;
			}
			CurrencyValue currencyValue = currencyValueMap.get("0");
			if (currencyValue == null) {
				continue;
			}
			bankSimpleModel.setBuyValue(currencyValue.getBuy());
			bankSimpleModel.setSalleValue(currencyValue.getSell());
			bankSimpleModels.add(bankSimpleModel);
		}
		return bankSimpleModels;
	}

	/*
	 * map difficult maps to simply model
	 * @param details bank
	 * @return  DetailsSimpleModel
	 */
	private DetailsSimpleModel mapToSimpleModel(Details details) { //FIXME improuve method
		DetailsSimpleModel detailsSimpleModel = new DetailsSimpleModel();
		List<String> keys = new ArrayList<>(details.getDetailsMap().keySet());
		Map<String, ContactInfo> infoMap = details.getDetailsMap().get(keys.get(0));
		Object adr = infoMap.get(CommonConstats.ADDRESS);//TODO FIXME
		if (adr == null) {
			return detailsSimpleModel;
		}
		String address = (String)((LinkedTreeMap) adr).get(CommonConstats.EN);//TODO FIXME
		detailsSimpleModel.setAddres(address);
		Object phone = infoMap.get("contacts");
		if (phone == null) {
			return detailsSimpleModel;
		}

		detailsSimpleModel.setPhone(phone.toString());
		return detailsSimpleModel;
	}

}
