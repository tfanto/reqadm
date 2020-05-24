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

import pdmf.model.OperationKey;
import pdmf.model.ProcessKey;
import pdmf.model.ProductKey;
import pdmf.model.TopicKey;
import pdmf.service.support.ServiceHelper;
import pdmf.sys.Db;

public class SearchService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchService.class);

	private String selectSQL_OPERATION = "select description::tsvector @@ '%s'::tsquery as found1, description, shortdescr::tsvector @@ '%s'::tsquery as found2, shortdescr, version, productname, topicname, processname, processseq, operationname, operationseq from oper where tenantid=?";
	private String selectSQL_PROCESS = "select description::tsvector @@ '%s'::tsquery as found1, description, shortdescr::tsvector @@ '%s'::tsquery as found2, shortdescr, version, productname, topicname, processname, processseq from process where tenantid=?";
	private String selectSQL_TOPIC = "select description::tsvector @@ '%s'::tsquery as found1, description, shortdescr::tsvector @@ '%s'::tsquery as found2, shortdescr, version, productname, topicname from topic where tenantid=?";
	private String selectSQL_PRODUCT = "select description::tsvector @@ '%s'::tsquery as found1, description, shortdescr::tsvector @@ '%s'::tsquery as found2, shortdescr, version, productname from product where tenantid=?";

	private String interpretWordsPerLine(String line) {
		String ret = "";

		String splited[] = line.split(" ");
		int n = splited.length;
		if (n <= 1) {
			return line;
		}

		for (int i = 0; i < n; i++) {
			String part = splited[i];
			part = part.trim();
			if (part.length() > 0) {
				ret += part;
				ret += " & ";
			}
		}

		if (ret.endsWith(" & ")) {
			ret = ret.substring(0, ret.length() - 3);
		}

		return " ( " + ret + " ) ";
	}

	private String formatCriteria(List<String> criteria) {

		String resultCriteria = "";
		for (String line : criteria) {
			String andLine = interpretWordsPerLine(line);
			andLine += " | ";
			resultCriteria += andLine;
		}
		resultCriteria = resultCriteria.substring(0, resultCriteria.length() - 3);
		return resultCriteria;
	}

	public List<Map<Object, List<String>>> search(List<String> criteriaList, Boolean searchInProduct,
			Boolean searchInTopic, Boolean searchInProcess, Boolean searchInOperation, String tenantid) {
		ServiceHelper.validate(criteriaList);
		ServiceHelper.validate("tenant", tenantid);
		ServiceHelper.validate("searchInProduct", searchInProduct);
		ServiceHelper.validate("searchInTopic", searchInTopic);
		ServiceHelper.validate("searchInProcess", searchInProcess);
		ServiceHelper.validate("searchInOperation", searchInOperation);

		if (criteriaList.size() < 1) {
			LOGGER.info("No search criteria. Aborting search");
			return null;
		}
		// String criteria = formatCriteria(criteriaList);
		String criteria = formatCriteria(criteriaList);
		LOGGER.debug(criteria);

		// lista av nyckel och data bestående av en lista av strängar pos 1 description
		// pos 2 shortdescr
		List<Map<Object, List<String>>> resultSet = new ArrayList<>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = Db.open();
			if (connection != null) {
				if (searchInOperation) {
					resultSet = searchInOperation(connection, tenantid, criteria, resultSet);
				}
				if (searchInProcess) {
					resultSet = searchInProcess(connection, tenantid, criteria, resultSet);
				}
				if (searchInTopic) {
					resultSet = searchInTopic(connection, tenantid, criteria, resultSet);
				}
				if (searchInProduct) {
					resultSet = searchInProduct(connection, tenantid, criteria, resultSet);
				}
			}
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
		} finally {
			Db.close(rs);
			Db.close(stmt);
			Db.close(connection);
		}
		return resultSet;
	}

	private List<Map<Object, List<String>>> searchInOperation(Connection connection, String tenantid, String criteria,
			List<Map<Object, List<String>>> resultSet) throws SQLException {

		String sql = String.format(selectSQL_OPERATION, criteria, criteria);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, tenantid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Boolean found1 = rs.getBoolean(1);
				String descr = rs.getString(2);
				Boolean found2 = rs.getBoolean(3);
				String shortdescr = rs.getString(4);
				if (!found1 && !found2) {
					continue;
				}

				// version, productname, topicname, processname, seq, operationname,
				// operationseq

				Integer version = rs.getInt(5);
				String productName = rs.getString(6);
				String topicName = rs.getString(7);
				String processName = rs.getString(8);
				Integer seq = rs.getInt(9);
				String operationName = rs.getString(10);
				Integer operationSeq = rs.getInt(11);

				// create a key
				OperationKey key = new OperationKey(tenantid, version, productName, topicName, processName, seq,
						operationName, operationSeq);
				// create data
				List<String> data = new ArrayList<>();
				data.add(descr);
				data.add(shortdescr);

				// create a record (map of key and list of data
				Map<Object, List<String>> record = new HashMap<>();
				record.put(key, data);

				// add record to resultset
				resultSet.add(record);
			}
			return resultSet;
		} finally {
			Db.close(rs);
			Db.close(stmt);
		}
	}

	private List<Map<Object, List<String>>> searchInProcess(Connection connection, String tenantid, String criteria,
			List<Map<Object, List<String>>> resultSet) throws SQLException {

		String sql = String.format(selectSQL_PROCESS, criteria, criteria);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, tenantid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Boolean found1 = rs.getBoolean(1);
				String descr = rs.getString(2);
				Boolean found2 = rs.getBoolean(3);
				String shortdescr = rs.getString(4);
				if (!found1 && !found2) {
					continue;
				}
				// version, productname, topicname, processname, seq

				Integer version = rs.getInt(5);
				String productName = rs.getString(6);
				String topicName = rs.getString(7);
				String processName = rs.getString(8);
				Integer seq = rs.getInt(9);

				// create a key
				ProcessKey key = new ProcessKey(tenantid, version, productName, topicName, processName, seq);
				// create data
				List<String> data = new ArrayList<>();
				data.add(descr);
				data.add(shortdescr);

				// create a record (map of key and list of data
				Map<Object, List<String>> record = new HashMap<>();
				record.put(key, data);

				// add record to resultset
				resultSet.add(record);
			}
			return resultSet;
		} finally {
			Db.close(rs);
			Db.close(stmt);
		}
	}

	private List<Map<Object, List<String>>> searchInTopic(Connection connection, String tenantid, String criteria,
			List<Map<Object, List<String>>> resultSet) throws SQLException {

		String sql = String.format(selectSQL_TOPIC, criteria, criteria);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, tenantid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Boolean found1 = rs.getBoolean(1);
				String descr = rs.getString(2);
				Boolean found2 = rs.getBoolean(3);
				String shortdescr = rs.getString(4);
				if (!found1 && !found2) {
					continue;
				}
				// version, productname, topicname

				Integer version = rs.getInt(5);
				String productName = rs.getString(6);
				String topicName = rs.getString(7);

				// create a key
				TopicKey key = new TopicKey(tenantid, version, productName, topicName);
				// create data
				List<String> data = new ArrayList<>();
				data.add(descr);
				data.add(shortdescr);

				// create a record (map of key and list of data
				Map<Object, List<String>> record = new HashMap<>();
				record.put(key, data);

				// add record to resultset
				resultSet.add(record);
			}
			return resultSet;
		} finally {
			Db.close(rs);
			Db.close(stmt);
		}
	}

	private List<Map<Object, List<String>>> searchInProduct(Connection connection, String tenantid, String criteria,
			List<Map<Object, List<String>>> resultSet) throws SQLException {

		String sql = String.format(selectSQL_PRODUCT, criteria, criteria);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, tenantid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Boolean found1 = rs.getBoolean(1);
				String descr = rs.getString(2);
				Boolean found2 = rs.getBoolean(3);
				String shortdescr = rs.getString(4);
				if (!found1 && !found2) {
					continue;
				}
				// version, productname

				Integer version = rs.getInt(5);
				String productName = rs.getString(6);

				// create a key
				ProductKey key = new ProductKey(tenantid, version, productName);
				// create data
				List<String> data = new ArrayList<>();
				data.add(descr);
				data.add(shortdescr);

				// create a record (map of key and list of data
				Map<Object, List<String>> record = new HashMap<>();
				record.put(key, data);

				// add record to resultset
				resultSet.add(record);
			}
			return resultSet;
		} finally {
			Db.close(rs);
			Db.close(stmt);
		}
	}

}
