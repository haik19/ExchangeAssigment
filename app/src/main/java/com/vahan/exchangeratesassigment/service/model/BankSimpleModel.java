package com.vahan.exchangeratesassigment.service.model;

public class BankSimpleModel {

	private String name;
	private String bankId;
	private String buyValue;
	private String salleValue;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBuyValue() {
		return buyValue;
	}

	public void setBuyValue(String buyValue) {
		this.buyValue = buyValue;
	}

	public String getSaleValue() {
		return salleValue;
	}

	public void setSalleValue(String salleValue) {
		this.salleValue = salleValue;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
}
