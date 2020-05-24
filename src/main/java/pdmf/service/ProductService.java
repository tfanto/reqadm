package pdmf.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pdmf.model.ProductKey;
import pdmf.model.ProductRec;
import pdmf.service.support.ServiceHelper;
import pdmf.sys.Db;
import pdmf.sys.RecordChangedByAnotherUser;

public class ProductService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

	public List<String> list(String tenantid) {
		ServiceHelper.validate("Tenant", tenantid);

		String theSQL = ServiceHelper.getSQL("productSelectSQL");

		List<String> ret = new ArrayList<>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setString(1, tenantid);
				rs = stmt.executeQuery();
				while (rs.next()) {
					String productName = rs.getString(1);
					ret.add(productName);
				}
				return ret;
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

	public List<ProductRec> list(String tenantid, String productName) {
		ServiceHelper.validate("Tenant", tenantid);
		ServiceHelper.validate("ProductName", productName);
		List<ProductRec> ret = new ArrayList<>();

		String theSQL = ServiceHelper.getSQL("productsSelectSQL_ALL_VERSIONS");

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setString(1, tenantid);
				stmt.setString(2, productName);
				rs = stmt.executeQuery();
				while (rs.next()) {

					Instant rs_crtdat = Db.TimeStamp2Instant(rs.getTimestamp("crtdat"));
					String rs_crtusr = rs.getString("crtusr");
					Instant rs_chgdat = Db.TimeStamp2Instant(rs.getTimestamp("chgdat"));
					String rs_chgusr = rs.getString("chgusr");
					Instant rs_dltdat = Db.TimeStamp2Instant(rs.getTimestamp("dltdat"));
					String rs_dltusr = rs.getString("dltusr");
					Integer rs_chgnbr = rs.getInt("chgnbr");
					Integer rs_crtver = rs.getInt("crtver");
					String rs_description = rs.getString("description");
					String rs_shortdescr = rs.getString("shortdescr");
					String rs_status = rs.getString("status");

					String rs_tenantid = rs.getString("tenantid");
					Integer rs_version = rs.getInt("version");
					String rs_productname = rs.getString("productname");

					ProductKey key = new ProductKey(rs_tenantid, rs_version, rs_productname);
					ProductRec rec = new ProductRec(key, rs_description, rs_crtdat, rs_chgnbr);
					rec.shortdescr = rs_shortdescr;
					rec.crtusr = rs_crtusr;
					rec.chgdat = rs_chgdat;
					rec.chgusr = rs_chgusr;
					rec.crtver = rs_crtver;
					rec.dltusr = rs_dltusr;
					rec.dltdat = rs_dltdat;
					rec.status = rs_status;
					ret.add(rec);
				}
				return ret;
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

	public List<ProductRec> list(String tenantid, String productName, Integer version) {
		ServiceHelper.validate("Tenant", tenantid);
		ServiceHelper.validate("ProductName", productName);
		ServiceHelper.validate("Version", version);
		List<ProductRec> ret = new ArrayList<>();

		String theSQL = ServiceHelper.getSQL("productsSelectSQL_ONE_PRODUCT_ONE_VERSION");

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setString(1, tenantid);
				stmt.setInt(2, version);
				stmt.setString(3, productName);
				rs = stmt.executeQuery();
				while (rs.next()) {
					Instant rs_crtdat = Db.TimeStamp2Instant(rs.getTimestamp("crtdat"));
					String rs_crtusr = rs.getString("crtusr");
					Instant rs_chgdat = Db.TimeStamp2Instant(rs.getTimestamp("chgdat"));
					String rs_chgusr = rs.getString("chgusr");
					Instant rs_dltdat = Db.TimeStamp2Instant(rs.getTimestamp("dltdat"));
					String rs_dltusr = rs.getString("dltusr");
					Integer rs_chgnbr = rs.getInt("chgnbr");
					Integer rs_crtver = rs.getInt("crtver");
					String rs_description = rs.getString("description");
					String rs_shortdescr = rs.getString("shortdescr");
					String rs_status = rs.getString("status");

					String rs_tenantid = rs.getString("tenantid");
					Integer rs_version = rs.getInt("version");
					String rs_productname = rs.getString("productname");

					ProductKey key = new ProductKey(rs_tenantid, rs_version, rs_productname);
					ProductRec rec = new ProductRec(key, rs_description, rs_crtdat, rs_chgnbr);
					rec.shortdescr = rs_shortdescr;
					rec.crtusr = rs_crtusr;
					rec.chgdat = rs_chgdat;
					rec.chgusr = rs_chgusr;
					rec.crtver = rs_crtver;
					rec.dltusr = rs_dltusr;
					rec.dltdat = rs_dltdat;
					rec.status = rs_status;
					ret.add(rec);
				}
				return ret;
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

	public List<ProductRec> list(String tenantid, Integer version) {
		ServiceHelper.validate("Tenant", tenantid);
		ServiceHelper.validate("Version", version);
		List<ProductRec> ret = new ArrayList<>();

		String theSQL = ServiceHelper.getSQL("productsSelectSQL");

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setString(1, tenantid);
				stmt.setInt(2, version);
				rs = stmt.executeQuery();
				while (rs.next()) {
					Instant rs_crtdat = Db.TimeStamp2Instant(rs.getTimestamp("crtdat"));
					String rs_crtusr = rs.getString("crtusr");
					Instant rs_chgdat = Db.TimeStamp2Instant(rs.getTimestamp("chgdat"));
					String rs_chgusr = rs.getString("chgusr");
					Instant rs_dltdat = Db.TimeStamp2Instant(rs.getTimestamp("dltdat"));
					String rs_dltusr = rs.getString("dltusr");
					Integer rs_chgnbr = rs.getInt("chgnbr");
					Integer rs_crtver = rs.getInt("crtver");
					String rs_description = rs.getString("description");
					String rs_shortdescr = rs.getString("shortdescr");
					String rs_status = rs.getString("status");

					String rs_tenantid = rs.getString("tenantid");
					Integer rs_version = rs.getInt("version");
					String rs_productname = rs.getString("productname");

					ProductKey key = new ProductKey(rs_tenantid, rs_version, rs_productname);
					ProductRec rec = new ProductRec(key, rs_description, rs_crtdat, rs_chgnbr);
					rec.shortdescr = rs_shortdescr;
					rec.crtusr = rs_crtusr;
					rec.chgdat = rs_chgdat;
					rec.chgusr = rs_chgusr;
					rec.crtver = rs_crtver;
					rec.dltusr = rs_dltusr;
					rec.dltdat = rs_dltdat;
					rec.status = rs_status;
					ret.add(rec);
				}
				return ret;
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

	public boolean exists(String tenantid, Integer version, String productName) {
		ServiceHelper.validate("Tenant", tenantid);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = Db.open();
			if (connection != null) {
				return exists(connection, tenantid, version, productName);
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

	public boolean exists(Connection connection, String tenantid, Integer version, String productName) {
		ServiceHelper.validate("Tenant", tenantid);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String theSQL = ServiceHelper.getSQL("productExistsSQL");
		try {
			stmt = connection.prepareStatement(theSQL);
			stmt.setString(1, tenantid);
			stmt.setInt(2, version);
			stmt.setString(3, productName);
			rs = stmt.executeQuery();
			rs.next();
			Integer n = rs.getInt(1);
			return n != 0;
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		} finally {
			Db.close(rs);
			Db.close(stmt);
		}
		return false;
	}

	public ProductRec get(String tenantid, Integer version, String productName) {
		ServiceHelper.validate("Tenant", tenantid);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = Db.open();
			if (connection != null) {
				return get(connection, tenantid, version, productName);
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

	public ProductRec get(Connection connection, String tenantid, Integer version, String productName) {
		ServiceHelper.validate("Tenant", tenantid);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String theSQL = ServiceHelper.getSQL("productSelectSingleRecSQL");
			stmt = connection.prepareStatement(theSQL);
			stmt.setString(1, tenantid);
			stmt.setInt(2, version);
			stmt.setString(3, productName);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Instant rs_crtdat = Db.TimeStamp2Instant(rs.getTimestamp("crtdat"));
				String rs_crtusr = rs.getString("crtusr");
				Instant rs_chgdat = Db.TimeStamp2Instant(rs.getTimestamp("chgdat"));
				String rs_chgusr = rs.getString("chgusr");
				Instant rs_dltdat = Db.TimeStamp2Instant(rs.getTimestamp("dltdat"));
				String rs_dltusr = rs.getString("dltusr");
				Integer rs_chgnbr = rs.getInt("chgnbr");
				Integer rs_crtver = rs.getInt("crtver");
				String rs_description = rs.getString("description");
				String rs_shortdescr = rs.getString("shortdescr");
				String rs_status = rs.getString("status");

				String rs_tenantid = rs.getString("tenantid");
				Integer rs_version = rs.getInt("version");
				String rs_productname = rs.getString("productname");

				ProductKey key = new ProductKey(rs_tenantid, rs_version, rs_productname);
				ProductRec rec = new ProductRec(key, rs_description, rs_crtdat, rs_chgnbr);
				rec.shortdescr = rs_shortdescr;
				rec.crtusr = rs_crtusr;
				rec.chgdat = rs_chgdat;
				rec.chgusr = rs_chgusr;
				rec.crtver = rs_crtver;
				rec.dltdat = rs_dltdat;
				rec.dltusr = rs_dltusr;
				rec.status = rs_status;

				return rec;
			}
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		} finally {
			Db.close(rs);
			Db.close(stmt);
		}
		return null;
	}

	public void store(ProductRec product, String loggedInUserId) {
		ServiceHelper.validate(product);
		ServiceHelper.validate("user", loggedInUserId);
		Connection connection = null;

		product.shortdescr = ServiceHelper.ensureStringLength(product.shortdescr, 100);
		product.description = ServiceHelper.ensureStringLength(product.description, 995);

		try {
			connection = Db.open();
			if (connection != null) {

				if (!exists(connection, product.key.tenantid, product.key.version, product.key.productName)) {
					insert(connection, product, loggedInUserId);
				} else {
					update(connection, product, loggedInUserId);
				}
			}
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		} finally {
			Db.close(connection);
		}
	}

	public void store(Connection connection, ProductRec product, String loggedInUserId) throws SQLException {
		ServiceHelper.validate(product);
		ServiceHelper.validate("user", loggedInUserId);

		product.shortdescr = ServiceHelper.ensureStringLength(product.shortdescr, 100);
		product.description = ServiceHelper.ensureStringLength(product.description, 995);

		if (!exists(product.key.tenantid, product.key.version, product.key.productName)) {
			insert(connection, product, loggedInUserId);
		} else {
			update(connection, product, loggedInUserId);
		}
	}

	public void insert(ProductRec product, String loggedInUserId) {
		ServiceHelper.validate(product);
		ServiceHelper.validate("user", loggedInUserId);
		Connection connection = null;

		try {
			connection = Db.open();
			if (connection != null) {
				insert(connection, product, loggedInUserId);
			}
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		} finally {
			Db.close(connection);
		}
	}

	public Integer insert(Connection connection, ProductRec product, String loggedInUserId) throws SQLException {
		ServiceHelper.validate(product);
		ServiceHelper.validate("user", loggedInUserId);

		product.shortdescr = ServiceHelper.ensureStringLength(product.shortdescr, 100);
		product.description = ServiceHelper.ensureStringLength(product.description, 995);

		String theSQL = ServiceHelper.getSQL("productInsertSQL");

		PreparedStatement stmt = null;
		try {
			Integer firstVersion = getFirstVersionForProduct(connection, product.key.tenantid, product.key.productName);
			stmt = connection.prepareStatement(theSQL);
			stmt.setString(1, product.key.tenantid);
			stmt.setInt(2, product.key.version);
			stmt.setString(3, product.key.productName);
			stmt.setString(4, product.description);
			stmt.setTimestamp(5, Db.Instant2TimeStamp(Instant.now()));
			stmt.setInt(6, 0);
			stmt.setInt(7, firstVersion == null ? product.key.version : firstVersion);
			stmt.setString(8, loggedInUserId);
			stmt.setString(9, product.shortdescr);
			stmt.setString(10, "wrk");
			return stmt.executeUpdate();
		} finally {
			Db.close(stmt);
		}
	}

	private Integer update(Connection connection, ProductRec product, String loggedInUserId) throws SQLException {

		PreparedStatement stmt = null;

		if (ProductService.isLocked(product.key.tenantid, product.key.version, product.key.productName)) {
			LOGGER.info("LOCKED " + product.key.tenantid + product.key.version + " " + product.key.productName);
			return -1;
		}

		try {
			ProductRec dbRec = get(product.key.tenantid, product.key.version, product.key.productName);
			if (dbRec == null) {
				return 0;
			}
			if (!product.chgnbr.equals(dbRec.chgnbr)) {
				throw new RecordChangedByAnotherUser();
			}

			Map<String, Object> key = new HashMap<>();
			key.put("version", product.key.version);
			key.put("productname", product.key.productName);
			key.put("tenantid", product.key.tenantid);

			Map<String, Object> value = new HashMap<>();
			value.put("description", product.description);
			value.put("shortdescr", product.shortdescr);
			value.put("chgusr", loggedInUserId);

			stmt = Db.prepareUpdateStatement(connection, "product", key, value);
			stmt = Db.addDataToPreparedUpdateStatement(stmt, key, value);

			return stmt.executeUpdate();
		} finally {
			Db.close(stmt);
		}
	}

	public void remove(String tenantid, Integer version, String productName, String userid) {
		ServiceHelper.validate("Tenant", tenantid);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		ServiceHelper.validate("Userid", userid);
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			connection = Db.open();
			if (connection != null) {
				connection.setAutoCommit(false);

				if (ProductService.isLocked(connection, tenantid, version, productName)) {
					LOGGER.info("LOCKED " + version + " " + productName);
					return;
				}
				stmt = connection.prepareStatement(
						"update product set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=?  and version=? and tenantid=?");
				stmt.setString(1, userid);
				stmt.setString(2, productName);
				stmt.setInt(3, version);
				stmt.setString(4, tenantid);
				stmt.executeUpdate();
				deleteAllDependencies(connection, tenantid, version, productName, userid);
				connection.commit();
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
			}
			LOGGER.error(e.toString(), e);
		} finally {
			Db.close(stmt);
			Db.close(connection);
		}
	}

	private void deleteAllDependencies(Connection connection, String tenantid, Integer version, String productName,
			String userId) throws SQLException {

		PreparedStatement stmtTopic = null;
		PreparedStatement stmtProcess = null;
		PreparedStatement stmtOperation = null;

		try {
			stmtTopic = connection.prepareStatement(
					"update topic set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=?  and version=? and tenantid = ?");
			stmtTopic.setString(1, userId);
			stmtTopic.setString(2, productName);
			stmtTopic.setInt(3, version);
			stmtTopic.setString(4, tenantid);
			stmtProcess = connection.prepareStatement(
					"update process set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=?  and version=? and tenantid = ?");
			stmtProcess.setString(1, userId);
			stmtProcess.setString(2, productName);
			stmtProcess.setInt(3, version);
			stmtProcess.setString(4, tenantid);
			stmtOperation = connection.prepareStatement(
					"update oper set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=?  and version=? and tenantid = ?");
			stmtOperation.setString(1, userId);
			stmtOperation.setString(2, productName);
			stmtOperation.setInt(3, version);
			stmtOperation.setString(4, tenantid);
			stmtTopic.executeUpdate();
			stmtProcess.executeUpdate();
			stmtOperation.executeUpdate();
		} finally {
			Db.close(stmtTopic);
			Db.close(stmtProcess);
			Db.close(stmtOperation);
		}
	}

	public Integer getFirstVersionForProduct(String tenantid, String productName) {
		ServiceHelper.validate("Tenant", tenantid);
		ServiceHelper.validate("Product", productName);
		Connection connection = null;
		try {
			connection = Db.open();
			if (connection != null) {
				return getFirstVersionForProduct(connection, tenantid, productName);
			}
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
			return -1;
		} finally {
			Db.close(connection);
		}
		return -1;
	}

	private Integer getFirstVersionForProduct(Connection connection, String tenantid, String product)
			throws SQLException {

		ServiceHelper.validate("Product", product);

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement(
					"select version from product where productname=? and tenantid=? order by version");
			stmt.setString(1, product);
			stmt.setString(2, tenantid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return null;
			}
		} finally {
			Db.close(rs);
			Db.close(stmt);
		}
	}

	public static boolean isLocked(String tenantid, Integer version, String productName) {
		ServiceHelper.validate("Tenant", tenantid);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		Connection connection = null;
		try {
			connection = Db.open();
			if (connection != null) {
				return ProductService.isLocked(connection, tenantid, version, productName);
			}
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
			return true;
		} finally {
			Db.close(connection);
		}
		return true;
	}

	public static boolean isLocked(Connection connection, String tenantid, Integer version, String productName)
			throws SQLException {
		ServiceHelper.validate("Tenant", tenantid);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection
					.prepareStatement("select status from product where version=? and productname=? and tenantid=? ");
			stmt.setInt(1, version);
			stmt.setString(2, productName);
			stmt.setString(3, tenantid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				String status = rs.getString(1);
				if (status == null)
					return false;
				if (status.equalsIgnoreCase("wrk")) {
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		} finally {
			Db.close(rs);
			Db.close(stmt);
		}
	}

	/**
	 * 
	 */

	public void createNewVersion(String tenantid, Integer fromVersion, Integer toVersion, String productName,
			String loggedInUserId) {
		ServiceHelper.validate("Tenant", tenantid);
		ServiceHelper.validate("FromVersion", fromVersion);
		ServiceHelper.validate("ToVersion", toVersion);
		ServiceHelper.validate("ProductName", productName);
		ServiceHelper.validate("user", loggedInUserId);

		if (ProductService.isLocked(tenantid, fromVersion, productName)) {
			LOGGER.info("LOCKED " + fromVersion + " " + productName + " kan inte användas som mall");
			return;
		}

		Connection connection = null;
		try {
			connection = Db.open();
			if (connection == null) {
				LOGGER.error("No connection to database");
				return;
			}
			connection.setAutoCommit(false);

			ProductRec productRec = get(connection, tenantid, fromVersion, productName);
			if (productRec == null) {
				LOGGER.error("Oldversion NOT found when trying to create a new version");
				throw new RuntimeException();
			}

			if (exists(connection, tenantid, toVersion, productName)) {
				LOGGER.error("Target version already exists " + productName + "  " + toVersion);
				throw new RuntimeException();
			}

			productRec.key.version = toVersion;
			productRec.status = "wrk";

			createVersion(productRec, loggedInUserId);

			copyData(connection, tenantid, productName, fromVersion, toVersion);

			connection.commit();
		} catch (RuntimeException | SQLException rte) {
			LOGGER.error(rte.toString(), rte);
			try {
				connection.rollback();
			} catch (SQLException e) {
				// what to do ?
			}
		} finally {
			Db.close(connection);
		}

	}

	private Integer createVersion(ProductRec productRec, String loggedInUserId) throws SQLException {
		ServiceHelper.validate(productRec);
		ServiceHelper.validate("user", loggedInUserId);

		if (exists(productRec.key.tenantid, productRec.key.version, productRec.key.productName)) {
			LOGGER.error(productRec.key.version + " " + productRec.key.productName + " already exists");
			throw new RuntimeException();
		}

		Connection connection = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			connection = Db.open();
			if (connection == null) {
				LOGGER.error("could not create version. No dbconnection");
				throw new RuntimeException();
			}

			Integer firstVersion = getFirstVersionForProduct(connection, productRec.key.tenantid,
					productRec.key.productName);

			stmt2 = connection
					.prepareStatement("update product set status='locked' where productname=? and tenantid=?");
			stmt2.setString(1, productRec.key.productName);
			stmt2.setString(2, productRec.key.tenantid);
			stmt2.executeUpdate();

			stmt = connection.prepareStatement(
					"insert into product (version,productname,crtdat,chgnbr,description,status,crtver,crtusr,shortdescr,tenantid) values (?,?,?,?,?,?,?,?,?,?)");
			stmt.setInt(1, productRec.key.version);
			stmt.setString(2, productRec.key.productName);
			stmt.setTimestamp(3, Db.Instant2TimeStamp(Instant.now()));
			stmt.setInt(4, 0);
			stmt.setString(5, productRec.description);
			stmt.setString(6, productRec.status);
			stmt.setInt(7, firstVersion == null ? productRec.key.version : firstVersion);
			stmt.setString(8, loggedInUserId);
			stmt.setString(9, productRec.shortdescr);
			stmt.setString(10, productRec.key.tenantid);
			return stmt.executeUpdate();
		} finally {
			Db.close(stmt2);
			Db.close(stmt);
			Db.close(connection);
		}
	}

	private void copyData(Connection connection, String tenantid, String productName, Integer fromVersion,
			Integer toVersion) throws SQLException {

		copyData(connection, tenantid, productName, fromVersion, toVersion, "topic");
		copyData(connection, tenantid, productName, fromVersion, toVersion, "process");
		copyData(connection, tenantid, productName, fromVersion, toVersion, "oper");
	}

	private void copyData(Connection connection, String tenantid, String productName, Integer fromVersion,
			Integer toVersion, String sourceTableName) throws SQLException {

		String tempTableName = "WRK";
		tempTableName = tempTableName.replace("-", "").toLowerCase();

		String cloneTable = String.format("create table  %s as table %s with no data", tempTableName, sourceTableName);
//		String copy = String.format("insert into %s (select * from %s where version=? and productname=? and dltusr isnull)", tempTableName, sourceTableName);
		String copy = String.format(
				"insert into %s (select * from %s where version=? and productname=? and tenantid=?)", tempTableName,
				sourceTableName);
		String upd1 = String.format("update  %s set version=?, crtdat=now(),chgdat=now()", tempTableName);
		String copyFromClone = String.format("insert into %s (select * from %s)", sourceTableName, tempTableName);
		String drop = String.format("drop table %s", tempTableName);

		PreparedStatement stmt_copy = null;
		PreparedStatement stmt_upd = null;
		PreparedStatement stmt_copyFromClone = null;
		PreparedStatement stmt_drop = null;

		try {
			connection.createStatement().executeUpdate(cloneTable);

			stmt_copy = connection.prepareStatement(copy);
			stmt_copy.setInt(1, fromVersion);
			stmt_copy.setString(2, productName);
			stmt_copy.setString(3, tenantid);
			stmt_copy.executeUpdate();

			stmt_upd = connection.prepareStatement(upd1);
			stmt_upd.setInt(1, toVersion);
			stmt_upd.executeUpdate();

			stmt_copyFromClone = connection.prepareStatement(copyFromClone);
			stmt_copyFromClone.executeUpdate();

			stmt_drop = connection.prepareStatement(drop);
			stmt_drop.executeUpdate();

		} finally {
			Db.close(stmt_drop);
			Db.close(stmt_copyFromClone);
			Db.close(stmt_upd);
			Db.close(stmt_copy);

		}
	}

	public boolean isDeleteMarked(ProductKey key) {

		ServiceHelper.validate(key);

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = Db.open();
			if (connection != null) {

				stmt = connection.prepareStatement(
						"select count(*) from  product where productname=? and version=? and tenantid=? and dltusr notnull");
				stmt.setString(1, key.productName);
				stmt.setInt(2, key.version);
				stmt.setString(3, key.tenantid);
				rs = stmt.executeQuery();
				if (rs.next()) {
					Integer n = rs.getInt(1);
					return n > 0;
				}
			}
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
			return true;
		} finally {
			Db.close(rs);
			Db.close(stmt);
			Db.close(connection);
		}
		return false;
	}

}
