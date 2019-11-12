import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.sql.*;

public class LiftRideDao {


//  public LiftRideDao() {
//    try {
//      conn = DBCPDataSource.getConnection();
//      preparedStatement = null;
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//  }

  public void createLiftRide(LiftRide newLiftRide) throws SQLException {
    String insert = "INSERT INTO LiftRides (skierId, resortId, seasonId, dayId, time, liftId, vertical) " +
        "VALUES (?,?,?,?,?,?,?)";
    try (Connection conn = DBCPDataSource.getConnection();
         PreparedStatement preparedStatement = conn.prepareStatement(insert)) {
      preparedStatement.setInt(1, newLiftRide.getSkierId());
      preparedStatement.setInt(2, newLiftRide.getResortId());
      preparedStatement.setString(3, newLiftRide.getSeasonId());
      preparedStatement.setString(4, newLiftRide.getDayId());
      preparedStatement.setInt(5, newLiftRide.getTime());
      preparedStatement.setInt(6, newLiftRide.getLiftId());
      preparedStatement.setInt(7, newLiftRide.getVertical());
      preparedStatement.executeUpdate();
    }
  }

  public Integer getVertical(Integer resortId, String seasonId, String dayId, Integer skierId) throws SQLException {
    String get = "SELECT SUM(vertical) FROM LiftRides WHERE resortId = ? AND seasonId = ? AND dayId = ? AND skierId = ?";
    try (Connection conn = DBCPDataSource.getConnection();
      PreparedStatement preparedStatement = conn.prepareStatement(get)) {
      preparedStatement.setInt(1, resortId);
      preparedStatement.setString(2, seasonId);
      preparedStatement.setString(3, dayId);
      preparedStatement.setInt(4, skierId);

      ResultSet res = preparedStatement.executeQuery();
      res.next();
      return res.getInt("SUM(vertical)");
    }
  }
}
