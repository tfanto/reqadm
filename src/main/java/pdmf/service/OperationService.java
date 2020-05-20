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
				stmt.setInt(1, tenant);
				stmt.setInt(2, version);
				stmt.setString(3, productName);
				stmt.setString(4, topicName);
				stmt.setString(5, processName);
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
					String rs_processname = rs.getString("processname");
					Integer rs_processseq = rs.getInt("processseq");
					String rs_operationname = rs.getString("operationname");
					Integer rs_operationseq = rs.getInt("operationseq");

					OperationKey key = new OperationKey(rs_tenant, rs_version, rs_productname, rs_topicname,
							rs_processname, rs_processseq, rs_operationname, rs_operationseq);
					OperationRec rec = new OperationRec(key, rs_description, rs_crtdat, rs_chgnbr);
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

	public List<OperationRec> list(Integer tenant, Integer version, String productName, String topicName,
			String processName, Integer processeq) {

		ServiceHelper.validate("Tenant", tenant);
		ServiceHelper.validate("Version", version);
		ServiceHelper.validate("Product", productName);
		ServiceHelper.validate("Topic", topicName);
		ServiceHelper.validate("Process", processName);
		ServiceHelper.validate("ProcessSeq", processeq);

		String theSQL = ServiceHelper.getSQL("operationSelectSQL");

		List<OperationRec> ret = new ArrayList<>();
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
				stmt.setString(4, topicName);
				stmt.setString(5, processName);
				stmt.setInt(6, processeq);
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
					String rs_processname = rs.getString("processname");
					Integer rs_processseq = rs.getInt("processseq");
					String rs_operationname = rs.getString("operationname");
					Integer rs_operationseq = rs.getInt("operationseq");

					OperationKey key = new OperationKey(rs_tenant, rs_version, rs_productname, rs_topicname,
							rs_processname, rs_processseq, rs_operationname, rs_operationseq);
					OperationRec rec = new OperationRec(key, rs_description, rs_crtdat, rs_chgnbr);
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
				stmt.setInt(1, rec.key.tenant);
				stmt.setInt(2, rec.key.version);
				stmt.setString(3, rec.key.productName);
				stmt.setString(4, rec.key.topicName);
				stmt.setString(5, rec.key.processName);
				stmt.setInt(6, rec.key.sequence);
				stmt.setString(7, rec.key.operationName);
				stmt.setInt(8, rec.key.operationSequence);
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
				stmt.setInt(1, key.tenant);
				stmt.setInt(2, key.version);
				stmt.setString(3, key.productName);
				stmt.setString(4, key.topicName);
				stmt.setString(5, key.processName);
				stmt.setInt(6, key.sequence);
				stmt.setString(7, key.operationName);
				stmt.setInt(8, key.operationSequence);
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
				stmt.setInt(1, tenant);
				stmt.setInt(2, version);
				stmt.setString(3, productName);
				stmt.setString(4, topicName);
				stmt.setString(5, processName);
				stmt.setInt(6, sequence);
				stmt.setString(7, operationName);
				stmt.setInt(8, operationSeq);
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
					String rs_processname = rs.getString("processname");
					Integer rs_processseq = rs.getInt("processseq");
					String rs_operationname = rs.getString("operationname");
					Integer rs_operationseq = rs.getInt("operationseq");

					OperationKey key = new OperationKey(rs_tenant, rs_version, rs_productname, rs_topicname,
							rs_processname, rs_processseq, rs_operationname, rs_operationseq);
					rec = new OperationRec(key, rs_description, rs_crtdat, rs_chgnbr);
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
