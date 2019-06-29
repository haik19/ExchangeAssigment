package com.vahan.exchangeratesassigment.view.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.vahan.exchangeratesassigment.R;
import com.vahan.exchangeratesassigment.service.Resource;
import com.vahan.exchangeratesassigment.service.model.BankSimpleModel;
import com.vahan.exchangeratesassigment.view.adapter.ExchangesAdapter;
import com.vahan.exchangeratesassigment.viewmodel.ExchangeRatesViewModel;

import java.util.List;

public class ExchangesFragment extends Fragment {

    private ExchangesAdapter exchangesAdapter;
    private ProgressBar progressBar;
    private ExchangeRatesViewModel exchangeRatesViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exchangeRatesViewModel = ViewModelProviders.of(this).get(ExchangeRatesViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.exchanges_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        exchangesAdapter = new ExchangesAdapter(exchangeRatesViewModel);
        recyclerView.setAdapter(exchangesAdapter);
        progressBar = view.findViewById(R.id.progress_bar);

        Spinner currencySpinner = view.findViewById(R.id.currency_spinner);
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exchangeRatesViewModel.filterByCurrency((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        exchangeRatesViewModel.loadExchangesInfo("en");
        exchangeRatesViewModel.getExchangesMediatorLiveData().observe(this, new Observer<Resource<List<BankSimpleModel>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<BankSimpleModel>> exchangesInfoResource) {
                if (exchangesInfoResource instanceof Resource.Success) {
                    progressBar.setVisibility(View.GONE);
                    exchangesAdapter.setBankInfos(exchangesInfoResource.getData());
                } else if (exchangesInfoResource instanceof Resource.Loading) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public static ExchangesFragment newInstance() {
        Bundle args = new Bundle();
        ExchangesFragment fragment = new ExchangesFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
