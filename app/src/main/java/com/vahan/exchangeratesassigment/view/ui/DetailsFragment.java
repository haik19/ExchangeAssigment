package com.vahan.exchangeratesassigment.view.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vahan.exchangeratesassigment.R;
import com.vahan.exchangeratesassigment.service.Resource;
import com.vahan.exchangeratesassigment.service.model.DetailsSimpleModel;
import com.vahan.exchangeratesassigment.viewmodel.ExchangeRatesViewModel;

public class DetailsFragment extends Fragment {
	public static final String ORG_ID_KEY = "org_id_key";
	public static final String BANK_NAME_KEY = "bank_name_key";
	private ExchangeRatesViewModel exchangeRatesViewModel;
	private String orgId;
	private String bankeName;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		exchangeRatesViewModel = ViewModelProviders.of(this).get(ExchangeRatesViewModel.class);
		Bundle args = getArguments();
		if (args != null) {
			orgId = args.getString(ORG_ID_KEY);
			bankeName = args.getString(BANK_NAME_KEY);
		}
		exchangeRatesViewModel.loadBranchDetail(orgId);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.details_fragment_layout,container,false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		final TextView addres = view.findViewById(R.id.address);
		final TextView phone = view.findViewById(R.id.phone);
		final TextView name = view.findViewById(R.id.title);
		exchangeRatesViewModel.getDetailsMediatorLiveData().observe(this, new Observer<Resource<DetailsSimpleModel>>() {
			@Override
			public void onChanged(@Nullable Resource<DetailsSimpleModel> detailsResource) {
				if (detailsResource instanceof Resource.Success) {
					addres.setText(detailsResource.getData().getAddres());
					name.setText(bankeName);
					phone.setText(detailsResource.getData().getPhone());

				}
			}
		});
	}

	public static DetailsFragment newInstance(String orgId, String bankName) {
		Bundle args = new Bundle();
		args.putString(ORG_ID_KEY, orgId);
		args.putString(BANK_NAME_KEY, bankName);
		DetailsFragment fragment = new DetailsFragment();
		fragment.setArguments(args);
		return fragment;
	}
}
