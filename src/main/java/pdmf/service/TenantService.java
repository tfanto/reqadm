package pdmf.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pdmf.model.TenantKey;
import pdmf.model.TenantRec;
import pdmf.service.support.ServiceHelper;
import pdmf.sys.Db;

public class TenantService {

	/**
	 * 
	 * https://www.postgresql.org/docs/8.1/datatype.html#DATATYPE-SERIAL
	 * 
	 * 
	 * 
	 */

	private static final Logger LOGGER = LoggerFactory.getLogger(TenantService.class);

	public List<TenantRec> list() {
		List<TenantRec> ret = new ArrayList<>();

//		String theSQL = ServiceHelper.getSQL("select name,description from tenant order by name");
		String theSQL = "select tenantid,description from tenant order by tenantid";

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				rs = stmt.executeQuery();
				while (rs.next()) {
					String rs_tenantid = rs.getString("tenantid");
					String rs_description = rs.getString("description");
					TenantKey key = new TenantKey(rs_tenantid);
					TenantRec rec = new TenantRec(key, rs_description);
					ret.add(rec);
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

	public Boolean exists(String tenantid) {
		ServiceHelper.validate("tenantid", tenantid);
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		// String theSQL = ServiceHelper.getSQL("tenantExistsSQL");
		String theSQL = "select count(*) from tenant where tenantid=?";
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setString(1, tenantid);
				rs = stmt.executeQuery();
				rs.next();
				Integer n = rs.getInt(1);
				return n != 0;
			}
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		} finally {
			Db.close(rs);
			Db.close(stmt);
			Db.close(connection);
		}
		return false;
	}

	public TenantRec get(String tenantid) {
		ServiceHelper.validate("tenantid", tenantid);
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String theSQL = "select tenantid,description from tenant where tenantid=?";
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setString(1, tenantid);
				rs = stmt.executeQuery();
				if (rs.next()) {
					String rs_tenantid = rs.getString("tenantid");
					String rs_description = rs.getString("description");
					TenantKey key = new TenantKey(rs_tenantid);
					TenantRec rec = new TenantRec(key, rs_description);
					return rec;
				}
			}
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		} finally {
			Db.close(rs);
			Db.close(stmt);
			Db.close(connection);
		}
		return null;

	}

	public void store(TenantRec tenant, String loggedInUserId) {
		ServiceHelper.validate(tenant);
		ServiceHelper.validate("user", loggedInUserId);
		Connection connection = null;

		tenant.key.tenantid = ServiceHelper.ensureStringLength(tenant.key.tenantid, 50);
		tenant.description = ServiceHelper.ensureStringLength(tenant.description, 100);

		try {
			connection = Db.open();
			if (connection != null) {

				if (!exists(tenant.key.tenantid)) {
					insert(connection, tenant, loggedInUserId);
				} else {
					update(connection, tenant, loggedInUserId);
				}
			}
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		} finally {
			Db.close(connection);
		}
	}

	private void insert(Connection connection, TenantRec tenant, String loggedInUserId) {
		ServiceHelper.validate(tenant);
		ServiceHelper.validate("user", loggedInUserId);

		PreparedStatement stmt = null;

		String theSQL = "insert into tenant (tenantid,description) values(?,?)";

		try {
			stmt = connection.prepareStatement(theSQL);
			stmt.setString(1, tenant.key.tenantid);
			stmt.setString(2, tenant.description);
			stmt.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		} finally {
			Db.close(stmt);
		}
	}

	private Integer update(Connection connection, TenantRec tenant, String loggedInUserId) throws SQLException {

		PreparedStatement stmt = null;

		try {
			TenantRec rec = get(tenant.key.tenantid);
			if (rec == null) {
				return 0;
			}

			Map<String, Object> key = new HashMap<>();
			key.put("tenantid", tenant.key.tenantid);

			Map<String, Object> value = new HashMap<>();
			value.put("description", tenant.description);

			stmt = Db.prepareUpdateStatement(connection, "tenant", key, value);
			stmt = Db.addDataToPreparedUpdateStatement(stmt, key, value);

			return stmt.executeUpdate();
		} finally {
			Db.close(stmt);
		}
	}

}
