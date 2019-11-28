import java.sql.*;
import org.apache.commons.dbcp2.*;


public class DBCPDataSource {
  private static BasicDataSource dataSource = new BasicDataSource();

  // NEVER store sensitive information below in plain text!
  private static final String HOST_NAME = System.getenv("HOST_NAME");
  private static final String PORT = System.getenv("MySQL_PORT");
  private static final String DATABASE = "LiftRides";
  private static final String USERNAME = System.getenv("DB_USERNAME");
  private static final String PASSWORD = System.getenv("DB_PASSWORD");

  static {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  static {
    // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html
    String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", HOST_NAME, PORT, DATABASE);
    dataSource.setUrl(url);
    dataSource.setUsername(USERNAME);
    dataSource.setPassword(PASSWORD);
    dataSource.setMinIdle(0);
    dataSource.setMaxIdle(-1);
    dataSource.setMaxTotal(-1);
    dataSource.setMaxOpenPreparedStatements(100);
  }

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
}
