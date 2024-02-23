package it.shadowlab.botFut.util;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.shadowlab.botFut.dto.Player;

public class FutBinUtil {

	static Logger logger = LoggerFactory.getLogger(FutBinUtil.class);

	private static HashMap<String, Rectangle> rectangles = null;
	private static HashMap<String, Dimension> positions = null;

	private static int minMOney = 0;

	static public List<Player> loadPlayers(Robot robot, HashMap<String, Rectangle> prectangles, HashMap<String, Dimension> ppositions, Config config) {
		Set<Player> players = new HashSet<>();

		rectangles = prectangles;
		positions = ppositions;

		// openFutBin(robot);

		Date start = new Date();

		int constBid = 1000;
		minMOney = 0;
		for (int i = 1; i < 20; i++) {
			if (i > 15) {
				constBid = 10000;
			}

			if (i > 20) {
				constBid = 10000000;
			}

			players.addAll(loadPlayers(robot, (i + 1) * constBid, config));
		}

		players.addAll(loadIcons(robot, config));

		for (Player play : players) {
			// System.out.println(play);
			logger.debug(play.toString());
		}

		// System.out.println("loaded "+players.size()+" players in "+(int)(((new
		// Date()).getTime() - start.getTime())/1000/60)+" minutes" );
		logger.debug("loaded " + players.size() + " players in " + (int) (((new Date()).getTime() - start.getTime()) / 1000 / 60) + " minutes");

		if (isTimeReload(config)) {

			Util.click(robot, positions.get("URL"));
			Util.write(robot, "https://www.ea.com/it-it/ea-sports-fc/ultimate-team/web-app/");
			robot.keyPress(KeyEvent.VK_ENTER);
			Util.waitAction(2000);
		}

		return players.stream().collect(Collectors.toList());
	}

	private static void openFutBin(Robot robot) {
		System.out.println("Opening tab with futbin");
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_T);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_T);
		Util.waitAction(2000);
		// Util.click(robot, positions.get("URL"));
	}

	static public Set<Player> loadIcons(Robot robot, Config config) {
		HashSet<Player> players = new HashSet<>();

		if (isTimeReload(config)) {
			Util.click(robot, positions.get("URL"));
			Util.write(robot, "https://www.futbin.com/24/pgp?page=1&order=desc&sort=games&version=icons", 100);

			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			Util.waitAction(2000);
		}
		loadPlayers(robot, 13, players, config);

		return players;
	}

	//

	static public Set<Player> loadPlayers(Robot robot, int maxMoney, Config config) {
		HashSet<Player> players = new HashSet<>();
		if (isTimeReload(config))
			goToFutPage(robot, maxMoney);

		loadPlayers(robot, maxMoney, players, config);

		try {
//			DatabaseService.updatePlayers(players);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return players;
	}

	private static boolean isTimeReload(Config config) {
		return new Date().getTime() - config.getLastLoadPlayer().getTime() > 1000 * 60 * 60 * 4;
	}

	private static void loadPlayers(Robot robot, int maxMoney, HashSet<Player> players, Config config) {
		boolean loadFromPages = false;
		if (loadFromPages) {

			int offset = 0;

			Rectangle playerName = rectangles.get("PLAYER_NAME_FUTBIN");
			Rectangle overall = rectangles.get("OVERALL_FUTBIN");
			Rectangle marketValueRect = rectangles.get("VALUE_FUTBIN");

			boolean findFirst = false;
			int offsetX = 0;
			int offsetMessgae = 16;

			while (offset < 4000) {
				String playerNameRead = Util.Read(robot, new Rectangle((int) playerName.getX(), (int) playerName.getY() + offset + offsetMessgae, (int) playerName.getWidth(), (int) playerName.getHeight()), false);

				offset += 56;
				try {

					if (StringUtils.isNotEmpty(playerNameRead)) {

//					System.out.println(playerNameRead);
						// String actualValue = "";

						String actualValue = Util.Read(robot, new Rectangle((int) overall.getX() + offsetX, (int) overall.getY() + offset + offsetMessgae, (int) overall.getWidth(), (int) overall.getHeight()), false).trim().replace("-", "3");

						while (!findFirst) {
							try {
								// System.out.println("temp actval "+actualValue);
								if (Integer.valueOf(actualValue) < 80) {
									throw new Exception();
								}
								findFirst = true;
								// System.out.println("found first "+actualValue+" with offset"+offsetX);
								Util.Read(robot, new Rectangle((int) overall.getX() + offsetX, (int) overall.getY() + offset + offsetMessgae, (int) overall.getWidth(), (int) overall.getHeight()), false).trim().replace("-", "3");

								offsetX += 2;
							} catch (Exception ex) {
								offsetX += 1;
								actualValue = Util.Read(robot, new Rectangle((int) overall.getX() + offsetX, (int) overall.getY() + offset + offsetMessgae, (int) overall.getWidth(), (int) overall.getHeight()), false).trim().replace("-", "3");

								if (offsetX > 300)
									findFirst = true;
							}
						}

						// System.out.print("Overall: " + actualValue+" ");

						String marketValue = Util.Read(robot, new Rectangle((int) marketValueRect.getX() + offsetX, (int) marketValueRect.getY() + offset + offsetMessgae, (int) marketValueRect.getWidth(), (int) marketValueRect.getHeight()), false).trim();

						// System.out.print("Market value: " + marketValue+" ");

						Player reece = validatePlayer(playerNameRead, marketValue, actualValue, true);

						if (reece != null) {
							players.add(reece);
							logger.debug(reece.toString());
							// System.out.println(reece);
						}
					}
				} catch (Exception ex) {
					// ex.printStackTrace();
				}

			}
		} else {

			try {

				if (isTimeReload(config)) {
					Thread.sleep(2000);
					// logger.debug("saving page");

					robot.keyPress(KeyEvent.VK_CONTROL);
					robot.keyPress(KeyEvent.VK_S);
					robot.keyRelease(KeyEvent.VK_CONTROL);
					robot.keyRelease(KeyEvent.VK_S);

					robot.keyPress(KeyEvent.VK_CONTROL);
					Thread.sleep(1000);
					robot.keyPress(KeyEvent.VK_S);
					robot.keyRelease(KeyEvent.VK_S);
					robot.keyRelease(KeyEvent.VK_CONTROL);

					Thread.sleep(2000);

					// logger.debug("Writing name page");
					Util.write(robot, "FUT_PAGE" + minMOney + "-" + maxMoney, 300);

					robot.keyPress(KeyEvent.VK_ENTER);

					Thread.sleep(3000);

					robot.keyPress(KeyEvent.VK_LEFT);
					// Thread.sleep(3000);
					robot.keyPress(KeyEvent.VK_ENTER);

					// Thread.sleep(30000);
					Thread.sleep(5000);
				}

				logger.debug("Reading page");

				File input = new File(config.getFolderFUT() + "/FUT_PAGE" + minMOney + "-" + maxMoney + ".html");
				Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");

				Elements thead = doc.select(".players_table_header");

				Elements playersEle = thead.next().select("tbody").select("tr");

				for (Element player : playersEle) {
					try {
						Elements playerNameA = player.select(".player_name_players_table");
						String href = playerNameA.attr("href");
						if (player.select(".rating").size() > 1) {
							String overall = player.select(".rating").get(1).text();

							String playerName = playerNameA.text();

							playerName = Util.normalizePlauerName(playerName);
							String actualValue = "";
							if (player.select(".ps4_color").text().contains(".")) {

								int idx = player.select(".ps4_color").text().indexOf(".");
								int after0 = player.select(".ps4_color").text().substring(idx + 1, player.select(".ps4_color").text().length()).length();
								switch (after0) {
								case 2:
									actualValue = player.select(".ps4_color").text().replace(".", "").replace("M", "00000").replace("K", "00");
									break;
								case 3:
									actualValue = player.select(".ps4_color").text().replace(".", "").replace("M", "0000").replace("K", "0");
								}

								// actualValue = player.select(".ps4_color").text().replace(".",
								// "").replace("M", "00000").replace("K", "00");
							} else {
								actualValue = player.select(".ps4_color").text().replace("M", "000000").replace("K", "000");
							}

							Player reece = validatePlayer(playerName, actualValue, overall, false);

							if (reece != null) {
								players.add(reece);
								logger.debug(reece.toString());
								//
							}

						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				Thread.sleep(1000);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		// System.out.println(minMOney+"-" + maxMoney+" loaded " + players.size() + "
		// players");
		logger.debug(minMOney + "-" + maxMoney + " loaded " + players.size() + " players");
		minMOney = maxMoney;
	}

	private static void goToFutPage(Robot robot, int maxMoney) {
		Util.click(robot, positions.get("URL"));
		Util.write(robot, "https://www.futbin.com/24/pgp?page=1&ps_price=" + minMOney + "-" + maxMoney + "&version=gold&sort=games&order=desc", 100);

		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		Util.waitAction(2000);
	}

	private static Player validatePlayer(String playerNameRead, String marketValue, String overall, boolean readFromOCr) {

		boolean isValid = true;

		playerNameRead = Util.normalizePlauerName(playerNameRead);

		marketValue = marketValue.replace("I", "1");

		if (marketValue.equalsIgnoreCase("K")) {
			marketValue = "1K";
		}

		if (marketValue.contains(".")) {
			marketValue = marketValue.toUpperCase().replace(".", "").replace("M", "00000").replace("K", "00");
		} else {
			if (readFromOCr && (marketValue.trim().length() == 3 || marketValue.trim().length() == 4)) {
				isValid = false;

			} else {
				marketValue = marketValue.toUpperCase().replace("M", "000000").replace("K", "000");
			}
		}

		try {
			Integer.valueOf(marketValue);
		} catch (Exception ex) {
			isValid = false;
		}

		try {
			Integer.valueOf(overall);
		} catch (Exception ex) {
			isValid = false;
		}

		/*
		 * if (Integer.valueOf(overall) < minOverall || Integer.valueOf(overall) > 100)
		 * { isValid = false; }
		 */

		if (Integer.valueOf(marketValue) < 1000)
			isValid = false;

		if (readFromOCr) {
			switch (Integer.valueOf(overall)) {
			case 84:
				if (Integer.valueOf(marketValue) > 4000)
					isValid = false;
				break;
			case 85:
				if (Integer.valueOf(marketValue) > 10000 || Integer.valueOf(marketValue) < 4000)
					isValid = false;
				break;
			case 86:
				if (Integer.valueOf(marketValue) > 20000 || Integer.valueOf(marketValue) < 10000)
					isValid = false;
				break;
			case 87:
			case 88:
			case 89:
			case 90:
			case 91:
			case 92:
			case 93:
			case 94:
			case 95:
			case 96:
			case 97:
			case 98:
			case 99:
				if (Integer.valueOf(marketValue) < 10000)
					isValid = false;
				break;
			}

			if (Integer.valueOf(marketValue) < 1000) {
				isValid = false;
			}
		}

		Player reece = null;
		if (isValid) {
			reece = new Player();
			reece.setName(playerNameRead.trim());
			reece.setMarketValue(Util.convertToInt(marketValue));
			reece.setBidToBuy((int) (Util.convertToInt(marketValue) * 0.9));
			reece.setBidToSell((int) (Util.convertToInt(marketValue) * 1.1));
			reece.setBidToSellNow((int) (Util.convertToInt(marketValue) * 1.2));
			// reece.setHref(href);
			reece.setOverall(Integer.valueOf(overall));
		} else {
			logger.error("Skipped player " + playerNameRead + " value " + marketValue + " overall " + overall);
			// System.out.println("Skipped player "+playerNameRead+ " value "+marketValue+"
			// overall "+overall);
		}

		return reece;
	}
}
