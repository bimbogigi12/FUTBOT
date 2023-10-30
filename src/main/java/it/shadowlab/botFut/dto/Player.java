package it.shadowlab.botFut.dto;

public class Player {
	String name;
	int bidToBuy;
	int bidToSell;
	int bidToSellNow;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getBidToBuy() {
		return bidToBuy;
	}
	public void setBidToBuy(int bidToBuy) {
		this.bidToBuy = bidToBuy;
	}
	public int getBidToSell() {
		return bidToSell;
	}
	public void setBidToSell(int bidToSell) {
		this.bidToSell = bidToSell;
	}
	public int getBidToSellNow() {
		return bidToSellNow;
	}
	public void setBidToSellNow(int bidToSellNow) {
		this.bidToSellNow = bidToSellNow;
	}
	@Override
	public String toString() {
		return "Player [name=" + name + ", bidToBuy=" + bidToBuy + ", bidToSell=" + bidToSell + ", bidToSellNow="
				+ bidToSellNow + "]";
	}
	
	
}
