package it.shadowlab.botFut;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.shadowlab.botFut.dto.Player;
import it.shadowlab.botFut.dto.Stats;
import it.shadowlab.botFut.util.Config;
import it.shadowlab.botFut.util.Costant;
import it.shadowlab.botFut.util.FutBinUtil;
import it.shadowlab.botFut.util.Util;

public class Bot extends Thread {

	static Logger logger = LoggerFactory.getLogger(Bot.class);

	String[] HEADERS = { "Name", "Count", "Bid", "Won", "Enabled" };

	private Robot robot;
	private HashMap<String, Rectangle> rectangles = null;
	private HashMap<String, Dimension> positions = null;
	List<Player> players = new ArrayList<>();
	List<Player> allPlayers = new ArrayList<>();
	Player selectedPlayer;

	HashMap<String, Stats> stats = new HashMap<>();

	Date lastRelist = null;
	Date lastRefresh = null;
	Date lastLoadPlayers = null;

	String username = "";
	String password = "";

	// int minMarketValue = 10000;
	int minMarketValue = 0;
	int minOverall = 86;
	int minDefaultOverall = 0;

	boolean debug = false;
	boolean filterPlayerByRange = false;
	boolean checkStat = false;

	boolean playersLoaded = false;
	boolean isResellingPlayersByMarket = false;

	Config configs = null;

	Bot(Robot robot) {
		this.robot = robot;

		int monitorSize = loadProperties();
		if (monitorSize == 1) {
			rectangles = Costant.SIZE1920_1080;
			positions = Costant.POSITION1920_1080;
		} else if (monitorSize == 2) {
			rectangles = Costant.SIZE3840_2160;
			positions = Costant.POSITION3840_2160;
		}
		logger.info("Let' begin");
		try {
			loadStats();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Util.waitAction(2000);

	}

	private int loadProperties() {
		configs = new Config();

		int monitorSize = configs.getMonitorSize();
		minDefaultOverall = configs.getMinOverall();
		lastLoadPlayers = configs.getLastLoadPlayer();
		username = configs.getUsername();
		password = configs.getPassword();
		return monitorSize;
	}

	private void loadPlayers() {

		if (players.isEmpty() || lastLoadPlayers == null || new Date().getTime() - lastLoadPlayers.getTime() > 1000 * 60 * 60 * 4) {
			if (!debug) {
				allPlayers = FutBinUtil.loadPlayers(robot, rectangles, positions, configs);

			} else {

				Player p = new Player();
				p.setName("grealish");
				p.setBidToBuy(100);
				p.setBidToSell(10700);
				p.setBidToSellNow(12000);
				players.add(p);
				allPlayers.add(p);

				p = new Player();
				p.setName("bounou");
				p.setBidToBuy(100);
				p.setBidToSell(9800);
				p.setBidToSellNow(11600);
				players.add(p);
				allPlayers.add(p);

				p = new Player();
				p.setName("pope");
				p.setBidToBuy(100);
				p.setBidToSell(4600);
				p.setBidToSellNow(5000);
				players.add(p);
				allPlayers.add(p);

				p = new Player();
				p.setName("huth");
				p.setBidToBuy(100);
				p.setBidToSell(8800);
				p.setBidToSellNow(9600);
				players.add(p);
				allPlayers.add(p);

				p = new Player();
				p.setName("Reece james");
				p.setBidToBuy(100);
				p.setBidToSell(3300);
				p.setBidToSellNow(3600);
				allPlayers.add(p);

				p = new Player();
				p.setName("gnabry");
				p.setBidToBuy(100);
				p.setBidToSell(4000);
				p.setBidToSellNow(4500);
				players.add(p);
				allPlayers.add(p);

				p = new Player();
				p.setName("neville");
				p.setBidToBuy(3150);
				p.setBidToSell(3850);
				p.setBidToSellNow(4200);
				players.add(p);
				allPlayers.add(p);

				p = new Player();
				p.setName("kirby");
				p.setBidToBuy(3150);
				p.setBidToSell(3750);
				p.setBidToSellNow(4100);
				players.add(p);
				allPlayers.add(p);

				p = new Player();
				p.setName("onana");
				p.setBidToBuy(7100);
				p.setBidToSell(8700);
				p.setBidToSellNow(9500);
				players.add(p);
				allPlayers.add(p);

				p = new Player();
				p.setName("Goretzka");
				p.setBidToBuy(4100);
				p.setBidToSell(4900);
				p.setBidToSellNow(5300);
				players.add(p);
				allPlayers.add(p);

				p = new Player();
				p.setName("Magull");
				p.setBidToBuy(10000);
				p.setBidToSell(12100);
				p.setBidToSellNow(13200);
				players.add(p);
				allPlayers.add(p);

				p = new Player();
				p.setName("kimmich");
				p.setBidToBuy(16000);
				p.setBidToSell(20000);
				p.setBidToSellNow(22000);
				players.add(p);
				allPlayers.add(p);

				p = new Player();
				p.setName("Alex Morgan");
				p.setBidToBuy(27000);
				p.setBidToSell(33000);
				p.setBidToSellNow(36000);
				players.add(p);
				allPlayers.add(p);

				p = new Player();
				p.setName("Maignan");
				p.setBidToBuy(10000);
				p.setBidToSell(12000);
				p.setBidToSellNow(13000);
				players.add(p);
				allPlayers.add(p);

				p = new Player();
				p.setName("Maignan");
				p.setBidToBuy(54000);
				p.setBidToSell(66000);
				p.setBidToSellNow(72000);
				players.add(p);
				allPlayers.add(p);

			}

			playersLoaded = true;
			lastLoadPlayers = new Date();
			configs.setLastLoadPlayer(lastLoadPlayers);
			Util.click(robot, positions.get("LOGIN"));
			checkLockSession();
		}

	}

	private List<Player> removeBlackList(List<Player> players) {
		List<String> blackList = Stream.of("BARNES", "SMITH").collect(Collectors.toList());

		List<Player> playersFiltered = new LinkedList();

		for (Player p : players) {

			boolean isBlacked = false;
			for (String blacked : blackList) {
				isBlacked |= p.getName().toUpperCase().contains(blacked.toUpperCase());
			}

			if (!isBlacked) {
				playersFiltered.add(p);
			}
		}

		return playersFiltered;
	}

	public void setRobot(Robot robot) {
		this.robot = robot;
	}

	int currentMoney = 0;

	public void run() {
		while (true) {
			try {

				checkDialog();

				loadPlayers();

				readCurrentMoney();

				Util.click(robot, positions.get("TRANSFER"));
				Util.waitAction(500);
				if (getActualBid() > 49) {
					logger.debug("Checking transfer by main loop");
					clearExpired();
					checkTransferd();
				}

				if (lastRelist == null || new Date().getTime() - lastRelist.getTime() > 1000 * 60 * 30) {
					relistPlayer();
					configs.saveConfig();
				}

				unlockScreen();

				getPlayerToBid();

				if (selectedPlayer != null) {
					auctPlayer();

				} else {
					logger.debug("No player to bid");
				}

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
		for (int i = 0; i < 20; i++) {
			robot.keyPress(KeyEvent.VK_CANCEL);
			robot.keyPress(KeyEvent.VK_BACK_SPACE);
		}

		Util.write(robot, username);
		Util.write(robot, "gmail.com");
		Util.click(robot, positions.get("PASSWORD"));
		Util.waitAction(300);
		Util.write(robot, password);
		Util.waitAction(300);
		Util.click(robot, positions.get("SIGNIN"));
	}

	private void unlockScreen() {
	}

	private void printStats() {
		for (Map.Entry<String, Stats> entry : stats.entrySet()) {
			Stats stat = entry.getValue();
			String playerName = entry.getKey();
			logger.debug(playerName + " Selected: " + stat.getBidCount() + " bidded " + stat.getTryedBuy() + " won " + stat.getWinCount());
		}
		try {
			if (!debug) {
				saveStats();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readCurrentMoney() {
		String scurrentMoney = Util.Read(robot, rectangles.get("CURRENT_MONEY"), false).trim();
		if (StringUtils.isNotEmpty(scurrentMoney) && currentMoney != Util.convertToInt(scurrentMoney)) {
			currentMoney = Util.convertToInt(scurrentMoney);

			/*
			 * if (currentMoney < 10000) { minMarketValue = 0; } else if (currentMoney <
			 * 20000) { minMarketValue = 100000; } else if (currentMoney < 50000) {
			 * minMarketValue = 200000; } else { minMarketValue = 0; }
			 */

			logger.debug("Current money: " + currentMoney);
			// System.out.println("Current money: " + scurrentMoney);
		}
	}

	private void getPlayerToBid() {
		if (currentMoney > 0) {
			if (selectedPlayer == null) {
				List<Player> playerBiddable = new ArrayList<Player>();

				players = allPlayers.stream().filter(p -> p.getMarketValue() >= minMarketValue).collect(Collectors.toList());

				if (!debug)
					players = players.stream().filter(p -> p.getOverall() >= minOverall).collect(Collectors.toList());

				players = removeBlackList(players);

				int maxCurrency = currentMoney;

				final AtomicInteger minCurrency = new AtomicInteger(maxCurrency);

				do {

					if (filterPlayerByRange) {
						minCurrency.set(minCurrency.get() - 20000);
						if (minCurrency.get() < 0)
							minCurrency.set(0);
					} else {
						minCurrency.set(0);
					}

					playerBiddable = players.stream().filter(p -> (p.getBidToBuy() <= maxCurrency && p.getBidToBuy() >= minCurrency.get())).collect(Collectors.toList());
				} while (playerBiddable.size() == 0 && minCurrency.get() != 0);

				/*
				 * for (Player p : players) { if (p.getBidToBuy() < currentMoney) {
				 * playerBiddable.add(p); } }
				 */

				if (!playerBiddable.isEmpty()) {
					double rnd = Math.random() * playerBiddable.size();
					int indexSelectedPlayer = (int) rnd;

					selectedPlayer = playerBiddable.get(indexSelectedPlayer);
					Stats playerStat = stats.get(selectedPlayer.getName());
					if (selectedPlayer.getBidToBuy() > currentMoney) {
						if (playerStat != null && !playerStat.isEnabled() && checkStat) {
							logger.debug("Player disabled " + selectedPlayer);
							logger.debug(playerStat.toString());
							getPlayerToBid();
						} else {
							selectedPlayer = null;
							return;
						}
					}

					// System.out.println(selectedPlayer);
					logger.debug(selectedPlayer.toString());
					if (playerStat != null) {
						logger.debug(playerStat.toString());
					}
				}
			}
		}
	}

	private void auctPlayer() {

		int levelBuy = 0;

		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ) {
			levelBuy = 3;
		} else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			levelBuy = 1;
		}

		// click Transfer
		Util.click(robot, positions.get("TRANSFER"));
		Util.waitAction(500);

		int remainingBid = 49 - getActualBid();

		if (canBeTransferedPlayer(97) && remainingBid > 0) {
			boolean canBuy = true;

			switch (levelBuy) {
			case 0:
				break;
			case 1:
			case 2:
				if (!canBeTransferedPlayer(90)) {
					canBuy = false;
				}
				break;
			case 3:
				canBuy = false;
				break;
			}

			if (!canBuy) return;
			
			String playerName = selectedPlayer.getName();
			Stats stat = stats.get(playerName);
			if (stat == null) {
				stat = new Stats();
			}

			// to market
			Util.click(robot, positions.get("TO_MARKET"));

			Util.waitAction(1000);

			// search player
			Util.click(robot, positions.get("SEARCH"));
			Util.waitAction(500);
			Util.write(robot, playerName);
			Util.waitAction(300);
			Util.click(robot, positions.get("SELECT_PLAYER"));
			Util.waitAction(300);

			Util.click(robot, positions.get("MAX_BID"));
			Util.waitAction(300);
			Util.write(robot, String.valueOf(selectedPlayer.getBidToBuy() - 100));

			stat.setTryedBuy(stat.getTryedBuy() + 1);

			Util.waitAction(300);
			Util.click(robot, positions.get("SEARCH_AUCTION"));
			Util.waitAction(300);

			String firstPlayerName = Util.normalizePlauerName(Util.Read(robot, rectangles.get("FIRST_BID"), false).trim());
			// System.out.println("bid for " + selectedPlayer.getName() + " found: " +
			// firstPlayerName);
			logger.debug("bid for " + selectedPlayer.getName() + " found: " + firstPlayerName);

			Util.waitAction(3000);
			if (StringUtils.isNotEmpty(firstPlayerName) && selectedPlayer.getName().toLowerCase().replace(" ", "").contains(Util.normalizePlauerName(firstPlayerName).replace(" ", "").toLowerCase())) {
				int offset = 0;
				stat.setBidCount(stat.getTryedBuy() + 1);
				double rnd = Math.random() * Math.min(Costant.MAX_BID_COUNT, remainingBid);
				int bidCOunt = 3 + (int) rnd;
				logger.debug("trying " + bidCOunt + " bid");
				for (int i = 0; i < bidCOunt; i++) {
					checkDialog();
					readCurrentMoney();
					if (currentMoney > selectedPlayer.getBidToBuy()) {

						if (StringUtils.isNotEmpty(Util.Read(robot, rectangles.get("LAST_BID"), false))) {
							Util.click(robot, new Dimension((int) rectangles.get("FIRST_BID").getY() + offset, (int) (rectangles.get("FIRST_BID").getX())));
							Util.waitAction(300);
							int actualbid = Util.convertToInt(Util.Read(robot, rectangles.get("LAST_BID"), false).trim());

							Rectangle timeToExpire = rectangles.get("REMAINING_TIME");

							String remainingTime = Util.Read(robot, new Rectangle((int) timeToExpire.getX(), (int) timeToExpire.getY() + offset, (int) timeToExpire.getWidth(), (int) timeToExpire.getHeight()), false).trim();

							// System.out.println("Actual bid " + actualbid);
							logger.debug("Actual bid " + actualbid + " remainig time " + remainingTime);

							if (selectedPlayer.getBidToBuy() > actualbid && (remainingTime.toUpperCase().contains("MINUTE") || remainingTime.toUpperCase().contains("SECOND"))) {
								Util.click(robot, positions.get("BID_TEXT"));
								Util.waitAction(300);
								Util.write(robot, String.valueOf(selectedPlayer.getBidToBuy()));
								Util.waitAction(300);
								Util.click(robot, positions.get("BID"));

								Util.waitAction(500);
							}

						}
						offset += 125;
					} else {
						logger.debug("not enough money");
					}
				}
				selectedPlayer = null;
			} else {
				selectedPlayer = null;
			}

			stats.put(playerName, stat);
			logger.debug(stat.toString());
		}

	}

	private void relistPlayer() {

		if (!isResellingPlayersByMarket) {
			logger.debug("Checking transfer by relist player");
			clearExpired();
			checkTransferd();
		}
		// System.out.println("Re listing player");
		logger.debug("Re listing player");
		// click Transfer
		Util.click(robot, positions.get("TRANSFER"));
		Util.waitAction(500);
		Util.click(robot, positions.get("TRANSFER_LIST"));
		Util.waitAction(500);

		Util.click(robot, positions.get("CLEAR_SOLD"));
		Util.waitAction(300);
		if (playersLoaded) {
			isResellingPlayersByMarket = true;
			logger.debug("Re listing by market");

			String playerSold = Util.normalizePlauerName(Util.Read(robot, rectangles.get("UNSOLD_ITEM"), false).replace("\n", ""));

			logger.debug("Relist: " + playerSold);

			List<Player> playerToSellList = new ArrayList<>();
			if (!StringUtils.isEmpty(playerSold)) {
				playerToSellList = allPlayers.stream().filter(p -> Util.normalizePlauerName(p.getName().toUpperCase()).contains(playerSold.replaceAll(" ", "").toUpperCase())).collect(Collectors.toList());
			}

			if (playerToSellList.size() > 0) {

				Player playerToSell = playerToSellList.get(0);
				logger.debug(playerToSell.toString());
				Util.click(robot, positions.get("UNSOLD_ITEM"));
				Util.waitAction(3000);
				Util.click(robot, positions.get("LIST_TO_TRANSFER"));
				Util.waitAction(300);
				Util.click(robot, positions.get("START_PRICE"));
				Util.waitAction(300);
				Util.write(robot, String.valueOf(playerToSell.getBidToSell()));
				Util.waitAction(300);
				Util.click(robot, positions.get("BUY_NOW"));
				Util.waitAction(300);
				Util.write(robot, String.valueOf(playerToSell.getBidToSellNow()));
				Util.waitAction(300);
				Util.click(robot, positions.get("TRANSFER_PLAYER"));
				Util.waitAction(300);
				relistPlayer();
			} else {
				if (StringUtils.isEmpty(playerSold)) {
					logger.debug("No more player to relist");
					isResellingPlayersByMarket = false;
				} else {
					Util.click(robot, positions.get("UNSOLD_ITEM"));
					Util.waitAction(300);
					Util.click(robot, positions.get("LIST_TO_TRANSFER"));
					Util.waitAction(300);
					Util.click(robot, positions.get("TRANSFER_PLAYER"));
					Util.waitAction(300);
					relistPlayer();
				}
				playersLoaded = false;
			}
		} else {
			logger.debug("Re listing all");
			Util.click(robot, positions.get("RELIST_ALL"));
			Util.waitAction(300);
			Util.click(robot, positions.get("CONFIRM_DIALOG"));
		}
		lastRelist = new Date();

	}

	private void checkTransferd() {

		// System.out.println("Checking transfered");
		logger.debug("Checking transfered");
		Util.click(robot, positions.get("TRANSFER"));
		Util.waitAction(500);

		checkDialog();

		if (getActualBid() == 0)
			return;

		if (canBeTransferedPlayer(99)) {

			Util.click(robot, positions.get("TRANSFER_TARGET"));
			Util.waitAction(500);

			boolean foundWon = false;

			for (int i = 0; i < 4 && !foundWon; i++) {

				if (i > 0) {
					Util.click(robot, positions.get("DOWN_TRANSFER_TARGETS"));
					Util.waitAction(500);
				}
				int offset = 0;

				Rectangle wonItemsRect = rectangles.get("WON_ITEMS");

				while (!foundWon && offset < 2000) {
					String wonItems = Util.Read(robot, new Rectangle((int) wonItemsRect.getX(), (int) wonItemsRect.getY() + offset, (int) wonItemsRect.getWidth(), (int) wonItemsRect.getHeight()), false);

					// System.out.println("wonItems :"+wonItems);
					if ("WON ITEMS".equalsIgnoreCase(wonItems.trim()) || "WON LTEMS".equalsIgnoreCase(wonItems.trim())) {

						Rectangle playerToSellRect = rectangles.get("PLAYER_TO_SELL");

						String playerToSell = Util.Read(robot, new Rectangle((int) playerToSellRect.getX(), (int) playerToSellRect.getY() + offset, (int) playerToSellRect.getWidth(), (int) playerToSellRect.getHeight()), false).replace("\n", "");

						String playerToSellNormalized = Util.normalizePlauerName(playerToSell);

						if (StringUtils.isNotEmpty(playerToSellNormalized)) {
							// System.out.println("Won :"+playerToSellNormalized);
							logger.debug("Won :" + playerToSellNormalized);

						}

						if (StringUtils.isNotEmpty(playerToSellNormalized)) {
							List<Player> playerConfgs = allPlayers.stream().filter(p -> p.getName().toLowerCase().contains(playerToSellNormalized.toLowerCase())).collect(Collectors.toList());

							if (playerConfgs.size() > 0) {
								/*
								 * if (playerConfgs.size() > 1) {s System.out.println("found more player for: "
								 * + playerToSell); logger.debug("found more player for: " + playerToSell); }
								 * else {
								 */
								// System.out.println("Player to sell: " + playerConfgs.get(0));
								logger.debug("Player to sell: " + playerConfgs.get(0));

								sellPlayer(offset, playerToSellRect, playerConfgs.get(0));

								Stats stat = stats.get(playerConfgs.get(0).getName());
								if (stat == null) {
									stat = new Stats();
								}
								stat.setWinCount(stat.getWinCount() + 1);
								stats.put(playerConfgs.get(0).getName(), stat);

								logger.debug(stat.toString());

								foundWon = true;
								// }
							} else {

								playerConfgs = allPlayers.stream().filter(p -> p.getName().toLowerCase().contains(playerToSellNormalized.toLowerCase())).collect(Collectors.toList());
								if (playerConfgs.size() > 0) {
									Player playerToSellFromAll = null;
									if (playerConfgs.size() > 1) {
										for (Player p : playerConfgs) {
											if (playerToSellFromAll == null || playerToSellFromAll.getBidToSell() < p.getBidToSell())
												playerToSellFromAll = p;
										}
									} else {
										playerToSellFromAll = playerConfgs.get(0);
									}

									sellPlayer(offset, playerToSellRect, playerToSellFromAll);
								}

								if (playerToSellNormalized.equalsIgnoreCase("Ahlal")) {
									playerConfgs = players.stream().filter(p -> p.getName().toLowerCase().contains("Ahlal".toLowerCase())).collect(Collectors.toList());
									sellPlayer(offset, playerToSellRect, playerConfgs.get(0));
								}

								// System.out.println("non player found for: " + playerToSellNormalized);
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
		}
	}

	private int getActualBid() {
		String selleingP = Util.Read(robot, rectangles.get("ACTUAL_BID"), false).replace("\"", "").replace("]", "").replace("|", "").replace("]00" + "", "").replace("\n", "").replace("l", "1").replace("(0", "0").replace("<{0", "0");
		selleingP = selleingP.replace("o", "9");
		int actualBid = 0;
		try {
			actualBid = Util.convertToInt(selleingP.trim());

		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}

		logger.debug("Bidding " + actualBid + " players");

		return actualBid;

	}

	private boolean canBeTransferedPlayer(int maxCheck) {
		String selleingP = Util.Read(robot, rectangles.get("TRANSFER_ITEM"), false).replace("\"", "").replace("]", "").replace("|", "").replace("]00" + "", "").replace("\n", "").replace("l", "1");
		selleingP = selleingP.replace("o", "9");
		int currentSelling = 0;
		try {
			currentSelling = Util.convertToInt(selleingP.trim());

			if (currentSelling < 80) {
				minOverall = Math.max(minDefaultOverall, 85);
			} else if (currentSelling < 85) {
				minOverall = Math.max(minDefaultOverall, 86);
			} else if (currentSelling < 90) {
				minOverall = Math.max(minDefaultOverall, 87);
			} else {
				minOverall = Math.max(minDefaultOverall, 87);

			}

			if (currentSelling > 100) {
				return true;

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return true;
		}

		logger.debug("Selling " + currentSelling + " players");

		return currentSelling < maxCheck;

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
		logger.debug("Checking transfer after sell player");
		checkTransferd();
	}

	private void clearExpired() {
		logger.debug("Clearing expired");
		boolean foundExpired = false;

		Util.click(robot, positions.get("TRANSFER"));
		Util.waitAction(500);

		if (getActualBid() == 0)
			return;

		Util.click(robot, positions.get("TRANSFER_TARGET"));
		Util.waitAction(500);

		Rectangle clearExp = rectangles.get("CLEAR_EXPIRED");

		for (int i = 0; i < 5 && !foundExpired; i++) {

			if (i > 0) {
				Util.click(robot, positions.get("DOWN_TRANSFER_TARGETS"));
				Util.waitAction(500);
			}
			int offset = 0;

			while (!foundExpired && offset < 2000) {
				Rectangle readRect = new Rectangle((int) clearExp.getX(), (int) clearExp.getY() + offset, (int) clearExp.getWidth(), (int) clearExp.getHeight());

				String clearExpired = Util.Read(robot, readRect, false);

				// logger.debug("read: "+clearExpired);

				if (clearExpired.toLowerCase().contains("clear expired")) {
					// System.out.println("clear expired: " + (int) clearExp.getY() + offset);
					foundExpired = true;
					Util.click(robot, new Dimension((int) clearExp.getY() + offset + 10, (int) positions.get("RELIST_ALL").getHeight()));
					Util.waitAction(300);

				} else {
					offset += 10;
				}
			}
		}
	}

	private void saveStats() throws IOException {

		FileWriter sw = new FileWriter(configs.getFolderFUT() + "/stat.csv");

		CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader(HEADERS).build();

		try (final CSVPrinter printer = new CSVPrinter(sw, csvFormat)) {
			stats.forEach((name, stat) -> {
				try {
					printer.printRecord(name, stat.getBidCount(), stat.getTryedBuy(), stat.getWinCount(), stat.isEnabled());
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}

	}

	private void loadStats() throws IOException {
		Reader in = new FileReader(configs.getFolderFUT() + "/stat.csv");

		CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader(HEADERS).setSkipHeaderRecord(true).build();

		Iterable<CSVRecord> records = csvFormat.parse(in);

		for (CSVRecord record : records) {
			String name = record.get("Name");
			String count = record.get("Count");
			String bid = record.get("Bid");
			String won = record.get("Won");
			String enabled = record.get("Enabled");

			Stats stat = new Stats();

			stat.setTryedBuy(Integer.parseInt(count));
			stat.setBidCount(Integer.parseInt(bid));
			stat.setWinCount(Integer.parseInt(won));

			boolean benabled = true;

			int percBide = 0;

			if (stat.getTryedBuy() > 5 && stat.getBidCount() > 0) {
				percBide = stat.getTryedBuy() / stat.getBidCount();
				benabled = percBide <= 2;
			}
			stat.setEnabled(benabled);

			stats.put(name, stat);
			logger.debug(name + " " + stat.toString() + " " + percBide);
		}

		logger.debug("Loaded " + stats.size() + " statistics");
	}

	private void checkDialog() {
		chechkLimitDialog();
//		chechkHighestDialog();
		chechkAuthenticationDialog();
		chechQuickSellDialog();
	}

	private void chechkLimitDialog() {
		String dialogText = Util.Read(robot, rectangles.get("LIMIT_DIALOG_TEXT"), false);
		if (dialogText.toUpperCase().contains("LIMIT")) {
			Util.click(robot, positions.get("LIMIT_DIALOG_OK"));
		}

	}

	private void chechkHighestDialog() {
		String dialogText = Util.Read(robot, rectangles.get("HIGHER_DIALOG_TEXT"), false);
		//
		if (dialogText.toUpperCase().contains("HIGHEST")) {
			logger.debug(dialogText);
			Util.click(robot, positions.get("HIGHER_DIALOG_OK"));
		}
	}

	private void chechQuickSellDialog() {
		String dialogText = Util.Read(robot, rectangles.get("HIGHER_DIALOG_TEXT"), false);
		//
		if (dialogText.toUpperCase().contains("QUICK SELL")) {
			logger.debug(dialogText);
			Util.click(robot, positions.get("QUICK_SELL_DIALOG_CANCEL"));
		}
	}

	private void chechkAuthenticationDialog() {
		String dialogText = Util.Read(robot, rectangles.get("HIGHER_DIALOG_TEXT"), false);
		//
		if (dialogText.toUpperCase().contains("AUTHENTIC")) {
			logger.debug(dialogText);
			Util.click(robot, positions.get("HIGHER_DIALOG_OK"));
			unlockScreen();
		}
	}
}
