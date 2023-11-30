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

	private static int minOverall = 84;
	private static HashMap<String, Rectangle> rectangles = null;
	private static HashMap<String, Dimension> positions = null;

	private static int minMOney = 0;

	static public List<Player> loadPlayers(Robot robot, HashMap<String, Rectangle> prectangles, HashMap<String, Dimension> ppositions) {
		Set<Player> players = new HashSet<>();

		rectangles = prectangles;
		positions = ppositions;

		// openFutBin(robot);

		logger.info("test");

		Date start = new Date();

		int constBid = 1000;
		for (int i = 3; i < 16; i++) {
			if (i > 15) {
				constBid = 5000;
			}

			players.addAll(loadPlayers(robot, (i + 1) * constBid));
		}

		for (Player play : players) {
			// System.out.println(play);
			logger.debug(play.toString());
		}

		// System.out.println("loaded "+players.size()+" players in "+(int)(((new
		// Date()).getTime() - start.getTime())/1000/60)+" minutes" );
		logger.debug("loaded " + players.size() + " players in " + (int) (((new Date()).getTime() - start.getTime()) / 1000 / 60) + " minutes");

		Util.click(robot, positions.get("URL"));
		Util.write(robot, "https://www.ea.com/it-it/ea-sports-fc/ultimate-team/web-app/");
		robot.keyPress(KeyEvent.VK_ENTER);
		Util.waitAction(2000);

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

	static public Set<Player> loadPlayers(Robot robot, int maxMoney) {
		HashSet<Player> players = new HashSet<>();
		boolean loadFromPages = false;
		if (loadFromPages) {

			goToFutPage(robot, maxMoney);

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

				// Util.waitAction(1000);
			}
		} else {

			try {

				/*Document doc = Jsoup.connect("https://www.futbin.com/24/pgp?page=1&ps_price=" + minMOney + "-" + maxMoney + "&version=gold").userAgent("PostmanRuntime/7.34.0").header("Authorization", "Bearer dummy_token").header("Cookie",
						"_gcl_au=1.1.299305964.1696960114; _vwo_uuid_v2=D206043F5ACF0B850F768030CE8795060|137a00f06ee7db5eb6d26d5b75a0b7ca; _vwo_uuid=D206043F5ACF0B850F768030CE8795060; _vwo_ds=3%241696960113%3A52.28841507%3A%3A; hubspotutk=74f804ea9606fc8a2f8a12b60ebf2939; addtl_consent=1~43.3.9.6.9.13.6.4.15.9.5.2.11.1.7.1.3.2.10.33.4.6.9.17.2.9.20.7.20.5.20.6.3.2.1.4.11.29.4.14.9.3.10.6.2.9.6.6.9.8.29.4.5.3.1.27.1.17.10.9.1.8.6.2.8.3.4.146.65.1.17.1.18.25.35.5.18.9.7.41.2.4.18.24.4.9.6.5.2.14.18.7.3.2.2.8.28.8.6.3.10.4.20.2.13.4.10.11.1.3.22.16.2.6.8.6.11.6.5.33.11.8.1.10.28.12.1.5.19.9.6.40.17.4.9.15.8.7.3.12.7.2.4.1.7.12.13.22.13.2.14.10.1.4.15.2.4.9.4.5.4.7.13.5.15.4.13.4.14.10.15.2.5.6.2.2.1.2.14.7.4.8.2.9.10.18.12.13.2.18.1.1.3.1.1.9.25.4.1.19.8.4.8.5.4.8.4.4.2.14.2.13.4.2.6.9.6.3.2.2.3.5.2.3.6.10.11.6.3.19.11.3.1.2.3.9.19.26.3.10.7.6.4.3.4.6.3.3.3.3.1.1.1.6.11.3.1.1.11.6.1.10.5.8.3.2.2.4.3.2.2.7.15.7.14.1.3.3.4.5.4.3.2.2.5.5.1.2.9.7.9.1.5.3.7.10.11.1.3.1.1.2.1.3.2.6.1.12.8.1.3.1.1.2.2.7.7.1.4.3.6.1.2.1.4.1.1.4.1.1.2.1.8.1.7.4.3.3.3.5.3.15.1.15.10.28.1.2.2.12.3.4.1.6.3.4.7.1.3.1.4.1.5.3.1.3.4.1.5.2.3.1.2.2.6.2.1.2.2.2.4.1.1.1.2.2.1.1.1.1.2.1.1.1.2.2.1.1.2.1.2.1.7.1.4.1.2.1.1.1.1.2.1.4.2.1.1.9.1.6.2.1.6.2.3.2.1.1.1.2.5.2.4.1.1.2.2.1.1.7.1.2.2.1.2.1.2.3.1.1.2.4.1.1.1.5.1.3.6.3.1.5.5.4.1.2.3.1.4.3.2.2.3.1.1.1.1.1.11.1.3.1.1.2.2.5.2.3.3.5.2.7.1.1.2.5.1.9.5.1.3.1.8.4.5.1.9.1.1.1.2.1.1.1.4.2.13.1.1.3.1.2.2.3.1.2.1.1.1.2.1.3.1.1.1.1.2.4.1.5.1.2.4.3.8.2.2.9.7.2.2.1.2.1.3.1.6.1.7.1.1.2.6.3.1.2.1.200.200.100.100.200.400.100.100.100.200.200.1700.100.204.596.100.1000.800.500.400.200.200.500.1300.801.99.506; _sharedid=09447d0f-102d-4d7d-b074-e86fe0462b87; __qca=P0-665940647-1696960114842; cookieconsent_status=dismiss; _hjSessionUser_3049116=eyJpZCI6IjljMTIwYzk5LWYwOGYtNWJkMy1iNTYwLTA1ZDFlOGVmN2YxNiIsImNyZWF0ZWQiOjE2OTY5NjAxMTQxNjcsImV4aXN0aW5nIjp0cnVlfQ==; xbox=true; ps=true; locale=it; pc=false; euconsent-v2=CP0M4kAP0M4kAAKAwAITDcCsAP_AAH_AAAwIJutV_H__bW9r8X7_aft0eY1P9_j77uQxBhfJE-4F3LvW_JwXx2E5NF36tqoKmRoEu3ZBIUNlHJHUTVmwaogVryHsakWcpTNKJ6BkkFMRM2dYCF5vm4tjeQKY5_p_d3fx2D-t_dv839zzz8VHn3c5f-e0-PCdU5-9Dfn9fRfb-9IP9_78v8v8_l_rk2_eT13_pcvr_D--f_87_XW-9-IKABJhoXEAXZEBITaBhFAgBGFYQEUCgAAAEgaICAFwYFOwMAl1gIgBAigAOCAEIAKMgAQAACQAIRABIEUCAACAQCAAEACAQCABgYABwAWggEAAIDoGKYUACgWECRGREKYEIUCQQEtlQglBUIK4QBFlgRQCI2CgAQBICKwABAWLwGAJASsSCBLqDaAAAgAQCilCoRSfmAIcEzZaq8UQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAA.d_gACAAAAAAA; player_page_mode=graph; _pbjs_userid_consent_data=6241146096663142; _gid=GA1.2.1384941198.1699260958; usersync=eNqrVipLLSrOzM9TsjLUUSrIrEjNKVayiq5WykxRsjLRUSquzEuOLy5JLCoBKjCztDQytzQ1t4CKJ-fnFuSklqQqWRnU6kC0mBKtxRCmxdCADD2GZOgxIV2PkQU5biPHIiNj0gPb2IQM5xlbYDrP3MjA1MjUDLdNJgZGJDgvthYAIkK7Yw..; _hjIncludedInSessionSample_3049116=0; PHPSESSID=2a594be3e2c086667ead1fb7d7e70c2d; theme_player=true; comments=true; platform=ps4; __cf_bm=5v3caK39okylaOSO8VwyppRX4EdhfrIITb_YA90RgCs-1699356512-0-AZJKVsMwiFd71sJ4T+JcbErYfcCGKEQ3nIoTTRH5j/kRNoBpxcN9vqjSUNTR6sBA7/MZb7guZAs+SiqAFUQmJbs=; _vis_opt_s=24%7C; _vis_opt_test_cookie=1; _hjSession_3049116=eyJpZCI6ImQ4MjVhZTNhLTRmNDItNDY1Yi04NzUzLTgzN2IwMWFkODI5MSIsImNyZWF0ZWQiOjE2OTkzNTY1MTMyODYsImluU2FtcGxlIjpmYWxzZSwic2Vzc2lvbml6ZXJCZXRhRW5hYmxlZCI6ZmFsc2V9; _hjAbsoluteSessionInProgress=0; cf_clearance=iZ0EYu7n7XpwA1YyP57AGmYlnNYLWSPuha1QPc16dko-1699356513-0-1-62792f21.5705a67a.544a6c8d-0.2.1699356513; __hstc=233342337.74f804ea9606fc8a2f8a12b60ebf2939.1696960115650.1699351501824.1699356514342.106; __hssrc=1; __gads=ID=a0498c1538335dd4:T=1696960119:RT=1699356516:S=ALNI_MYmtVqx-ujtkKyDdRihD5fji57iHg; __gpi=UID=00000c9488812a04:T=1696960119:RT=1699356516:S=ALNI_MaTGYtJ9LebaFQa7gjv83dvjLBRPg; _ga_46JQHZ1KXP=GS1.1.1699356512.111.1.1699356533.0.0.0; _ga=GA1.2.2019712751.1696960114; sc_is_visitor_unique=rx9767571.1699356534.714ACDAFAA3A4F13DD999178171D2056.107.82.51.41.30.23.11.7.6; _vwo_sn=2391387%3A2; __hssc=233342337.2.1699356514342; _awl=2.1699356534.5-d8cc8a77b982e03dabf47c9c1a5cf401-6763652d6575726f70652d7765737431-1; cto_bundle=My1b7V9DQWVzODglMkJrVk95ejNvdFJUdVNjY0kxNnZybXdCR0VQR2JyeXZDb3BvM2x6U3dVNnFzSHlIdVN6N2dkMk00TlZEYmJVZnlmYktVZk5iQ2VJdXlhenQlMkJ0MEMlMkJ5SlBMcEhFdm1Da01Xb3oxakxPdSUyRkdpaG9OUyUyQlBhMlV0Y1lPNyUyQjgzRE9hJTJGUzFDSFJyczlibGMyVmhMdyUzRCUzRA; cto_bidid=GiEmyF9HWGZyWWNDMzBycU4lMkZZZjIyNDc2Zkw2VTIlMkZNaEhJN1V3eU8zUEtqSzdTMUR5UFBMOWFQWllZWmFLR1pxOXdiWjVWJTJCQ2huMWVDVWU2eDhSSzU0Q0ExaFpwblY1blhoWFQlMkJvdmNnRUE5QVZRJTNE")
						.header("Cache-Control", "no-cache").header("Accept", "*//* ").header("Accept-Encoding", "gzip, deflate, br").header("Connection", "keep-alive").ignoreContentType(true).get();*/
				
				goToFutPage(robot, maxMoney);
				
				Thread.sleep(2000);
				//logger.debug("saving page");
				
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
				
				//logger.debug("Writing name page");
				Util.write(robot, "FUT_PAGE" + minMOney + "-" + maxMoney, 300);
				
				robot.keyPress(KeyEvent.VK_ENTER);
				
				Thread.sleep(3000);
				
				robot.keyPress(KeyEvent.VK_LEFT);
				//Thread.sleep(3000);
				robot.keyPress(KeyEvent.VK_ENTER);
				
				Thread.sleep(30000);
				
				logger.debug("Reading page");
				
				File input = new File("d:/temp/FUT/FUT_PAGE"+ minMOney + "-" + maxMoney+".html");
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
									actualValue = player.select(".ps4_color").text().replace(".", "").replace("M", "00000").replace("K", "00");
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

		try {
//			DatabaseService.updatePlayers(players);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return players;
	}

	private static void goToFutPage(Robot robot, int maxMoney) {
		Util.click(robot, positions.get("URL"));
		Util.write(robot, "https://www.futbin.com/24/pgp?page=1&ps_price=" + minMOney + "-" + maxMoney + "&version=gold", 100);

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

		/*if (Integer.valueOf(overall) < minOverall || Integer.valueOf(overall) > 100) {
			isValid = false;
		}*/

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
