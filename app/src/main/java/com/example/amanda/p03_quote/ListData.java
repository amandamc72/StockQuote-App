package com.example.amanda.p03_quote;

public class ListData {
	private String mv_symbol;
	private String mv_name;
	private String mv_price;
	private String mv_change;
	private String mv_percent;

	public ListData(){
	}

	public ListData(String symbol, String name, String price, String change, String percent){
		this.mv_symbol = symbol;
		this.mv_name = name;
		this.mv_price = price;
		this.mv_change = change;
		this.mv_percent = percent;
	}

	public String getSymbol(){
		return this.mv_symbol;
	}
	public String getName(){
		return this.mv_name;
	}
	public String getPrice(){
		return this.mv_price;
	}
	public String getChange(){
		return this.mv_change;
	}
	public String getPercent(){
		return this.mv_percent;
	}

	public void setSymbol(String symbol){
		this.mv_symbol = symbol;
	}
	public void setName(String name){
		this.mv_name = name;
	}
	public void setPrice(String price){
		this.mv_price = price;
	}
	public void setChange(String change){
		this.mv_change = change;
	}
	public void setPercent(String percent){
		this.mv_percent = percent;
	}
}


