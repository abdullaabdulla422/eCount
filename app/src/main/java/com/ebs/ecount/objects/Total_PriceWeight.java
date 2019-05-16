package com.ebs.ecount.objects;

public class Total_PriceWeight {

	Double total_weight = 0.0;
	Double total_price = 0.0;
	int order = 0;

	public Total_PriceWeight(Double total_weight, Double total_price, int order) {
		this.total_weight = total_weight;
		this.total_price = total_price;
		this.order = order;
	}


	public Total_PriceWeight() {
	}


	public Double getTotal_weight() {
		return total_weight;
	}

	public void setTotal_weight(Double total_weight) {
		this.total_weight = total_weight;
	}

	public Double getTotal_price() {
		return total_price;
	}

	public void setTotal_price(Double total_price) {
		this.total_price = total_price;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
