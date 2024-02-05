package it.shadowlab.botFut.util;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.HashMap;

public class Costant {
	
	public static int MAX_BID_COUNT = 10;

	public static String FOLDER_FUT = "d:/temp/FUT";
	
	public static HashMap<String, Rectangle> SIZE1920_1080 = new HashMap<>();
	public static HashMap<String, Rectangle> SIZE3840_2160 = new HashMap<>();

	public static HashMap<String, Dimension> POSITION1920_1080 = new HashMap<>();
	public static HashMap<String, Dimension> POSITION3840_2160 = new HashMap<>();
	
	private static int left = 10;
	private static int top = 60;

	static {
		SIZE1920_1080.put("CURRENT_MONEY", new Rectangle(1570, 166, 170, 17));
		
		
		POSITION1920_1080.put("TRANSFER", new Dimension(400, 100)); 
		POSITION1920_1080.put("SEARCH", new Dimension(400, 1000));

		
		SIZE3840_2160.put("CURRENT_MONEY", new Rectangle(3475, 173, 120, 27));
		SIZE3840_2160.put("LAST_BID", new Rectangle(2250, 920, 170, 40));
		SIZE3840_2160.put("TRANSFER_ITEM", new Rectangle(2130, 340, 60, 50));
		SIZE3840_2160.put("ACTUAL_BID", new Rectangle(1530, 670, 60, 50));
		
		SIZE3840_2160.put("WON_ITEMS", new Rectangle(1360, 600, 200, 40));
		SIZE3840_2160.put("PLAYER_TO_SELL", new Rectangle(1470, 670, 200, 30));
		SIZE3840_2160.put("CLEAR_EXPIRED", new Rectangle(2090, 900, 120, 30));
		SIZE3840_2160.put("FIRST_BID", new Rectangle(1470, 392, 120, 20));
		SIZE3840_2160.put("UNSOLD_ITEM", new Rectangle(1470, 660, /*200, 200));*/120, 20));
		
		//Dialog
		SIZE3840_2160.put("LIMIT_DIALOG_TEXT", new Rectangle(1850, 1010, 180, 50));
		POSITION3840_2160.put("LIMIT_DIALOG_OK", new Dimension(1200, 2000));
		
		
		POSITION3840_2160.put("TRANSFER", new Dimension(440, 70));
		POSITION3840_2160.put("TO_MARKET", new Dimension(430, 1600));
		POSITION3840_2160.put("SEARCH", new Dimension(470, 1500));
		POSITION3840_2160.put("SELECT_PLAYER", new Dimension(550, 1500));
		POSITION3840_2160.put("MAX_BID", new Dimension(800+top, 2100));
		POSITION3840_2160.put("SEARCH_AUCTION", new Dimension(1950, 2100));
		POSITION3840_2160.put("BID_TEXT", new Dimension(930, 2400));
		POSITION3840_2160.put("BID", new Dimension(990, 2400));
		POSITION3840_2160.put("TRANSFER_LIST", new Dimension(430, 2300));
		POSITION3840_2160.put("RELIST_ALL", new Dimension(600, 2120));
		POSITION3840_2160.put("CONFIRM_DIALOG", new Dimension(1205, 1880));
		POSITION3840_2160.put("TRANSFER_TARGET", new Dimension(630, 1600));
		POSITION3840_2160.put("LIST_TO_TRANSFER", new Dimension(910, 2250));
		POSITION3840_2160.put("START_PRICE", new Dimension(1025, 2300));
		POSITION3840_2160.put("BUY_NOW", new Dimension(1130, 2300));
		POSITION3840_2160.put("TRANSFER_PLAYER", new Dimension(1235, 2250));
		POSITION3840_2160.put("UNSOLD_ITEM", new Dimension(650, 1520));
		POSITION3840_2160.put("CLEAR_SOLD", new Dimension(395, 2110));
		POSITION3840_2160.put("LOGIN", new Dimension(1180, 2260));
		POSITION3840_2160.put("DOWN_TRANSFER_TARGETS", new Dimension(1950, 2220));
		
		
		POSITION3840_2160.put("USER", new Dimension(390, 1800));
		POSITION3840_2160.put("PASSWORD", new Dimension(460, 1800));
		POSITION3840_2160.put("SIGNIN", new Dimension(600, 1800));
		POSITION3840_2160.put("URL", new Dimension(60, 200));
		
		
		//FUTBIN
		SIZE3840_2160.put("PLAYER_NAME_FUTBIN", new Rectangle(1455, 470, 250, 20));
		SIZE3840_2160.put("OVERALL_FUTBIN", new Rectangle(1728, 470, 20, 20));
		SIZE3840_2160.put("VALUE_FUTBIN", new Rectangle(1791, 470, 49, 20));
	}
}
