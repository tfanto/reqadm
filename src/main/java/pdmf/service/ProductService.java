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

	public List<String> list(Integer tenant) {
		ServiceHelper.validate("Tenant", tenant);

		String theSQL = ServiceHelper.getSQL("productSelectSQL");

		List<String> ret = new ArrayList<>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setInt(1, tenant);
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

	public List<ProductRec> list(Integer tenant, String productName) {
		ServiceHelper.validate("Tenant", tenant);
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
				stmt.setString(1, productName);
				stmt.setInt(2, tenant);
				rs = stmt.executeQuery();
				while (rs.next()) {
					Integer ver = rs.getInt(1);
					String name = rs.getString(2);
					String descr = rs.getString(3);
					Instant crtdat = Db.TimeStamp2Instant(rs.getTimestamp(4));
					Integer chgnbr = rs.getInt(5);

					String shortdescr = rs.getString(6);
					String crtusr = rs.getString(7);
					Instant chgdat = Db.TimeStamp2Instant(rs.getTimestamp(8));
					String chgusr = rs.getString(9);
					Integer crtver = rs.getInt(10);
					Instant dltdat = Db.TimeStamp2Instant(rs.getTimestamp(11));
					String dltusr = rs.getString(12);
					String status = rs.getString(13);

					ProductKey key = new ProductKey(tenant, ver, name);
					ProductRec rec = new ProductRec(key, descr, crtdat, chgnbr);
					rec.shortdescr = shortdescr;
					rec.crtusr = crtusr;
					rec.chgdat = chgdat;
					rec.chgusr = chgusr;
					rec.crtver = crtver;
					rec.dltusr = dltusr;
					rec.dltdat = dltdat;
					rec.status = status;
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

	public List<ProductRec> list(Integer tenant, String productName, Integer version) {
		ServiceHelper.validate("Tenant", tenant);
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
				stmt.setInt(1, tenant);
				stmt.setInt(2, version);
				stmt.setString(3, productName);
				rs = stmt.executeQuery();
				while (rs.next()) {
					Integer ver = rs.getInt(1);
					String name = rs.getString(2);
					String descr = rs.getString(3);
					Instant crtdat = Db.TimeStamp2Instant(rs.getTimestamp(4));
					Integer chgnbr = rs.getInt(5);

					String shortdescr = rs.getString(6);
					String crtusr = rs.getString(7);
					Instant chgdat = Db.TimeStamp2Instant(rs.getTimestamp(8));
					String chgusr = rs.getString(9);
					Integer crtver = rs.getInt(10);
					Instant dltdat = Db.TimeStamp2Instant(rs.getTimestamp(11));
					String dltusr = rs.getString(12);
					String status = rs.getString(13);

					ProductKey key = new ProductKey(tenant, ver, name);
					ProductRec rec = new ProductRec(key, descr, crtdat, chgnbr);
					rec.shortdescr = shortdescr;
					rec.crtusr = crtusr;
					rec.chgdat = chgdat;
					rec.chgusr = chgusr;
					rec.crtver = crtver;
					rec.dltusr = dltusr;
					rec.dltdat = dltdat;
					rec.status = status;
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

	public List<ProductRec> list(Integer tenant, Integer version) {
		ServiceHelper.validate("Tenant", tenant);
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
				stmt.setInt(1, version);
				stmt.setInt(2, tenant);
				rs = stmt.executeQuery();
				while (rs.next()) {
					Integer ver = rs.getInt(1);
					String name = rs.getString(2);
					String descr = rs.getString(3);
					Instant crtdat = Db.TimeStamp2Instant(rs.getTimestamp(4));
					Integer chgnbr = rs.getInt(5);

					String shortdescr = rs.getString(6);
					String crtusr = rs.getString(7);
					Instant chgdat = Db.TimeStamp2Instant(rs.getTimestamp(8));
					String chgusr = rs.getString(9);
					Integer crtver = rs.getInt(10);
					Instant dltdat = Db.TimeStamp2Instant(rs.getTimestamp(11));
					String dltusr = rs.getString(12);
					String status = rs.getString(13);

					ProductKey key = new ProductKey(tenant, ver, name);
					ProductRec rec = new ProductRec(key, descr, crtdat, chgnbr);
					rec.shortdescr = shortdescr;
					rec.crtusr = crtusr;
					rec.chgdat = chgdat;
					rec.chgusr = chgusr;
					rec.crtver = crtver;
					rec.dltusr = dltusr;
					rec.dltdat = dltdat;
					rec.status = status;
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

	public boolean exists(Integer tenant, Integer version, String productName) {
		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = Db.open();
			if (connection != null) {
				return exists(connection, tenant, version, productName);
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

	public boolean exists(Connection connection, Integer tenant, Integer version, String productName) {
		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String theSQL = ServiceHelper.getSQL("productExistsSQL");
		try {
			stmt = connection.prepareStatement(theSQL);
			stmt.setInt(1, tenant);
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

	public ProductRec get(Integer tenant, Integer version, String productName) {
		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = Db.open();
			if (connection != null) {
				return get(connection, tenant, version, productName);
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

	public ProductRec get(Connection connection, Integer tenant, Integer version, String productName) {
		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String theSQL = ServiceHelper.getSQL("productSelectSingleRecSQL");
			stmt = connection.prepareStatement(theSQL);
			stmt.setInt(1, tenant);
			stmt.setInt(2, version);
			stmt.setString(3, productName);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Integer rs_tenant = rs.getInt(1);
				Integer rs_version = rs.getInt(2);
				String rs_productname = rs.getString(3);
				String rs_description = rs.getString(4);
				String shortdescr = rs.getString(5);
				Instant rs_crtdat = Db.TimeStamp2Instant(rs.getTimestamp(6));
				String rs_crtusr = rs.getString(7);
				Instant rs_chgdat = Db.TimeStamp2Instant(rs.getTimestamp(8));
				String rs_chgusr = rs.getString(9);
				Instant rs_dltdat = Db.TimeStamp2Instant(rs.getTimestamp(10));
				String rs_dltusr = rs.getString(11);
				String rs_status = rs.getString(12);				
				Integer rs_chgnbr = rs.getInt(13);
				Integer rs_crtver = rs.getInt(14);

				ProductKey key = new ProductKey(rs_tenant, rs_version, rs_productname);
				ProductRec rec = new ProductRec(key, rs_description, rs_crtdat, rs_chgnbr);
				rec.shortdescr = shortdescr;
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

				if (!exists(connection, product.key.tenant, product.key.version, product.key.productName)) {
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

		if (!exists(product.key.tenant, product.key.version, product.key.productName)) {
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
			Integer firstVersion = getFirstVersionForProduct(connection, product.key.tenant, product.key.productName);
			stmt = connection.prepareStatement(theSQL);
			stmt.setInt(1, product.key.tenant);
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

		if (ProductService.isLocked(product.key.tenant, product.key.version, product.key.productName)) {
			LOGGER.info("LOCKED " + product.key.tenant + product.key.version + " " + product.key.productName);
			return -1;
		}

		try {
			ProductRec dbRec = get(product.key.tenant, product.key.version, product.key.productName);
			if (dbRec == null) {
				return 0;
			}
			if (!product.chgnbr.equals(dbRec.chgnbr)) {
				throw new RecordChangedByAnotherUser();
			}

			Map<String, Object> key = new HashMap<>();
			key.put("version", product.key.version);
			key.put("productname", product.key.productName);
			key.put("tenant", product.key.tenant);

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

	public void remove(Integer tenant, Integer version, String productName, String userid) {
		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		ServiceHelper.validate("Userid", userid);
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			connection = Db.open();
			if (connection != null) {
				connection.setAutoCommit(false);

				if (ProductService.isLocked(connection, tenant, version, productName)) {
					LOGGER.info("LOCKED " + version + " " + productName);
					return;
				}
				stmt = connection.prepareStatement(
						"update product set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=?  and version=? and tenant=?");
				stmt.setString(1, userid);
				stmt.setString(2, productName);
				stmt.setInt(3, version);
				stmt.setInt(4, tenant);
				stmt.executeUpdate();
				deleteAllDependencies(connection, tenant, version, productName, userid);
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

	private void deleteAllDependencies(Connection connection, Integer tenant, Integer version, String productName,
			String userId) throws SQLException {

		PreparedStatement stmtTopic = null;
		PreparedStatement stmtProcess = null;
		PreparedStatement stmtOperation = null;

		try {
			stmtTopic = connection.prepareStatement(
					"update topic set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=?  and version=? and tenant = ?");
			stmtTopic.setString(1, userId);
			stmtTopic.setString(2, productName);
			stmtTopic.setInt(3, version);
			stmtTopic.setInt(4, tenant);
			stmtProcess = connection.prepareStatement(
					"update process set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=?  and version=? and tenant = ?");
			stmtProcess.setString(1, userId);
			stmtProcess.setString(2, productName);
			stmtProcess.setInt(3, version);
			stmtProcess.setInt(4, tenant);
			stmtOperation = connection.prepareStatement(
					"update oper set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=?  and version=? and tenant = ?");
			stmtOperation.setString(1, userId);
			stmtOperation.setString(2, productName);
			stmtOperation.setInt(3, version);
			stmtOperation.setInt(4, tenant);
			stmtTopic.executeUpdate();
			stmtProcess.executeUpdate();
			stmtOperation.executeUpdate();
		} finally {
			Db.close(stmtTopic);
			Db.close(stmtProcess);
			Db.close(stmtOperation);
		}
	}

	public Integer getFirstVersionForProduct(Integer tenant, String productName) {
		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("Product", productName);
		Connection connection = null;
		try {
			connection = Db.open();
			if (connection != null) {
				return getFirstVersionForProduct(connection, tenant, productName);
			}
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
			return -1;
		} finally {
			Db.close(connection);
		}
		return -1;
	}

	private Integer getFirstVersionForProduct(Connection connection, Integer tenant, String product)
			throws SQLException {

		ServiceHelper.validate("Product", product);

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection
					.prepareStatement("select version from product where productname=? and tenant=? order by version");
			stmt.setString(1, product);
			stmt.setInt(2, tenant);
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

	public static boolean isLocked(Integer tenant, Integer version, String productName) {
		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		Connection connection = null;
		try {
			connection = Db.open();
			if (connection != null) {
				return ProductService.isLocked(connection, tenant, version, productName);
			}
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
			return true;
		} finally {
			Db.close(connection);
		}
		return true;
	}

	public static boolean isLocked(Connection connection, Integer tenant, Integer version, String productName)
			throws SQLException {
		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection
					.prepareStatement("select status from product where version=? and productname=? and tenant=? ");
			stmt.setInt(1, version);
			stmt.setString(2, productName);
			stmt.setInt(3, tenant);
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

	public void createNewVersion(Integer tenant, Integer fromVersion, Integer toVersion, String productName,
			String loggedInUserId) {
		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("FromVersion", fromVersion);
		ServiceHelper.validate("ToVersion", toVersion);
		ServiceHelper.validate("ProductName", productName);
		ServiceHelper.validate("user", loggedInUserId);

		if (ProductService.isLocked(tenant, fromVersion, productName)) {
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

			ProductRec productRec = get(connection, tenant, fromVersion, productName);
			if (productRec == null) {
				LOGGER.error("Oldversion NOT found when trying to create a new version");
				throw new RuntimeException();
			}

			if (exists(connection, tenant, toVersion, productName)) {
				LOGGER.error("Target version already exists " + productName + "  " + toVersion);
				throw new RuntimeException();
			}

			productRec.key.version = toVersion;
			productRec.status = "wrk";

			createVersion(productRec, loggedInUserId);

			copyData(connection, tenant, productName, fromVersion, toVersion);

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

		if (exists(productRec.key.tenant, productRec.key.version, productRec.key.productName)) {
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

			Integer firstVersion = getFirstVersionForProduct(connection, productRec.key.tenant,
					productRec.key.productName);

			stmt2 = connection.prepareStatement("update product set status='locked' where productname=? and tenant=?");
			stmt2.setString(1, productRec.key.productName);
			stmt2.setInt(2, productRec.key.tenant);
			stmt2.executeUpdate();

			stmt = connection.prepareStatement(
					"insert into product (version,productname,crtdat,chgnbr,description,status,crtver,crtusr,shortdescr,tenant) values (?,?,?,?,?,?,?,?,?,?)");
			stmt.setInt(1, productRec.key.version);
			stmt.setString(2, productRec.key.productName);
			stmt.setTimestamp(3, Db.Instant2TimeStamp(Instant.now()));
			stmt.setInt(4, 0);
			stmt.setString(5, productRec.description);
			stmt.setString(6, productRec.status);
			stmt.setInt(7, firstVersion == null ? productRec.key.version : firstVersion);
			stmt.setString(8, loggedInUserId);
			stmt.setString(9, productRec.shortdescr);
			stmt.setInt(10, productRec.key.tenant);
			return stmt.executeUpdate();
		} finally {
			Db.close(stmt2);
			Db.close(stmt);
			Db.close(connection);
		}
	}

	private void copyData(Connection connection, Integer tenant, String productName, Integer fromVersion,
			Integer toVersion) throws SQLException {

		copyData(connection, tenant, productName, fromVersion, toVersion, "topic");
		copyData(connection, tenant, productName, fromVersion, toVersion, "process");
		copyData(connection, tenant, productName, fromVersion, toVersion, "oper");
	}

	private void copyData(Connection connection, Integer tenant, String productName, Integer fromVersion,
			Integer toVersion, String sourceTableName) throws SQLException {

		String tempTableName = "WRK";
		tempTableName = tempTableName.replace("-", "").toLowerCase();

		String cloneTable = String.format("create table  %s as table %s with no data", tempTableName, sourceTableName);
//		String copy = String.format("insert into %s (select * from %s where version=? and productname=? and dltusr isnull)", tempTableName, sourceTableName);
		String copy = String.format("insert into %s (select * from %s where version=? and productname=? and tenant=?)",
				tempTableName, sourceTableName);
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
			stmt_copy.setInt(3, tenant);
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
						"select count(*) from  product where productname=? and version=? and tenant=? and dltusr notnull");
				stmt.setString(1, key.productName);
				stmt.setInt(2, key.version);
				stmt.setInt(3, key.tenant);
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
