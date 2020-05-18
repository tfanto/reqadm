package pdmf.sys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// https://github.com/pgjdbc/pgjdbc/blob/master/pgjdbc/src/main/java/org/postgresql/jdbc/PgPreparedStatement.java
// rad 908 setObject

//https://mkyong.com/java8/java-8-convert-localdatetime-to-timestamp/

public class Db {

	private static final Logger LOGGER = LoggerFactory.getLogger(Db.class);

	private static DataSource dataSource;
	private static Boolean done = false;

	public static void clear() {
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement("TRUNCATE product CASCADE");
				stmt.executeUpdate();
			}

		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		} finally {
			Db.close(stmt);
			Db.close(connection);
		}
	}

	public static void setupDatabasePool() {

		if (!done) {
			done = true;
//			String url = "jdbc:postgresql://localhost:5432/apidef";
			String url = "jdbc:postgresql://localhost:5432/reqadm";
			String uid = "reqadm";
			String pwd = "reqadm";

			BasicDataSource ds = new BasicDataSource();
			ds.setDriverClassName("org.postgresql.Driver");
			ds.setUrl(url);
			ds.setUsername(uid);
			ds.setPassword(pwd);
			dataSource = (DataSource) ds;
		}

	}

	public static void stopDatabasePool() {
		if (done) {
			BasicDataSource bds = (BasicDataSource) dataSource;
			try {
				bds.close();
				done = false;
			} catch (SQLException e) {
				LOGGER.error(e.toString(), e);
			}
		}
	}

	public static Connection open() throws SQLException {
		Connection conn = dataSource.getConnection();
		if (!conn.getAutoCommit()) {
			conn.setAutoCommit(true);
		}

		return conn;
	}

	public static void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.error(e.toString(), e);
			}
		}

	}

	public static void close(ResultSet resultSet) {

		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				LOGGER.error(e.toString(), e);
			}
		}
	}

	public static void close(Statement statement) {

		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				LOGGER.error(e.toString(), e);
			}
		}
	}

	//@formatter:off
	public static PreparedStatement prepareUpdateStatement(Connection connection, String table,Map<String, Object> keyAndValue, Map<String, Object> fieldNameValue) throws SQLException {
	//@formatter:on

		String sql = "update " + table;
		sql = sql + " set chgnbr=chgnbr + 1,";
		sql = sql + " chgdat=now(),";

		for (String fieldName : fieldNameValue.keySet()) {
			sql = sql + fieldName;
			sql = sql + " = ?";
			sql = sql + ",";
		}
		sql = sql.substring(0, sql.length() - 1);
		sql = sql + " where ";

		for (String keyName : keyAndValue.keySet()) {
			sql = sql + keyName;
			sql = sql + " = ?";
			sql = sql + " and ";
		}
		sql = sql.substring(0, sql.length() - 5);

		return connection.prepareStatement(sql);

	}

	//@formatter:off
	public static PreparedStatement addDataToPreparedUpdateStatement(PreparedStatement stmt,Map<String, Object> keyAndValue, Map<String, Object> fieldNameValue) throws SQLException {
	//@formatter:on

		int parameterIndex = 1;
		for (String fieldName : fieldNameValue.keySet()) {
			Object value = fieldNameValue.get(fieldName);
			stmt.setObject(parameterIndex, value);
			parameterIndex++;
		}
		for (String keyName : keyAndValue.keySet()) {
			Object value = keyAndValue.get(keyName);
			stmt.setObject(parameterIndex, value);
			parameterIndex++;
		}
		return stmt;
	}

	public static List<Map<String, Object>> simpleQuery(String sql) {

		List<Map<String, Object>> ret = new ArrayList<>();

		// only allow selects
		String theSql = sql.toLowerCase();
		if (theSql.contains("delete") || theSql.contains("insert") || theSql.contains("update") || theSql.contains("drop") || theSql.contains("truncate")) {
			return ret;
		}

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(sql);
				rs = stmt.executeQuery();
				// first collect columnnames in resultset
				ResultSetMetaData rsmd = rs.getMetaData();
				int columns = rsmd.getColumnCount();
				List<String> columnNames = new ArrayList<>();
				for (int i = 1; i <= columns; i++) {
					String columnName = rsmd.getColumnLabel(i);
					columnNames.add(columnName);
				}
				while (rs.next()) {
					Map<String, Object> record = new HashMap<>();
					for (String columnName : columnNames) {
						Object data = rs.getObject(columnName);
						record.put(columnName, data);
					}
					ret.add(record);
				}
			}

		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		} finally {
			Db.close(rs);
			Db.close(stmt);
			Db.close(connection);
		}
		return ret;
	}

	// myPreparedStatement.setObject( … , instant ) ;
	public static java.sql.Timestamp Instant2TimeStamp(Instant instant) {
		if (instant == null)
			return null;
		return java.sql.Timestamp.from(instant);
	}

	// Instant instant = myResultSet.getObject( … , Instant.class ) ;
	public static Instant TimeStamp2Instant(java.sql.Timestamp timestamp) {
		if (timestamp == null) {
			return null;
		}
		return timestamp.toInstant();
	}

}
