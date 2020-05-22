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

import pdmf.model.TopicKey;
import pdmf.model.TopicRec;
import pdmf.service.support.ServiceHelper;
import pdmf.sys.Db;
import pdmf.sys.RecordChangedByAnotherUser;

public class TopicService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TopicService.class);

	public List<TopicRec> list(Integer tenant, Integer version, String productName) {

		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		List<TopicRec> ret = new ArrayList<>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String theSQL = ServiceHelper.getSQL("topicsSelectSQL");

		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setInt(1, tenant);
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

					Integer rs_tenant = rs.getInt("tenant");
					Integer rs_version = rs.getInt("version");
					String rs_productname = rs.getString("productname");
					String rs_topicname = rs.getString("topicname");

					TopicKey key = new TopicKey(rs_tenant, rs_version, rs_productname, rs_topicname);
					TopicRec rec = new TopicRec(key, rs_description, rs_crtdat, rs_chgnbr);
					rec.shortdescr = rs_shortdescr;
					rec.crtusr = rs_crtusr;
					rec.chgdat = rs_chgdat;
					rec.chgusr = rs_chgusr;
					rec.crtver = rs_crtver;
					rec.dltusr = rs_dltusr;
					rec.dltdat = rs_dltdat;
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

	public boolean exists(TopicRec topic) {
		ServiceHelper.validate(topic);

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String theSQL = ServiceHelper.getSQL("topicExistsSQL");

		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setInt(1, topic.key.tenant);
				stmt.setInt(2, topic.key.version);
				stmt.setString(3, topic.key.productName);
				stmt.setString(4, topic.key.topicName);
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

	public boolean isDeleteMarked(TopicKey key) {
		ServiceHelper.validate(key);

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String theSQL = ServiceHelper.getSQL("topicDeleteMarkedSQL");

		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setInt(1, key.tenant);
				stmt.setInt(2, key.version);
				stmt.setString(3, key.productName);
				stmt.setString(4, key.topicName);
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

	public TopicRec get(Integer tenant, Integer version, String productName, String topicName) {
		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		ServiceHelper.validate("Topic", topicName);

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		TopicRec rec = null;

		String theSQL = ServiceHelper.getSQL("topicSelectSingleRecSQL");

		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setInt(1, tenant);
				stmt.setInt(2, version);
				stmt.setString(3, productName);
				stmt.setString(4, topicName);
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

					Integer rs_tenant = rs.getInt("tenant");
					Integer rs_version = rs.getInt("version");
					String rs_productname = rs.getString("productname");
					String rs_topicname = rs.getString("topicname");

					TopicKey key = new TopicKey(rs_tenant, rs_version, rs_productname, rs_topicname);
					rec = new TopicRec(key, rs_description, rs_crtdat, rs_chgnbr);
					rec.shortdescr = rs_shortdescr;
					rec.crtusr = rs_crtusr;
					rec.chgdat = rs_chgdat;
					rec.chgusr = rs_chgusr;
					rec.crtver = rs_crtver;
					rec.dltdat = rs_dltdat;
					rec.dltusr = rs_dltusr;
				}
			}
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		} finally {
			Db.close(rs);
			Db.close(stmt);
			Db.close(connection);
		}
		return rec;
	}

	public String store(TopicRec topic, String loggedInUserId) {
		ServiceHelper.validate(topic);
		ServiceHelper.validate("user", loggedInUserId);

		if (ProductService.isLocked(topic.key.tenant, topic.key.version, topic.key.productName)) {
			LOGGER.info("LOCKED " + topic.key.tenant + " " + topic.key.version + " " + topic.key.productName);
			return "";
		}

		if (isDeleteMarked(topic.key)) {
			LOGGER.info("Record is marked for delete. No Action.");
			return null;
		}

		topic.shortdescr = ServiceHelper.ensureStringLength(topic.shortdescr, 100);
		topic.description = ServiceHelper.ensureStringLength(topic.description, 995);

		if (!exists(topic)) {
			insert(topic, loggedInUserId);
		} else {
			update(topic, loggedInUserId);
		}

		return null;
	}

	private Integer insert(TopicRec topic, String loggedInUserId) {
		Connection connection = null;
		PreparedStatement stmt = null;

		String theSQL = ServiceHelper.getSQL("topicInsertSQL");

		try {
			connection = Db.open();
			if (connection != null) {
				Integer firstVersion = getFirstVersionForTopic(connection, topic.key.tenant, topic.key.productName,
						topic.key.topicName);
				stmt = connection.prepareStatement(theSQL);
				stmt.setInt(1, topic.key.tenant);
				stmt.setInt(2, topic.key.version);
				stmt.setString(3, topic.key.productName);
				stmt.setString(4, topic.key.topicName);
				stmt.setString(5, topic.description);
				stmt.setTimestamp(6, Db.Instant2TimeStamp(Instant.now()));
				stmt.setInt(7, 0);
				stmt.setInt(8, firstVersion == null ? topic.key.version : firstVersion);
				stmt.setString(9, loggedInUserId);
				stmt.setString(10, topic.shortdescr);

				return stmt.executeUpdate();
			}
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		} finally {
			Db.close(stmt);
			Db.close(connection);
		}
		return null;
	}

	private Integer update(TopicRec topic, String loggedInUserId) {
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			connection = Db.open();
			if (connection != null) {
				TopicRec dbRec = get(topic.key.tenant, topic.key.version, topic.key.productName, topic.key.topicName);
				if (dbRec == null) {
					return 0;
				}
				if (!topic.chgnbr.equals(dbRec.chgnbr)) {
					throw new RecordChangedByAnotherUser();
				}

				Map<String, Object> key = new HashMap<>();
				key.put("productname", topic.key.productName);
				key.put("topicname", topic.key.topicName);
				key.put("version", topic.key.version);
				key.put("tenant", topic.key.tenant);

				Map<String, Object> value = new HashMap<>();
				value.put("description", topic.description);
				value.put("shortdescr", topic.shortdescr);
				value.put("chgusr", loggedInUserId);

				stmt = Db.prepareUpdateStatement(connection, "topic", key, value);
				stmt = Db.addDataToPreparedUpdateStatement(stmt, key, value);

				return stmt.executeUpdate();
			}
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		} finally {
			Db.close(stmt);
			Db.close(connection);
		}
		return null;
	}

	public void remove(Integer tenant, Integer version, String productName, String topicName, String userId) {
		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		ServiceHelper.validate("Topic", topicName);
		ServiceHelper.validate("Userid", userId);
		Connection connection = null;
		PreparedStatement stmt = null;

		if (ProductService.isLocked(tenant, version, productName)) {
			LOGGER.info("LOCKED " + tenant + "  " + version + " " + productName);
			return;
		}

		// already done we dont want to change the delete date
		TopicKey key = new TopicKey(tenant, version, productName, topicName);
		if (isDeleteMarked(key)) {
			LOGGER.info("Record is already marked for delete. No Action.");
			return;
		}

		try {
			connection = Db.open();
			if (connection != null) {
				connection.setAutoCommit(false);

				stmt = connection.prepareStatement(
						"update topic set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=? and topicname=? and version=? and tenant=?");
				stmt.setString(1, userId);
				stmt.setString(2, productName);
				stmt.setString(3, topicName);
				stmt.setInt(4, version);
				stmt.setInt(5, tenant);
				stmt.executeUpdate();
				deleteAllDependencies(connection, tenant, version, productName, topicName, userId);
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
			String topicName, String userId) throws SQLException {

		PreparedStatement stmtProcess = null;
		PreparedStatement stmtOperation = null;

		try {
			stmtProcess = connection.prepareStatement(
					"update process set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=? and topicname=? and version=? and tenant=?");
			stmtProcess.setString(1, userId);
			stmtProcess.setString(2, productName);
			stmtProcess.setString(3, topicName);
			stmtProcess.setInt(4, version);
			stmtProcess.setInt(5, tenant);
			stmtOperation = connection.prepareStatement(
					"update oper set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=? and topicname=? and version=? and tenant=?");
			stmtOperation.setString(1, userId);
			stmtOperation.setString(2, productName);
			stmtOperation.setString(3, topicName);
			stmtOperation.setInt(4, version);
			stmtOperation.setInt(5, tenant);

			stmtProcess.executeUpdate();
			stmtOperation.executeUpdate();
		} finally {
			Db.close(stmtProcess);
			Db.close(stmtOperation);
		}
	}

	private Integer getFirstVersionForTopic(Connection connection, Integer tenant, String product, String topic)
			throws SQLException {

		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("Product", product);
		ServiceHelper.validate("Topic", topic);

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement(
					"select version from topic where productname=? and topicname=? and tenant=? order by version");
			stmt.setString(1, product);
			stmt.setString(2, topic);
			stmt.setInt(3, tenant);
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

}
