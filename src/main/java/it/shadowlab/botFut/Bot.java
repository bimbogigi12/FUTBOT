package it.shadowlab.botFut;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.shadowlab.botFut.dto.Player;
import it.shadowlab.botFut.util.Costant;
import it.shadowlab.botFut.util.FutBinUtil;
import it.shadowlab.botFut.util.Util;

public class Bot extends Thread {

	static Logger logger = LoggerFactory.getLogger(Bot.class);

	private Robot robot;
	private HashMap<String, Rectangle> rectangles = null;
	private HashMap<String, Dimension> positions = null;
	List<Player> players = new ArrayList<>();
	Player selectedPlayer;

	Date lastRelist = null;
	Date lastRefresh = null;
	Date lastLoadPlayers = null;
	
	String username = "grassi.maurizio79@gmail.com";
	String password = "26Giugno2010";

	int bidCount = 0;
	
	int minMarketValue = 0;
	int minOverall = 85;

	boolean debug = false;

	Bot(Robot robot, int monitorSize) {
		this.robot = robot;
		if (monitorSize == 1) {
			rectangles = Costant.SIZE1920_1080;
			positions = Costant.POSITION1920_1080;
		} else if (monitorSize == 2) {
			rectangles = Costant.SIZE3840_2160;
			positions = Costant.POSITION3840_2160;
		}
		logger.info("Let' begin");
		
		Util.waitAction(5000);

	}

	private void loadPlayers() {

		if (players.isEmpty() || lastLoadPlayers == null || new Date().getTime() - lastLoadPlayers.getTime() > 1000 * 60 * 60 * 2) {
			if (!debug) {
				players = FutBinUtil.loadPlayers(robot, rectangles, positions);
				
				players = players.stream().filter(p->p.getMarketValue()>=minMarketValue).collect(Collectors.toList());
				
				players = players.stream().filter(p->p.getOverall()>=minOverall).collect(Collectors.toList());
				
			} else {

				Player p = new Player();
				p.setName("grealish");
				p.setBidToBuy(100);
				p.setBidToSell(10700);
				p.setBidToSellNow(12000);
				players.add(p);

				p = new Player();
				p.setName("bounou");
				p.setBidToBuy(100);
				p.setBidToSell(9800);
				p.setBidToSellNow(11600);
				players.add(p);

				p = new Player();
				p.setName("pope");
				p.setBidToBuy(100);
				p.setBidToSell(4600);
				p.setBidToSellNow(5000);
				players.add(p);

				p = new Player();
				p.setName("huth");
				p.setBidToBuy(100);
				p.setBidToSell(8800);
				p.setBidToSellNow(9600);
				players.add(p);
				
				p = new Player();
				p.setName("Reece james");
				p.setBidToBuy(100);
				p.setBidToSell(3300);
				p.setBidToSellNow(3600);
				players.add(p);
				
				p = new Player();
				p.setName("gnabry");
				p.setBidToBuy(100);
				p.setBidToSell(4000);
				p.setBidToSellNow(4500);
				players.add(p);
				
				p = new Player();
				p.setName("neville");
				p.setBidToBuy(3150);
				p.setBidToSell(3850);
				p.setBidToSellNow(4200);
				players.add(p);
				
				p = new Player();
				p.setName("kirby");
				p.setBidToBuy(3150);
				p.setBidToSell(3750);
				p.setBidToSellNow(4100);
				players.add(p);
				
				p = new Player();
				p.setName("onana");
				p.setBidToBuy(7100);
				p.setBidToSell(8700);
				p.setBidToSellNow(9500);
				players.add(p);
				
				try {
					DatabaseService.updatePlayers(new HashSet<>(players));
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
			lastLoadPlayers = new Date();
			Util.click(robot, positions.get("LOGIN"));
			checkLockSession();
		}

	}

	public void setRobot(Robot robot) {
		this.robot = robot;
	}

	int currentMoney = 0;

	public void run() {
		while (true) {
			try {

				loadPlayers();

				readCurrentMoney();

				getPlayerToBid();

				if (selectedPlayer != null) {
					auctPlayer();

				}

				// checkTransferd();

				if (lastRelist == null || new Date().getTime() - lastRelist.getTime() > 1000 * 60 * 60) {
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

	private void checkLockSession() {
		// Util.click(robot, positions.get("LOGIN"));
		Util.click(robot, positions.get("USER"));
		Util.waitAction(300);
		Util.write(robot, username);
		Util.click(robot, positions.get("PASSWORD"));
		Util.waitAction(300);
		Util.write(robot, password);
		Util.waitAction(300);
		Util.click(robot, positions.get("SIGNIN"));
	}

	private void unlockScreen() {

		if (lastRefresh == null || new Date().getTime() - lastRefresh.getTime() > 1000 * 60 * 5) {
			clearExpired();
			checkTransferd();
			//System.out.println("Refresh page");
			logger.debug("Refresh page");
			robot.keyPress(KeyEvent.VK_F5);
			Util.waitAction(5000);
			robot.keyPress(KeyEvent.VK_F5);
			lastRefresh = new Date();
			Util.waitAction(5000);
			//System.out.println("Waited");
			// Util.click(robot, positions.get("CONFIRM_DIALOG"));
			Util.click(robot, positions.get("LOGIN"));
			Util.waitAction(5000);
			checkLockSession();
		}
	}

	private void readCurrentMoney() {
		// System.out.println("Reading money");
		String scurrentMoney = Util.Read(robot, rectangles.get("CURRENT_MONEY"), false).trim();
		// System.out.println("Current money: " + scurrentMoney);
		if (StringUtils.isNotEmpty(scurrentMoney) && currentMoney != Util.convertToInt(scurrentMoney)) {
			currentMoney = Util.convertToInt(scurrentMoney);
			logger.debug("Current money: " + scurrentMoney);
			//System.out.println("Current money: " + scurrentMoney);
		}
	}

	private void getPlayerToBid() {
		if (currentMoney > 0) {
			if (selectedPlayer == null) {
				List<Player> playerBiddable = new ArrayList<Player>();
				/*
				 * players.stream().filter( p->p.getBidToBuy()<= currentMoney
				 * ).collect(Collectors.toList());
				 */

				for (Player p : players) {
					if (p.getBidToBuy() < currentMoney) {
						playerBiddable.add(p);
					}
				}

				if (!playerBiddable.isEmpty()) {
					double rnd = Math.random() * playerBiddable.size();
					int indexSelectedPlayer = (int) rnd;

					selectedPlayer = playerBiddable.get(indexSelectedPlayer);
					if (selectedPlayer.getBidToBuy() > currentMoney) {
						selectedPlayer = null;
						return;
					}

					//System.out.println(selectedPlayer);
					logger.debug(selectedPlayer.toString());
				}
			}
		}
	}

	private void auctPlayer() {
		// click Transfer
		Util.click(robot, positions.get("TRANSFER"));
		Util.waitAction(500);
		// to market
		Util.click(robot, positions.get("TO_MARKET"));

		Util.waitAction(1000);

		// search player
		Util.click(robot, positions.get("SEARCH"));
		Util.waitAction(500);
		Util.write(robot, selectedPlayer.getName());
		Util.waitAction(300);
		Util.click(robot, positions.get("SELECT_PLAYER"));
		Util.waitAction(300);

		Util.click(robot, positions.get("MAX_BID"));
		Util.waitAction(300);
		Util.write(robot, String.valueOf(selectedPlayer.getBidToBuy() - 100));

		Util.waitAction(300);
		Util.click(robot, positions.get("SEARCH_AUCTION"));
		Util.waitAction(300);

		String firstPlayerName = Util.normalizePlauerName(Util.Read(robot, rectangles.get("FIRST_BID"), false).trim());
		//System.out.println("bid for " + selectedPlayer.getName() + " found: " + firstPlayerName);
		logger.debug("bid for " + selectedPlayer.getName() + " found: " + firstPlayerName);

		Util.waitAction(3000);
		if (selectedPlayer.getName().toLowerCase().replace(" ", "").contains(Util.normalizePlauerName(firstPlayerName).toLowerCase())) {
			if (StringUtils.isNotEmpty(Util.Read(robot, rectangles.get("LAST_BID"), false))) {

				int actualbid = Util.convertToInt(Util.Read(robot, rectangles.get("LAST_BID"), false).trim());

				//System.out.println("Actual bid " + actualbid);
				logger.debug("Actual bid " + actualbid);

				if (selectedPlayer.getBidToBuy() > actualbid) {
					Util.click(robot, positions.get("BID_TEXT"));
					Util.waitAction(300);
					Util.write(robot, String.valueOf(selectedPlayer.getBidToBuy()));
					Util.waitAction(300);
					Util.click(robot, positions.get("BID"));
					bidCount++;
				}
				selectedPlayer = null;

				if (bidCount > 45) {
					bidCount = 0;
					Util.waitAction(30 * 1000);
					checkTransferd();
				}
			} else {
				selectedPlayer = null;
			}
		} else {
			selectedPlayer = null;
		}

	}

	private void relistPlayer() {
		checkTransferd();
		//System.out.println("Re listing player");
		logger.debug("Re listing player");
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

		//System.out.println("Checking transfered");
		logger.debug("Checking transfered");
		Util.click(robot, positions.get("TRANSFER"));
		Util.waitAction(500);
		Util.click(robot, positions.get("TRANSFER_TARGET"));
		Util.waitAction(500);

		boolean foundWon = false;
		int offset = 0;

		Rectangle wonItemsRect = rectangles.get("WON_ITEMS");

		while (!foundWon && offset < 2000) {
			String wonItems = Util.Read(robot, new Rectangle((int) wonItemsRect.getX(), (int) wonItemsRect.getY() + offset, (int) wonItemsRect.getWidth(), (int) wonItemsRect.getHeight()), false);
			
			//System.out.println("wonItems :"+wonItems);
			if ("WON ITEMS".equalsIgnoreCase(wonItems.trim()) || "WON LTEMS".equalsIgnoreCase(wonItems.trim())) {

				Rectangle playerToSellRect = rectangles.get("PLAYER_TO_SELL");

				String playerToSell = Util.Read(robot, new Rectangle((int) playerToSellRect.getX(), (int) playerToSellRect.getY() + offset, (int) playerToSellRect.getWidth(), (int) playerToSellRect.getHeight()), false).replace("\n", "");

				String playerToSellNormalized = Util.normalizePlauerName(playerToSell);

				if (StringUtils.isNotEmpty(playerToSellNormalized)) {
					//System.out.println("Won :"+playerToSellNormalized);
					logger.debug("Won :"+playerToSellNormalized);
				}
				
				if (StringUtils.isNotEmpty(playerToSellNormalized)) {
					List<Player> playerConfgs = players.stream().filter(p -> p.getName().toLowerCase().contains(playerToSellNormalized.toLowerCase())).collect(Collectors.toList());

					if (playerConfgs.size() > 0) {
						/*
						 * if (playerConfgs.size() > 1) {s System.out.println("found more player for: "
						 * + playerToSell); logger.debug("found more player for: " + playerToSell); }
						 * else {
						 */
						//System.out.println("Player to sell: " + playerConfgs.get(0));
						logger.debug("Player to sell: " + playerConfgs.get(0));

						sellPlayer(offset, playerToSellRect, playerConfgs.get(0));
						foundWon = true;
						// }
					} else {
						if (playerToSellNormalized.equalsIgnoreCase("Ahlal")) {
							playerConfgs = players.stream().filter(p -> p.getName().toLowerCase().contains("Ahlal".toLowerCase())).collect(Collectors.toList());
							sellPlayer(offset, playerToSellRect, playerConfgs.get(0));
						}

						//System.out.println("non player found for: " + playerToSellNormalized);
						logger.debug("non player found for: " + playerToSellNormalized);
						offset += 20;
					}
				} else {
					offset += 2000;
				}
			} else {
				offset += 5;
			}

		}
	}

	private void sellPlayer(int offset, Rectangle playerToSellRect, Player playerConfgs) {
		Util.click(robot, new Dimension((int) playerToSellRect.getY() + offset + 20, (int) playerToSellRect.getX()));
		Util.waitAction(3000);
		Util.click(robot, positions.get("LIST_TO_TRANSFER"));
		Util.waitAction(300);
		Util.click(robot, positions.get("START_PRICE"));
		Util.waitAction(300);
		Util.write(robot, String.valueOf(playerConfgs.getBidToSell()));
		Util.waitAction(300);
		Util.click(robot, positions.get("BUY_NOW"));
		Util.waitAction(300);
		Util.write(robot, String.valueOf(playerConfgs.getBidToSellNow()));
		Util.waitAction(300);
		Util.click(robot, positions.get("TRANSFER_PLAYER"));
		Util.waitAction(300);
		checkTransferd();
	}

	private void clearExpired() {
		boolean foundExpired = false;

		int offset = 0;

		Rectangle clearExp = rectangles.get("CLEAR_EXPIRED");

		while (!foundExpired && offset < 2000) {
			Rectangle readRect = new Rectangle((int) clearExp.getX(), (int) clearExp.getY() + offset, (int) clearExp.getWidth(), (int) clearExp.getHeight());

			String clearExpired = Util.Read(robot, readRect, false);

			if (clearExpired.toLowerCase().contains("clear expired")) {
				// System.out.println("clear expired: " + (int) clearExp.getY() + offset);
				foundExpired = true;
				Util.click(robot, new Dimension((int) clearExp.getY() + offset + 10, (int) positions.get("RELIST_ALL").getHeight()));
				Util.waitAction(300);

			} else {
				offset += 20;
			}
		}
	}

}
