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

	private String processDeleteMarkedSQL = "select count(*) from  process  where productname=? and topicname=? and processname=? and seq=? and version=? and dltusr notnull";
	private String processExistsSQL = "select count(*) from  process  where productname=? and topicname=? and processname=? and seq=? and version=?";
	private String processSelectSingleRecSQL = "select processname,seq, description,crtdat,chgnbr,shortdescr,crtusr,chgdat,chgusr,crtver,dltdat,dltusr  from process where productname=? and topicname=? and processname=? and seq=? and version=?";
	private String processInsertSQL = "insert into process  (version, productname, topicname,processname,seq,description,crtdat,chgnbr,crtver,crtusr,shortdescr) values(?,?,?,?,?,?,?,?,?,?,?)";
	private String processSelectSQL = "select topicname,processname,seq, description,crtdat,chgnbr,shortdescr,crtusr,chgdat,chgusr,crtver,dltdat,dltusr  from process where productname=?  and topicname=?  and version=? order by productname,topicname,processname,seq";

	public List<ProcessRec> list(Integer version, String productName, String topicName) {
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		ServiceHelper.validate("Topic", topicName);

		List<ProcessRec> ret = new ArrayList<>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(processSelectSQL);
				stmt.setString(1, productName);
				stmt.setString(2, topicName);
				stmt.setInt(3, version);
				rs = stmt.executeQuery();
				while (rs.next()) {
					String processName = rs.getString(2);
					Integer sequence = rs.getInt(3);
					String description = rs.getString(4);
					Instant crtdat = Db.TimeStamp2Instant(rs.getTimestamp(5));
					Integer chgnbr = rs.getInt(6);
					String shortdescr = rs.getString(7);
					String crtusr = rs.getString(8);
					Instant chgdat = Db.TimeStamp2Instant(rs.getTimestamp(9));
					String chgusr = rs.getString(10);
					Integer crtver = rs.getInt(11);
					Instant dltdat = Db.TimeStamp2Instant(rs.getTimestamp(12));
					String dltusr = rs.getString(13);

					ProcessKey key = new ProcessKey(version, productName, topicName, processName, sequence);
					ProcessRec rec = new ProcessRec(key, description, crtdat, chgnbr);
					rec.shortdescr = shortdescr;
					rec.crtusr = crtusr;
					rec.chgdat = chgdat;
					rec.chgusr = chgusr;
					rec.crtver = crtver;
					rec.dltdat = dltdat;
					rec.dltusr = dltusr;
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
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(processExistsSQL);
				stmt.setString(1, process.key.productName);
				stmt.setString(2, process.key.topicName);
				stmt.setString(3, process.key.processName);
				stmt.setInt(4, process.key.sequence);
				stmt.setInt(5, process.key.version);
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
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(processDeleteMarkedSQL);
				stmt.setString(1, key.productName);
				stmt.setString(2, key.topicName);
				stmt.setString(3, key.processName);
				stmt.setInt(4, key.sequence);
				stmt.setInt(5, key.version);
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

	public ProcessRec get(Integer version, String productName, String topicName, String processName, Integer sequence) {
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		ServiceHelper.validate("Topic", topicName);
		ServiceHelper.validate("Process", processName);
		ServiceHelper.validate("Sequence", sequence);

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ProcessRec rec = null;
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(processSelectSingleRecSQL);
				stmt.setString(1, productName);
				stmt.setString(2, topicName);
				stmt.setString(3, processName);
				stmt.setInt(4, sequence);
				stmt.setInt(5, version);
				rs = stmt.executeQuery();
				if (rs.next()) {
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

					ProcessKey key = new ProcessKey(version, productName, topicName, processName, sequence);
					rec = new ProcessRec(key, descr, crtdat, chgnbr);
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

	public String store(ProcessRec rec, String loggedInUserId) {
		ServiceHelper.validate(rec);
		ServiceHelper.validate("userid", loggedInUserId);

		if (ProductService.isLocked(rec.key.version, rec.key.productName)) {
			LOGGER.info("LOCKED " + rec.key.version + " " + rec.key.productName);
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
		try {
			connection = Db.open();
			if (connection != null) {
				Integer firstVersion = getFirstVersionForProcess(connection, rec.key.productName, rec.key.topicName, rec.key.processName, rec.key.sequence);
				stmt = connection.prepareStatement(processInsertSQL);
				stmt.setInt(1, rec.key.version);
				stmt.setString(2, rec.key.productName);
				stmt.setString(3, rec.key.topicName);
				stmt.setString(4, rec.key.processName);
				stmt.setInt(5, rec.key.sequence);
				stmt.setString(6, rec.description);
				stmt.setTimestamp(7, Db.Instant2TimeStamp(Instant.now()));
				stmt.setInt(8, 0);
				stmt.setInt(9, firstVersion == null ? rec.key.version : firstVersion);
				stmt.setString(10, loggedInUserId);
				stmt.setString(11, rec.shortdescr);
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
				ProcessRec dbRec = get(rec.key.version, rec.key.productName, rec.key.topicName, rec.key.processName, rec.key.sequence);
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
				key.put("seq", rec.key.sequence);
				key.put("version", rec.key.version);

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

	public void remove(Integer version, String productName, String topicName, String processName, Integer sequence, String userid) {
		ServiceHelper.validate("Userid", userid);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		ServiceHelper.validate("Topic", topicName);
		ServiceHelper.validate("Process", processName);
		ServiceHelper.validate("Sequence", sequence);
		Connection connection = null;
		PreparedStatement stmt = null;

		// already done we dont want to change the delete date
		ProcessKey key = new ProcessKey(version, productName, topicName, processName, sequence);
		if (isDeleteMarked(key)) {
			LOGGER.info("Record is already marked for delete. No Action.");
			return;
		}

		try {
			connection = Db.open();
			if (connection != null) {
				connection.setAutoCommit(false);
				if (ProductService.isLocked(version, productName)) {
					LOGGER.info("LOCKED " + version + " " + productName);
					return;
				}

				stmt = connection.prepareStatement("update process set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=? and topicname=? and processname=? and seq=?  and version=?");

				stmt.setString(1, userid);
				stmt.setString(2, productName);
				stmt.setString(3, topicName);
				stmt.setString(4, processName);
				stmt.setInt(5, sequence);
				stmt.setInt(6, version);
				stmt.executeUpdate();
				deleteAllDependencies(connection, version, productName, topicName, processName, sequence, userid);
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

	private void deleteAllDependencies(Connection connection, Integer version, String productName, String topicName, String processName, Integer processSeq, String userId) throws SQLException {

		PreparedStatement stmtOperation = null;

		try {
			stmtOperation = connection.prepareStatement("update oper set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=? and topicname=?  and processname=? and seq=? and version=?");
			stmtOperation.setString(1, userId);
			stmtOperation.setString(2, productName);
			stmtOperation.setString(3, topicName);
			stmtOperation.setString(4, processName);
			stmtOperation.setInt(5, processSeq);
			stmtOperation.setInt(6, version);
			stmtOperation.executeUpdate();
		} finally {
			Db.close(stmtOperation);
		}
	}

	private Integer getFirstVersionForProcess(Connection connection, String product, String topic, String process, Integer processSeq) throws SQLException {
		ServiceHelper.validate("Product", product);
		ServiceHelper.validate("Topic", topic);
		ServiceHelper.validate("Process", process);
		ServiceHelper.validate("ProcessSeq", processSeq);

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("select version from process where productname=? and topicname=? and processname=? and seq=? order by version");
			stmt.setString(1, product);
			stmt.setString(2, topic);
			stmt.setString(3, process);
			stmt.setInt(4, processSeq);
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