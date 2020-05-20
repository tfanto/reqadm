package pdmf.service.support;

import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import pdmf.model.OperationRec;
import pdmf.model.ProcessRec;
import pdmf.model.ProductKey;
import pdmf.model.ProductRec;
import pdmf.model.TopicRec;
import pdmf.service.OperationService;
import pdmf.service.ProcessService;
import pdmf.service.ProductService;
import pdmf.service.TopicService;

public class ExportSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExportSupport.class);

	private ProductService productService = new ProductService();
	private TopicService topicService = new TopicService();
	private ProcessService processService = new ProcessService();
	private OperationService operationService = new OperationService();

	public String convertToXML(ProductKey key) {
		ServiceHelper.validate(key);

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			doc.setXmlStandalone(true);

			ProductRec productRec = productService.get(key.tenant, key.version, key.productName);
			Element product = buildProduct(doc, productRec);
			doc.appendChild(product);

			List<TopicRec> topics = topicService.list(productRec.key.tenant,productRec.key.version, productRec.key.productName);
			for (TopicRec topicRec : topics) {
				Element topic = buildTopic(doc, topicRec);
				product.appendChild(topic);

				List<ProcessRec> processes = processService.list(topicRec.key.version, topicRec.key.productName, topicRec.key.topicName);
				for (ProcessRec processRec : processes) {
					Element process = buildProcess(doc, processRec);
					topic.appendChild(process);

					List<OperationRec> operations = operationService.list(processRec.key.version, processRec.key.productName, processRec.key.topicName, processRec.key.processName);
					for (OperationRec operationRec : operations) {
						Element operation = buildOperation(doc, operationRec);
						process.appendChild(operation);
					}
				}

			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			// this is for debug
			// transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			// transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
			// transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
			// "2");
			//

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new StringWriter());
			transformer.transform(source, result);
			String xmlString = result.getWriter().toString();
			return xmlString;
		} catch (ParserConfigurationException pce) {
			LOGGER.error(pce.toString(), pce);
			return null;
		} catch (TransformerException tfe) {
			LOGGER.error(tfe.toString(), tfe);
			return null;
		} catch (Exception tfe) {
			LOGGER.error(tfe.toString(), tfe);
			return null;
		}
	}

	private Element buildProduct(Document doc, ProductRec rec) {
		Element product = doc.createElement("product");
		product.setAttribute("name", rec.key.productName);
		product.setAttribute("version", String.valueOf(rec.key.version));
		product.setAttribute("created", rec.crtdat.toString());
		product.setAttribute("createdby", rec.crtusr);
		if (rec.chgusr != null && rec.chgdat != null) {
			product.setAttribute("changed", rec.chgdat.toString());
			product.setAttribute("changedby", rec.chgusr);
		}
		if (rec.dltusr != null && rec.dltdat != null) {
			product.setAttribute("deletemarked", rec.dltdat.toString());
			product.setAttribute("deletemarkedby", rec.dltusr);
		}

		Element description = doc.createElement("description");
		description.setAttribute("info", rec.description);
		description.setAttribute("shortinfo", rec.shortdescr);
		product.appendChild(description);

		return product;
	}

	private Element buildTopic(Document doc, TopicRec rec) {
		Element topic = doc.createElement("topic");
		topic.setAttribute("name", rec.key.topicName);
		topic.setAttribute("created", rec.crtdat.toString());
		topic.setAttribute("createdby", rec.crtusr);
		if (rec.chgusr != null && rec.chgdat != null) {
			topic.setAttribute("changed", rec.chgdat.toString());
			topic.setAttribute("changedby", rec.chgusr);
		}
		if (rec.dltusr != null && rec.dltdat != null) {
			topic.setAttribute("deletemarked", rec.dltdat.toString());
			topic.setAttribute("deletemarkedby", rec.dltusr);
		}

		Element description = doc.createElement("description");
		description.setAttribute("info", rec.description);
		description.setAttribute("shortinfo", rec.shortdescr);
		topic.appendChild(description);


		return topic;
	}

	private Element buildProcess(Document doc, ProcessRec rec) {
		Element process = doc.createElement("process");
		process.setAttribute("name", rec.key.processName);
		process.setAttribute("created", rec.crtdat.toString());
		process.setAttribute("createdby", rec.crtusr);
		if (rec.chgusr != null && rec.chgdat != null) {
			process.setAttribute("changed", rec.chgdat.toString());
			process.setAttribute("changedby", rec.chgusr);
		}
		if (rec.dltusr != null && rec.dltdat != null) {
			process.setAttribute("deletemarked", rec.dltdat.toString());
			process.setAttribute("deletemarkedby", rec.dltusr);
		}

		Element description = doc.createElement("description");
		description.setAttribute("info", rec.description);
		description.setAttribute("shortinfo", rec.shortdescr);
		process.appendChild(description);


		return process;
	}

	private Element buildOperation(Document doc, OperationRec rec) {
		Element operation = doc.createElement("operation");
		operation.setAttribute("name", rec.key.operationName);
		operation.setAttribute("created", rec.crtdat.toString());
		operation.setAttribute("createdby", rec.crtusr);
		if (rec.chgusr != null && rec.chgdat != null) {
			operation.setAttribute("changed", rec.chgdat.toString());
			operation.setAttribute("changedby", rec.chgusr);
		}
		if (rec.dltusr != null && rec.dltdat != null) {
			operation.setAttribute("deletemarked", rec.dltdat.toString());
			operation.setAttribute("deletemarkedby", rec.dltusr);
		}

		Element description = doc.createElement("description");
		description.setAttribute("info", rec.description);
		description.setAttribute("shortinfo", rec.shortdescr);
		operation.appendChild(description);


		return operation;
	}

}
