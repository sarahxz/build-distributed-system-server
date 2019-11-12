import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDao {
  public void createRequest(Request request) throws SQLException {
    String insert = "INSERT INTO Requests (url, operation, latency)" +
        "VALUES (?,?,?)";
    try (Connection conn = DBCPDataSource.getConnection();
         PreparedStatement preparedStatement = conn.prepareStatement(insert)) {
      preparedStatement.setString(1, request.getUrl());
      preparedStatement.setString(2, request.getOperation());
      preparedStatement.setInt(3, request.getLatency());
      preparedStatement.executeUpdate();
    }
  }

  public ArrayList<Integer> getPost() throws SQLException {
    String get = "SELECT AVG(latency), MAX(latency) FROM Requests WHERE url = ? AND operation = ? GROUP BY url, operation";
    try (Connection conn = DBCPDataSource.getConnection();
         PreparedStatement preparedStatement = conn.prepareStatement(get)) {
      preparedStatement.setString(1, "/skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}");
      preparedStatement.setString(2, "POST");

      ResultSet res = preparedStatement.executeQuery();
      ArrayList<Integer> list = new ArrayList<Integer>();
      res.next();
      list.add(res.getInt("AVG(latency)"));
      list.add(res.getInt("MAX(latency)"));
      return list;
    }
  }

  public ArrayList<Integer> getGet() throws SQLException {
    String get = "SELECT AVG(latency), MAX(latency) FROM Requests WHERE url = ? AND operation = ? GROUP BY url, operation";
    try (Connection conn = DBCPDataSource.getConnection();
         PreparedStatement preparedStatement = conn.prepareStatement(get)) {
      preparedStatement.setString(1, "/skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}");
      preparedStatement.setString(2, "GET");

      ResultSet res = preparedStatement.executeQuery();
      ArrayList<Integer> list = new ArrayList<Integer>();
      res.next();
      list.add(res.getInt("AVG(latency)"));
      list.add(res.getInt("MAX(latency)"));
      return list;
    }
  }

}
