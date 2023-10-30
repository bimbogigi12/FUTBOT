package it.shadowlab.botFut;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.shadowlab.botFut.dto.Player;
import it.shadowlab.botFut.util.Costant;
import it.shadowlab.botFut.util.Util;

public class Bot extends Thread {

	static Logger log = LoggerFactory.getLogger(Bot.class);
	
	private Robot robot;
	private HashMap<String, Rectangle> rectangles = null;
	private HashMap<String, Dimension> positions = null;
	List<Player> players = new ArrayList<>();
	Player selectedPlayer;
	
	Date lastRelist = null;
	Date lastRefresh = null;
	
	Bot(Robot robot, int monitorSize){
		this.robot = robot;
		if (monitorSize == 1) {
			rectangles = Costant.SIZE1920_1080;
			positions = Costant.POSITION1920_1080;
		} else if (monitorSize == 2) {
			rectangles = Costant.SIZE3840_2160;
			positions = Costant.POSITION3840_2160;
		}
		
		loadPlayers();
	}
	
	private void loadPlayers() {
		
		Player reece = new Player(); 
		reece.setName("reece james");
		reece.setBidToBuy(2500);
		reece.setBidToSell(3000);
		reece.setBidToSellNow(3300);
		players.add(reece);
		
		Player foden = new Player();
		foden.setName("phil foden");
		foden.setBidToBuy(6400);
		foden.setBidToSell(7000);
		foden.setBidToSellNow(7350);
		players.add(foden);
		
		Player silva = new Player();
		silva.setName("thiago silva");
		silva.setBidToBuy(1800);
		silva.setBidToSell(2200);
		silva.setBidToSellNow(2300);
		players.add(silva);
		
		Player williamson = new Player();
		williamson.setName("lea williamson");
		williamson.setBidToBuy(6500);
		williamson.setBidToSell(7100);
		williamson.setBidToSellNow(7300);
		players.add(williamson);
		
		Player martinez = new Player();
		martinez.setName("emiliano martinez");
		martinez.setBidToBuy(6300);
		martinez.setBidToSell(7000);
		martinez.setBidToSellNow(7200);
		players.add(martinez);
		
		Player grealish = new Player();
		grealish.setName("jack grealish");
		grealish.setBidToBuy(5800);
		grealish.setBidToSell(6500);
		grealish.setBidToSellNow(7000);
		players.add(grealish);
		
		Player jota = new Player();
		jota.setName("diogo jota");
		jota.setBidToBuy(6000);
		jota.setBidToSell(6500);
		jota.setBidToSellNow(6900);
		players.add(grealish);
	} 
	
	public void setRobot(Robot robot) {
		this.robot = robot;
	}
	
	int currentMoney = 0;
	
	public void run() {
		while (true) {
			try {
				readCurrentMoney();
				
				getPlayerToBid();
				
				if (selectedPlayer != null) {
					auctPlayer();
					
				}
				
				checkTransferd();
				
				if (lastRelist == null || new Date().getTime() - lastRelist.getTime() > 1000*60*70) {
					relistPlayer();
				}
				
				unlockScreen();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void unlockScreen() {

		if (lastRefresh == null || new Date().getTime() - lastRefresh.getTime() > 1000 * 60 * 10) {
			robot.keyPress(KeyEvent.VK_F5);
			lastRefresh = new Date();
			Util.waitAction(5000);
			Util.click(robot, positions.get("CONFIRM_DIALOG"));
		}
	}
	
	private void readCurrentMoney() {
		//System.out.println("Reading money");
		String scurrentMoney = Util.Read(robot, rectangles.get("CURRENT_MONEY"), false).trim();
		if (StringUtils.isNotEmpty(scurrentMoney) && currentMoney != Util.convertToInt(scurrentMoney)) {
			currentMoney = Util.convertToInt(scurrentMoney);
			log.info("Current money: " + scurrentMoney);
			System.out.println("Current money: " + scurrentMoney);
		}
	}

	private void getPlayerToBid() {
		double rnd = Math.random()* players.size();
		int indexSelectedPlayer = (int) rnd;
		
		selectedPlayer = players.get(indexSelectedPlayer);
		if (selectedPlayer.getBidToBuy() > currentMoney) {
			selectedPlayer = null;
			return;
		}
		
		System.out.println(selectedPlayer);
	}
	
	private void auctPlayer() {
		// click Transfer
		Util.click(robot, positions.get("TRANSFER"));
		Util.waitAction(500);
		// to market
		Util.click(robot, positions.get("TO_MARKET"));
		
		Util.waitAction(1000);
		
		//search player
		Util.click(robot, positions.get("SEARCH"));
		Util.waitAction(500);
		Util.write(robot, selectedPlayer.getName());
		Util.waitAction(300);
		
		Util.click(robot, positions.get("SELECT_PLAYER"));
		Util.waitAction(300);
		
		Util.click(robot, positions.get("MAX_BID"));
		Util.waitAction(300);
		Util.write(robot, String.valueOf(selectedPlayer.getBidToBuy()-100));
		
		Util.waitAction(300);
		Util.click(robot, positions.get("SEARCH_AUCTION"));
		Util.waitAction(300);
		
		int actualbid = Util.convertToInt(Util.Read(robot, rectangles.get("LAST_BID"), true).trim());
		
		System.out.println("Actual bid "+actualbid);
		
		if (selectedPlayer.getBidToBuy() > actualbid) {
			Util.click(robot, positions.get("BID_TEXT"));
			Util.waitAction(300);
			Util.write(robot, String.valueOf(selectedPlayer.getBidToBuy()));
			Util.waitAction(300);
			Util.click(robot, positions.get("BID"));
		}
		
	}
	
	
	private void relistPlayer() {
		clearExpired();
		
		System.out.println("Re listing player");
		// click Transfer
		Util.click(robot, positions.get("TRANSFER"));
		Util.waitAction(500);
		Util.click(robot, positions.get("TRANSFER_LIST"));
		Util.waitAction(500);
		Util.click(robot, positions.get("CLEAR_SOLD"));
		Util.waitAction(300);
		Util.click(robot, positions.get("RELIST_ALL"));
		Util.waitAction(300);
		Util.click(robot, positions.get("CONFIRM_DIALOG"));
		lastRelist = new Date();
		
	}
	
	private void checkTransferd() {
		System.out.println("Checking transfered");
		Util.click(robot, positions.get("TRANSFER"));
		Util.waitAction(500);
		Util.click(robot, positions.get("TRANSFER_TARGET"));
		Util.waitAction(500);
		String wonItems = Util.Read(robot, rectangles.get("WON_ITEMS"), false);
		
		if ("WON ITEMS".equalsIgnoreCase(wonItems.trim()) || "WON LTEMS".equalsIgnoreCase(wonItems.trim())){
			
			String playerToSell = Util.Read(robot, rectangles.get("PLAYER_TO_SELL"), true).replace("\n", "");
			
			List<Player> playerConfgs = players.stream().filter(p -> 
			p.getName().toLowerCase().contains(playerToSell.toLowerCase())).collect(Collectors.toList());
			
			if (playerConfgs.size() > 0) {
				if (playerConfgs.size() > 1) {
					System.out.println("found more player for: "+playerToSell);	
				} else {
					System.out.println(playerConfgs.get(0));
					
					Util.click(robot, positions.get("LIST_TO_TRANSFER"));
					Util.waitAction(300);
					Util.click(robot, positions.get("START_PRICE"));
					Util.waitAction(300);
					Util.write(robot, String.valueOf(playerConfgs.get(0).getBidToSell()));
					Util.waitAction(300);
					Util.click(robot, positions.get("BUY_NOW"));
					Util.waitAction(300);
					Util.write(robot, String.valueOf(playerConfgs.get(0).getBidToSellNow()));
					Util.waitAction(300);
					Util.click(robot, positions.get("TRANSFER_PLAYER"));
					Util.waitAction(300);
				}
			} else {
				System.out.println("non player found for: "+playerToSell);
				
				
			}
		}
		
	}

	private void clearExpired() {
		boolean foundExpired = false;
		
		int offset = 0;
		
		Rectangle clearExp = rectangles.get("CLEAR_EXPIRED");
		
		while(!foundExpired && offset < 2000) {
			Rectangle readRect = new Rectangle((int)clearExp.getX(), (int)clearExp.getY()+offset, (int)clearExp.getWidth(), (int)clearExp.getHeight());
			
			String clearExpired = Util.Read(robot, readRect, false);
			
			if (clearExpired.toLowerCase().contains("clear expired")) {
				System.out.println("clear expired: "+(int)clearExp.getY()+offset);		
				foundExpired = true;
				Util.click(robot, new Dimension((int)clearExp.getY()+offset+10, (int)positions.get("RELIST_ALL").getHeight() ) );
				Util.waitAction(300);
				
			} else {
				offset+=20;
			}
		}
	}
	
}
