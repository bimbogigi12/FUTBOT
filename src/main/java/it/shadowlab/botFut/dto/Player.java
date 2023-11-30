package it.shadowlab.botFut.dto;

import java.util.Date;
import java.util.Objects;

public class Player {
	String name;
	int marketValue;
	int bidToBuy;
	int bidToSell;
	int bidToSellNow;
	String href;
	int overall;
	Date lastUpdate= null;
	
	
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
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public int getOverall() {
		return overall;
	}
	public void setOverall(int overall) {
		this.overall = overall;
	}
	
	public int getMarketValue() {
		return marketValue;
	}
	public void setMarketValue(int marketValue) {
		this.marketValue = marketValue;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	@Override
	public String toString() {
		return "Player [name=" + name + ", marketValue=" + marketValue + ", bidToBuy=" + bidToBuy + ", bidToSell=" + bidToSell + ", bidToSellNow=" + bidToSellNow + ", href=" + href + ", overall=" + overall + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return Objects.equals(name, other.name);
	}
	
	
	
}
