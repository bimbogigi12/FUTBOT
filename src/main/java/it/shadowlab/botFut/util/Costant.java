package it.shadowlab.botFut.util;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.HashMap;

public class Costant {

	public static HashMap<String, Rectangle> SIZE1920_1080 = new HashMap<>();
	public static HashMap<String, Rectangle> SIZE3840_2160 = new HashMap<>();

	public static HashMap<String, Dimension> POSITION1920_1080 = new HashMap<>();
	public static HashMap<String, Dimension> POSITION3840_2160 = new HashMap<>();

	static {
		SIZE1920_1080.put("CURRENT_MONEY", new Rectangle(1570, 170, 170, 17));
		
		
		POSITION1920_1080.put("TRANSFER", new Dimension(400, 100)); 
		POSITION1920_1080.put("SEARCH", new Dimension(400, 1000));
		
		
				
		
		SIZE3840_2160.put("CURRENT_MONEY", new Rectangle(3500, 160, 95, 27));
		SIZE3840_2160.put("LAST_BID", new Rectangle(2280, 900, 200, 40));
		SIZE3840_2160.put("LAST_BID", new Rectangle(2280, 900, 200, 40));
		SIZE3840_2160.put("WON_ITEMS", new Rectangle(1360, 790, 200, 40));
		SIZE3840_2160.put("PLAYER_TO_SELL", new Rectangle(1470, 840, 200, 40));
		SIZE3840_2160.put("CLEAR_EXPIRED", new Rectangle(2070, 900, 120, 40));
		
		POSITION3840_2160.put("TRANSFER", new Dimension(450, 70));
		POSITION3840_2160.put("TO_MARKET", new Dimension(430, 1600));
		POSITION3840_2160.put("SEARCH", new Dimension(470, 1500));
		POSITION3840_2160.put("SELECT_PLAYER", new Dimension(550, 1500));
		POSITION3840_2160.put("MAX_BID", new Dimension(850, 2100));
		POSITION3840_2160.put("SEARCH_AUCTION", new Dimension(1950, 2100));
		POSITION3840_2160.put("BID_TEXT", new Dimension(930, 2400));
		POSITION3840_2160.put("BID", new Dimension(1000, 2400));
		POSITION3840_2160.put("TRANSFER_LIST", new Dimension(430, 2300));
		POSITION3840_2160.put("RELIST_ALL", new Dimension(585, 2180));
		POSITION3840_2160.put("CONFIRM_DIALOG", new Dimension(1190, 1880));
		POSITION3840_2160.put("TRANSFER_TARGET", new Dimension(630, 1600));
		POSITION3840_2160.put("LIST_TO_TRANSFER", new Dimension(900, 2300));
		POSITION3840_2160.put("START_PRICE", new Dimension(1020, 2300));
		POSITION3840_2160.put("BUY_NOW", new Dimension(1120, 2300));
		POSITION3840_2160.put("TRANSFER_PLAYER", new Dimension(1240, 2300));
		POSITION3840_2160.put("CLEAR_SOLD", new Dimension(385, 2180));
	}
}
