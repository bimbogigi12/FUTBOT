package it.shadowlab.botFut;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import it.shadowlab.botFut.dto.Player;

public class DatabaseService {
	//static String connectionUrl = "jdbc:sqlserver://localhost:1433;" + "database=FUT;" + "user=admin;" + "password=admin;" + "encrypt=true;" + "trustServerCertificate=false;" + "loginTimeout=10;";
	static String connectionUrl = "lpc:localhost[\\SQLEXPRESS];" + "database=FUT;" + "user=admin;" + "password=admin;" + "encrypt=true;" + "trustServerCertificate=false;" + "loginTimeout=10;";
	

	static Connection conn;

	private static  Connection getConnection(){
		if (conn == null) {
		try {
			DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			conn = DriverManager.getConnection(connectionUrl);
			if (conn != null) {
				System.out.println("Connected");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		}
		
		return conn;
	}

	public static void updatePlayers(Set<Player> players) {
		PreparedStatement psPlayers = null;
		try {
			Set<Player> loadedPlayers = loadPlayers();

			for (Player player: players) {
			
				List<Player> playerFound = loadedPlayers.stream().filter(p->p.getName().toLowerCase().equals(player.getName().toLowerCase())).collect(Collectors.toList());
				
				if (!playerFound.isEmpty()) {
					psPlayers = getConnection().prepareStatement("insert into PLAYER (NAME, VALUE, UPDATE_TIME, OVERALL)VALUES (?, ?, ?, ?)");
					psPlayers.setString(1, player.getName());
					psPlayers.setInt(2, player.getMarketValue());
					psPlayers.setDate(3, new java.sql.Date((new Date()).getTime()));
					psPlayers.setInt(4, player.getOverall());
				} else {
					psPlayers = getConnection().prepareStatement("UPDATE PLAYER VALUE = ?, UPDATE_TIME = (sysdatetime()), OVERALL = ? WHERE NAME = ?");
					psPlayers.setString(3, player.getName());
					psPlayers.setInt(1, player.getMarketValue());
					psPlayers.setInt(2, player.getOverall());
				}
				if (psPlayers != null) {
					psPlayers.execute();
					psPlayers.close();
				}
			}
			
			// PreparedStatement psPlayers = conn.prepareStatement();

			// psPlayers.get
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (psPlayers != null)
				psPlayers.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static Set<Player> loadPlayers() throws SQLException {
		Set<Player> loadedPlayers = new HashSet<Player>();
		Statement stmt = getConnection().createStatement();
		ResultSet rs = stmt.executeQuery("select * from PLAYERS");
		while (rs.next()) {
			Player p = new Player();
			p.setName(rs.getString("NAME"));
			p.setMarketValue(rs.getInt("VALUE"));
			p.setBidToBuy((int) (rs.getInt("VALUE") * 0.9));
			p.setBidToSell((int) (rs.getInt("VALUE") * 1.1));
			p.setBidToSellNow((int) (rs.getInt("VALUE") * 1.2));
			p.setOverall(rs.getInt("OVERALL"));
			p.setLastUpdate(rs.getDate("UPDATE_TIME"));

			loadedPlayers.add(p);
		}
		
		return loadedPlayers;
	}
}
