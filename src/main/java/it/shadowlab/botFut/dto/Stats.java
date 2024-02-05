package it.shadowlab.botFut.dto;

public class Stats {
	int bidCount;
	int tryedBuy;
	int winCount;
	boolean enabled;

	public int getBidCount() {
		return bidCount;
	}

	public void setBidCount(int bidCount) {
		this.bidCount = bidCount;
	}

	public int getTryedBuy() {
		return tryedBuy;
	}

	public void setTryedBuy(int tryedBuy) {
		this.tryedBuy = tryedBuy;
	}

	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	
}
