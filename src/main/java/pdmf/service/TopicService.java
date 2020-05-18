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

	private String topicDeleteMarkedSQL = "select count(*) from  topic  where productname=? and topicname=? and version=? and dltusr notnull";
	private String topicExistsSQL = "select count(*) from  topic  where productname=? and topicname=? and version=?";
	private String topicSelectSingleRecSQL = "select topicname,description,crtdat,chgnbr,shortdescr,crtusr,chgdat,chgusr,crtver,dltdat,dltusr from topic where productname=? and topicname=? and version=?";
	private String topicInsertSQL = "insert into topic  (version, productname, topicname,description,crtdat,chgnbr,crtver,crtusr,shortdescr) values(?,?,?,?,?,?,?,?,?)";
	private String topicsSelectSQL = "select topicname,description,crtdat,chgnbr,shortdescr,crtusr,chgdat,chgusr,crtver,dltdat,dltusr from topic where productname=?  and version=? order by productname,topicname";

	public List<TopicRec> list(Integer version, String productName) {

		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		List<TopicRec> ret = new ArrayList<>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(topicsSelectSQL);
				stmt.setString(1, productName);
				stmt.setInt(2, version);
				rs = stmt.executeQuery();
				while (rs.next()) {
					String topicName = rs.getString(1);
					String descr = rs.getString(2);
					Instant crtdat = Db.TimeStamp2Instant(rs.getTimestamp(3));
					Integer chgnbr = rs.getInt(4);

					String shortdescr = rs.getString(5);
					String crtusr = rs.getString(6);
					Instant chgdat = Db.TimeStamp2Instant(rs.getTimestamp(7));
					String chgusr = rs.getString(8);
					Integer crtver = rs.getInt(9);
					Instant dltdat = Db.TimeStamp2Instant(rs.getTimestamp(10));
					String dltusr = rs.getString(11);

					TopicKey key = new TopicKey(version, productName, topicName);
					TopicRec rec = new TopicRec(key, descr, crtdat, chgnbr);
					rec.shortdescr = shortdescr;
					rec.crtusr = crtusr;
					rec.chgdat = chgdat;
					rec.chgusr = chgusr;
					rec.crtver = crtver;
					rec.dltusr = dltusr;
					rec.dltdat = dltdat;
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
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(topicExistsSQL);
				stmt.setString(1, topic.key.productName);
				stmt.setString(2, topic.key.topicName);
				stmt.setInt(3, topic.key.version);
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
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(topicDeleteMarkedSQL);
				stmt.setString(1, key.productName);
				stmt.setString(2, key.topicName);
				stmt.setInt(3, key.version);
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

	public TopicRec get(Integer version, String productName, String topicName) {
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		ServiceHelper.validate("Topic", topicName);

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		TopicRec rec = null;
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(topicSelectSingleRecSQL);
				stmt.setString(1, productName);
				stmt.setString(2, topicName);
				stmt.setInt(3, version);
				rs = stmt.executeQuery();
				if (rs.next()) {
					String tName = rs.getString(1);
					String descr = rs.getString(2);
					Instant crtdat = Db.TimeStamp2Instant(rs.getTimestamp(3));
					Integer chgnbr = rs.getInt(4);
					String shortdescr = rs.getString(5);
					String crtusr = rs.getString(6);
					Instant chgdat = Db.TimeStamp2Instant(rs.getTimestamp(7));
					String chgusr = rs.getString(8);
					Integer crtver = rs.getInt(9);
					Instant dltdat = Db.TimeStamp2Instant(rs.getTimestamp(10));
					String dltusr = rs.getString(11);

					TopicKey key = new TopicKey(version, productName, tName);
					rec = new TopicRec(key, descr, crtdat, chgnbr);
					rec.shortdescr = shortdescr;
					rec.crtusr = crtusr;
					rec.chgdat = chgdat;
					rec.chgusr = chgusr;
					rec.crtver = crtver;
					rec.dltdat = dltdat;
					rec.dltusr = dltusr;
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

		if (ProductService.isLocked(topic.key.version, topic.key.productName)) {
			LOGGER.info("LOCKED " + topic.key.version + " " + topic.key.productName);
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
		try {
			connection = Db.open();
			if (connection != null) {
				Integer firstVersion = getFirstVersionForTopic(connection, topic.key.productName, topic.key.topicName);
				stmt = connection.prepareStatement(topicInsertSQL);
				stmt.setInt(1, topic.key.version);
				stmt.setString(2, topic.key.productName);
				stmt.setString(3, topic.key.topicName);
				stmt.setString(4, topic.description);
				stmt.setTimestamp(5, Db.Instant2TimeStamp(Instant.now()));
				stmt.setInt(6, 0);
				stmt.setInt(7, firstVersion == null ? topic.key.version : firstVersion);
				stmt.setString(8, loggedInUserId);
				stmt.setString(9, topic.shortdescr);

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
				TopicRec dbRec = get(topic.key.version, topic.key.productName, topic.key.topicName);
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

	public void remove(Integer version, String productName, String topicName, String userId) {
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		ServiceHelper.validate("Topic", topicName);
		ServiceHelper.validate("Userid", userId);
		Connection connection = null;
		PreparedStatement stmt = null;

		if (ProductService.isLocked(version, productName)) {
			LOGGER.info("LOCKED " + version + " " + productName);
			return;
		}
		
		// already done we dont want to change the delete date
		TopicKey key = new TopicKey(version, productName, topicName);
		if (isDeleteMarked(key)) {
			LOGGER.info("Record is already marked for delete. No Action.");
			return;
		}

		try {
			connection = Db.open();
			if (connection != null) {
				connection.setAutoCommit(false);


				stmt = connection.prepareStatement("update topic set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=? and topicname=? and version=?");
				stmt.setString(1, userId);
				stmt.setString(2, productName);
				stmt.setString(3, topicName);
				stmt.setInt(4, version);
				stmt.executeUpdate();
				deleteAllDependencies(connection, version, productName, topicName, userId);
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

	private void deleteAllDependencies(Connection connection, Integer version, String productName, String topicName, String userId) throws SQLException {

		PreparedStatement stmtProcess = null;
		PreparedStatement stmtOperation = null;

		try {
			stmtProcess = connection.prepareStatement("update process set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=? and topicname=? and version=?");
			stmtProcess.setString(1, userId);
			stmtProcess.setString(2, productName);
			stmtProcess.setString(3, topicName);
			stmtProcess.setInt(4, version);
			stmtOperation = connection.prepareStatement("update oper set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=? and topicname=? and version=?");
			stmtOperation.setString(1, userId);
			stmtOperation.setString(2, productName);
			stmtOperation.setString(3, topicName);
			stmtOperation.setInt(4, version);

			stmtProcess.executeUpdate();
			stmtOperation.executeUpdate();
		} finally {
			Db.close(stmtProcess);
			Db.close(stmtOperation);
		}
	}

	private Integer getFirstVersionForTopic(Connection connection, String product, String topic) throws SQLException {

		ServiceHelper.validate("Product", product);
		ServiceHelper.validate("Topic", topic);

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("select version from topic where productname=? and topicname=? order by version");
			stmt.setString(1, product);
			stmt.setString(2, topic);
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
