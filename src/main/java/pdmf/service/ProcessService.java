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

import pdmf.model.ProcessKey;
import pdmf.model.ProcessRec;
import pdmf.service.support.ServiceHelper;
import pdmf.sys.Db;
import pdmf.sys.RecordChangedByAnotherUser;

public class ProcessService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessService.class);

	public List<ProcessRec> list(String tenantid, Integer version, String productName, String topicName) {
		ServiceHelper.validate("Tenant", tenantid);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		ServiceHelper.validate("Topic", topicName);

		String theSQL = ServiceHelper.getSQL("processSelectSQL");
		List<ProcessRec> ret = new ArrayList<>();
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
				stmt.setString(4, topicName);
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

					String rs_tenantid = rs.getString("tenantid");
					Integer rs_version = rs.getInt("version");
					String rs_productname = rs.getString("productname");
					String rs_topicname = rs.getString("topicname");
					String rs_processname = rs.getString("processname");
					Integer rs_processseq = rs.getInt("processseq");

					ProcessKey key = new ProcessKey(rs_tenantid, rs_version, rs_productname, rs_topicname,
							rs_processname, rs_processseq);
					ProcessRec rec = new ProcessRec(key, rs_description, rs_crtdat, rs_chgnbr);
					rec.shortdescr = rs_shortdescr;
					rec.crtusr = rs_crtusr;
					rec.chgdat = rs_chgdat;
					rec.chgusr = rs_chgusr;
					rec.crtver = rs_crtver;
					rec.dltdat = rs_dltdat;
					rec.dltusr = rs_dltusr;
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

	public boolean exists(ProcessRec process) {
		ServiceHelper.validate(process);

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String theSQL = ServiceHelper.getSQL("processExistsSQL");
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setString(1, process.key.tenantid);
				stmt.setInt(2, process.key.version);
				stmt.setString(3, process.key.productName);
				stmt.setString(4, process.key.topicName);
				stmt.setString(5, process.key.processName);
				stmt.setInt(6, process.key.processSeq);
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

	public boolean isDeleteMarked(ProcessKey key) {
		ServiceHelper.validate(key);

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String theSQL = ServiceHelper.getSQL("processDeleteMarkedSQL");
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setString(1, key.tenantid);
				stmt.setInt(2, key.version);
				stmt.setString(3, key.productName);
				stmt.setString(4, key.topicName);
				stmt.setString(5, key.processName);
				stmt.setInt(6, key.processSeq);
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

	public ProcessRec get(String tenantid, Integer version, String productName, String topicName, String processName,
			Integer sequence) {
		ServiceHelper.validate("Tenant", tenantid);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		ServiceHelper.validate("Topic", topicName);
		ServiceHelper.validate("Process", processName);
		ServiceHelper.validate("Sequence", sequence);
		String theSQL = ServiceHelper.getSQL("processSelectSingleRecSQL");

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ProcessRec rec = null;
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setString(1, tenantid);
				stmt.setInt(2, version);
				stmt.setString(3, productName);
				stmt.setString(4, topicName);
				stmt.setString(5, processName);
				stmt.setInt(6, sequence);
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

					String rs_tenantid = rs.getString("tenantid");
					Integer rs_version = rs.getInt("version");
					String rs_productname = rs.getString("productname");
					String rs_topicname = rs.getString("topicname");
					String rs_processname = rs.getString("processname");
					Integer rs_processseq = rs.getInt("processseq");

					ProcessKey key = new ProcessKey(rs_tenantid, rs_version, rs_productname, rs_topicname, rs_processname,
							rs_processseq);
					rec = new ProcessRec(key, rs_description, rs_crtdat, rs_chgnbr);
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

	public String store(ProcessRec rec, String loggedInUserId) {
		ServiceHelper.validate(rec);
		ServiceHelper.validate("userid", loggedInUserId);

		if (ProductService.isLocked(rec.key.tenantid, rec.key.version, rec.key.productName)) {
			LOGGER.info("LOCKED " + rec.key.tenantid + " " + rec.key.version + " " + rec.key.productName);
			return null;
		}

		if (isDeleteMarked(rec.key)) {
			LOGGER.info("Record is marked for delete. No Action.");
			return null;
		}

		rec.shortdescr = ServiceHelper.ensureStringLength(rec.shortdescr, 100);
		rec.description = ServiceHelper.ensureStringLength(rec.description, 995);

		if (!exists(rec)) {
			insert(rec, loggedInUserId);
		} else {
			update(rec, loggedInUserId);
		}

		return null;
	}

	private Integer insert(ProcessRec rec, String loggedInUserId) {
		Connection connection = null;
		PreparedStatement stmt = null;
		String theSQL = ServiceHelper.getSQL("processInsertSQL");

		try {
			connection = Db.open();
			if (connection != null) {
				Integer firstVersion = getFirstVersionForProcess(connection, rec.key.tenantid, rec.key.productName,
						rec.key.topicName, rec.key.processName, rec.key.processSeq);
				stmt = connection.prepareStatement(theSQL);
				stmt.setString(1, rec.key.tenantid);
				stmt.setInt(2, rec.key.version);
				stmt.setString(3, rec.key.productName);
				stmt.setString(4, rec.key.topicName);
				stmt.setString(5, rec.key.processName);
				stmt.setInt(6, rec.key.processSeq);
				stmt.setString(7, rec.description);
				stmt.setTimestamp(8, Db.Instant2TimeStamp(Instant.now()));
				stmt.setInt(9, 0);
				stmt.setInt(10, firstVersion == null ? rec.key.version : firstVersion);
				stmt.setString(11, loggedInUserId);
				stmt.setString(12, rec.shortdescr);
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

	private Integer update(ProcessRec rec, String loggedInUserId) {
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			connection = Db.open();
			if (connection != null) {
				ProcessRec dbRec = get(rec.key.tenantid, rec.key.version, rec.key.productName, rec.key.topicName,
						rec.key.processName, rec.key.processSeq);
				if (dbRec == null) {
					return 0;
				}
				if (!rec.chgnbr.equals(dbRec.chgnbr)) {
					throw new RecordChangedByAnotherUser();
				}

				Map<String, Object> key = new HashMap<>();
				key.put("productname", rec.key.productName);
				key.put("topicname", rec.key.topicName);
				key.put("processname", rec.key.processName);
				key.put("processseq", rec.key.processSeq);
				key.put("version", rec.key.version);
				key.put("tenantid", rec.key.tenantid);

				Map<String, Object> value = new HashMap<>();
				value.put("description", rec.description);
				value.put("shortdescr", rec.shortdescr);
				value.put("chgusr", loggedInUserId);

				stmt = Db.prepareUpdateStatement(connection, "process", key, value);
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

	public void remove(String tenantid, Integer version, String productName, String topicName, String processName,
			Integer sequence, String userid) {
		ServiceHelper.validate("tenant", tenantid);
		ServiceHelper.validate("Userid", userid);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		ServiceHelper.validate("Topic", topicName);
		ServiceHelper.validate("Process", processName);
		ServiceHelper.validate("Sequence", sequence);
		Connection connection = null;
		PreparedStatement stmt = null;

		// already done we dont want to change the delete date
		ProcessKey key = new ProcessKey(tenantid, version, productName, topicName, processName, sequence);
		if (isDeleteMarked(key)) {
			LOGGER.info("Record is already marked for delete. No Action.");
			return;
		}

		try {
			connection = Db.open();
			if (connection != null) {
				connection.setAutoCommit(false);
				if (ProductService.isLocked(tenantid, version, productName)) {
					LOGGER.info("LOCKED " + tenantid + " " + version + " " + productName);
					return;
				}

				stmt = connection.prepareStatement(
						"update process set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=? and topicname=? and processname=? and processseq=?  and version=? and tenantid=?");

				stmt.setString(1, userid);
				stmt.setString(2, productName);
				stmt.setString(3, topicName);
				stmt.setString(4, processName);
				stmt.setInt(5, sequence);
				stmt.setInt(6, version);
				stmt.setString(7, tenantid);
				stmt.executeUpdate();
				deleteAllDependencies(connection, tenantid, version, productName, topicName, processName, sequence,
						userid);
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
			String topicName, String processName, Integer processSeq, String userId) throws SQLException {

		PreparedStatement stmtOperation = null;

		try {
			stmtOperation = connection.prepareStatement(
					"update oper set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=? and topicname=?  and processname=? and processseq=? and version=? and tenantid=?");
			stmtOperation.setString(1, userId);
			stmtOperation.setString(2, productName);
			stmtOperation.setString(3, topicName);
			stmtOperation.setString(4, processName);
			stmtOperation.setInt(5, processSeq);
			stmtOperation.setInt(6, version);
			stmtOperation.setString(7, tenantid);
			stmtOperation.executeUpdate();
		} finally {
			Db.close(stmtOperation);
		}
	}

	private Integer getFirstVersionForProcess(Connection connection, String tenantid, String product, String topic,
			String process, Integer processSeq) throws SQLException {
		ServiceHelper.validate("Tenant", tenantid);
		ServiceHelper.validate("Product", product);
		ServiceHelper.validate("Topic", topic);
		ServiceHelper.validate("Process", process);
		ServiceHelper.validate("ProcessSeq", processSeq);

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement(
					"select version from process where productname=? and topicname=? and processname=? and processseq=? and tenantid=? order by version");
			stmt.setString(1, product);
			stmt.setString(2, topic);
			stmt.setString(3, process);
			stmt.setInt(4, processSeq);
			stmt.setString(5, tenantid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return null;
			}
		} finally

		{
			Db.close(rs);
			Db.close(stmt);
		}
	}

}
