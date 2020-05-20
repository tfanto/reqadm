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

import pdmf.model.OperationKey;
import pdmf.model.OperationRec;
import pdmf.service.support.ServiceHelper;
import pdmf.sys.Db;
import pdmf.sys.RecordChangedByAnotherUser;

public class OperationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OperationService.class);

	public List<OperationRec> list(Integer tenant, Integer version, String productName, String topicName,
			String processName) {

		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		ServiceHelper.validate("Topic", topicName);
		ServiceHelper.validate("Process", processName);

		String theSQL = ServiceHelper.getSQL("operationSelectSQL2");

		List<OperationRec> ret = new ArrayList<>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setString(1, productName);
				stmt.setString(2, topicName);
				stmt.setString(3, processName);
				stmt.setInt(4, version);
				stmt.setInt(5, tenant);
				rs = stmt.executeQuery();
				while (rs.next()) {
					Integer seq = rs.getInt(3);
					String operationName = rs.getString(4);
					Integer operationSeq = rs.getInt(5);
					String description = rs.getString(6);
					Instant crtdat = Db.TimeStamp2Instant(rs.getTimestamp(7));
					Integer chgnbr = rs.getInt(8);
					String shortdescr = rs.getString(9);
					String crtusr = rs.getString(10);
					Instant chgdat = Db.TimeStamp2Instant(rs.getTimestamp(11));
					String chgusr = rs.getString(12);
					Integer crtver = rs.getInt(13);
					Instant dltdat = Db.TimeStamp2Instant(rs.getTimestamp(14));
					String dltusr = rs.getString(15);
					OperationKey key = new OperationKey(tenant, version, productName, topicName, processName, seq,
							operationName, operationSeq);
					OperationRec rec = new OperationRec(key, description, crtdat, chgnbr);
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

	public List<OperationRec> list(Integer tenant, Integer version, String productName, String topicName,
			String processName, Integer seq) {

		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		ServiceHelper.validate("Topic", topicName);
		ServiceHelper.validate("Process", processName);
		ServiceHelper.validate("ProcessSeq", seq);

		String theSQL = ServiceHelper.getSQL("operationSelectSQL");

		List<OperationRec> ret = new ArrayList<>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setString(1, productName);
				stmt.setString(2, topicName);
				stmt.setString(3, processName);
				stmt.setInt(4, seq);
				stmt.setInt(5, version);
				stmt.setInt(6, tenant);
				rs = stmt.executeQuery();
				while (rs.next()) {
					String operationName = rs.getString(4);
					Integer operationSeq = rs.getInt(5);
					String description = rs.getString(6);
					Instant crtdat = Db.TimeStamp2Instant(rs.getTimestamp(7));
					Integer chgnbr = rs.getInt(8);
					String shortdescr = rs.getString(9);
					String crtusr = rs.getString(10);
					Instant chgdat = Db.TimeStamp2Instant(rs.getTimestamp(11));
					String chgusr = rs.getString(12);
					Integer crtver = rs.getInt(13);
					Instant dltdat = Db.TimeStamp2Instant(rs.getTimestamp(14));
					String dltusr = rs.getString(15);
					OperationKey key = new OperationKey(tenant, version, productName, topicName, processName, seq,
							operationName, operationSeq);
					OperationRec rec = new OperationRec(key, description, crtdat, chgnbr);
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

	public boolean exists(OperationRec rec) {
		ServiceHelper.validate(rec);

		String theSQL = ServiceHelper.getSQL("operationExistsSQL");

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setString(1, rec.key.productName);
				stmt.setString(2, rec.key.topicName);
				stmt.setString(3, rec.key.processName);
				stmt.setInt(4, rec.key.sequence);
				stmt.setString(5, rec.key.operationName);
				stmt.setInt(6, rec.key.operationSequence);
				stmt.setInt(7, rec.key.version);
				stmt.setInt(8, rec.key.tenant);
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

	public boolean isDeleteMarked(OperationKey key) {
		ServiceHelper.validate(key);

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String theSQL = ServiceHelper.getSQL("operationDeleteMarkedSQL");
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setString(1, key.productName);
				stmt.setString(2, key.topicName);
				stmt.setString(3, key.processName);
				stmt.setInt(4, key.sequence);
				stmt.setString(5, key.operationName);
				stmt.setInt(6, key.operationSequence);
				stmt.setInt(7, key.version);
				stmt.setInt(8, key.tenant);
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

	public OperationRec get(Integer tenant, Integer version, String productName, String topicName, String processName,
			Integer sequence, String operationName, Integer operationSeq) {
		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		ServiceHelper.validate("Topic", topicName);
		ServiceHelper.validate("Process", processName);
		ServiceHelper.validate("Sequence", sequence);
		ServiceHelper.validate("OperationName", operationName);
		ServiceHelper.validate("OperationSeq", operationSeq);

		String theSQL = ServiceHelper.getSQL("operationSelectSingleRecSQL");
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		OperationRec rec = null;
		try {
			connection = Db.open();
			if (connection != null) {
				stmt = connection.prepareStatement(theSQL);
				stmt.setString(1, productName);
				stmt.setString(2, topicName);
				stmt.setString(3, processName);
				stmt.setInt(4, sequence);
				stmt.setString(5, operationName);
				stmt.setInt(6, operationSeq);
				stmt.setInt(7, version);
				stmt.setInt(8, tenant);
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

					OperationKey key = new OperationKey(tenant, version, productName, topicName, processName, sequence,
							operationName, operationSeq);
					rec = new OperationRec(key, descr, crtdat, chgnbr);
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

	public String store(OperationRec rec, String loggedInUserId) {

		ServiceHelper.validate(rec);
		ServiceHelper.validate("userid", loggedInUserId);

		if (ProductService.isLocked(rec.key.tenant, rec.key.version, rec.key.productName)) {
			LOGGER.info("LOCKED " + rec.key.tenant + " " + rec.key.version + " " + rec.key.productName);
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

	private Integer insert(OperationRec rec, String loggedInUserId) {
		Connection connection = null;
		PreparedStatement stmt = null;
		String theSQL = ServiceHelper.getSQL("operationInsertSQL");

		try {
			connection = Db.open();
			if (connection != null) {

				Integer firstVersion = getFirstVersionForOperation(connection, rec.key.tenant, rec.key.productName,
						rec.key.topicName, rec.key.processName, rec.key.sequence, rec.key.operationName,
						rec.key.operationSequence);
				stmt = connection.prepareStatement(theSQL);
				stmt.setInt(1, rec.key.tenant);
				stmt.setInt(2, rec.key.version);
				stmt.setString(3, rec.key.productName);
				stmt.setString(4, rec.key.topicName);
				stmt.setString(5, rec.key.processName);
				stmt.setInt(6, rec.key.sequence);
				stmt.setString(7, rec.key.operationName);
				stmt.setInt(8, rec.key.operationSequence);
				stmt.setString(9, rec.description);
				stmt.setTimestamp(10, Db.Instant2TimeStamp(Instant.now()));
				stmt.setInt(11, 0);
				stmt.setString(12, loggedInUserId);
				stmt.setInt(13, firstVersion == null ? rec.key.version : firstVersion);

				stmt.setString(14, rec.shortdescr);
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

	private Integer update(OperationRec rec, String loggedInUserId) {
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			connection = Db.open();
			if (connection != null) {
				OperationRec dbRec = get(rec.key.tenant, rec.key.version, rec.key.productName, rec.key.topicName,
						rec.key.processName, rec.key.sequence, rec.key.operationName, rec.key.operationSequence);
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
				key.put("processseq", rec.key.sequence);
				key.put("operationname", rec.key.operationName);
				key.put("operationseq", rec.key.operationSequence);
				key.put("version", rec.key.version);
				key.put("tenant", rec.key.tenant);

				Map<String, Object> value = new HashMap<>();
				value.put("description", rec.description);
				value.put("shortdescr", rec.shortdescr);
				value.put("chgusr", loggedInUserId);

				stmt = Db.prepareUpdateStatement(connection, "oper", key, value);
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

	public void remove(Integer tenant, Integer version, String productName, String topicName, String processName,
			Integer sequence, String operationName, Integer operationSequence, String userid) {
		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("Userid", userid);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		ServiceHelper.validate("Topic", topicName);
		ServiceHelper.validate("Process", processName);
		ServiceHelper.validate("Sequence", sequence);
		ServiceHelper.validate("OperationName", operationName);
		ServiceHelper.validate("OperationSequence", operationSequence);
		Connection connection = null;
		PreparedStatement stmt = null;

		// already done we dont want to change the delete date
		OperationKey key = new OperationKey(tenant, version, productName, topicName, processName, sequence,
				operationName, operationSequence);
		if (isDeleteMarked(key)) {
			LOGGER.info("Record is already marked for delete. No Action.");
			return;
		}

		try {
			connection = Db.open();
			if (connection != null) {

				if (ProductService.isLocked(tenant, version, productName)) {
					LOGGER.info("LOCKED " + tenant + " " + version + " " + productName);
					return;
				}

				stmt = connection.prepareStatement(
						"update oper set dltdat=now(), chgnbr = chgnbr + 1, dltusr=? where productname=? and topicname=? and processname=? and processseq=? and operationname=? and operationseq=?  and version=? and tenant=?");
				stmt.setString(1, userid);
				stmt.setString(2, productName);
				stmt.setString(3, topicName);
				stmt.setString(4, processName);
				stmt.setInt(5, sequence);
				stmt.setString(6, operationName);
				stmt.setInt(7, operationSequence);
				stmt.setInt(8, version);
				stmt.setInt(9, tenant);
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		} finally {
			Db.close(stmt);
			Db.close(connection);
		}
	}

	private Integer getFirstVersionForOperation(Connection connection, Integer tenant, String product, String topic,
			String process, Integer processSeq, String operation, Integer operationSeq) throws SQLException {
		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("Product", product);
		ServiceHelper.validate("Topic", topic);
		ServiceHelper.validate("Process", process);
		ServiceHelper.validate("ProcessSeq", processSeq);
		ServiceHelper.validate("Operation", operation);
		ServiceHelper.validate("OperationSeq", operationSeq);

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement(
					"select version from oper where productname=? and topicname=? and processname=? and processseq=?  and operationname=? and operationseq=? and tenant=? order by version");
			stmt.setString(1, product);
			stmt.setString(2, topic);
			stmt.setString(3, process);
			stmt.setInt(4, processSeq);
			stmt.setString(5, operation);
			stmt.setInt(6, operationSeq);
			stmt.setInt(7, tenant);
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
