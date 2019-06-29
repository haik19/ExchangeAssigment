package com.vahan.exchangeratesassigment.view.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vahan.exchangeratesassigment.R;
import com.vahan.exchangeratesassigment.service.model.BankSimpleModel;
import com.vahan.exchangeratesassigment.view.ui.DetailActivity;
import com.vahan.exchangeratesassigment.view.ui.DetailsFragment;
import com.vahan.exchangeratesassigment.viewmodel.ExchangeRatesViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExchangesAdapter extends RecyclerView.Adapter<ExchangesAdapter.ExchangeViewHolder> {

    private List<BankSimpleModel> bankSimpleModels;
    private ExchangeRatesViewModel exchangeRatesViewModel;

    public ExchangesAdapter(ExchangeRatesViewModel exchangeRatesViewModel){
        bankSimpleModels = new ArrayList<>();
        this.exchangeRatesViewModel = exchangeRatesViewModel;
    }

    @NonNull
    @Override
    public ExchangeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exchange_item,viewGroup,false);
        return new ExchangeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExchangeViewHolder exchangeViewHolder, int i) {
        final BankSimpleModel bankInfo = bankSimpleModels.get(i);
        exchangeViewHolder.bankName.setText(bankInfo.getName());
        exchangeViewHolder.sellValue.setText(bankInfo.getSaleValue() );
        exchangeViewHolder.buyValue.setText(bankInfo.getBuyValue());
        exchangeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra(DetailsFragment.ORG_ID_KEY, bankInfo.getBankId());
                intent.putExtra(DetailsFragment.BANK_NAME_KEY, bankInfo.getName());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bankSimpleModels.size();
    }

    static class ExchangeViewHolder extends RecyclerView.ViewHolder{
        TextView bankName;
        TextView distance;
        TextView buyValue;
        TextView sellValue;

        public ExchangeViewHolder(@NonNull View itemView) {
            super(itemView);
            bankName = itemView.findViewById(R.id.bank_name);
            buyValue = itemView.findViewById(R.id.buy_value);
            sellValue = itemView.findViewById(R.id.sell_value);
        }
    }

    public void setBankInfos(List<BankSimpleModel> infos) {
        bankSimpleModels.clear();
        bankSimpleModels.addAll(infos);
        notifyDataSetChanged();
    }
}
